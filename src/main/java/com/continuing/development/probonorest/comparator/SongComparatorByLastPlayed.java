package com.continuing.development.probonorest.comparator;

import com.continuing.development.probonorest.model.Song;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Comparator;

public class SongComparatorByLastPlayed implements Comparator<Song> {

    @Override
    public int compare(Song song1, Song song2) {
        if(datePresent(song1) && datePresent(song2)) {
            return song1.getPlayed().get(song1.getPlayed().size()-1).getDatePlayed()
                    .compareTo(song2.getPlayed().get(song2.getPlayed().size()-1).getDatePlayed());
        }
        return 0;
    }

    private boolean datePresent(Song song){
        return ObjectUtils.isNotEmpty(song) && CollectionUtils.isNotEmpty(song.getPlayed()) &&
                ObjectUtils.allNotNull(
                        song.getPlayed().get(song.getPlayed().size() -1).getDatePlayed());
    }
}
