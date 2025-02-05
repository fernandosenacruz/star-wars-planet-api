package com.github.io.fernandosenacruz.star_wars_planet_api.application.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.io.fernandosenacruz.star_wars_planet_api.application.services.PlanetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.io.fernandosenacruz.star_wars_planet_api.common.PlanetConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {
    @MockBean
    private PlanetService planetService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createPlanet_WithVValidData_ReturnsCreated() throws Exception {
        when(planetService.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET))
                                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
               .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void createPlanet_WithVInvalidData_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(INVALID_PLANET))
                                        .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistingPlanet_ReturnsConflict() throws Exception {
        when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET))
                                        .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isConflict());
    }

    @Test
    public void getPlanet_ById_ReturnsPlanet() throws Exception {
        Long id = anyLong();
        when(planetService.get(id)).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/{id}", id))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByNonexistentId_ReturnsNotFound() throws Exception {
        Long id = anyLong();
        when(planetService.get(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/planets/{id}", id))
               .andExpect(status().isNotFound());
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
        when(planetService.getByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/name/" + PLANET.getName()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByNonexistentName_ReturnsNotFound() throws Exception {
        String name = anyString();
        when(planetService.getByName(name)).thenReturn(Optional.empty());

        mockMvc.perform(get("/planets/name/", name))
               .andExpect(status().isNotFound());
    }

    @Test
    public void getPlanets_ReturnsFilteredPlanets() throws Exception {
        when(planetService.getPlanets(null, null)).thenReturn(PLANETS);
        when(planetService.getPlanets(PLANET.getClimate(), PLANET.getTerrain())).thenReturn(List.of(PLANET));

        mockMvc.perform(get("/planets"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(PLANETS.size())));

        mockMvc.perform(
                       get("/planets?" + String.format(
                               "climate=%s&terrain=%s",
                               PLANET.getClimate(),
                               PLANET.getTerrain()
                       )))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0]").value(PLANET));
    }

    @Test
    public void getPlanets_ReturnsNoPlanets() throws Exception {
        when(planetService.getPlanets(null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/planets"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void removePlanet_WithExistingId_ShouldRemovePlanet() throws Exception {
        Long id = anyLong();

        mockMvc.perform(delete("/planets/{id}", id))
               .andExpect(status().isNoContent());
    }

    @Test
    public void removePlanet_WithNonexistentId_ShouldThrowException() throws Exception {
        Long id = 1L;
        doThrow(new EmptyResultDataAccessException(1)).when(planetService).delete(id);

        mockMvc.perform(delete("/planets/{id}", id))
               .andExpect(status().isNotFound());
    }
}
