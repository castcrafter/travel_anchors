package de.castcrafter.travelanchors.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.moddingx.libx.network.PacketSerializer;

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
