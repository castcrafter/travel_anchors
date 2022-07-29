package de.castcrafter.travelanchors.network;

import de.castcrafter.travelanchors.TravelAnchorList;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.moddingx.libx.network.PacketHandler;
import org.moddingx.libx.network.PacketSerializer;

import java.util.function.Supplier;

public record AnchorListUpdateMessage(CompoundTag nbt) {

    public static class Handler implements PacketHandler<AnchorListUpdateMessage> {

        @Override
        public Target target() {
            return Target.MAIN_THREAD;
        }

        @Override
        public boolean handle(AnchorListUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
            if (Minecraft.getInstance().level != null) {
                TravelAnchorList.get(Minecraft.getInstance().level).load(msg.nbt);
            }

            return true;
        }
    }

    public static class Serializer implements PacketSerializer<AnchorListUpdateMessage> {

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
    }
}
