package xaero.hud;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import xaero.common.HudMod;
import xaero.common.controls.ControlsHandler;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.hud.controls.key.KeyMappingTickHandler;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.module.HudModule;
import xaero.hud.module.ModuleSession;
import xaero.hud.module.ModuleSessionHandler;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/HudSession.class */
public class HudSession {
    protected final HudMod modMain;

    @Deprecated
    protected ControlsHandler controls;
    protected KeyMappingTickHandler keyMappingTickHandler;
    private final Map<HudModule<?>, ModuleSession<?>> moduleSessions = new HashMap();
    private HudModule<?> lastModuleSessionRequest;
    private ModuleSession<?> lastModuleSessionPassed;
    protected boolean usable;

    public HudSession(HudMod modMain) {
        this.modMain = modMain;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <MS extends ModuleSession<MS>> MS getSession(HudModule<MS> hudModule) {
        if (hudModule == this.lastModuleSessionRequest) {
            return (MS) this.lastModuleSessionPassed;
        }
        MS ms = (MS) this.moduleSessions.get(hudModule);
        this.lastModuleSessionRequest = hudModule;
        this.lastModuleSessionPassed = ms;
        return ms;
    }

    public void init(ClientPacketListener connection) throws IOException {
        this.lastModuleSessionRequest = null;
        this.lastModuleSessionPassed = null;
        this.keyMappingTickHandler = new KeyMappingTickHandler(this.modMain.getKeyMappingControllers());
        ModuleSessionHandler sessionHandler = this.modMain.getHud().getSessionHandler();
        HudMod hudMod = this.modMain;
        Map<HudModule<?>, ModuleSession<?>> map = this.moduleSessions;
        Objects.requireNonNull(map);
        sessionHandler.resetSessions(hudMod, connection, (v1, v2) -> {
            r3.put(v1, v2);
        });
        this.usable = true;
        MinimapLogs.LOGGER.info("New Xaero hud session initialized!");
    }

    public final void tryCleanup() {
        try {
            cleanup();
            MinimapLogs.LOGGER.info("Xaero hud session finalized.");
        } catch (Throwable t) {
            MinimapLogs.LOGGER.error("Xaero hud session failed to finalize properly.", t);
        }
        this.moduleSessions.clear();
        this.usable = false;
    }

    protected void cleanup() {
        this.lastModuleSessionRequest = null;
        this.lastModuleSessionPassed = null;
        this.modMain.getHud().getSessionHandler().closeSessions(this.modMain);
    }

    @Deprecated
    public MultiTextureRenderTypeRendererProvider getMultiTextureRenderTypeRenderers() {
        return ((MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession()).getMultiTextureRenderTypeRenderers();
    }

    public static HudSession getCurrentSession() {
        HudSession session = getForPlayer(Minecraft.getInstance().player);
        if (session == null && XaeroMinimapCore.currentSession != null && XaeroMinimapCore.currentSession.usable) {
            session = XaeroMinimapCore.currentSession;
        }
        return session;
    }

    public static HudSession getForPlayer(LocalPlayer player) {
        if (player == null || player.connection == null) {
            return null;
        }
        return player.connection.getXaero_minimapSession();
    }

    @Deprecated
    public ControlsHandler getControls() {
        return this.controls;
    }

    public KeyMappingTickHandler getKeyMappingTickHandler() {
        return this.keyMappingTickHandler;
    }

    public HudMod getHudMod() {
        return this.modMain;
    }
}
