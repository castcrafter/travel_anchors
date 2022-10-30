package de.castcrafter.travelanchors.network;

import de.castcrafter.travelanchors.block.TileTravelAnchor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import org.moddingx.libx.network.PacketHandler;
import org.moddingx.libx.network.PacketSerializer;

import java.util.function.Supplier;

public record AnchorLockMessage(BlockPos pos) {

    public static class Handler implements PacketHandler<AnchorLockMessage> {

        @Override
        public Target target() {
            return Target.MAIN_THREAD;
        }

        @Override
        public boolean handle(AnchorLockMessage msg, Supplier<NetworkEvent.Context> ctx) {
            if (ctx.get().getSender() != null) {
                //noinspection ConstantConditions
                ServerLevel level = ctx.get().getSender().getLevel();
                //noinspection deprecation
                if (level.hasChunkAt(msg.pos)) {
                    BlockEntity be = level.getBlockEntity(msg.pos);
                    if (be instanceof TileTravelAnchor) {
                        ((TileTravelAnchor) be).setLocked(true);
                        ServerPlayer sender = ctx.get().getSender();
                        if (sender != null) {
                            sender.displayClientMessage(Component.translatable("travelanchors.lock.locked"), true);
                        }
                        be.setChanged();
                    }
                }
            }

            return true;
        }
    }

    public static class Serializer implements PacketSerializer<AnchorLockMessage> {

        @Override
        public Class<AnchorLockMessage> messageClass() {
            return AnchorLockMessage.class;
        }

        @Override
        public void encode(AnchorLockMessage msg, FriendlyByteBuf buffer) {
            buffer.writeBlockPos(msg.pos);
        }

        @Override
        public AnchorLockMessage decode(FriendlyByteBuf buffer) {
            return new AnchorLockMessage(buffer.readBlockPos());
        }
    }
}
