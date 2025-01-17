package com.github.io.fernandosenacruz.star_wars_planet_api.application.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class PlanetQueryBuilder {
    public static Example<Planet> example(Planet planet) {
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreCase()
                .withIgnoreNullValues();

        return Example.of(planet, matcher);
    }
}
