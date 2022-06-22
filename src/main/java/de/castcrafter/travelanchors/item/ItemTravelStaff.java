package de.castcrafter.travelanchors.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.moddingx.libx.base.ItemBase;
import org.moddingx.libx.mod.ModX;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTravelStaff extends ItemBase {

    public ItemTravelStaff(ModX mod, Properties properties) {
        super(mod, properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, List<Component> tooltip, @Nonnull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.travelanchors.travel_staff").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public int getEnchantmentValue() {
        return 20;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }
}
