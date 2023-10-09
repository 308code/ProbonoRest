package com.continuing.development.probonorest.service;

import com.continuing.development.probonorest.comparator.PlayedComparatorByDate;
import com.continuing.development.probonorest.comparator.SongComparatorByLastPlayed;
import com.continuing.development.probonorest.dao.SongDao;
import com.continuing.development.probonorest.model.Played;
import com.continuing.development.probonorest.model.Song;
import com.continuing.development.probonorest.model.Well;
import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SongServiceMongoDbImpl implements SongService{
    private final SongComparatorByLastPlayed songComparator = new SongComparatorByLastPlayed();
    private final PlayedComparatorByDate playedComparatorByDate = new PlayedComparatorByDate();
    private final SongDao songDao;

    @Autowired
    public SongServiceMongoDbImpl(SongDao songDao){
        this.songDao = songDao;
    }


    @Override
    public ResponseEntity<String> createSong(Song song){
        try{
            song.setId(UUID.randomUUID().toString().replace("-",""));
            Song result = songDao.insert(song);
            return new ResponseEntity<>(result.getId(), HttpStatus.CREATED);
        }catch (MongoException e){
            return new ResponseEntity<>("Not Created",HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public Song getSongById(String id) {
        Song song;
        List<Played> tempPlayedList;
        try{
            song = songDao.findById(id).orElse(null);
            if(!ObjectUtils.isEmpty(song)) {
                tempPlayedList = song.getPlayed();
                if (!CollectionUtils.isEmpty(tempPlayedList)) {
                    tempPlayedList.sort(playedComparatorByDate.reversed());
                }
                song.setPlayed(tempPlayedList);
            }
        }catch (MongoException e){
            log.error("Error getting all Songs:  MESSAGE: {}  STACKTRACE:  {}", e.getMessage(), e.getStackTrace());
            return null;
        }
        return song;
    }

    @Override
    public List<Song> getAllSongs() {
        List<Song> songs;
        try{
            songs = songDao.findAll();
        }catch (MongoException e){
            log.error("Error getting all Songs:  MESSAGE: {}  STACKTRACE:  {}", e.getMessage(), e.getStackTrace());
            return new ArrayList<>();
        }
        return songs;
    }

    @Override
    public List<Song> getAllSongsByPlayedDate() {
        List<Song> songs = new ArrayList<>();
        try{
            songs = songDao.findAll();
        }catch (MongoException e){
            log.error("Error getting all songs:  MESSAGE: {}  STACKTRACE:  {}", e.getMessage(), e.getStackTrace());
            return songs;
        }
        List<Song> modifiableList = (CollectionUtils.isEmpty(songs) ? new ArrayList<>() : new ArrayList<>(songs));
        if(! CollectionUtils.isEmpty(modifiableList)){
            modifiableList.sort(songComparator.reversed());
        }
        return modifiableList;
    }

    @Override
    public List<Song> getAllSongsPlayedBetweenDates(Date from, Date to){
        List<Song> wells;
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(from);
        Calendar calTo = Calendar.getInstance();
        calTo.setTime(to);
        try{
            calFrom.set(Calendar.HOUR_OF_DAY,0);
            calFrom.set(Calendar.MINUTE,0);
            calFrom.set(Calendar.SECOND,0);
            calTo.set(Calendar.HOUR_OF_DAY,23);
            calTo.set(Calendar.MINUTE,59);
            calTo.set(Calendar.SECOND,59);

            wells = songDao.findAllByDatePlayedBetweenOrderByTitleAscArtistAsc(calFrom.getTime(),calTo.getTime());
        }catch (MongoException e){
            log.error("Error getting all Songs Played between " + from + " and " + to + ":  MESSAGE: {}  STACKTRACE:  {}", e.getMessage(), e.getStackTrace());
            return new ArrayList<>();
        }
        return wells;
    }

    @Override
    public List<Song> getSongsByArtistName(String artistName) {
        List<Song> songs;
        songs = songDao.findAllByArtistRegexIgnoreCaseOrderByArtistAscTitleAsc(artistName);
        return songs;
    }

    @Override
    public List<Song> getSongsByTitleName(String titleName) {
        List<Song> songs;
        songs = songDao.findAllByTitleRegexIgnoreCaseOrderByTitleAscArtistAsc(titleName);
        return songs;
    }


//    @Override
//    public ResponseEntity<String> createWell(@RequestBody Well well){
//        try{
//            well.setId(UUID.randomUUID().toString().replace("-",""));
//            Well result = wellDao.insert(well);
//            return new ResponseEntity<>(result.getId(),HttpStatus.CREATED);
//        }catch (MongoException e){
//            return new ResponseEntity<>("Not Created",HttpStatus.NO_CONTENT);
//        }
//    }
//    @Override
//    public ResponseEntity<List<Well>> getAllWells() {
//        List<Well> wells;
//        try{
//            wells = wellDao.findAllByOrderByCountyNameAscTownshipNameAscWellNameAscWellNumberAsc();
//        }catch (MongoException e){
//            log.error("Error getting all wells:  MESSAGE: {}  STACKTRACE:  {}", e.getMessage(), e.getStackTrace());
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        HttpHeaders headers = addCountToHeader(wells);
//        return getListResponseEntity(wells,headers);
//    }
//    public ResponseEntity<List<Well>> getAllWellsOrderedByMostRecentProduction(){
//        List<Well> wells;
//        try{
//            wells = wellDao.findAll();
//        }catch (MongoException e){
//            log.error("Error getting all wells:  MESSAGE: {}  STACKTRACE:  {}", e.getMessage(), e.getStackTrace());
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        List<Well> modifiableList = (CollectionUtils.isEmpty(wells) ? new ArrayList<>() : new ArrayList<>(wells));
//        if(! CollectionUtils.isEmpty(modifiableList)){
//            //Collections.sort(modifiableList,wellComparator.reversed());
//            modifiableList.sort(wellComparator.reversed());
//        }
//
//        HttpHeaders headers = addCountToHeader(modifiableList);
//        return getListResponseEntity(modifiableList,headers);
//    }
//
//    @Override
//    public ResponseEntity<List<WellReport>> generateProductionReportWithinDateRange(Date from, Date to) {
//        List<WellReport> results = new ArrayList<>();
//        List<Well> wells = wellDao.findAllByProductionPayedDateBetweenOrderByCountyAscTownshipAscWellNameAscWellNumberAsc(from, to);
//        wells.forEach( well -> well.getProduction().forEach(production -> {
//            if(production.getPayedDate().after(from) && production.getPayedDate().before(to)){
//                WellReport wellReport = new WellReport(well,production.getProdQtyByType("oil"),
//                        production.getProdQtyByType("gas"),production.getProdQtyByType("brine"));
//                int i = results.indexOf(wellReport);
//
//                if(i == -1){
//                    results.add(wellReport);
//                }else {
//                    results.get(i).setOilTotal(results.get(i).getOilTotal() + wellReport.getOilTotal());
//                    results.get(i).setGasTotal(results.get(i).getGasTotal() + wellReport.getGasTotal());
//                    results.get(i).setBrineTotal(results.get(i).getBrineTotal() + wellReport.getBrineTotal());
//                }
//            }
//        }));
//        return new ResponseEntity<>(results,HttpStatus.OK);
//    }
//
//    @Override
//    public ResponseEntity<Well> getWellById(String id)  {
//        Well well;
//        List<Production> tempProdList = null;
//        try{
//            Optional<Well> optional = wellDao.findById(id);
//            well = optional.orElse(null);
//            if(!ObjectUtils.isEmpty(well)){
//                tempProdList = well.getProduction();
//            }
//        }catch (MongoException e){
//            log.error("Error getting well with id: {} MESSAGE: {}  STACKTRACE: {}", id, e.getMessage(), e.getStackTrace());
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        if((!CollectionUtils.isEmpty(tempProdList)) && (tempProdList.size() > 1)){
//            tempProdList.sort(this.productionComparatorByDate.reversed());
//        }
//
//        if(!ObjectUtils.isEmpty(well)) {
//            well.setProduction(tempProdList);
//        }
//
//        HttpHeaders headers = addCountToHeader(well);
//        if(ObjectUtils.isEmpty(well)){
//            return new ResponseEntity<>(headers,HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(well,headers,HttpStatus.OK);
//    }

//    private ResponseEntity<List<Song>> getListResponseEntity(List<Song> songs, HttpHeaders headers) {
//        if(CollectionUtils.isEmpty(songs)){
//            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(songs, headers, HttpStatus.OK);
//    }


    @Override
    public Song updateSong(Song song){
        if(songDao.existsById(song.getId())){
            return songDao.save(song);
        }
        return null;
    }

    @Override
    public boolean deleteSongById(String id){
        if(songDao.existsById(id)){
            songDao.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

//    private HttpHeaders addCountToHeader(List<Song> songList) {
//        HttpHeaders headers = new HttpHeaders();
//
//        if (CollectionUtils.isEmpty(songList)) {
//            songList = new ArrayList<>(0);
//        }
//        headers.add("count", String.valueOf(songList.size()));
//        return headers;
//    }
//
//    private HttpHeaders addCountToHeader(Song song) {
//        HttpHeaders headers = new HttpHeaders();
//
//        if (ObjectUtils.isEmpty(song)) {
//            headers.add("count", String.valueOf(0));
//        } else {
//            headers.add("count", String.valueOf(1));
//        }
//
//        return headers;
//    }
}
