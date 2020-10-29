package de.castcrafter.travel_anchors.network;

import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class AnchorNameChangeSerializer implements PacketSerializer<AnchorNameChangeSerializer.AnchorNameChangeMessage> {

    @Override
    public Class<AnchorNameChangeMessage> messageClass() {
        return AnchorNameChangeMessage.class;
    }

    @Override
    public void encode(AnchorNameChangeMessage msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
        buffer.writeString(msg.name);
    }

    @Override
    public AnchorNameChangeMessage decode(PacketBuffer buffer) {
        return new AnchorNameChangeMessage(buffer.readBlockPos(), buffer.readString(32767));
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
