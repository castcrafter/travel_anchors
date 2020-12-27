package de.castcrafter.travel_anchors.enchantments;

import de.castcrafter.travel_anchors.ModComponents;
import de.castcrafter.travel_anchors.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class RangeEnchantment extends Enchantment {

    public RangeEnchantment() {
        super(Enchantment.Rarity.RARE, ModEnchantments.TELEPORTABLE, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() == ModComponents.travelStaff || EnchantmentHelper.getEnchantmentLevel(ModEnchantments.teleportation, stack) > 0;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() == ModComponents.travelStaff || stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK;
    }
}
