package de.castcrafter.travel_anchors.network;

import de.castcrafter.travel_anchors.TravelAnchorList;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.network.NetworkX;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import javax.annotation.Nullable;

public class Networking extends NetworkX {

    public Networking(ModX mod) {
        super(mod);
    }

    @Override
    protected Protocol getProtocol() {
        return Protocol.of("5");
    }

    protected void registerPackets() {
        this.register(new AnchorNameChangeSerializer(), () -> AnchorNameChangeHandler::handle, NetworkDirection.PLAY_TO_SERVER);
        this.register(new AnchorListUpdateSerializer(), () -> AnchorListUpdateHandler::handle, NetworkDirection.PLAY_TO_CLIENT);
        this.register(new ClientEventSerializer(), () -> ClientEventHandler::handle, NetworkDirection.PLAY_TO_SERVER);
    }

    public void sendNameChange(Level level, BlockPos pos, String name) {
        if (level.isClientSide) {
            this.channel.sendToServer(new AnchorNameChangeSerializer.AnchorNameChangeMessage(pos, name));
        }
    }

    public void updateTravelAnchorList(Level level, @Nullable TravelAnchorList list) {
        if (!level.isClientSide) {
            if (list == null) {
                list = TravelAnchorList.get(level);
            }
            this.channel.send(PacketDistributor.DIMENSION.with(level::dimension), new AnchorListUpdateSerializer.AnchorListUpdateMessage(list.save(new CompoundTag())));
        }
    }

    public void updateTravelAnchorList(Player player) {
        if (!player.getCommandSenderWorld().isClientSide && player instanceof ServerPlayer) {
            this.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new AnchorListUpdateSerializer.AnchorListUpdateMessage(TravelAnchorList.get(player.getCommandSenderWorld()).save(new CompoundTag())));
        }
    }

    public void sendClientEventToServer(Level level, ClientEventSerializer.ClientEvent event) {
        if (level.isClientSide) {
            this.channel.sendToServer(event);
        }
    }
}
