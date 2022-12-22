package com.continuing.development.probonorest.service;

import com.continuing.development.probonorest.model.Song;

import java.util.Date;
import java.util.List;

public interface SongService {
    String createSong(Song song);
    List<Song> getAllSongs();
    List<Song> getAllSongsByPlayedDate();
    Song getSongById(String id);
    List<Song> getSongsByArtistName(String artistName);
    List<Song> getSongsByTitleName(String titleName);
    List<Song> getAllSongsPlayedBetweenDates(Date from, Date to);
    Song updateSong(Song song);
    boolean deleteSongById(String id);

}
