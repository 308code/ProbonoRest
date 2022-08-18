package com.continuing.development.probonorest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        if(this.getType().equalsIgnoreCase(type)){
            return this.getQuantity();
        }
        return 0;
    }
}
