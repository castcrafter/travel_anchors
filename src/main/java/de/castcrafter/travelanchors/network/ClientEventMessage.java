package de.castcrafter.travelanchors.network;

import de.castcrafter.travelanchors.TeleportHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;
import org.moddingx.libx.network.PacketHandler;
import org.moddingx.libx.network.PacketSerializer;

import java.util.function.Supplier;

public record ClientEventMessage(Type type) {

    public static class Handler implements PacketHandler<ClientEventMessage> {

        @Override
        public Target target() {
            return Target.MAIN_THREAD;
        }

        @Override
        public boolean handle(ClientEventMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                switch (msg.type) {
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

            return true;
        }
    }

    public static class Serializer implements PacketSerializer<ClientEventMessage> {

        @Override
        public Class<ClientEventMessage> messageClass() {
            return ClientEventMessage.class;
        }

        @Override
        public void encode(ClientEventMessage msg, FriendlyByteBuf buffer) {
            buffer.writeEnum(msg.type);
        }

        @Override
        public ClientEventMessage decode(FriendlyByteBuf buffer) {
            return new ClientEventMessage(buffer.readEnum(Type.class));
        }
    }

    public enum Type {
        JUMP,
        EMPTY_HAND_INTERACT,
        SNEAK,
        JUMP_TP
    }
}
