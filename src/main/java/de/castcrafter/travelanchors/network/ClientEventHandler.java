package de.castcrafter.travelanchors.network;

import de.castcrafter.travelanchors.TeleportHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientEventHandler {

    public static void handle(ClientEventSerializer.ClientEvent msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                switch (msg) {
                    case JUMP:
                        if (TeleportHandler.canElevate(player)) {
                            if (TeleportHandler.elevateUp(player)) {
                                player.setDeltaMovement(player.getDeltaMovement().multiply(1, 0, 1));
                            }
                        }
                        break;

                    case EMPTY_HAND_INTERACT:
                        if (TeleportHandler.canBlockTeleport(player) && !player.isShiftKeyDown()) {
                            TeleportHandler.anchorTeleport(player.getCommandSenderWorld(), player, player.blockPosition().immutable().below(), InteractionHand.MAIN_HAND);
                        }
                        break;
                        
                    case SNEAK:
                        if (TeleportHandler.canElevate(player)) {
                            if (TeleportHandler.elevateDown(player)) {
                                player.setDeltaMovement(player.getDeltaMovement().multiply(1, 0, 1));
                            }
                        }
                        break;
                        
                    case JUMP_TP:
                        // Client has configured to use jump as telport not elevate
                        if (TeleportHandler.canBlockTeleport(player) && !player.isShiftKeyDown()) {
                            TeleportHandler.anchorTeleport(player.getCommandSenderWorld(), player, player.blockPosition().immutable().below(), null);
                        }
                        break;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
