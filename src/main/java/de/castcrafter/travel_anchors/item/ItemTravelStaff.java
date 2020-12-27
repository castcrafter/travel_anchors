package de.castcrafter.travel_anchors.item;

import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTravelStaff extends ItemBase {
    public ItemTravelStaff(ModX mod, Properties properties) {
        super(mod, properties);
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, @Nonnull ITooltipFlag flags) {
        tooltip.add(new TranslationTextComponent("tooltip.travel_anchors.travel_staff").mergeStyle(TextFormatting.GOLD));
    }

    @Override
    public int getItemEnchantability() {
        return 1000;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }
}