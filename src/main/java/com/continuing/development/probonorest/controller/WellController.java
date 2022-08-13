package com.continuing.development.probonorest.controller;

import com.continuing.development.probonorest.model.Well;
import com.continuing.development.probonorest.service.WellService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/wells")
public class WellController {
    private final WellService wellService;

    @Autowired
    public WellController(WellService wellService){
        this.wellService = wellService;
    }

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createWell(@RequestBody Well well){return wellService.createWell(well);}

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Well> updateWell(@RequestBody Well well){return wellService.updateWell(well);}

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Well>> getAllWells(){
        return wellService.getAllWells();
    }

    @GetMapping(value="/recent-production" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Well>> getAllWellsByProduction(){
        return wellService.getAllWellsOrderedByMostRecentProduction();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Well> getWellById(@PathVariable("id") String id){
        return wellService.getWellById(id);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteWellById(@PathVariable("id") String id){
        return wellService.deleteWellById(id);
    }
}
