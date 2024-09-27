package net.satisfy.beachparty;

import net.satisfy.beachparty.event.CommonEvents;
import net.satisfy.beachparty.networking.BeachpartyMessages;
import net.satisfy.beachparty.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Beachparty {
    public static final String MOD_ID = "beachparty";
    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        ObjectRegistry.init();
        EntityTypeRegistry.init();
        TabRegistry.init();
        MobEffectRegistry.init();
        SoundEventRegistry.init();
        ScreenHandlerTypesRegistry.init();
        CommonEvents.init();
        BeachpartyMessages.registerC2SPackets();
        RecipeRegistry.init();
    }
}