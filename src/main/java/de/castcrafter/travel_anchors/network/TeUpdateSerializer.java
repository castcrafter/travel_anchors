package de.castcrafter.travel_anchors.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class TeUpdateSerializer implements PacketSerializer<TeUpdateSerializer.TeUpdateMessage> {

    @Override
    public Class<TeUpdateMessage> messageClass() {
        return TeUpdateMessage.class;
    }

    @Override
    public void encode(TeUpdateMessage msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
        buffer.writeResourceLocation(msg.id);
        buffer.writeCompoundTag(msg.nbt);
    }

    @Override
    public TeUpdateMessage decode(PacketBuffer buffer) {
        TeUpdateMessage msg = new TeUpdateMessage();
        msg.pos = buffer.readBlockPos();
        msg.id = buffer.readResourceLocation();
        msg.nbt = buffer.readCompoundTag();
        return msg;
    }

    public static class TeUpdateMessage {

        public TeUpdateMessage() {
        }

        public TeUpdateMessage(BlockPos pos, ResourceLocation id, CompoundNBT nbt) {
            this.pos = pos;
            this.id = id;
            this.nbt = nbt;
        }

        public BlockPos pos;
        public ResourceLocation id;
        public CompoundNBT nbt;
    }
}
