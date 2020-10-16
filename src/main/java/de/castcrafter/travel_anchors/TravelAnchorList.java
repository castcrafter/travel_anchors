package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.network.Networking;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TravelAnchorList extends WorldSavedData {

    private static final TravelAnchorList clientInstance = new TravelAnchorList();

    public static TravelAnchorList get(World world) {
        if (!world.isRemote) {
            DimensionSavedDataManager storage = ((ServerWorld) world).getSavedData();
            return storage.getOrCreate(TravelAnchorList::new, TravelAnchors.MODID);
        } else {
            return clientInstance;
        }
    }

    public TravelAnchorList() {
        super(TravelAnchors.MODID);
    }

    public TravelAnchorList(String name) {
        super(name);
    }

    public final HashMap<BlockPos, String> anchors = new HashMap<>();

    @Override
    public void read(@Nonnull CompoundNBT nbt) {
        this.anchors.clear();
        if (nbt.contains("anchors", Constants.NBT.TAG_LIST)) {
            ListNBT list = nbt.getList("anchors", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundNBT entryNBT = list.getCompound(i);
                if (entryNBT.contains("x") && entryNBT.contains("y") && entryNBT.contains("z") && entryNBT.contains("name")) {
                    BlockPos pos = new BlockPos(entryNBT.getInt("x"), entryNBT.getInt("y"), entryNBT.getInt("z")).toImmutable();
                    String name = entryNBT.getString("name");
                    if (!name.isEmpty()) {
                        this.anchors.put(pos, entryNBT.getString("name"));
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        for (Map.Entry<BlockPos, String> entry : this.anchors.entrySet()) {
            CompoundNBT entryNBT = new CompoundNBT();
            entryNBT.putInt("x", entry.getKey().getX());
            entryNBT.putInt("y", entry.getKey().getY());
            entryNBT.putInt("z", entry.getKey().getZ());
            entryNBT.putString("name", entry.getValue());
            list.add(entryNBT);
        }
        nbt.put("anchors", list);
        return nbt;
    }

    public void setAnchor(World world, BlockPos pos, @Nullable String name) {
        if (!world.isRemote) {
            boolean needsUpdate = false;
            BlockPos immutablePos = pos.toImmutable();
            if (name == null || name.trim().isEmpty()) {
                if (this.anchors.containsKey(immutablePos)) {
                    this.anchors.remove(immutablePos);
                    needsUpdate = true;
                }
            } else {
                String oldName = this.anchors.getOrDefault(immutablePos, null);
                if (oldName == null || !oldName.equals(name)) {
                    this.anchors.put(pos.toImmutable(), name);
                    needsUpdate = true;
                }
            }
            this.markDirty();
            if (needsUpdate) {
                Networking.updateTravelAnchorList(world, this);
            }
        }
    }

    public String getAnchor(BlockPos pos) {
        return this.anchors.getOrDefault(pos.toImmutable(), null);
    }

    public Stream<Pair<BlockPos, String>> getAnchorsAround(Vector3d pos, double maxDistanceSq) {
        return this.anchors.entrySet().stream()
                .filter(entry -> entry.getKey().distanceSq(pos.x, pos.y, pos.z, true) < maxDistanceSq)
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()));
    }
}

