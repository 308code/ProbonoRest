package com.continuing.development.probonorest.dao;

import com.continuing.development.probonorest.model.Well;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WellDao extends MongoRepository<Well,String> {
    List<Well> findAllByOrderByCountyNameAscTownshipNameAscWellNameAscWellNumberAsc();
    Well findWellById(String id);

}
