package de.castcrafter.travel_anchors.network;

import de.castcrafter.travel_anchors.block.TravelAnchorTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AnchorNameChangeHandler {

    public static void handle(AnchorNameChangeSerializer.AnchorNameChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) {
                //noinspection ConstantConditions
                ServerWorld world = ctx.get().getSender().getServerWorld();
                //noinspection deprecation
                if (world.isBlockLoaded(msg.pos)) {
                    TileEntity te = world.getTileEntity(msg.pos);
                    if (te instanceof TravelAnchorTile) {
                        ((TravelAnchorTile) te).setName(msg.name);
                        te.markDirty();
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
