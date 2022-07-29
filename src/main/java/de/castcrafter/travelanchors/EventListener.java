package de.castcrafter.travelanchors;

import de.castcrafter.travelanchors.config.ClientConfig;
import de.castcrafter.travelanchors.network.ClientEventSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.moddingx.libx.event.ClickBlockEmptyHandEvent;

public class EventListener {

    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        TravelAnchors.getNetwork().updateTravelAnchorList(event.getEntity());
    }

    @SubscribeEvent
    public void playerChangeDim(PlayerEvent.PlayerChangedDimensionEvent event) {
        TravelAnchors.getNetwork().updateTravelAnchorList(event.getEntity());
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        if (TeleportHandler.canPlayerTeleport(player, event.getHand()) && !event.getItemStack().isEmpty()) {
            if (player.isShiftKeyDown() && TeleportHandler.canItemTeleport(player, event.getHand())) {
                if (TeleportHandler.shortTeleport(level, player, event.getHand())) {
                    event.setResult(Event.Result.DENY);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    player.getCooldowns().addCooldown(event.getItemStack().getItem(), 30);
                }
            } else {
                if (TeleportHandler.anchorTeleport(level, player, player.blockPosition().immutable().below(), event.getHand())) {
                    event.setResult(Event.Result.DENY);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEmptyClick(PlayerInteractEvent.RightClickEmpty event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        if (TeleportHandler.canBlockTeleport(player) && !player.isShiftKeyDown() && event.getHand() == InteractionHand.MAIN_HAND && event.getEntity().getItemInHand(InteractionHand.OFF_HAND).isEmpty() && event.getItemStack().isEmpty()) {
            TravelAnchors.getNetwork().sendClientEventToServer(level, ClientEventSerializer.ClientEvent.EMPTY_HAND_INTERACT);
            event.setResult(Event.Result.DENY);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }
    
    @SubscribeEvent
    public void emptyBlockClick(ClickBlockEmptyHandEvent event) {
        if (event.getHand() == InteractionHand.MAIN_HAND) {
            // Empty offhand does not count. In that case the main hand will either produce
            // this event or PlayerInteractEvent.RightClickItem
            if (TeleportHandler.canPlayerTeleport(event.getPlayer(), event.getHand())) {
                if (!event.getPlayer().isShiftKeyDown()) {
                    if (TeleportHandler.anchorTeleport(event.getLevel(), event.getPlayer(), event.getPlayer().blockPosition().immutable().below(), event.getHand())) {
                        event.setCanceled(true);
                        event.setCancellationResult(InteractionResult.SUCCESS);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ClientConfig.disable_elevation) {
                if (TeleportHandler.canBlockTeleport(player) && !player.isShiftKeyDown()) {
                    TravelAnchors.getNetwork().sendClientEventToServer(player.getCommandSenderWorld(), ClientEventSerializer.ClientEvent.JUMP_TP);
                }
            } else {
                if (TeleportHandler.canElevate(player) && !player.isShiftKeyDown()) {
                    TravelAnchors.getNetwork().sendClientEventToServer(player.getCommandSenderWorld(), ClientEventSerializer.ClientEvent.JUMP);
                }
            }
        }
    }
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onSneak(MovementInputUpdateEvent event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().options.keyShift.consumeClick()) {
            if (!ClientConfig.disable_elevation) {
                if (TeleportHandler.canElevate(Minecraft.getInstance().player)) {
                    TravelAnchors.getNetwork().sendClientEventToServer(Minecraft.getInstance().player.getCommandSenderWorld(), ClientEventSerializer.ClientEvent.SNEAK);
                }
            }
        }
    }
}
