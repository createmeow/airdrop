package xaero.hud.minimap.radar.render.element;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.Entity;
import xaero.common.HudMod;
import xaero.common.minimap.element.render.MinimapElementRenderProvider;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.hud.minimap.radar.state.RadarList;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/render/element/RadarRenderProvider.class */
public abstract class RadarRenderProvider extends MinimapElementRenderProvider<Entity, RadarRenderContext> {
    private boolean used;
    private Entity renderEntity;
    private Iterator<RadarList> entityLists;
    private RadarList currentList;
    private RadarList listForContext;
    private int currentListIndex;

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public void begin(MinimapElementRenderLocation location, RadarRenderContext context) {
        MinimapSession minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        this.used = true;
        this.renderEntity = Minecraft.getInstance().getCameraEntity();
        context.reversedOrder = ModSettings.keyReverseEntityRadar.isDown();
        Screen screenBU = Minecraft.getInstance().screen;
        Minecraft.getInstance().screen = null;
        context.playerListDown = Minecraft.getInstance().options.keyPlayerList.isDown() || ModSettings.keyAlternativeListPlayers.isDown();
        Minecraft.getInstance().screen = screenBU;
        this.entityLists = minimapSession.getRadarSession().getState().getRadarLists().iterator();
        this.currentList = null;
        this.listForContext = null;
        this.currentListIndex = 0;
    }

    private void ensureList(MinimapElementRenderLocation location, RadarRenderContext context) {
        while (true) {
            if (this.currentList == null || this.currentListIndex >= this.currentList.size() || this.currentListIndex < 0) {
                while (this.entityLists.hasNext()) {
                    this.currentList = this.entityLists.next();
                    this.currentListIndex = context.reversedOrder ? this.currentList.size() - 1 : 0;
                    if (location == MinimapElementRenderLocation.IN_MINIMAP || location == MinimapElementRenderLocation.OVER_MINIMAP) {
                        if ((location == MinimapElementRenderLocation.OVER_MINIMAP) == shouldRenderOverMinimap(context)) {
                            break;
                        }
                    }
                }
                this.currentList = null;
                this.currentListIndex = 0;
                return;
            }
            return;
        }
    }

    private boolean shouldRenderOverMinimap(RadarRenderContext context) {
        int settingValue = ((Double) this.currentList.getEffective(EntityRadarCategorySettings.RENDER_OVER_MINIMAP)).intValue();
        return settingValue == 2 || (settingValue == 1 && context.playerListDown);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public boolean hasNext(MinimapElementRenderLocation location, RadarRenderContext context) {
        ensureList(location, context);
        if (this.currentList == null) {
            return false;
        }
        return (!context.reversedOrder && this.currentListIndex < this.currentList.size()) || (context.reversedOrder && this.currentListIndex >= 0);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public Entity setupContextAndGetNext(MinimapElementRenderLocation location, RadarRenderContext context) {
        ensureList(location, context);
        if (this.listForContext != this.currentList) {
            context.radarList = this.currentList;
            setupContextForList(this.currentList, context);
            this.listForContext = this.currentList;
        }
        Entity result = getNext(location, context);
        if (result == null) {
            return null;
        }
        setupContextForEntity(result, context);
        return result;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public Entity getNext(MinimapElementRenderLocation location, RadarRenderContext context) {
        Entity result = this.currentList.get(this.currentListIndex);
        this.currentListIndex += context.reversedOrder ? -1 : 1;
        if (this.renderEntity == result) {
            return null;
        }
        return result;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public void end(MinimapElementRenderLocation location, RadarRenderContext context) {
        this.used = false;
        this.renderEntity = null;
        context.radarList = null;
    }

    public void setupContextForList(RadarList radarList, RadarRenderContext context) {
        int iIntValue;
        context.iconScale = ((Double) radarList.getEffective(EntityRadarCategorySettings.ICON_SCALE)).doubleValue();
        if (context.isMainDot) {
            iIntValue = ((Integer) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.RADAR_MAIN_DOT_SIZE)).intValue();
        } else {
            iIntValue = ((Double) radarList.getEffective(EntityRadarCategorySettings.DOT_SIZE)).intValue();
        }
        context.dotSize = iIntValue;
        context.dotScale = 1.0d + (0.5d * (context.dotSize - 1));
        int icons = ((Double) radarList.getEffective(EntityRadarCategorySettings.ICONS)).intValue();
        context.iconsForCategory = (icons == 1 && context.playerListDown) || icons == 2;
    }

    public void setupContextForEntity(Entity entity, RadarRenderContext context) {
        context.icon = context.iconsForCategory;
    }

    public boolean isUsed() {
        return this.used;
    }
}
