package de.castcrafter.travel_anchors.enchantments;

import de.castcrafter.travel_anchors.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class RangeEnchantment extends Enchantment {

    public RangeEnchantment() {
        super(Enchantment.Rarity.RARE, ModEnchantments.TELEPORTABLE, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
