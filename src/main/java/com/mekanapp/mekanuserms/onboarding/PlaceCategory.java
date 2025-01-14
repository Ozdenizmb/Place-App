package com.mekanapp.mekanuserms.onboarding;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "place_categories", schema = "util_sch")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceCategory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "status")
    private String status;
}
