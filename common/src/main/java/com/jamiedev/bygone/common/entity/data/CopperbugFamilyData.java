package com.jamiedev.bygone.common.entity.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CopperbugFamilyData extends SavedData {
    private static final SavedData.Factory<CopperbugFamilyData> FACTORY = new SavedData.Factory<>(CopperbugFamilyData::new, CopperbugFamilyData::load, null);
    private final Map<UUID, Set<UUID>> families = new HashMap<>();

    public static CopperbugFamilyData get(ServerLevel serverLevel) {
        return serverLevel.getServer().overworld().getDataStorage().computeIfAbsent(
                FACTORY, "bygone_copperbug_families");
    }

    public static CopperbugFamilyData load(CompoundTag compoundTag, HolderLookup.Provider provider) {
        CopperbugFamilyData familyData = new CopperbugFamilyData();
        ListTag families = compoundTag.getList("Families", Tag.TAG_COMPOUND);
        for (int familyIndex = 0; familyIndex < families.size(); ++familyIndex) {
            CompoundTag familyTag = families.getCompound(familyIndex);
            if (!familyTag.hasUUID("FamilyId")) {
                continue;
            }

            Set<UUID> members = new HashSet<>();
            for (Tag memberTag : familyTag.getList("Members", Tag.TAG_INT_ARRAY)) {
                members.add(NbtUtils.loadUUID(memberTag));
            }
            familyData.families.put(familyTag.getUUID("FamilyId"), members);
        }
        return familyData;
    }

    public void addMember(UUID familyId, UUID copperbugId) {
        if (this.families.computeIfAbsent(familyId, family -> new HashSet<>()).add(copperbugId)) {
            this.setDirty();
        }
    }

    public void removeMember(UUID familyId, UUID copperbugId) {
        Set<UUID> members = this.families.get(familyId);
        if (members == null || !members.remove(copperbugId)) {
            return;
        }

        if (members.isEmpty()) {
            this.families.remove(familyId);
        }
        this.setDirty();
    }

    public int getFamilySize(UUID familyId) {
        Set<UUID> members = this.families.get(familyId);
        return members == null ? 0 : members.size();
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag families = new ListTag();
        for (Map.Entry<UUID, Set<UUID>> family : this.families.entrySet()) {
            CompoundTag familyTag = new CompoundTag();
            familyTag.putUUID("FamilyId", family.getKey());
            ListTag members = new ListTag();
            for (UUID copperbugId : family.getValue()) {
                members.add(NbtUtils.createUUID(copperbugId));
            }
            familyTag.put("Members", members);
            families.add(familyTag);
        }
        compoundTag.put("Families", families);
        return compoundTag;
    }
}
