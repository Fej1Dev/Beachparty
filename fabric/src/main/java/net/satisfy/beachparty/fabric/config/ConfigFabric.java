package net.satisfy.beachparty.fabric.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.satisfy.beachparty.Beachparty;

@Config(name = Beachparty.MOD_ID)
public class ConfigFabric implements ConfigData {
    @ConfigEntry.Category("worldgen")
    public boolean spawnPalms = true;
    public boolean spawnSeashells = true;
    public boolean spawnSandwaves = true;

    @ConfigEntry.Category("gameplay")
    public boolean spawnSandwave = true;


}