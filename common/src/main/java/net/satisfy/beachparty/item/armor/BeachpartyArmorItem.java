package net.satisfy.beachparty.item.armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class BeachpartyArmorItem extends ArmorItem {
    private final ResourceLocation texture;

    public BeachpartyArmorItem(ArmorMaterial material, Type slot, Properties settings, ResourceLocation texture) {
        super(material, slot, settings);
        this.texture = texture;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }
}
