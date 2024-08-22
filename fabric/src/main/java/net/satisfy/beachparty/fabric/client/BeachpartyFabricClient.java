package net.satisfy.beachparty.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.beachparty.entity.BeachpartyBoatEntity;
import net.satisfy.beachparty.fabric.client.renderer.BeachHatRenderer;
import net.satisfy.beachparty.fabric.client.renderer.RubberRingRenderer;
import net.satisfy.beachparty.registry.EntityTypeRegistry;

import static net.satisfy.beachparty.registry.ObjectRegistry.*;

public class BeachpartyFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BeachPartyClient.preInitClient();
        BeachPartyClient.initClient();

        registerBoatModels();

        ArmorRenderer.register(new BeachHatRenderer(), BEACH_HAT.get());
        ArmorRenderer.register(new RubberRingRenderer(), RUBBER_RING_PINK.get(), RUBBER_RING_BLUE.get(), RUBBER_RING_STRIPPED.get(), RUBBER_RING_AXOLOTL.get(), RUBBER_RING_PELICAN.get());

        BlockEntityRenderers.register(EntityTypeRegistry.HANGING_SIGN.get(), HangingSignRenderer::new);
        BlockEntityRenderers.register(EntityTypeRegistry.SIGN.get(), SignRenderer::new);
    }

    private void registerBoatModels() {
        for (BeachpartyBoatEntity.Type type : BeachpartyBoatEntity.Type.values()) {
            String modId = Beachparty.MOD_ID;
            EntityModelLayerRegistry.registerModelLayer(new ModelLayerLocation(new ResourceLocation(modId, type.getModelLocation()), "main"), BoatModel::createBodyModel);
            EntityModelLayerRegistry.registerModelLayer(new ModelLayerLocation(new ResourceLocation(modId, type.getChestModelLocation()), "main"), ChestBoatModel::createBodyModel);
        }

    }
}
