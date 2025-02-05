package com.github.io.fernandosenacruz.star_wars_planet_api.application.e2e;

import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.github.io.fernandosenacruz.star_wars_planet_api.common.PlanetConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ActiveProfiles("it")
@Sql(scripts = {"/insert_many_planets.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/truncate_planets.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) @AutoConfigureWebTestClient
public class PlanetIT {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void createPlanet_ReturnsCreated() {
        webTestClient.post().uri("/planets").bodyValue(HOTH).exchange().expectStatus().isCreated()
                     .expectBody(Planet.class).value(planet -> {
                         assertThat(planet.getId()).isNotNull();
                         assertThat(planet.getName()).isEqualTo(HOTH.getName());
                         assertThat(planet.getClimate()).isEqualTo(HOTH.getClimate());
                         assertThat(planet.getTerrain()).isEqualTo(HOTH.getTerrain());
                     });
    }

    @Test
    public void getPlanet_ReturnsPlanet() {
        webTestClient.get().uri("/planets/1").exchange().expectStatus().isOk()
                     .expectBody(Planet.class).value(planet -> {
                         assertThat(planet.getId()).isNotNull();
                         assertThat(planet.getName()).isEqualTo(PLANET.getName());
                         assertThat(planet.getClimate()).isEqualTo(PLANET.getClimate());
                         assertThat(planet.getTerrain()).isEqualTo(PLANET.getTerrain());
                     });
    }

    @Test
    public void getPlanetByName_ReturnsPlanet() {
        webTestClient.get().uri("/planets/name/{name}", PLANET.getName()).exchange().expectStatus().isOk()
                     .expectBody(Planet.class).value(planet -> {
                         assertThat(planet.getId()).isNotNull();
                         assertThat(planet.getName()).isEqualTo(PLANET.getName());
                         assertThat(planet.getClimate()).isEqualTo(PLANET.getClimate());
                         assertThat(planet.getTerrain()).isEqualTo(PLANET.getTerrain());
                     });
    }

    @Test
    public void getPlanets_ReturnsAllPlanets() {
        webTestClient.get().uri("/planets").exchange().expectStatus().isOk()
                     .expectBodyList(Planet.class).value(planets -> {
                         assertThat(planets).isNotNull();
                         assertThat(planets.size()).isEqualTo(PLANETS.size());

                         List<Planet> normalizedPlanets = planets.stream()
                                                                 .map(p -> new Planet(
                                                                         null, p.getName(), p.getClimate(),
                                                                         p.getTerrain()
                                                                 )).toList();

                         assertThat(normalizedPlanets).isEqualTo(PLANETS);
                     });
    }

    @Test
    public void getPlanetsByClimate_ReturnsPlanets() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/planets")
                                                        .queryParam("climate", ALDERAAN.getClimate())
                                                        .build())
                     .exchange().expectStatus().isOk()
                     .expectBodyList(Planet.class).value(planets -> {
                         List<Planet> expected = List.of(ALDERAAN, YAVINIV);

                         assertThat(planets.size()).isEqualTo(expected.size());

                         List<Planet> normalizedPlanets = planets.stream()
                                                                 .map(p -> new Planet(
                                                                         null, p.getName(), p.getClimate(),
                                                                         p.getTerrain()
                                                                 )).toList();

                         assertThat(normalizedPlanets).isEqualTo(expected);
                     });
    }

    @Test
    public void getPlanetsByTerrain_ReturnsPlanets() {
        webTestClient.get()
                     .uri(uriBuilder -> uriBuilder.path("/planets")
                                                  .queryParam("terrain", YAVINIV.getTerrain()).build())
                     .exchange().expectStatus().isOk()
                     .expectBodyList(Planet.class).value(planets -> {
                         assertThat(planets.size()).isEqualTo(1);

                         planets.get(0).setId(null);

                         assertThat(planets.get(0)).usingRecursiveComparison().isEqualTo(YAVINIV);
                     });
    }

    @Test
    public void removePlanet_ReturnsNoContent() {
        webTestClient.delete().uri("/planets/1").exchange().expectStatus().isNoContent();
    }
}

