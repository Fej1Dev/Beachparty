package net.satisfy.beachparty;

import com.google.common.reflect.Reflection;
import dev.architectury.hooks.item.tool.AxeItemHooks;
import net.satisfy.beachparty.event.CommonEvents;
import net.satisfy.beachparty.networking.BeachpartyMessages;
import net.satisfy.beachparty.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Beachparty {
    public static final String MOD_ID = "beachparty";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    //TODO
    public static void init() {
        Reflection.initialize(
                ObjectRegistry.class,
                EntityTypeRegistry.class,
                TabRegistry.class
        );
        SoundEventRegistry.init();
        ScreenHandlerTypesRegistry.init();
        PlacerTypesRegistry.init();
        CommonEvents.init();
        BeachpartyMessages.registerC2SPackets();
    }

    public static void commonSetup() {
        ObjectRegistry.commonInit();

        AxeItemHooks.addStrippable(ObjectRegistry.PALM_LOG.get(), ObjectRegistry.STRIPPED_PALM_LOG.get());
        AxeItemHooks.addStrippable(ObjectRegistry.PALM_WOOD.get(), ObjectRegistry.STRIPPED_PALM_WOOD.get());
    }
}