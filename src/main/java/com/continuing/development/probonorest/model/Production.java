package com.continuing.development.probonorest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Production {
    private String type;
    private double quantity;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payedDate;

    public double getProdQtyByType(String type){
        if(ObjectUtils.isNotEmpty(this) &&
                ObjectUtils.isNotEmpty(this.getType()) &&
                this.getType().equalsIgnoreCase(type)){
            return this.getQuantity();
        }
        return 0;
    }
}
