package de.castcrafter.travel_anchors.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface PacketSerializer<T> {

    Class<T> messageClass();
    void encode(T msg, PacketBuffer buffer);
    T decode(PacketBuffer buffer);
}
