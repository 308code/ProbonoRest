package com.continuing.development.probonorest.model;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class ProductionTest {

    @Test
    void getProdQtyByType_WhenTypeMatchesWhatIsExpected() {
        String TYPE = "oil";
        double QTY = 23.43;
        Production production = Production.builder()
                .type(TYPE)
                .quantity(QTY)
                .payedDate(Calendar.getInstance().getTime()).build();
        assertEquals(QTY,production.getProdQtyByType(TYPE));
    }

    @Test
    void getProdQtyByType_WhenTypeDoesNotMatchesWhatIsExpected() {
        String TYPE = "oil";
        double QTY = 23.43;
        Production production = Production.builder()
                .type(TYPE)
                .quantity(QTY)
                .payedDate(Calendar.getInstance().getTime()).build();
        assertEquals(0,production.getProdQtyByType("gas"));
    }

    @Test
    void getProdQtyByType_WhenTypeIsEmpty() {
        String TYPE = "oil";
        double QTY = 23.43;
        Production production = Production.builder()
                .type("")
                .quantity(QTY)
                .payedDate(Calendar.getInstance().getTime()).build();
        assertEquals(0,production.getProdQtyByType(TYPE));
    }

    @Test
    void getProdQtyByType_WhenTypeIsNull() {
        String TYPE = "oil";
        double QTY = 23.43;
        Production production = Production.builder()
                .type(null)
                .quantity(QTY)
                .payedDate(Calendar.getInstance().getTime()).build();
        assertEquals(0,production.getProdQtyByType(TYPE));
    }
}