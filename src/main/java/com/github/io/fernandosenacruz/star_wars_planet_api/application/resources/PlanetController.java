package com.github.io.fernandosenacruz.star_wars_planet_api.application.resources;

import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.Planet;
import com.github.io.fernandosenacruz.star_wars_planet_api.application.services.PlanetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planets")
public class PlanetController {
    @Autowired
    private PlanetService planetService;

    @PostMapping
    public ResponseEntity<Planet> createPlanet(@RequestBody @Valid Planet planet) {
         Planet newPlanet = planetService.create(planet);
         return ResponseEntity.status(HttpStatus.CREATED).body(newPlanet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> getPlanet(@PathVariable Long id) {
        return planetService.get(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Planet> getPlanetByName(@PathVariable String name) {
        return planetService.getByName(name).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Planet>> getPlanets(
            @RequestParam(required = false) String climate,
            @RequestParam(required = false) String terrain
    ) {
        List<Planet> planets = planetService.getPlanets(climate, terrain);
        return ResponseEntity.ok(planets);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanet(@PathVariable Long id) {
        planetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
