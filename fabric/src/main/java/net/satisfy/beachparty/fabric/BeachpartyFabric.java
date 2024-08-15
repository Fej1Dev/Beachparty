package net.satisfy.beachparty.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.fabric.config.ConfigFabric;
import net.satisfy.beachparty.registry.CompostablesRegistry;
import net.satisfy.beachparty.util.BeachpartyIdentifier;
import net.satisfy.beachparty.world.PlacedFeatures;

import java.util.function.Predicate;

public class BeachpartyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Beachparty.init();
        CompostablesRegistry.init();
        Beachparty.commonSetup();
        addBiomeModification();
    }

    void addBiomeModification() {
        ConfigFabric config = AutoConfig.getConfigHolder(ConfigFabric.class).getConfig();
        BiomeModification world = BiomeModifications.create(new BeachpartyIdentifier("world_features"));
        Predicate<BiomeSelectionContext> beachBiomes = getBeachpartySelector();

        if (config.spawnPalms) {
            world.add(ModificationPhase.ADDITIONS, beachBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.PALM_TREE_KEY));
        } else {
            world.add(ModificationPhase.REMOVALS, beachBiomes, ctx -> ctx.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.PALM_TREE_KEY));
        }
        if (config.spawnSandwaves) {
            world.add(ModificationPhase.ADDITIONS, beachBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.SANDWAVES_KEY));
        } else {
            world.add(ModificationPhase.REMOVALS, beachBiomes, ctx -> ctx.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.SANDWAVES_KEY));
        }
        if (config.spawnSeashells) {
            world.add(ModificationPhase.ADDITIONS, beachBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.SEASHELLS_KEY));
        } else {
            world.add(ModificationPhase.REMOVALS, beachBiomes, ctx -> ctx.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.SEASHELLS_KEY));
        }
    }

    private static Predicate<BiomeSelectionContext> getBeachpartySelector() {
        return BiomeSelectors.tag(TagKey.create(Registries.BIOME, new BeachpartyIdentifier("beach")));
    }

}
