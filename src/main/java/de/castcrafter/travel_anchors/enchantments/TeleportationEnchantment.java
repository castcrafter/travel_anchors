package de.castcrafter.travel_anchors.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class TeleportationEnchantment extends Enchantment {

    public TeleportationEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentType.BREAKABLE, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
