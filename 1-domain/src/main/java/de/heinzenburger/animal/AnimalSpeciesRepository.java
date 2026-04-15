package de.heinzenburger.animal;

import de.heinzenburger.shared.BiomeType;

import java.util.List;

public interface AnimalSpeciesRepository {
    List<AnimalSpecies> findAll();
    List<AnimalSpecies> findByLevel(int level);
    List<AnimalSpecies> findByHabitat(BiomeType biomeType);
}
