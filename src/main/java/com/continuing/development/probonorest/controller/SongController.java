package com.continuing.development.probonorest.controller;

import com.continuing.development.probonorest.aspect.LogMethod;
import com.continuing.development.probonorest.model.Song;
import com.continuing.development.probonorest.service.SongService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/songs")
public class SongController {
    private static final String URI = "http://18.223.159.186:8080/";
    //private static final String URI = "http://localhost:8080/";
    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    private static Calendar CAL = Calendar.getInstance();
    static {
        SDF.setTimeZone(TimeZone.getTimeZone("EST"));
        CAL.setTimeZone(TimeZone.getTimeZone("EST"));
    }
    private SongService songService;

    @Autowired
    public SongController(SongService songService){
        this.songService = songService;
    }

    @LogMethod(level="INFO")
    @PostMapping( produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createSong(@RequestBody Song song){
        String id = UUID.randomUUID().toString().replace("-","");
        song.setId(id);
        ResponseEntity<String> newSongId = songService.createSong(song);
        HttpHeaders httpHeaders = addCountToHeader(song);
        if(StringUtils.isEmpty(newSongId.getBody())){
            return new ResponseEntity<String>(httpHeaders,HttpStatus.NO_CONTENT);
        }
        httpHeaders.add("location",URI + "api/songs/" + newSongId);
        return new ResponseEntity<String>(newSongId.getBody(), httpHeaders, HttpStatus.CREATED);
    }

    @LogMethod(level="INFO")
    @GetMapping(value = "{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Song> getSongById(@PathVariable String id){
        Song song = songService.getSongById(id);
        HttpHeaders httpHeaders = addCountToHeader(song);
        if(ObjectUtils.isEmpty(song)){
            return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(song,httpHeaders,HttpStatus.OK);
    }

    @LogMethod(level="INFO")
    @GetMapping(value = "artist/{name}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Song>> getSongsByArtist(@PathVariable String name){
        List<Song> songs = songService.getSongsByArtistName(name);
        HttpHeaders httpHeaders = addCountToHeader(songs);
        if(CollectionUtils.isEmpty(songs)){
            return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(songs,httpHeaders,HttpStatus.OK);
    }

    @LogMethod(level="INFO")
    @GetMapping(value = "title/{name}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Song>> getSongsByTitle(@PathVariable String name){
        List<Song> songs = songService.getSongsByTitleName(name);
        HttpHeaders httpHeaders = addCountToHeader(songs);
        if(CollectionUtils.isEmpty(songs)){
            return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(songs,httpHeaders,HttpStatus.OK);
    }

    @LogMethod(level="INFO")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Song>> getAllSongs(){
        List<Song> songs = songService.getAllSongs();
        HttpHeaders httpHeaders = addCountToHeader(songs);
        if(CollectionUtils.isEmpty(songs)){
            return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(songs,httpHeaders,HttpStatus.OK);
    }

    @LogMethod(level="INFO")
    @GetMapping(value="{from}/{to}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Song>> getAllSongsPlayedBetweenDates(@PathVariable("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                                    @PathVariable("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        from.setDate(from.getDate() +1);
        List<Song> songs = songService.getAllSongsPlayedBetweenDates(prepDate(from), prepDate(to));
        HttpHeaders httpHeaders = addCountToHeader(songs);
        if(CollectionUtils.isEmpty(songs)){
            return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(songs,httpHeaders,HttpStatus.OK);
    }

    @LogMethod(level="INFO")
    @GetMapping(value="/played" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Song>> getAllSongsOrderedByMostRecentlyPlayed(){
        List<Song> songs = songService.getAllSongsByPlayedDate();
        HttpHeaders httpHeaders = addCountToHeader(songs);
        if(CollectionUtils.isEmpty(songs)){
            return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(songs,httpHeaders,HttpStatus.OK);
    }

    @LogMethod(level="INFO")
    @PutMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Song> putSong(@RequestBody Song song){
        Song updatedSong = songService.updateSong(song);
        HttpHeaders httpHeaders = addCountToHeader(updatedSong);
        if(ObjectUtils.isEmpty(updatedSong)){
            return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(updatedSong, httpHeaders, HttpStatus.OK);
    }

    @LogMethod(level="INFO")
    @DeleteMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteSongById(@PathVariable String id){
        boolean successfullyDeleted = songService.deleteSongById(id);
        if(successfullyDeleted){
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        }
        return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NO_CONTENT);
    }

    private HttpHeaders addCountToHeader(List<Song> songList){
        HttpHeaders headers = new HttpHeaders();

        if(CollectionUtils.isEmpty(songList)){
            songList = new ArrayList<>(0);
        }
        headers.add("count", String.valueOf(songList.size()));
        return headers;
    }

    private HttpHeaders addCountToHeader(Song song){
        HttpHeaders headers = new HttpHeaders();

        if (org.apache.commons.lang3.ObjectUtils.isEmpty(song)) {
            headers.add("count", String.valueOf(0));
        } else {
            headers.add("count", String.valueOf(1));
        }
        return headers;
    }
    private Date prepDate(Date incoming){
        CAL.setTime(incoming);
        return CAL.getTime();
    }
}
