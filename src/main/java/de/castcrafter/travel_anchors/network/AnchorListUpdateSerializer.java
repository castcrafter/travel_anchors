package de.castcrafter.travel_anchors.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class AnchorListUpdateSerializer implements PacketSerializer<AnchorListUpdateSerializer.AnchorListUpdateMessage> {

    @Override
    public Class<AnchorListUpdateMessage> messageClass() {
        return AnchorListUpdateMessage.class;
    }

    @Override
    public void encode(AnchorListUpdateMessage msg, PacketBuffer buffer) {
        buffer.writeCompoundTag(msg.nbt);
    }

    @Override
    public AnchorListUpdateMessage decode(PacketBuffer buffer) {
        return new AnchorListUpdateMessage(buffer.readCompoundTag());
    }

    public static class AnchorListUpdateMessage {

        public AnchorListUpdateMessage() {

        }

        public AnchorListUpdateMessage(CompoundNBT nbt) {
            this.nbt = nbt;
        }

        public CompoundNBT nbt;
    }
}
