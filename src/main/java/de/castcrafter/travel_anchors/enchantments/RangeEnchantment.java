package de.castcrafter.travel_anchors.enchantments;

import de.castcrafter.travel_anchors.TravelAnchors;
import de.castcrafter.travel_anchors.config.ServerConfig;
import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TravelAnchors.MODID)
public class RangeEnchantment extends Enchantment {

    public RangeEnchantment(Rarity rarity, EnchantmentType type, EquipmentSlotType[] slots) {
        super(rarity, type, slots);

//        ItemStack stack = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND);
//        int lvl = EnchantmentHelper.getEnchantmentLevel(Registration.RANGE_ENCHANTMENT.get(), stack);
//        ServerConfig.MAX_DISTANCE.set(ServerConfig.MAX_DISTANCE.get() * (lvl * 0.1 +1));
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @SubscribeEvent
    public static void doStuff(LivingEntityUseItemEvent event){
        System.out.println("YEAH");
    }
}
