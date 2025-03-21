package com.github.io.fernandosenacruz.star_wars_planet_api.application.services;

import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.Planet;
import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.PlanetQueryBuilder;
import com.github.io.fernandosenacruz.star_wars_planet_api.application.repository.PlanetRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetService {
    private final PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public Planet create(Planet planet) {
        return planetRepository.save(planet);
    }

    public Optional<Planet> get(Long id) {
        return planetRepository.findById(id);
    }

    public Optional<Planet> getByName(String name) {
        return planetRepository.findByName(name);
    }

    public List<Planet> getPlanets(String climate, String terrain) {
        Example<Planet> query = PlanetQueryBuilder.example(new Planet(climate, terrain));
        return planetRepository.findAll(query);
    }

    public void delete(Long id) {
        planetRepository.deleteById(id);
    }
}
