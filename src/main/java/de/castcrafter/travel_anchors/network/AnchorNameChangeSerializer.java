package de.castcrafter.travel_anchors.network;

import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

public class AnchorNameChangeSerializer implements PacketSerializer<AnchorNameChangeSerializer.AnchorNameChangeMessage> {

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

    public static class AnchorNameChangeMessage {


        public BlockPos pos;
        public String name;

        public AnchorNameChangeMessage() {

        }

        public AnchorNameChangeMessage(BlockPos pos, String name) {
            this.pos = pos;
            this.name = name;
        }
    }
}
