package de.castcrafter.travel_anchors.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class TeRequestSerializer implements PacketSerializer<TeRequestSerializer.TeRequestMessage> {

    @Override
    public Class<TeRequestMessage> messageClass() {
        return TeRequestMessage.class;
    }

    @Override
    public void encode(TeRequestMessage msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public TeRequestMessage decode(PacketBuffer buffer) {
        TeRequestMessage msg = new TeRequestMessage();
        msg.pos = buffer.readBlockPos();
        return msg;
    }

    public static class TeRequestMessage {

        public TeRequestMessage() {

        }

        public TeRequestMessage(BlockPos pos) {
            this.pos = pos;
        }

        public BlockPos pos;
    }
}
