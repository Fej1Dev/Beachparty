package net.satisfy.beachparty.fabric;

import net.fabricmc.api.ModInitializer;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.fabric.world.BeachpartyBiomeModification;
import net.satisfy.beachparty.registry.CompostablesRegistry;

public class BeachpartyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Beachparty.init();
        CompostablesRegistry.init();
        Beachparty.commonSetup();
        BeachpartyBiomeModification.init();
    }
}
