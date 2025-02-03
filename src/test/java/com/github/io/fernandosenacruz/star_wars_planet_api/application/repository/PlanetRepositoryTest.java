package com.github.io.fernandosenacruz.star_wars_planet_api.application.repository;

import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.Planet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static com.github.io.fernandosenacruz.star_wars_planet_api.common.PlanetConstants.PLANET;
import static com.github.io.fernandosenacruz.star_wars_planet_api.common.PlanetConstants.INVALID_PLANET;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    public void setNullPlanetId() {
        PLANET.setId(null);
    }

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
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);

        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ById_ReturnsPlanet() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> planet_sut = planetRepository.findById(planet.getId());

        assertThat(planet_sut.isPresent()).isTrue();
        assertThat(planet_sut.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanet_ByNonexistentId_ReturnsNotFound() {
        Optional<Planet> planet = planetRepository.findById(anyLong());

        assertThat(planet.isPresent()).isFalse();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> planet_sut = planetRepository.findByName(planet.getName());

        assertThat(planet_sut.isPresent()).isTrue();
        assertThat(planet_sut.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanet_ByNonexistentName_ReturnsNotFound() {
        Optional<Planet> planet_sut = planetRepository.findByName(anyString());

        assertThat(planet_sut.isPresent()).isFalse();
    }
}
