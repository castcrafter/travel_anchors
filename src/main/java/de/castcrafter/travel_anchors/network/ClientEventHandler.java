package de.castcrafter.travel_anchors.network;

import de.castcrafter.travel_anchors.TeleportHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientEventHandler {

    public static void handle(ClientEventSerializer.ClientEvent msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                switch (msg) {
                    case JUMP:
                        if (TeleportHandler.canElevate(player)) {
                            if (TeleportHandler.elevateUp(player)) {
                                player.setMotion(player.getMotion().mul(1, 0, 1));
                            }
                        }
                        break;

                    case EMPTY_HAND_INTERACT:
                        if (TeleportHandler.canBlockTeleport(player) && !player.isSneaking()) {
                            TeleportHandler.anchorTeleport(player.getEntityWorld(), player, player.getPosition().toImmutable().down(), Hand.MAIN_HAND);
                        }
                        break;
                        
                    case SNEAK:
                        if (TeleportHandler.canElevate(player)) {
                            if (TeleportHandler.elevateDown(player)) {
                                player.setMotion(player.getMotion().mul(1, 0, 1));
                            }
                        }
                        break;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
