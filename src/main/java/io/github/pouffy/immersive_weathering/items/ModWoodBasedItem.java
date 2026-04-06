package io.github.pouffy.immersive_weathering.items;

import net.mehvahdjukaar.moonlight.api.item.WoodBasedItem;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class ModWoodBasedItem extends WoodBasedItem {

    public final int burnTime;

    public ModWoodBasedItem(Properties builder, WoodType woodType, int burnTime) {
        super(builder, woodType);
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTime;
    }
}
