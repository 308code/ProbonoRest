package com.continuing.development.probonorest.comparator;

import com.continuing.development.probonorest.model.Well;
import com.flextrade.jfixture.JFixture;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class WellComparatorByLastProducedDateTest {
    WellComparatorByLastProducedDate wellComparatorByLastProducedDate = new WellComparatorByLastProducedDate();
    JFixture fixture = new JFixture();

        @Test
        void compare_Obj1GreaterThanObj2() {
            Well well_a = fixture.create(Well.class);
            Well well_b = fixture.create(Well.class);
            Calendar prodCal1 = Calendar.getInstance();
            Calendar prodCal2 = Calendar.getInstance();
            prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) + 1);
            well_a.getProduction().get(2).setPayedDate(prodCal1.getTime());
            well_b.getProduction().get(2).setPayedDate(prodCal2.getTime());
            assertEquals(1, wellComparatorByLastProducedDate.compare(well_a,well_b));
        }

    @Test
    void compare_Obj1LessThanObj2() {
        Well well_a = fixture.create(Well.class);
        Well well_b = fixture.create(Well.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) - 1);
        well_a.getProduction().get(2).setPayedDate(prodCal1.getTime());
        well_b.getProduction().get(2).setPayedDate(prodCal2.getTime());
        assertEquals(-1, wellComparatorByLastProducedDate.compare(well_a,well_b));
    }

    @Test
    void compare_Obj1EqualsObj2() {
        Well well_a = fixture.create(Well.class);
        Well well_b = fixture.create(Well.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = prodCal1;
        well_a.getProduction().get(2).setPayedDate(prodCal1.getTime());
        well_b.getProduction().get(2).setPayedDate(prodCal2.getTime());
        assertEquals(0, wellComparatorByLastProducedDate.compare(well_a,well_b));
    }

    @Test
    void compare_Obj1ProductionListIsNull() {
        Well well_b = fixture.create(Well.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) + 1);
        well_b.getProduction().get(2).setPayedDate(prodCal2.getTime());
        assertEquals(0, wellComparatorByLastProducedDate.compare(null,well_b));
    }

    @Test
    void compare_Obj1IsNull() {
        Well well_a = fixture.create(Well.class);
        Well well_b = fixture.create(Well.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) + 1);
        well_a.getProduction().get(2).setPayedDate(prodCal1.getTime());
        well_b.getProduction().get(2).setPayedDate(prodCal2.getTime());
        well_a.getProduction().get(2).setPayedDate(null);
        well_a.setProduction(null);
        assertEquals(0, wellComparatorByLastProducedDate.compare(well_a,well_b));
    }

    @Test
    void compare_Obj1LastProductionsProducedDateIsNull() {
        Well well_a = fixture.create(Well.class);
        Well well_b = fixture.create(Well.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) + 1);
        well_a.getProduction().get(2).setPayedDate(prodCal1.getTime());
        well_b.getProduction().get(2).setPayedDate(prodCal2.getTime());
        well_a.getProduction().get(2).setPayedDate(null);
        assertEquals(0, wellComparatorByLastProducedDate.compare(well_a,well_b));
    }
}