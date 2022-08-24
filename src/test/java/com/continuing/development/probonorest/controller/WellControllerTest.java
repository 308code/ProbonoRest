package com.continuing.development.probonorest.controller;

import com.continuing.development.probonorest.comparator.WellComparatorByLastProducedDate;
import com.continuing.development.probonorest.dao.WellDao;
import com.continuing.development.probonorest.model.Well;
import com.continuing.development.probonorest.model.WellReport;
import com.continuing.development.probonorest.service.WellService;
import com.continuing.development.probonorest.service.WellServiceMongoDbImpl;
import com.flextrade.jfixture.FixtureCollections;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.MultipleCount;
import com.mongodb.MongoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WellControllerTest {
    static JFixture fixture;
    static FixtureCollections fixtureCollections;
    static MultipleCount multipleCount_10;
    static MultipleCount multipleCount_6;
    static Well wellRequest;
    static Well wellResponse;
    static List<Well> wellListRequest;
    static ResponseEntity responseEntity;
    static ResponseEntity<List<Well>> responseEntityListOfWells;
    static ResponseEntity<Well> responseEntityWell;

    @Mock
    private WellDao mockWellDao = mock(WellDao.class);
    private WellService wellService = new WellServiceMongoDbImpl(mockWellDao);
    WellController wellController = new WellController(wellService);
    
    @BeforeAll
    public static void initialize(){
        fixture = new JFixture();
        wellRequest = fixture.create(Well.class);
        wellResponse = fixture.create(Well.class);
        multipleCount_10 = new MultipleCount(10);
        multipleCount_6 = new MultipleCount(6);
        fixtureCollections = new FixtureCollections(fixture, multipleCount_10);
        wellListRequest = fixtureCollections.createCollection(Well.class).stream().toList();
    }

    @Test
    void createWell_success() {
        // When
        when(mockWellDao.insert(wellRequest)).thenReturn(wellRequest);
        responseEntity = wellController.createWell(wellRequest);

        // Then
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
    }
    @Test
    void createWell_Throws_MongoException() {

        // When
        when(mockWellDao.insert(wellRequest)).thenThrow(new MongoException("Insert Failed!"));
        responseEntity = wellController.createWell(wellRequest);

        // Then
        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
        assertEquals("Not Created",responseEntity.getBody());
    }

    @Test
    void getAllWells_success() {
        // When
        when(mockWellDao.findAllByOrderByCountyNameAscTownshipNameAscWellNameAscWellNumberAsc())
                .thenReturn(wellListRequest);
        responseEntity = wellController.getAllWells();

        // Then
        List<Well> actualResponse = (List<Well>)responseEntity.getBody();
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(10,Integer.parseInt(responseEntity.getHeaders().getFirst("count")));
        assertEquals(wellListRequest.toString(),actualResponse.toString());
    }

    @Test
    void getAllWells_fails_due_to_mongo_exception_being_thrown() {
        // When
        doThrow(new MongoException("MongoException Thrown")).when(mockWellDao)
                .findAllByOrderByCountyNameAscTownshipNameAscWellNameAscWellNumberAsc();
        responseEntity = wellController.getAllWells();

        // Then
        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8})
    void getAllWellsOrderedByMostRecentProduction_Successful(int index) {

        // When
        when(mockWellDao.findAll()).thenReturn(wellListRequest);

        responseEntityListOfWells = wellController.getAllWellsByProduction();

        // Then
        List<Well> wellsResponse = responseEntityListOfWells.getBody();
        assertTrue( wellsResponse.get(index).getProduction().get(2).getPayedDate().after(
                    wellsResponse.get(index+1).getProduction().get(2).getPayedDate()) );
    }

    @Test
    void getAllWellsOrderedByMostRecentProduction_returns_null_Successful() {

        // When
        when(mockWellDao.findAll()).thenReturn(null);

        responseEntityListOfWells = wellController.getAllWellsByProduction();

        // Then
        List<Well> wellsResponse = responseEntityListOfWells.getBody();
        assertEquals("0",responseEntityListOfWells.getHeaders().getFirst("count"));
    }

    @Test
    void getAllWellsOrderedByMostRecentProduction_ThrowsMongoException() {

        // When
        doThrow(new MongoException("Threw a mongo exception!")).when(mockWellDao).findAll();

        responseEntityListOfWells = wellController.getAllWellsByProduction();

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntityListOfWells.getStatusCode());
        List<Well> wellsResponse = responseEntityListOfWells.getBody();
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1})
    void getWellById_Successful(int index) {

        // When
        when(mockWellDao.findById("1FB8D54E0B9340C98115E170C104E733")).thenReturn(Optional.ofNullable(wellRequest));

        responseEntityWell = wellController.getWellById("1FB8D54E0B9340C98115E170C104E733");

        // Then
        Well wellsResponse = responseEntityWell.getBody();
        assertNotNull(wellsResponse);
        assertTrue( wellsResponse.getProduction().get(index).getPayedDate().after(
                wellsResponse.getProduction().get(index+1).getPayedDate()) );
        assertEquals(1,Integer.parseInt(responseEntityWell.getHeaders().getFirst("count")));
        assertEquals(HttpStatus.OK, responseEntityWell.getStatusCode());
    }

    @Test
    void getWellById_returns_null() {

        // When
        when(mockWellDao.findById("1FB8D54E0B9340C98115E170C104E733")).thenReturn(Optional.empty());

        responseEntityWell = wellController.getWellById("1FB8D54E0B9340C98115E170C104E733");

        // Then
        Well wellsResponse = responseEntityWell.getBody();
        assertNull(wellsResponse);
        assertEquals(0,Integer.parseInt(responseEntityWell.getHeaders().getFirst("count")));
        assertEquals(HttpStatus.NO_CONTENT, responseEntityWell.getStatusCode());
    }

    @Test
    void getWellById_throws_mongodb_exception() {

        // When
        doThrow(new MongoException("Mongo Exception thrown!")).when(mockWellDao)
                .findById("1FB8D54E0B9340C98115E170C104E733");

        responseEntityWell = wellController.getWellById("1FB8D54E0B9340C98115E170C104E733");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntityWell.getStatusCode());
        assertNull(responseEntityWell.getHeaders().getFirst("count"));
        assertNull(responseEntityWell.getBody());
    }

    @Test
    void getProductionTotalsWithinRange_Successful_One_Well_In_Range() {
        // Given
        List<Well> knowValueWells = setupKnownWells();
        ResponseEntity<List<WellReport>> responseEntitiesWellReport;
        Calendar cal = Calendar.getInstance();
        cal.set(2022,05,16,0,0,0);
        Date fromDate = cal.getTime();
        cal.set(2022,05,18,23,59,59);
        Date toDate = cal.getTime();

        // When
        when(mockWellDao.findAllByProductionPayedDateBetweenOrderByCountyAscTownshipAscWellNameAscWellNumberAsc(
                fromDate,toDate)).thenReturn(knowValueWells);

        responseEntitiesWellReport = wellController.getProductionTotalsWithinRange(fromDate,toDate);

        // Then
        List<WellReport> wellReportsResponse = responseEntitiesWellReport.getBody();
        assertNotNull(wellReportsResponse);
        assertEquals(1,wellReportsResponse.size());
        assertEquals(225,wellReportsResponse.get(0).getBrineTotal());
        assertEquals(HttpStatus.OK, responseEntitiesWellReport.getStatusCode());
    }

    @Test
    void getProductionTotalsWithinRange_Successful_Three_Wells_In_Range() {
        // Given
        List<Well> knowValueWells = setupKnownWells();
        ResponseEntity<List<WellReport>> responseEntitiesWellReport;
        Calendar cal = Calendar.getInstance();
        cal.set(2022,05,3,0,0,0);
        Date fromDate = cal.getTime();
        cal.set(2022,05,7,23,59,59);
        Date toDate = cal.getTime();

        // When
        when(mockWellDao.findAllByProductionPayedDateBetweenOrderByCountyAscTownshipAscWellNameAscWellNumberAsc(
                fromDate,toDate)).thenReturn(knowValueWells);

        responseEntitiesWellReport = wellController.getProductionTotalsWithinRange(fromDate,toDate);

        // Then
        List<WellReport> wellReportsResponse = responseEntitiesWellReport.getBody();
        assertNotNull(wellReportsResponse);
        assertEquals(3,wellReportsResponse.size());
        assertEquals(50,wellReportsResponse.get(0).getOilTotal());
        assertEquals(150,wellReportsResponse.get(1).getOilTotal());
        assertEquals(50,wellReportsResponse.get(2).getOilTotal());
        assertEquals(HttpStatus.OK, responseEntitiesWellReport.getStatusCode());
    }

    private List<Well> setupKnownWells(){
        fixtureCollections = new FixtureCollections(fixture, multipleCount_6);
        List<Well>results = fixtureCollections.createCollection(Well.class).stream().toList();
        Calendar cal = Calendar.getInstance();
        cal.set(2022,5,1);
        results.get(0).getProduction().get(0).setType("oil");
        results.get(0).getProduction().get(0).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 2);
        results.get(0).getProduction().get(1).setType("brine");
        results.get(0).getProduction().get(1).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 3);
        results.get(0).getProduction().get(2).setQuantity(50);
        results.get(0).getProduction().get(2).setType("oil");
        results.get(0).getProduction().get(2).setPayedDate(cal.getTime());

        cal.set(Calendar.DAY_OF_MONTH, 4);
        results.get(1).getProduction().get(0).setQuantity(50);
        results.get(1).getProduction().get(0).setType("oil");
        results.get(1).getProduction().get(0).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 5);
        results.get(1).getProduction().get(1).setQuantity(50);
        results.get(1).getProduction().get(1).setType("oil");
        results.get(1).getProduction().get(1).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 6);
        results.get(1).getProduction().get(2).setQuantity(50);
        results.get(1).getProduction().get(2).setType("oil");
        results.get(1).getProduction().get(2).setPayedDate(cal.getTime());

        cal.set(Calendar.DAY_OF_MONTH, 7);
        results.get(2).getProduction().get(0).setQuantity(50);
        results.get(2).getProduction().get(0).setType("oil");
        results.get(2).getProduction().get(0).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 8);
        results.get(2).getProduction().get(1).setType("oil");
        results.get(2).getProduction().get(1).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 9);
        results.get(2).getProduction().get(2).setType("gas");
        results.get(2).getProduction().get(2).setPayedDate(cal.getTime());

        cal.set(Calendar.DAY_OF_MONTH, 10);
        results.get(3).getProduction().get(0).setType("oil");
        results.get(3).getProduction().get(0).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 11);
        results.get(3).getProduction().get(1).setType("oil");
        results.get(3).getProduction().get(1).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 12);
        results.get(3).getProduction().get(2).setType("oil");
        results.get(3).getProduction().get(2).setPayedDate(cal.getTime());

        cal.set(Calendar.DAY_OF_MONTH, 13);
        results.get(4).getProduction().get(0).setType("gas");
        results.get(4).getProduction().get(0).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 14);
        results.get(4).getProduction().get(1).setType("gas");
        results.get(4).getProduction().get(1).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 15);
        results.get(4).getProduction().get(2).setType("gas");
        results.get(4).getProduction().get(2).setPayedDate(cal.getTime());

        cal.set(Calendar.DAY_OF_MONTH, 16);
        results.get(5).getProduction().get(0).setType("brine");
        results.get(5).getProduction().get(0).setQuantity(50);
        results.get(5).getProduction().get(0).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 17);
        results.get(5).getProduction().get(1).setType("brine");
        results.get(5).getProduction().get(1).setQuantity(150);
        results.get(5).getProduction().get(1).setPayedDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 18);
        results.get(5).getProduction().get(2).setType("brine");
        results.get(5).getProduction().get(2).setQuantity(25);
        results.get(5).getProduction().get(2).setPayedDate(cal.getTime());
        return results;
    }

    @Test
    void updateWell_success() {
        // When
        when(mockWellDao.save(wellRequest)).thenReturn(wellResponse);

        responseEntity = wellController.updateWell(wellRequest);

        // Then
        Well actualResponse = (Well)responseEntity.getBody();
        assertEquals(HttpStatus.ACCEPTED,responseEntity.getStatusCode());
        assertEquals(actualResponse.toString(),wellResponse.toString());
    }

    @Test
    void updateWell_Throws_MongoException(){
        // When
        when(mockWellDao.save(wellRequest)).thenThrow(new MongoException("Threw a new MongoException"));

        responseEntity = wellController.updateWell(wellRequest);

        // Then
        Well actualResponse = (Well)responseEntity.getBody();
        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
        assertEquals(wellRequest.toString(),actualResponse.toString());
    }

    @Test
    void updateWell_Throws_IllegalArgumentException(){
        // When
        when(mockWellDao.save(wellRequest)).thenThrow(new IllegalArgumentException("Threw a new MongoException"));

        responseEntity = wellController.updateWell(wellRequest);

        // Then
        Well actualResponse = (Well)responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
        assertEquals(wellRequest.toString(),actualResponse.toString());
    }

    @Test
    void deleteWell_fails_due_to_null_id(){
        // Given
        wellRequest.setId(null);

        // When
        when(mockWellDao.existsById(wellRequest.getId())).thenReturn(false);
        responseEntity = wellController.deleteWellById(wellRequest.getId());

        // Then
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
        assertEquals("false",responseEntity.getBody().toString());
    }

    @Test
    void deleteWell_fails_due_to_MongoException_being_thrown(){
        // When
        when(mockWellDao.existsById(wellRequest.getId())).thenReturn(false);
        doThrow(new MongoException("MongoException thrown")).when(mockWellDao).deleteById(wellRequest.getId());

        responseEntity = wellController.deleteWellById(wellRequest.getId());

        // Then
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
        assertEquals("false",responseEntity.getBody().toString());
    }

    @Test
    void deleteWell_fails_due_to_MongoException_being_thrown_during_existance_check(){
        // When
        doThrow(new MongoException("MongoException thrown")).when(mockWellDao).existsById(wellRequest.getId());

        responseEntity = wellController.deleteWellById(wellRequest.getId());

        // Then
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        assertEquals("false",responseEntity.getBody().toString());
    }

    @Test
    void deleteWell_is_successful(){
        // Given
        ResponseEntity<Boolean> result = new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);

        // When
        when(mockWellDao.existsById(wellRequest.getId())).thenReturn(true);
        doNothing().when(mockWellDao).deleteById(wellRequest.getId());

        responseEntity = wellController.deleteWellById(wellRequest.getId());

        // Then
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals("true",responseEntity.getBody().toString());
    }
}