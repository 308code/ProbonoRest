package com.continuing.development.probonorest.comparator;

import com.continuing.development.probonorest.model.Played;
import com.flextrade.jfixture.JFixture;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayedComparatorByDateTest {
    PlayedComparatorByDate playedComparatorByDate = new PlayedComparatorByDate();
    JFixture fixture = new JFixture();
    @Test
    void compare_Obj1GreaterThanObj2() {
        Played played_a = fixture.create(Played.class);
        Played played_b = fixture.create(Played.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) + 1);
        played_a.setDatePlayed(prodCal1.getTime());
        played_b.setDatePlayed(prodCal2.getTime());
        assertEquals(1, playedComparatorByDate.compare(played_a,played_b));
    }

    @Test
    void compare_Obj1LessThanObj2() {
        Played played_a = fixture.create(Played.class);
        Played played_b = fixture.create(Played.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) - 1);
        played_a.setDatePlayed(prodCal1.getTime());
        played_b.setDatePlayed(prodCal2.getTime());
        assertEquals(-1, playedComparatorByDate.compare(played_a,played_b));
    }

    @Test
    void compare_Obj1EqualsObj2() {
        Played played_a = fixture.create(Played.class);
        Played played_b = fixture.create(Played.class);
        Calendar prodCal1 = Calendar.getInstance();
        played_a.setDatePlayed(prodCal1.getTime());
        played_b.setDatePlayed(prodCal1.getTime());
        assertEquals(0, playedComparatorByDate.compare(played_a,played_b));
    }
}