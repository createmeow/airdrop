package xaero.hud.minimap.radar.state;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import xaero.common.HudMod;
import xaero.common.effect.Effects;
import xaero.common.minimap.mcworld.MinimapClientWorldDataHelper;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;
import xaero.hud.category.rule.resolver.ObjectCategoryRuleResolver;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.radar.category.EntityRadarCategory;
import xaero.hud.minimap.radar.category.EntityRadarCategoryManager;
import xaero.hud.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.hud.minimap.radar.state.RadarList;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/state/RadarStateUpdater.class */
public class RadarStateUpdater {
    private final EntityRadarCategoryManager categoryManager;
    private final RadarState state;
    private Entity lastRenderEntity;
    private final Map<EntityRadarCategory, Map<EntityRadarCategory, RadarList>> updateMap = new HashMap();

    public RadarStateUpdater(EntityRadarCategoryManager categoryManager, RadarState state) {
        this.categoryManager = categoryManager;
        this.state = state;
    }

    public void update(ClientLevel world, Entity renderEntity, Player player) {
        EntityRadarCategory syncedEntityCategory;
        int entityNumber;
        if (renderEntity == null) {
            renderEntity = this.lastRenderEntity;
        }
        List<RadarList> radarLists = this.state.getUpdatableLists();
        EntityRadarCategory rootCategory = this.categoryManager.getRootCategory();
        EntityRadarCategory syncedRootCategory = this.categoryManager.getEffectiveSyncedRootCategory();
        ensureCategories(this.state, rootCategory, syncedRootCategory, radarLists);
        radarLists.forEach((v0) -> {
            v0.clearEntities();
        });
        if (HudMod.INSTANCE.isFairPlay()) {
            return;
        }
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        boolean displayRadar = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.DISPLAY_RADAR)).booleanValue();
        if ((!displayRadar && !isWorldMapRadarEnabled()) || world == null || renderEntity == null || player == null || Misc.hasEffect(player, Effects.NO_RADAR) || Misc.hasEffect(player, Effects.NO_RADAR_HARMFUL)) {
            return;
        }
        if (!HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getServerSynced().isChannelPresentOnServer() && !MinimapClientWorldDataHelper.getWorldData(world).getSyncedRules().allowRadarOnServer) {
            return;
        }
        ObjectCategoryRuleResolver categoryRuleResolver = this.categoryManager.getRuleResolver();
        Iterable<Entity> worldEntities = world.entitiesForRendering();
        boolean shouldHideInvisible = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.RADAR_HIDE_INVISIBLE)).booleanValue();
        for (Entity entity : worldEntities) {
            if (entity != null && (!shouldHideInvisible || !isInvisibleTo(entity, player))) {
                EntityRadarCategory entityCategory = (EntityRadarCategory) categoryRuleResolver.resolve(rootCategory, entity, player);
                if (entityCategory != null) {
                    if (syncedRootCategory == null) {
                        syncedEntityCategory = null;
                    } else {
                        syncedEntityCategory = (EntityRadarCategory) categoryRuleResolver.resolve(syncedRootCategory, entity, player);
                        if (syncedEntityCategory == null) {
                        }
                    }
                    RadarList radarList = this.updateMap.get(entityCategory).get(syncedEntityCategory == null ? entityCategory : syncedEntityCategory);
                    if (((Boolean) radarList.getEffective(EntityRadarCategorySettings.DISPLAYED)).booleanValue()) {
                        double offY = renderEntity.getY() - entity.getY();
                        int heightLimit = ((Double) radarList.getEffective(EntityRadarCategorySettings.HEIGHT_LIMIT)).intValue();
                        if (offY * offY <= heightLimit * heightLimit && ((entityNumber = ((Double) radarList.getEffective(EntityRadarCategorySettings.ENTITY_NUMBER)).intValue()) == 0 || radarList.size() < entityNumber)) {
                            radarList.add(entity);
                        }
                    }
                }
            }
        }
    }

    private void ensureCategories(RadarState state, EntityRadarCategory rootCategory, EntityRadarCategory syncedRootCategory, List<RadarList> radarLists) {
        boolean reversedOrder = ModSettings.keyReverseEntityRadar.isDown();
        if (state.getListsGeneratedForConfig() != rootCategory || state.getListsGeneratedForSyncedConfig() != syncedRootCategory) {
            this.updateMap.clear();
            radarLists.clear();
            traceAddCategories(rootCategory, syncedRootCategory, radarLists);
            Collections.sort(radarLists);
            state.setListsGeneratedForConfig(rootCategory);
            state.setListsGeneratedForSyncedConfig(syncedRootCategory);
            state.setListsReversedOrder(false);
        }
        if (state.getListsReversedOrder() == reversedOrder) {
            return;
        }
        Collections.reverse(radarLists);
        state.setListsReversedOrder(reversedOrder);
    }

    private void traceAddCategories(EntityRadarCategory category, EntityRadarCategory syncedRootCategory, List<RadarList> radarLists) {
        category.getDirectSubCategoryIterator().forEachRemaining(sb -> {
            traceAddCategories(sb, syncedRootCategory, radarLists);
        });
        if (syncedRootCategory == null) {
            RadarList radarList = RadarList.Builder.getDefault().build().setClientCategory(category).setSyncedCategory(null);
            putOnUpdateMap(category, category, radarList);
            radarLists.add(radarList);
            return;
        }
        traceAddSyncedCategories(category, syncedRootCategory, radarLists);
    }

    private void traceAddSyncedCategories(EntityRadarCategory category, EntityRadarCategory syncedCategory, List<RadarList> radarLists) {
        syncedCategory.getDirectSubCategoryIterator().forEachRemaining(sb -> {
            traceAddSyncedCategories(category, sb, radarLists);
        });
        RadarList radarList = RadarList.Builder.getDefault().build().setClientCategory(category).setSyncedCategory(syncedCategory);
        putOnUpdateMap(category, syncedCategory, radarList);
        radarLists.add(radarList);
    }

    private void putOnUpdateMap(EntityRadarCategory category, EntityRadarCategory syncedCategory, RadarList radarList) {
        Map<EntityRadarCategory, RadarList> syncedToListMap = this.updateMap.get(category);
        if (syncedToListMap == null) {
            Map<EntityRadarCategory, Map<EntityRadarCategory, RadarList>> map = this.updateMap;
            HashMap map2 = new HashMap();
            syncedToListMap = map2;
            map.put(category, map2);
        }
        syncedToListMap.put(syncedCategory, radarList);
    }

    private boolean isWorldMapRadarEnabled() {
        if (!HudMod.INSTANCE.getSupportMods().worldmap()) {
            return false;
        }
        return HudMod.INSTANCE.getSupportMods().worldmapSupport.worldMapIsRenderingRadar();
    }

    private boolean isInvisibleTo(Entity entity, Player player) {
        return entity.isInvisibleTo(player) || shouldHideForSneaking(entity, player);
    }

    private boolean shouldHideForSneaking(Entity e, Player p) {
        if (!e.isShiftKeyDown()) {
            return false;
        }
        PlayerTeam team = e.getTeam();
        return team == null || team != p.getTeam();
    }

    public void setLastRenderViewEntity(Entity lastRenderEntity) {
        this.lastRenderEntity = lastRenderEntity;
    }
}
