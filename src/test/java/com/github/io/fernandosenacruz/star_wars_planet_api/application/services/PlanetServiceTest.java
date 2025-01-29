package com.github.io.fernandosenacruz.star_wars_planet_api.application.services;

import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.Planet;
import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.PlanetQueryBuilder;
import com.github.io.fernandosenacruz.star_wars_planet_api.application.repository.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.*;

import static com.github.io.fernandosenacruz.star_wars_planet_api.common.PlanetConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {
    @InjectMocks
    private PlanetService planetService;

    @Mock
    private PlanetRepository planetRepository;

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        Planet planet_sut = planetService.create(PLANET);

        assertThat(planet_sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        Long id = anyLong();
        when(planetRepository.findById(id)).thenReturn(Optional.of(PLANET));

        Optional<Planet> planet_sut = planetService.get(id);

        assertThat(planet_sut).isNotEmpty();
        assertThat(planet_sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByInvalidId_ReturnsEmpty() {
        Long id = anyLong();
        when(planetRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Planet> planet_sut = planetService.get(id);

        assertThat(planet_sut).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        Optional<Planet> planet_sut = planetService.getByName(PLANET.getName());

        assertThat(planet_sut).isNotEmpty();
        assertThat(planet_sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByInvalidName_ReturnsEmpty() {
        String INVALID_NAME = anyString();
        when(planetRepository.findByName(INVALID_NAME)).thenReturn(Optional.empty());

        Optional<Planet> planet_sut = planetService.getByName(INVALID_NAME);

        assertThat(planet_sut).isEmpty();
    }

    @Test
    public void getPlanets_ReturnsAllPlanets() {
        List<Planet> planets = new ArrayList<>() {{ add(PLANET); }};
        Example<Planet> query = PlanetQueryBuilder.example(new Planet(PLANET.getClimate(), PLANET.getName()));
        when(planetRepository.findAll(query)).thenReturn(planets);

        List<Planet> planets_sut = planetService.getPlanets(PLANET.getClimate(), PLANET.getName());

        assertThat(planets_sut).isNotEmpty();
        assertThat(planets_sut.size()).isEqualTo(planets.size());
        assertThat(planets_sut.stream().findFirst().orElseThrow()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanets_ReturnsEmpty() {
        when(planetRepository.findAll((Example<Planet>) any())).thenReturn(Collections.emptyList());

        List<Planet> planets_sut = planetService.getPlanets(PLANET.getClimate(), PLANET.getName());

        assertThat(planets_sut).isEmpty();
    }

    @Test
    public void deletePlanet_WithValidId_doesNotThrowAnyException() {
        assertThatCode(() -> planetService.delete(anyLong())).doesNotThrowAnyException();
    }

    @Test void deletePlanet_WithInvalidId_ThrowsException() {
        doThrow(new RuntimeException()).when(planetRepository).deleteById(anyLong());

        assertThatThrownBy(() -> planetService.delete(anyLong())).isInstanceOf(RuntimeException.class);
    }
}
