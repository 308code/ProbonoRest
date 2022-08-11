package com.continuing.development.probonorest.comparator;

import com.continuing.development.probonorest.model.Well;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Comparator;

public class WellComparatorByLastProducedDate implements Comparator<Well> {

    @Override
    public int compare(Well well1, Well well2) {
        if(datePresent(well1) && datePresent(well2)) {
            return well1.getProduction().get(well1.getProduction().size()-1).getPayedDate()
                    .compareTo(well2.getProduction().get(well2.getProduction().size()-1).getPayedDate());
        }
        return 0;
    }

    private boolean datePresent(Well well){
        return ObjectUtils.isNotEmpty(well) && CollectionUtils.isNotEmpty(well.getProduction()) &&
                ObjectUtils.allNotNull(well.getProduction().get(well.getProduction().size()-1).getPayedDate());
    }
}
