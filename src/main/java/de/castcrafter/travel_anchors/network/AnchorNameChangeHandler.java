package de.castcrafter.travel_anchors.network;

import de.castcrafter.travel_anchors.block.TileTravelAnchor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class AnchorNameChangeHandler {

    public static void handle(AnchorNameChangeSerializer.AnchorNameChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) {
                //noinspection ConstantConditions
                ServerLevel level = ctx.get().getSender().getLevel();
                //noinspection deprecation
                if (level.hasChunkAt(msg.pos)) {
                    BlockEntity be = level.getBlockEntity(msg.pos);
                    if (be instanceof TileTravelAnchor) {
                        ((TileTravelAnchor) be).setName(msg.name);
                        be.setChanged();
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
