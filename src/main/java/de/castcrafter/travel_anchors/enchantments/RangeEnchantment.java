package de.castcrafter.travel_anchors.enchantments;

import de.castcrafter.travel_anchors.ModComponents;
import de.castcrafter.travel_anchors.ModEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class RangeEnchantment extends Enchantment {

    public RangeEnchantment() {
        super(Enchantment.Rarity.RARE, ModEnchantments.TELEPORTABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() == ModComponents.travelStaff || EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.teleportation, stack) > 0;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() == ModComponents.travelStaff || stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK;
    }

    @Override
    public int getMinCost(int level) {
        return level * 7 + 3;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level)*2 + 2;
    }
}
