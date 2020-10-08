package de.castcrafter.travel_anchors.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RangeEnchantment extends Enchantment {

    public RangeEnchantment(Rarity rarity, EnchantmentType type, EquipmentSlotType[] slots) {
        super(rarity, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @SubscribeEvent
    public static void rangeExtension(TickEvent.PlayerTickEvent event){
        if(event.player.isSneaking()){
            System.out.println("sneaking");
        }
        System.out.println("asdasd");
    }
}
