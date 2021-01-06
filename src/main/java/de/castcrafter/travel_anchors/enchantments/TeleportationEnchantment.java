package de.castcrafter.travel_anchors.enchantments;

import de.castcrafter.travel_anchors.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class TeleportationEnchantment extends Enchantment {

    public TeleportationEnchantment() {
        super(Enchantment.Rarity.RARE, ModEnchantments.TELEPORTABLE_NO_STAFF, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
