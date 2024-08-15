package net.satisfy.beachparty.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.beachparty.entity.BeachpartyBoat;
import net.satisfy.beachparty.fabric.client.renderer.BeachpartyArmorRenderer;
import net.satisfy.beachparty.registry.ObjectRegistry;

public class BeachpartyFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BeachPartyClient.preInitClient();
        BeachPartyClient.initClient();

        registerBoatModels();

        net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer.register(new BeachpartyArmorRenderer(), ObjectRegistry.BEACH_HAT.get());
    }

    private void registerBoatModels() {
        for (BeachpartyBoat.Type type : BeachpartyBoat.Type.values()) {
            String modId = Beachparty.MOD_ID;
            EntityModelLayerRegistry.registerModelLayer(new ModelLayerLocation(new ResourceLocation(modId, type.getModelLocation()), "main"), BoatModel::createBodyModel);
            EntityModelLayerRegistry.registerModelLayer(new ModelLayerLocation(new ResourceLocation(modId, type.getChestModelLocation()), "main"), ChestBoatModel::createBodyModel);
        }
    }
}
