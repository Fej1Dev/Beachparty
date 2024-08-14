package net.satisfy.beachparty.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.beachparty.registry.ObjectRegistry;

public class BeachpartyFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BeachPartyClient.preInitClient();
        BeachPartyClient.initClient();

        ArmorRenderer.register(new BeachHatRenderer(), ObjectRegistry.BEACH_HAT.get());
    }
}
