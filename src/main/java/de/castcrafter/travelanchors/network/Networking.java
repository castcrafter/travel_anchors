package de.castcrafter.travelanchors.network;

import de.castcrafter.travelanchors.TravelAnchorList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.network.NetworkX;

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
        this.registerGame(NetworkDirection.PLAY_TO_SERVER, new AnchorNameChangeMessage.Serializer(), () -> AnchorNameChangeMessage.Handler::new);
        this.registerGame(NetworkDirection.PLAY_TO_CLIENT, new AnchorListUpdateMessage.Serializer(), () -> AnchorListUpdateMessage.Handler::new);
        this.registerGame(NetworkDirection.PLAY_TO_SERVER, new ClientEventMessage.Serializer(), () -> ClientEventMessage.Handler::new);
    }

    public void sendNameChange(Level level, BlockPos pos, String name) {
        if (level.isClientSide) {
            this.channel.sendToServer(new AnchorNameChangeMessage(pos, name));
        }
    }

    public void updateTravelAnchorList(Level level, @Nullable TravelAnchorList list) {
        if (!level.isClientSide) {
            if (list == null) {
                list = TravelAnchorList.get(level);
            }
            this.channel.send(PacketDistributor.DIMENSION.with(level::dimension), new AnchorListUpdateMessage(list.save(new CompoundTag())));
        }
    }

    public void updateTravelAnchorList(Player player) {
        if (!player.getCommandSenderWorld().isClientSide && player instanceof ServerPlayer) {
            this.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new AnchorListUpdateMessage(TravelAnchorList.get(player.getCommandSenderWorld()).save(new CompoundTag())));
        }
    }

    public void sendClientEventToServer(Level level, ClientEventMessage.Type type) {
        if (level.isClientSide) {
            this.channel.sendToServer(new ClientEventMessage(type));
        }
    }
}
