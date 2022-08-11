package com.continuing.development.probonorest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection="camphire_drilling_wells")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Well {
    @Id
    private String id;
    private String apiNumber;
    private String permitNumber;
    private String wellName;
    private String wellNumber;
    @Field("county")
    private String countyName;
    @Field("township")
    private String townshipName;
    private List<Production> production;
}
