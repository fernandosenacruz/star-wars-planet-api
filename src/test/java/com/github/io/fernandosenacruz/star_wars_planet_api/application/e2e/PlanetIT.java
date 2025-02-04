package com.github.io.fernandosenacruz.star_wars_planet_api.application.e2e;

import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.io.fernandosenacruz.star_wars_planet_api.common.PlanetConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("it")
@Sql(scripts = {"/insert_many_planets.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/truncate_planets.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) public class PlanetIT {
    @Autowired private TestRestTemplate restTemplate;

    // TODO success cases

    @Test public void createPlanet_ReturnsCreated() {
        ResponseEntity<Planet> planet_sut = restTemplate.postForEntity("/planets", HOTH, Planet.class);

        assertThat(planet_sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(planet_sut.getBody()).getId()).isNotNull();
        assertThat(planet_sut.getBody().getName()).isEqualTo(HOTH.getName());
        assertThat(planet_sut.getBody().getClimate()).isEqualTo(HOTH.getClimate());
        assertThat(planet_sut.getBody().getTerrain()).isEqualTo(HOTH.getTerrain());
    }

    @Test public void getPlanet_ReturnsPlanet() {
        ResponseEntity<Planet> planet_sut = restTemplate.getForEntity("/planets/1", Planet.class);

        assertThat(planet_sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(planet_sut.getBody()).getId()).isNotNull();
        assertThat(planet_sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(planet_sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(planet_sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test public void getPlanetByName_ReturnsPlanet() {
        ResponseEntity<Planet> planet_sut =
                restTemplate.getForEntity("/planets/name/" + PLANET.getName(), Planet.class);

        assertThat(planet_sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(planet_sut.getBody()).getId()).isNotNull();
        assertThat(planet_sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(planet_sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(planet_sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test public void getPlanets_ReturnsAllPlanets() {
        ResponseEntity<Planet[]> response = restTemplate.getForEntity("/planets", Planet[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Planet> planetList = Arrays.asList(Objects.requireNonNull(response.getBody()));

        assertThat(planetList.stream().filter(Objects::nonNull).count()).isEqualTo(PLANETS.size()); assertThat(
                planetList.stream()
                          .map(p -> new Planet(null, p.getName(), p.getClimate(), p.getTerrain()))
                          .collect(Collectors.toList())).isEqualTo(PLANETS);
    }

    @Test public void getPlanetsByClimate_ReturnsPlanets() {
        ResponseEntity<Planet[]> response =
                restTemplate.getForEntity(
                        "/planets?" + String.format("climate=%s", ALDERAAN.getClimate()),
                        Planet[].class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Planet> planetList = Arrays.asList(Objects.requireNonNull(response.getBody()));
        List<Planet> planetsByClimate = Arrays.asList(ALDERAAN, YAVINIV);

        assertThat(planetList.stream().filter(Objects::nonNull).count()).isEqualTo(planetsByClimate.size());
        assertThat(planetList.stream()
                             .map(p -> new Planet(null, p.getName(), p.getClimate(), p.getTerrain()))
                             .collect(Collectors.toList())).isEqualTo(
                planetsByClimate);

    }

    @Test public void getPlanetsByTerrain_ReturnsPlanets() {
        ResponseEntity<Planet[]> response =
                restTemplate.getForEntity(
                        "/planets?" + String.format("terrain=%s", YAVINIV.getTerrain()),
                        Planet[].class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Planet> planetList = Arrays.asList(Objects.requireNonNull(response.getBody()));

        assertThat(planetList.stream().filter(Objects::nonNull).count()).isEqualTo(1);
        assertThat(planetList.stream().map(p -> new Planet(null, p.getName(), p.getClimate(), p.getTerrain()))
                             .collect(Collectors.toList())).isEqualTo(List.of(YAVINIV));
    }

    @Test public void removePlanet_ReturnsNoContent() {
        ResponseEntity<Void> response =
                restTemplate.exchange("/planets/1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
