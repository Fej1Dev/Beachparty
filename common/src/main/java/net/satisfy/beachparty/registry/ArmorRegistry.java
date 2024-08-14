package net.satisfy.beachparty.registry;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.satisfy.beachparty.client.model.BeachHatModel;
import net.satisfy.beachparty.client.model.RubberRingAxolotlModel;
import net.satisfy.beachparty.client.model.RubberRingModel;
import net.satisfy.beachparty.client.model.RubberRingPelicanModel;

import java.util.HashMap;
import java.util.Map;

public class ArmorRegistry {
    private static final Map<Item, BeachHatModel<?>> models = new HashMap<>();

    public static void registerCustomArmorLayers() {
        EntityModelLayerRegistry.register(RubberRingPelicanModel.LAYER_LOCATION, RubberRingPelicanModel::getTexturedModelData);
        EntityModelLayerRegistry.register(RubberRingAxolotlModel.LAYER_LOCATION, RubberRingAxolotlModel::getTexturedModelData);
        EntityModelLayerRegistry.register(RubberRingModel.LAYER_LOCATION, RubberRingModel::getTexturedModelData);
    }


    public static <T extends LivingEntity> void registerModels(Map<Item, EntityModel<T>> models, EntityModelSet modelLoader) {
        models.put(ObjectRegistry.RUBBER_RING_PELICAN.get(), new RubberRingPelicanModel<>(modelLoader.bakeLayer(RubberRingPelicanModel.LAYER_LOCATION)));
        models.put(ObjectRegistry.RUBBER_RING_AXOLOTL.get(), new RubberRingAxolotlModel<>(modelLoader.bakeLayer(RubberRingAxolotlModel.LAYER_LOCATION)));
        models.put(ObjectRegistry.RUBBER_RING_BLUE.get(), new RubberRingModel<>(modelLoader.bakeLayer(RubberRingModel.LAYER_LOCATION)));
        models.put(ObjectRegistry.RUBBER_RING_PINK.get(), new RubberRingModel<>(modelLoader.bakeLayer(RubberRingModel.LAYER_LOCATION)));
        models.put(ObjectRegistry.RUBBER_RING_STRIPPED.get(), new RubberRingModel<>(modelLoader.bakeLayer(RubberRingModel.LAYER_LOCATION)));
    }


    public static Model getHatModel(Item item, ModelPart baseHead) {
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        BeachHatModel<?> model = models.computeIfAbsent(item, key -> {
            if (key == ObjectRegistry.BEACH_HAT.get()) {
                return new BeachHatModel<>(modelSet.bakeLayer(BeachHatModel.LAYER_LOCATION));
            } else {
                return null;
            }
        });

        assert model != null;
        model.copyHead(baseHead);

        return model;
    }
}
