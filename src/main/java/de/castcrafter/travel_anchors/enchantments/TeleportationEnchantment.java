package de.castcrafter.travel_anchors.enchantments;

import de.castcrafter.travel_anchors.TeleportHandler;
import de.castcrafter.travel_anchors.TravelAnchors;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event){
        PlayerEntity player = event.getPlayer();
        World world = event.getWorld();
        if(player.isSneaking()){
            TeleportHandler.shortTeleport(world, player);
        }
        else{
            TeleportHandler.anchorTeleport(world, player);
        }
        player.swingArm(event.getHand());
    }
}
