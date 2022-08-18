package com.continuing.development.probonorest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WellReport {
    @EqualsAndHashCode.Include
    private String id;
    @EqualsAndHashCode.Include
    private String apiNumber;
    private String permitNumber;
    private String wellNameAndNumber;
    private String countyName;
    private String townshipName;
    private double oilTotal;
    private double gasTotal;
    private double brineTotal;

    public WellReport(Well well, double oil, double gas, double brine){
        this.id = well.getId();
        this.apiNumber = well.getApiNumber();
        this.permitNumber = well.getPermitNumber();
        this.wellNameAndNumber = well.getWellName() + " #" + well.getWellNumber();
        this.countyName = well.getCountyName();
        this.townshipName = well.getTownshipName();
        this.oilTotal = oil;
        this.gasTotal = gas;
        this.brineTotal = brine;
    }
}
