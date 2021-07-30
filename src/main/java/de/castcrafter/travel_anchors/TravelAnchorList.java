package de.castcrafter.travel_anchors;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TravelAnchorList extends SavedData {

    private static final TravelAnchorList clientInstance = new TravelAnchorList();

    public static TravelAnchorList get(Level level) {
        if (!level.isClientSide) {
            DimensionDataStorage storage = ((ServerLevel) level).getDataStorage();
            return storage.computeIfAbsent(TravelAnchorList::new, TravelAnchorList::new, TravelAnchors.getInstance().modid);
        } else {
            return clientInstance;
        }
    }

    public TravelAnchorList() {
        
    }

    public TravelAnchorList(CompoundTag nbt) {
        this();
        this.load(nbt);
    }

    public final HashMap<BlockPos, Entry> anchors = new HashMap<>();

    public void load(@Nonnull CompoundTag nbt) {
        this.anchors.clear();
        if (nbt.contains("anchors", Constants.NBT.TAG_LIST)) {
            ListTag list = nbt.getList("anchors", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entryNBT = list.getCompound(i);
                if (entryNBT.contains("x") && entryNBT.contains("y") && entryNBT.contains("z") && entryNBT.contains("name")) {
                    BlockPos pos = new BlockPos(entryNBT.getInt("x"), entryNBT.getInt("y"), entryNBT.getInt("z")).immutable();
                    String name = entryNBT.getString("name");
                    if (!name.isEmpty()) {
                        this.anchors.put(pos, new Entry(entryNBT.getString("name"), entryNBT.contains("state") ? Block.stateById(entryNBT.getInt("state")) : ModComponents.travelAnchor.defaultBlockState()));
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag compound) {
        ListTag list = new ListTag();
        for (Map.Entry<BlockPos, Entry> entry : this.anchors.entrySet()) {
            CompoundTag entryNBT = new CompoundTag();
            entryNBT.putInt("x", entry.getKey().getX());
            entryNBT.putInt("y", entry.getKey().getY());
            entryNBT.putInt("z", entry.getKey().getZ());
            entryNBT.putString("name", entry.getValue().name);
            entryNBT.putInt("state", Block.getId(entry.getValue().state));
            list.add(entryNBT);
        }
        compound.put("anchors", list);
        return compound;
    }

    public void setAnchor(Level level, BlockPos pos, @Nullable String name, @Nullable BlockState state) {
        if (!level.isClientSide) {
            boolean needsUpdate = false;
            BlockPos immutablePos = pos.immutable();
            if (name == null || name.trim().isEmpty()) {
                if (this.anchors.containsKey(immutablePos)) {
                    this.anchors.remove(immutablePos);
                    needsUpdate = true;
                }
            } else {
                if (state == null) state = ModComponents.travelAnchor.defaultBlockState();
                Entry oldEntry = this.anchors.getOrDefault(immutablePos, null);
                if (oldEntry == null || !oldEntry.name.equals(name) || oldEntry.state != state) {
                    this.anchors.put(pos.immutable(), new Entry(name, state));
                    needsUpdate = true;
                }
            }
            this.setDirty();
            if (needsUpdate) {
                TravelAnchors.getNetwork().updateTravelAnchorList(level, this);
            }
        }
    }

    public String getAnchor(BlockPos pos) {
        Entry entry = this.getEntry(pos);
        return entry == null ? null : entry.name;
    }
    
    public Entry getEntry(BlockPos pos) {
        return this.anchors.getOrDefault(pos.immutable(), null);
    }

    public Stream<Pair<BlockPos, String>> getAnchorsAround(Vec3 pos, double maxDistanceSq) {
        return this.anchors.entrySet().stream()
                .filter(entry -> entry.getKey().distSqr(pos.x, pos.y, pos.z, true) < maxDistanceSq)
                .map(entry -> Pair.of(entry.getKey(), entry.getValue().name));
    }
    
    public static class Entry {
        
        public String name;
        public BlockState state;

        public Entry(String name, BlockState state) {
            this.name = name;
            this.state = state;
        }
    }
}

