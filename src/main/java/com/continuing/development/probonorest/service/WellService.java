package com.continuing.development.probonorest.service;

import com.continuing.development.probonorest.model.Well;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WellService {
    ResponseEntity<String> createWell(Well well);
    ResponseEntity<Well> updateWell(Well well);

    ResponseEntity<Boolean> deleteWellById(String id);

    ResponseEntity<List<Well>> getAllWells();
    ResponseEntity<List<Well>> getAllWellsOrderedByMostRecentProduction();


        ResponseEntity<Well> getWellById(String id);
}
