package com.continuing.development.probonorest.comparator;

import com.continuing.development.probonorest.model.Production;
import com.flextrade.jfixture.JFixture;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class ProductionComparatorByDateTest {
    ProductionComparatorByDate productionComparatorByDate = new ProductionComparatorByDate();
    JFixture fixture = new JFixture();
    @Test
    void compare_Obj1GreaterThanObj2() {
        Production production_a = fixture.create(Production.class);
        Production production_b = fixture.create(Production.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) + 1);
        production_a.setPayedDate(prodCal1.getTime());
        production_b.setPayedDate(prodCal2.getTime());
        assertEquals(1, productionComparatorByDate.compare(production_a,production_b));
    }

    @Test
    void compare_Obj1LessThanObj2() {
        Production production_a = fixture.create(Production.class);
        Production production_b = fixture.create(Production.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = Calendar.getInstance();
        prodCal1.set(Calendar.HOUR_OF_DAY, prodCal1.get(Calendar.HOUR_OF_DAY) - 1);
        production_a.setPayedDate(prodCal1.getTime());
        production_b.setPayedDate(prodCal2.getTime());
        assertEquals(-1, productionComparatorByDate.compare(production_a,production_b));
    }

    @Test
    void compare_Obj1EqualsObj2() {
        Production production_a = fixture.create(Production.class);
        Production production_b = fixture.create(Production.class);
        Calendar prodCal1 = Calendar.getInstance();
        Calendar prodCal2 = prodCal1;
        production_a.setPayedDate(prodCal1.getTime());
        production_b.setPayedDate(prodCal2.getTime());
        assertEquals(0, productionComparatorByDate.compare(production_a,production_b));
    }
}