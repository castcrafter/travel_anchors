package de.castcrafter.travel_anchors.enchantments;

import de.castcrafter.travel_anchors.ModEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class TeleportationEnchantment extends Enchantment {

    public TeleportationEnchantment() {
        super(Enchantment.Rarity.RARE, ModEnchantments.TELEPORTABLE_NO_STAFF, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
