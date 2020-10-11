package de.castcrafter.travel_anchors.enchantments;

import de.castcrafter.travel_anchors.config.ServerConfig;
import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.world.NoteBlockEvent;

public class RangeEnchantment extends Enchantment {

    public RangeEnchantment(Rarity rarity, EnchantmentType type, EquipmentSlotType[] slots) {
        super(rarity, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public static double getMaxDistance(PlayerEntity player){
        ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
        int lvl = EnchantmentHelper.getEnchantmentLevel(Registration.RANGE_ENCHANTMENT.get(), stack);
        if(lvl == 0){
            return ServerConfig.MAX_DISTANCE.get();
        }
        else {
            return ServerConfig.MAX_DISTANCE.get() * (lvl * 0.5 +1);
        }
    }
}
