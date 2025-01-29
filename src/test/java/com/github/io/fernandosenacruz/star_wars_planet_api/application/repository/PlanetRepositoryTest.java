package com.github.io.fernandosenacruz.star_wars_planet_api.application.repository;

import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.Planet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static com.github.io.fernandosenacruz.star_wars_planet_api.common.PlanetConstants.PLANET;
import static com.github.io.fernandosenacruz.star_wars_planet_api.common.PlanetConstants.INVALID_PLANET;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void createPlanet_WithValidData_ShouldCreatePlanet() {
        Planet planet = planetRepository.save(PLANET);
        Planet planet_sut = testEntityManager.find(Planet.class, planet.getId());

        assertThat(planet_sut).isNotNull();
        assertThat(planet_sut.getName()).isEqualTo(planet.getName());
        assertThat(planet_sut.getClimate()).isEqualTo(planet.getClimate());
        assertThat(planet_sut.getTerrain()).isEqualTo(planet.getTerrain());
    }

    @Test
    public void createPlanet_WithInvalidData_ShouldThrowException() {
        assertThatThrownBy(() -> planetRepository.save(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createPlanet_WithExistingPlanet_ShouldThrowException() {
        Planet planet = testEntityManager.persistAndFlush(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);

        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }
}
