package de.castcrafter.travelanchors.network;

import de.castcrafter.travelanchors.block.TileTravelAnchor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import org.moddingx.libx.network.PacketHandler;
import org.moddingx.libx.network.PacketSerializer;

import java.util.function.Supplier;

public record AnchorNameChangeMessage(BlockPos pos, String name) {

    public static class Handler implements PacketHandler<AnchorNameChangeMessage> {

        @Override
        public Target target() {
            return Target.MAIN_THREAD;
        }

        @Override
        public boolean handle(AnchorNameChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
            if (ctx.get().getSender() != null) {
                //noinspection ConstantConditions
                ServerLevel level = ctx.get().getSender().getLevel();
                //noinspection deprecation
                if (level.hasChunkAt(msg.pos)) {
                    BlockEntity be = level.getBlockEntity(msg.pos);
                    if (be instanceof TileTravelAnchor) {
                        ((TileTravelAnchor) be).setName(msg.name);
                        be.setChanged();
                    }
                }
            }

            return true;
        }
    }

    public static class Serializer implements PacketSerializer<AnchorNameChangeMessage> {

        @Override
        public Class<AnchorNameChangeMessage> messageClass() {
            return AnchorNameChangeMessage.class;
        }

        @Override
        public void encode(AnchorNameChangeMessage msg, FriendlyByteBuf buffer) {
            buffer.writeBlockPos(msg.pos);
            buffer.writeUtf(msg.name);
        }

        @Override
        public AnchorNameChangeMessage decode(FriendlyByteBuf buffer) {
            return new AnchorNameChangeMessage(buffer.readBlockPos(), buffer.readUtf(32767));
        }
    }
}
