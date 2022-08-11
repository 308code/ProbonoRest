package com.continuing.development.probonorest.comparator;

import com.continuing.development.probonorest.model.Production;

import java.util.Comparator;

public class ProductionComparatorByDate implements Comparator<Production> {
    @Override
    public int compare(Production production1, Production production2) {
        return production1.getPayedDate().compareTo(production2.getPayedDate());
    }
}
