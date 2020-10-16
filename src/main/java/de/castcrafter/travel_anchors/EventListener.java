package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.network.ClientEventSerializer;
import de.castcrafter.travel_anchors.network.Networking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventListener {

    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Networking.updateTravelAnchorList(event.getPlayer());
    }

    @SubscribeEvent
    public void playerChangeDim(PlayerEvent.PlayerChangedDimensionEvent event) {
        Networking.updateTravelAnchorList(event.getPlayer());
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();
        if (TeleportHandler.canPlayerTeleport(player) && !event.getItemStack().isEmpty()) {
            if (player.isSneaking() && TeleportHandler.canItemTeleport(player)) {
                if (TeleportHandler.shortTeleport(world, player)) {
                    event.setResult(Event.Result.DENY);
                    event.setCancellationResult(ActionResultType.SUCCESS);
                    player.getCooldownTracker().setCooldown(event.getItemStack().getItem(), 30);
                }
            } else {
                if (TeleportHandler.anchorTeleport(world, player, player.getPosition().toImmutable().down())) {
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
        if (TeleportHandler.canBlockTeleport(player) && !player.isSneaking() && event.getHand() == Hand.MAIN_HAND) {
            Networking.sendClientEventToServer(world, ClientEventSerializer.ClientEvent.EMPTY_HAND_INTERACT);
            event.setResult(Event.Result.DENY);
            event.setCancellationResult(ActionResultType.SUCCESS);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (TeleportHandler.canPlayerTeleport(event.getPlayer()) && TeleportHandler.getAnchorToTeleport(event.getWorld(), event.getPlayer(), event.getPlayer().getPosition().toImmutable().down()) != null) {
            if (event.getItemStack().isEmpty()) {
                // We need to handle it here it i's empty. Because minecraft.
                if (TeleportHandler.anchorTeleport(event.getWorld(), event.getPlayer(), event.getPlayer().getPosition().toImmutable().down())) {
                    event.setCanceled(true);
                }
            } else {
                event.setResult(event.getUseItem());
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (TeleportHandler.canBlockTeleport(player)) {
                Networking.sendClientEventToServer(player.getEntityWorld(), ClientEventSerializer.ClientEvent.JUMP);
            }
        }
    }
}
