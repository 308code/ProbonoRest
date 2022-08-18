package com.continuing.development.probonorest.service;

import com.continuing.development.probonorest.comparator.WellComparatorByLastProducedDate;
import com.continuing.development.probonorest.dao.WellDao;
import com.continuing.development.probonorest.model.Well;
import com.continuing.development.probonorest.model.WellReport;
import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class WellServiceMongoDbImpl implements WellService{
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    private final WellComparatorByLastProducedDate wellComparator = new WellComparatorByLastProducedDate();
    private final WellDao wellDao;

    @Autowired
    public WellServiceMongoDbImpl(WellDao wellDao){
        this.wellDao = wellDao;
    }

    @Override
    public ResponseEntity<String> createWell(@RequestBody Well well){
        try{
            well.setId(UUID.randomUUID().toString().replace("-",""));
            Well result = wellDao.insert(well);
            return new ResponseEntity<>(result.getId(),HttpStatus.CREATED);
        }catch (MongoException e){
            return new ResponseEntity<>("",HttpStatus.NO_CONTENT);
        }
    }
    @Override
    public ResponseEntity<Well> updateWell(Well well){
        Well responseWell = wellDao.save(well);
        return new ResponseEntity<>(responseWell,HttpStatus.ACCEPTED);
    }
    @Override
    public ResponseEntity<Boolean> deleteWellById(String id) {
        try {
            if (!wellDao.existsById(id)) {
                return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
            }
            wellDao.deleteById(id);
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } catch (MongoException e) {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Well>> getAllWellsOrderedByMostRecentProduction(){
        List<Well> wells;
        try{
            wells = wellDao.findAll();
        }catch (MongoException e){
            log.error("Error getting all wells:  MESSAGE: {}  STACKTRACE:  {}", e.getMessage(), e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        if(! CollectionUtils.isEmpty(wells)){
            wells.sort(wellComparator.reversed());
        }

        HttpHeaders headers = addCountToHeader(wells);
        return getListResponseEntity(wells,headers);
    }

    @Override
    public ResponseEntity<List<WellReport>> generateProductionReportWithinDateRange(Date from, Date to) {
        List<WellReport> results = new ArrayList<>();
        List<Well> wells = wellDao.findAllByProductionPayedDateBetweenOrderByCountyAscTownshipAscWellNameAscWellNumberAsc(from, to);
        wells.forEach( well -> {
            well.getProduction().forEach( production -> {
                if(production.getPayedDate().after(from) && production.getPayedDate().before(to)){
                    WellReport wellReport = new WellReport(well,production.getProdQtyByType("oil"),
                            production.getProdQtyByType("gas"),production.getProdQtyByType("brine"));
                    int i = results.indexOf(wellReport);

                    if(i == -1){
                        results.add(wellReport);
                    }else {
                        results.get(i).setOilTotal(results.get(i).getOilTotal() + wellReport.getOilTotal());
                        results.get(i).setGasTotal(results.get(i).getGasTotal() + wellReport.getGasTotal());
                        results.get(i).setBrineTotal(results.get(i).getBrineTotal() + wellReport.getBrineTotal());
                    }
                }
            });
        });
        return new ResponseEntity<>(results,HttpStatus.OK);
    }




    @Override
    public ResponseEntity<List<Well>> getAllWells() {
        List<Well> wells;
        try{
            wells = wellDao.findAllByOrderByCountyNameAscTownshipNameAscWellNameAscWellNumberAsc();
        }catch (MongoException e){
            log.error("Error getting all wells:  MESSAGE: {}  STACKTRACE:  {}", e.getMessage(), e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
//        if(! CollectionUtils.isEmpty(wells)){
//            wells.sort(wellComparator);
//        }
        HttpHeaders headers = addCountToHeader(wells);
        return getListResponseEntity(wells,headers);
    }

    @Override
    public ResponseEntity<Well> getWellById(String id)  {
        Well well;
        try{
            Optional<Well> optional = wellDao.findById(id);
            well = optional.orElse(null);
        }catch (MongoException e){
            log.error("Error getting well with id: {} MESSAGE: {}  STACKTRACE: {}", id, e.getMessage(), e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = addCountToHeader(well);
        if(ObjectUtils.isEmpty(well)){
            return new ResponseEntity<>(headers,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(well,headers,HttpStatus.OK);
    }
    private HttpHeaders addCountToHeader(List<Well> wellList){
        HttpHeaders headers = new HttpHeaders();

        if(CollectionUtils.isEmpty(wellList)){
            wellList = new ArrayList<>(0);
        }
        headers.add("count", String.valueOf(wellList.size()));
        return headers;
    }

    private HttpHeaders addCountToHeader(Well well){
        HttpHeaders headers = new HttpHeaders();

        if (ObjectUtils.isEmpty(well)) {
            headers.add("count", String.valueOf(0));
        } else {
            headers.add("count", String.valueOf(1));
        }

        return headers;
    }

    private ResponseEntity<List<Well>> getListResponseEntity(List<Well> wells, HttpHeaders headers) {
        if(CollectionUtils.isEmpty(wells)){
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(wells, headers, HttpStatus.OK);
    }

}
