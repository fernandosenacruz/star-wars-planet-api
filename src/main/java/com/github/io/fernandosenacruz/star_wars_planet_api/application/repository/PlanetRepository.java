package com.github.io.fernandosenacruz.star_wars_planet_api.application.repository;

import com.github.io.fernandosenacruz.star_wars_planet_api.application.domain.Planet;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanetRepository
        extends JpaRepository<Planet, Long>, QueryByExampleExecutor<Planet>
{
    Optional<Planet> findByName(String name);

    @Override
    <S extends Planet> List<S> findAll(Example<S> example);
}
