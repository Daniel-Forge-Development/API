package com.envyful.api.reforged.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnSet;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.BiomeHelper;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UtilPokemonInfo {

    public static List<String> getSpawnBiomes(Stats pokemon) {
        List<String> names = new ArrayList<>();

        for (List<SpawnSet> spawnSet : PixelmonSpawning.getAll().values()) {
            for (SpawnSet set : spawnSet) {
                for (SpawnInfo spawnInfo : set.spawnInfos) {
                    if (!(spawnInfo instanceof SpawnInfoPokemon)) {
                        continue;
                    }

                    SpawnInfoPokemon spawnInfoPokemon = (SpawnInfoPokemon)spawnInfo;

                    if (!spawnInfoPokemon.getSpecies().equals(pokemon.getParentSpecies()) || spawnInfoPokemon.spawnSpecificBossRate != null) {
                        continue;
                    }

                    for (ResourceLocation biome : spawnInfoPokemon.condition.biomes) {
                        String name = BiomeHelper.getLocalizedBiomeName(biome).getString();

                        if (!names.contains(name)) {
                            names.add(name);
                        }
                    }
                }
            }
        }

        return names;
    }

    public static List<String> getSpawnTimes(Stats pokemon) {
        List<String> names = new ArrayList<>();

        for (List<SpawnSet> spawnSet : PixelmonSpawning.getAll().values()) {
            for (SpawnSet set : spawnSet) {
                for (SpawnInfo spawnInfo : set.spawnInfos) {
                    if (!(spawnInfo instanceof SpawnInfoPokemon)) {
                        continue;
                    }

                    SpawnInfoPokemon spawnInfoPokemon = (SpawnInfoPokemon)spawnInfo;

                    if (!spawnInfoPokemon.getSpecies().equals(pokemon.getParentSpecies()) || spawnInfoPokemon.condition.times == null) {
                        continue;
                    }

                    for (WorldTime time : spawnInfoPokemon.condition.times) {
                        if (time == null || names.contains(time.getLocalizedName())) {
                            continue;
                        }

                        String name = time.getLocalizedName();

                        if (!names.contains(name)) {
                            names.add(name);
                        }
                    }
                }
            }
        }

        return names;
    }


    public static List<String> getCatchRate(Stats pokemon) {
        double males = pokemon.getMalePercentage();
        if (males == -1) {
            return Collections.singletonList("§7Base rate: " + String.format("%.2f", pokemon.getCatchRate() / 255.0D * 100.0D) + "%");
        } else {
            return List.of(
                    "§b♂ Male: " + String.format("%.2f", pokemon.getMalePercentage()) + "%",
                    "§d♀ Female: " + String.format("%.2f", (100 - pokemon.getMalePercentage())) + "%"
            );
        }
    }
}
