package de.castcrafter.travel_anchors.network;

import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class AnchorListUpdateSerializer implements PacketSerializer<AnchorListUpdateSerializer.AnchorListUpdateMessage> {

    @Override
    public Class<AnchorListUpdateMessage> messageClass() {
        return AnchorListUpdateMessage.class;
    }

    @Override
    public void encode(AnchorListUpdateMessage msg, FriendlyByteBuf buffer) {
        buffer.writeNbt(msg.nbt);
    }

    @Override
    public AnchorListUpdateMessage decode(FriendlyByteBuf buffer) {
        return new AnchorListUpdateMessage(buffer.readNbt());
    }

    public static class AnchorListUpdateMessage {

        public AnchorListUpdateMessage() {

        }

        public AnchorListUpdateMessage(CompoundTag nbt) {
            this.nbt = nbt;
        }

        public CompoundTag nbt;
    }
}
