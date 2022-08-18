package com.continuing.development.probonorest.dao;

import com.continuing.development.probonorest.model.Well;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface WellDao extends MongoRepository<Well,String> {
    List<Well> findAllByOrderByCountyNameAscTownshipNameAscWellNameAscWellNumberAsc();

    //Find wells production in the given date range ad returned Sorted by county then township then wellName then wellNumber all in ASC order
    @Query(value = "{ 'production.payedDate' : { '$gt' : { '$date' : ?0}, '$lt' : { '$date' : ?1}}}",
            sort= "{ county:1 , township:1, wellName:1 , wellNumber:1}")
    List<Well> findAllByProductionPayedDateBetweenOrderByCountyAscTownshipAscWellNameAscWellNumberAsc(Date fromDate, Date toDate);

    //Find all wells from a particular county
    @Query(value= "{'county': { '$regularExpression' : { 'pattern' : ?0, 'options' : 'i'}}}",
            sort ="{county:1 , township:1, wellName:1 , wellNumber:1}" )
    List<Well> findAllByCountyRegexIgnoreCaseOrderByTownshipAscWellNameAscWellNumberAsc(String county);

    //Find all wells that contain the incoming string
    @Query(value= "{'wellName': { '$regularExpression' : { 'pattern' : ?0, 'options' : 'i'}}}",
            sort ="{county:1 , township:1, wellName:1 , wellNumber:1}" )
    List<Well> findAllByWellNameRegexOrderByCountyAscTownshipAscWellNameAscWellNumberAsc(String wellName);
}
