package com.github.io.fernandosenacruz.star_wars_planet_api.common;

import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.Planet;

import java.util.Arrays;
import java.util.List;

public class PlanetConstants {
    public static final Planet PLANET = new Planet("Tatooine", "arid", "desert");
    public static final Planet INVALID_PLANET = new Planet("", "", "");
    public static final Planet ALDERAAN = new Planet("Alderaan", "temperate", "grassland, mountains");
    public static final Planet YAVINIV = new Planet("Yavin IV", "temperate, tropical", "jungle, rainforest");
    public static final List<Planet> PLANETS = Arrays.asList(PLANET, ALDERAAN, YAVINIV);
}
