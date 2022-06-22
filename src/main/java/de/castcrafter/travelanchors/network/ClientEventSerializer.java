package de.castcrafter.travelanchors.network;

import net.minecraft.network.FriendlyByteBuf;
import org.moddingx.libx.network.PacketSerializer;

public class ClientEventSerializer implements PacketSerializer<ClientEventSerializer.ClientEvent> {

    @Override
    public Class<ClientEvent> messageClass() {
        return ClientEvent.class;
    }

    @Override
    public void encode(ClientEvent msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.name());
    }

    @Override
    public ClientEvent decode(FriendlyByteBuf buffer) {
        return ClientEvent.valueOf(buffer.readUtf(32767));
    }

    public enum ClientEvent {
        JUMP,
        EMPTY_HAND_INTERACT,
        SNEAK,
        JUMP_TP
    }
}
