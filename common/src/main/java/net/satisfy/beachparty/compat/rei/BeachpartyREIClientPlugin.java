package net.satisfy.beachparty.compat.rei;

import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.satisfy.beachparty.compat.rei.category.MiniFridgeCategory;
import net.satisfy.beachparty.compat.rei.category.TikiBarCategory;
import net.satisfy.beachparty.compat.rei.display.MiniFridgeDisplay;
import net.satisfy.beachparty.compat.rei.display.TikiBarDisplay;
import net.satisfy.beachparty.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.recipe.TikiBarRecipe;
import net.satisfy.beachparty.registry.ObjectRegistry;

public class BeachpartyREIClientPlugin {

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new MiniFridgeCategory());
        registry.add(new TikiBarCategory());

        registry.addWorkstations(MiniFridgeCategory.MINE_FRIDGE_DISPLAY, EntryStacks.of(ObjectRegistry.MINI_FRIDGE.get()));
        registry.addWorkstations(TikiBarCategory.TIKI_BAR_DISPLAY, EntryStacks.of(ObjectRegistry.TIKI_BAR.get()));
    }


    public static void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(MiniFridgeRecipe.class, MiniFridgeDisplay::new);
        registry.registerFiller(TikiBarRecipe.class, TikiBarDisplay::new);
    }
}
