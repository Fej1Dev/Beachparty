package net.satisfy.beachparty.block.properties;

import net.minecraft.world.level.block.state.properties.WoodType;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.platform.PlatformHelper;

public class BeachpartyWoodType {
    public static final WoodType PALM = PlatformHelper.createWoodType(Beachparty.MOD_ID + ":palm", BeachpartyBlockSetType.PALM);
}