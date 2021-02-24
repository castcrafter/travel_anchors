package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.network.ClientEventSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventListener {

    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        TravelAnchors.getNetwork().updateTravelAnchorList(event.getPlayer());
    }

    @SubscribeEvent
    public void playerChangeDim(PlayerEvent.PlayerChangedDimensionEvent event) {
        TravelAnchors.getNetwork().updateTravelAnchorList(event.getPlayer());
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();
        if (TeleportHandler.canPlayerTeleport(player, event.getHand()) && !event.getItemStack().isEmpty()) {
            if (player.isSneaking() && TeleportHandler.canItemTeleport(player, event.getHand())) {
                if (TeleportHandler.shortTeleport(world, player, event.getHand())) {
                    event.setResult(Event.Result.DENY);
                    event.setCancellationResult(ActionResultType.SUCCESS);
                    player.getCooldownTracker().setCooldown(event.getItemStack().getItem(), 30);
                }
            } else {
                if (TeleportHandler.anchorTeleport(world, player, player.getPosition().toImmutable().down(), event.getHand())) {
                    event.setResult(Event.Result.DENY);
                    event.setCancellationResult(ActionResultType.SUCCESS);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEmptyClick(PlayerInteractEvent.RightClickEmpty event) {
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();
        if (TeleportHandler.canBlockTeleport(player) && !player.isSneaking() && event.getHand() == Hand.MAIN_HAND && event.getPlayer().getHeldItem(Hand.OFF_HAND).isEmpty() && event.getItemStack().isEmpty()) {
            TravelAnchors.getNetwork().sendClientEventToServer(world, ClientEventSerializer.ClientEvent.EMPTY_HAND_INTERACT);
            event.setResult(Event.Result.DENY);
            event.setCancellationResult(ActionResultType.SUCCESS);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (TeleportHandler.canPlayerTeleport(event.getPlayer(), event.getHand()) && TeleportHandler.getAnchorToTeleport(event.getWorld(), event.getPlayer(), event.getPlayer().getPosition().toImmutable().down()) != null) {
            if (event.getItemStack().isEmpty()) {
                // We need to handle it here it it's empty. Because minecraft.
                if (TeleportHandler.anchorTeleport(event.getWorld(), event.getPlayer(), event.getPlayer().getPosition().toImmutable().down(), event.getHand())) {
                    event.setCanceled(true);
                }
            } else {
                event.setResult(event.getUseItem());
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (TeleportHandler.canElevate(player) && !player.isSneaking()) {
                TravelAnchors.getNetwork().sendClientEventToServer(player.getEntityWorld(), ClientEventSerializer.ClientEvent.JUMP);
            }
        }
    }
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onSneak(InputUpdateEvent event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().gameSettings.keyBindSneak.isPressed()) {
            if (TeleportHandler.canElevate(Minecraft.getInstance().player)) {
                TravelAnchors.getNetwork().sendClientEventToServer(Minecraft.getInstance().player.getEntityWorld(), ClientEventSerializer.ClientEvent.SNEAK);
            }
        }
    }
}
