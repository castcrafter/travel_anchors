package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.network.Networking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
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
        if (TeleportHandler.canPlayerTeleport(player)) {
            if (player.isSneaking() && TeleportHandler.canItemTeleport(player)) {
                if (TeleportHandler.shortTeleport(world, player)) {
                    event.setResult(Event.Result.DENY);
                    event.setCancellationResult(ActionResultType.SUCCESS);
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
        if (TeleportHandler.canBlockTeleport(player) && !player.isSneaking()) {
            if (TeleportHandler.anchorTeleport(world, player, player.getPosition().toImmutable().down())) {
                event.setResult(Event.Result.DENY);
                event.setCancellationResult(ActionResultType.SUCCESS);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (TeleportHandler.canPlayerTeleport(event.getPlayer()) && TeleportHandler.getAnchorToTeleport(event.getWorld(), event.getPlayer(), event.getPlayer().getPosition().toImmutable().down()) != null) {
            event.setResult(event.getUseItem());
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (TeleportHandler.canBlockTeleport(player)) {
                TeleportHandler.anchorTeleport(player.getEntityWorld(), player, player.getPosition().toImmutable().down());
            }
        }
    }
}
