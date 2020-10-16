package de.castcrafter.travel_anchors.enchantments;

import de.castcrafter.travel_anchors.TravelAnchors;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TravelAnchors.MODID)
public class TeleportationEnchantment extends Enchantment {

    public TeleportationEnchantment(Rarity rarity, EnchantmentType type, EquipmentSlotType[] slots) {
        super(rarity, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
