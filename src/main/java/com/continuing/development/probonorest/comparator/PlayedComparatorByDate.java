package com.continuing.development.probonorest.comparator;

import com.continuing.development.probonorest.model.Played;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;

public class PlayedComparatorByDate implements Comparator<Played> {
    @Override
    public int compare(Played played1, Played played2) {
        if(!ObjectUtils.isEmpty(played1) && !ObjectUtils.isEmpty(played2) &&
        !ObjectUtils.isEmpty(played1.getDatePlayed()) && !ObjectUtils.isEmpty(played2.getDatePlayed()) ) {
            return played1.getDatePlayed().compareTo(played2.getDatePlayed());
        }else{
            return 0;
        }
    }
}
