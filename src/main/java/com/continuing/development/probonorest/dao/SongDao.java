package com.continuing.development.probonorest.dao;

import com.continuing.development.probonorest.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
public interface SongDao extends MongoRepository<Song,String> {
    //Find songs played in the given date range and returned Sorted by title then artist all in ASC order
    @Query(value = "{ 'played.datePlayed' : { '$gte' : { '$date' : ?0}, '$lte' : { '$date' : ?1}}}",sort = "{title: 1, artist: 1}")
    List<Song> findAllByDatePlayedBetweenOrderByTitleAscArtistAsc(Date fromDate, Date toDate);

    //Find all songs from a particular artist sorted by artist then by title both in ASC order
    @Query(value= "{'artist': { '$regularExpression' : { 'pattern' : ?0, 'options' : 'i'}}}",
            sort ="{artist:1, title:1}" )
    List<Song> findAllByArtistRegexIgnoreCaseOrderByArtistAscTitleAsc(String artist);

    //Find all songs with a particular title (either in title or aka) sorted by title then by artist both in ASC order
    @Query(value= "{$or : [{'title': { '$regularExpression' : { 'pattern' : ?0, 'options' : 'i'}}}, {'aka': { '$regularExpression' : { 'pattern' : ?0, 'options' : 'i'}}}]}",
            sort ="{title:1 , artist:1}" )
    List<Song> findAllByTitleRegexIgnoreCaseOrderByTitleAscArtistAsc(String title);

}
