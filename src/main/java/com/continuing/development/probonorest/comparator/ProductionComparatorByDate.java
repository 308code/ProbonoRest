package com.continuing.development.probonorest.comparator;

import com.continuing.development.probonorest.model.Production;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;

public class ProductionComparatorByDate implements Comparator<Production> {
    @Override
    public int compare(Production production1, Production production2) {
        if(!ObjectUtils.isEmpty(production1) && !ObjectUtils.isEmpty(production2) &&
        !ObjectUtils.isEmpty(production1.getPayedDate()) && !ObjectUtils.isEmpty(production2.getPayedDate()) ) {
            return production1.getPayedDate().compareTo(production2.getPayedDate());
        }else{
            return 0;
        }
    }
}
