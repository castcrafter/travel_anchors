package de.castcrafter.travel_anchors.network;

import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.PacketBuffer;

public class ClientEventSerializer implements PacketSerializer<ClientEventSerializer.ClientEvent> {

    @Override
    public Class<ClientEvent> messageClass() {
        return ClientEvent.class;
    }

    @Override
    public void encode(ClientEvent msg, PacketBuffer buffer) {
        buffer.writeString(msg.name());
    }

    @Override
    public ClientEvent decode(PacketBuffer buffer) {
        return ClientEvent.valueOf(buffer.readString(32767));
    }

    public enum ClientEvent {
        JUMP,
        EMPTY_HAND_INTERACT,
        SNEAK
    }
}
