package com.github.io.fernandosenacruz.star_wars_planet_api.common;

import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.Planet;

public class PlanetConstants {
    public static final Planet PLANET = new Planet("Tatooine", "arid", "desert");
    public static final Planet INVALID_PLANET = new Planet("", "", "");
}
