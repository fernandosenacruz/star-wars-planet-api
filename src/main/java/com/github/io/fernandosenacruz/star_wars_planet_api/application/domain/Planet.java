package com.github.io.fernandosenacruz.star_wars_planet_api.application.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "planets", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String climate;

    @NotEmpty
    private String terrain;

    public Planet(Long id, String name, String climate, String terrain) {
        this.id = id;
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public Planet(String name, String climate, String terrain) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public Planet(String climate, String terrain) {
        this.climate = climate;
        this.terrain = terrain;
    }
}
