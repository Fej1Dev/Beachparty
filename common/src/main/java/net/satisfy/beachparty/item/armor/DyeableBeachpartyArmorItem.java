package net.satisfy.beachparty.item.armor;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;


public class DyeableBeachpartyArmorItem extends DyeableArmorItem {
    private final int defaultColor;

    public DyeableBeachpartyArmorItem(ArmorMaterial material, Type slot, int color, Item.Properties settings) {
        super(material, slot, settings);
        defaultColor = color;
    }

    public int getDefaultColor() {
        return defaultColor;
    }



    @Override
    public int getColor(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTagElement("display");
        return compoundTag != null && compoundTag.contains("color", 99) ? compoundTag.getInt("color") : this.defaultColor;
    }
/*
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.beachparty.dyeable").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
        if (world != null && world.isClientSide()) {
            ArmorRegistry.appendTooltip(tooltip);
        }
    }*/
}