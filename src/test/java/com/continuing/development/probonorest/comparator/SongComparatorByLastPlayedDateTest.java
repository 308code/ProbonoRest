package com.continuing.development.probonorest.comparator;

import com.continuing.development.probonorest.model.Song;
import com.flextrade.jfixture.JFixture;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SongComparatorByLastPlayedDateTest {
    SongComparatorByLastPlayed songComparatorByLastPlayed = new SongComparatorByLastPlayed();
    JFixture fixture = new JFixture();

        @Test
        void compare_Obj1GreaterThanObj2() {
            Song song_a = fixture.create(Song.class);
            Song song_b = fixture.create(Song.class);
            Calendar prodCal1 = Calendar.getInstance();
            Calendar prodCal2 = Calendar.getInstance();
            prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) + 1);
            song_a.getPlayed().get(2).setDatePlayed(prodCal1.getTime());
            song_b.getPlayed().get(2).setDatePlayed(prodCal2.getTime());
            assertEquals(1, songComparatorByLastPlayed.compare(song_a,song_b));
        }

    @Test
    void compare_Obj1LessThanObj2() {
        Song song_a = fixture.create(Song.class);
        Song song_b = fixture.create(Song.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) - 1);
        song_a.getPlayed().get(2).setDatePlayed(prodCal1.getTime());
        song_b.getPlayed().get(2).setDatePlayed(prodCal2.getTime());
        assertEquals(-1, songComparatorByLastPlayed.compare(song_a,song_b));
    }

    @Test
    void compare_Obj1EqualsObj2() {
        Song song_a = fixture.create(Song.class);
        Song song_b = fixture.create(Song.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        song_a.getPlayed().get(2).setDatePlayed(prodCal1.getTime());
        song_b.getPlayed().get(2).setDatePlayed(prodCal2.getTime());
        assertEquals(0, songComparatorByLastPlayed.compare(song_a,song_b));
    }

    @Test
    void compare_Obj1ProductionListIsNull() {
        Song song_b = fixture.create(Song.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) + 1);
        song_b.getPlayed().get(2).setDatePlayed(prodCal2.getTime());
        assertEquals(0, songComparatorByLastPlayed.compare(null,song_b));
    }

    @Test
    void compare_Obj1IsNull() {
        Song song_a = fixture.create(Song.class);
        Song song_b = fixture.create(Song.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) + 1);
        song_a.getPlayed().get(2).setDatePlayed(prodCal1.getTime());
        song_b.getPlayed().get(2).setDatePlayed(prodCal2.getTime());
        song_a.getPlayed().get(2).setDatePlayed(null);
        song_a.setPlayed(null);
        assertEquals(0, songComparatorByLastPlayed.compare(song_a,song_b));
    }

    @Test
    void compare_Obj1LastProductionsProducedDateIsNull() {
        Song song_a = fixture.create(Song.class);
        Song song_b = fixture.create(Song.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) + 1);
        song_a.getPlayed().get(2).setDatePlayed(prodCal1.getTime());
        song_b.getPlayed().get(2).setDatePlayed(prodCal2.getTime());
        song_a.getPlayed().get(2).setDatePlayed(null);
        assertEquals(0, songComparatorByLastPlayed.compare(song_a,song_b));
    }
}