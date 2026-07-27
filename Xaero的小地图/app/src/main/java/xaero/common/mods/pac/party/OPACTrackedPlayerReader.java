package xaero.common.mods.pac.party;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader;
import xaero.pac.common.parties.party.api.IPartyMemberDynamicInfoSyncableAPI;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mods/pac/party/OPACTrackedPlayerReader.class */
public class OPACTrackedPlayerReader implements ITrackedPlayerReader<IPartyMemberDynamicInfoSyncableAPI> {
    private final Map<ResourceLocation, ResourceKey<Level>> dimensionKeyCache = new HashMap();

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public UUID getId(IPartyMemberDynamicInfoSyncableAPI player) {
        return player.getPlayerId();
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public double getX(IPartyMemberDynamicInfoSyncableAPI player) {
        return player.getX();
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public double getY(IPartyMemberDynamicInfoSyncableAPI player) {
        return player.getY();
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public double getZ(IPartyMemberDynamicInfoSyncableAPI player) {
        return player.getZ();
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public ResourceKey<Level> getDimension(IPartyMemberDynamicInfoSyncableAPI player) {
        if (player.getDimension() == null) {
            return null;
        }
        ResourceKey<Level> result = this.dimensionKeyCache.get(player.getDimension());
        if (result == null) {
            Map<ResourceLocation, ResourceKey<Level>> map = this.dimensionKeyCache;
            ResourceLocation dimension = player.getDimension();
            ResourceKey<Level> resourceKeyCreate = ResourceKey.create(Registries.DIMENSION, player.getDimension());
            result = resourceKeyCreate;
            map.put(dimension, resourceKeyCreate);
        }
        return result;
    }
}
