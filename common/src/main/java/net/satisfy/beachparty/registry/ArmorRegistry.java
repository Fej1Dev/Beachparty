package net.satisfy.beachparty.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.item.Item;
import net.satisfy.beachparty.client.model.*;

import java.util.HashMap;
import java.util.Map;

public class ArmorRegistry {
    private static final Map<Item, BeachHatModel<?>> hatmodels = new HashMap<>();
    private static final Map<Item, RubberRingModel> ringmodels = new HashMap<>();

    public static Model getBodyModel(Item item, ModelPart baseBody) {
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        RubberRingModel model = ringmodels.computeIfAbsent(item, key -> {
            if (key == ObjectRegistry.RUBBER_RING_BLUE.get()) {
                return new RubberRingColoredModel<>(modelSet.bakeLayer(RubberRingColoredModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.RUBBER_RING_PINK.get()) {
                return new RubberRingColoredModel<>(modelSet.bakeLayer(RubberRingColoredModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.RUBBER_RING_STRIPPED.get()) {
                return new RubberRingColoredModel<>(modelSet.bakeLayer(RubberRingColoredModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.RUBBER_RING_AXOLOTL.get()) {
                return new RubberRingAxolotlModel<>(modelSet.bakeLayer(RubberRingAxolotlModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.RUBBER_RING_PELICAN.get()) {
                return new RubberRingPelicanModel<>(modelSet.bakeLayer(RubberRingPelicanModel.LAYER_LOCATION));

            } else {
                return null;
            }
        });

        assert model != null;

        model.copyBody(baseBody);

        return (Model) model;
    }

    public static Model getHatModel(Item item, ModelPart baseHead) {
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        BeachHatModel<?> model = hatmodels.computeIfAbsent(item, key -> {
            if (key == ObjectRegistry.BEACH_HAT.get()) {
                return new BeachHatModel<>(modelSet.bakeLayer(BeachHatModel.LAYER_LOCATION));
            } else {
                return null;
            }
        });
        if (model == null) {
            return null;
        }

        model.copyHead(baseHead);
        return model;
    }

}
