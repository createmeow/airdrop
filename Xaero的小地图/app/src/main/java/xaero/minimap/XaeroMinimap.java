package xaero.minimap;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xaero.common.HudClientOnlyBase;
import xaero.common.HudMod;
import xaero.common.XaeroMinimapSession;
import xaero.common.events.ClientEventsListener;
import xaero.common.gui.GuiHelper;
import xaero.common.mods.SupportMods;
import xaero.common.mods.SupportXaeroWorldmap;
import xaero.common.platform.Services;
import xaero.common.settings.ModSettings;
import xaero.hud.common.config.channel.register.HudModConfigCommonRegistryHandler;
import xaero.hud.config.channel.register.HudModConfigClientRegistryHandler;
import xaero.hud.controls.ControlsRegister;
import xaero.hud.minimap.controls.key.MinimapKeyMappings;
import xaero.hud.xminimap.controls.XMinimapControlsRegister;
import xaero.hud.xminimap.controls.key.XMinimapKeyMappings;
import xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler;
import xaero.lib.patreon.Patreon;
import xaero.minimap.gui.MinimapGuiHelper;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/minimap/XaeroMinimap.class */
public abstract class XaeroMinimap extends HudMod {
    public static final Logger LOGGER = LogManager.getLogger();
    public static XaeroMinimap instance;
    public static final String MOD_ID = "xaerominimap";

    public XaeroMinimap() {
        instance = this;
    }

    @Override // xaero.common.HudMod
    protected void loadClient() throws IOException {
        super.loadClient();
    }

    @Override // xaero.common.HudMod
    protected void loadCommon() throws ClassNotFoundException {
        SupportMods.checkForMinimapDuplicates("xaero.pvp.BetterPVP");
        super.loadCommon();
    }

    @Override // xaero.common.HudMod
    protected String getCommonConfigFileName() {
        return "xaerominimap-common.txt";
    }

    @Override // xaero.common.IXaeroMinimap
    public String getModId() {
        return MOD_ID;
    }

    @Override // xaero.common.HudMod
    protected ModSettings createModSettings() {
        return new ModSettings(this);
    }

    @Override // xaero.common.HudMod
    protected GuiHelper createGuiHelper() {
        return new MinimapGuiHelper(this);
    }

    @Override // xaero.common.HudMod
    public String getOldConfigFileName() {
        return "xaerominimap.txt";
    }

    @Override // xaero.common.HudMod
    protected HudClientOnlyBase createClientOnly() {
        return new MinimapClientOnly();
    }

    @Override // xaero.common.HudMod
    protected String getModName() {
        return MinimapKeyMappings.CATEGORY;
    }

    @Override // xaero.common.HudMod
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override // xaero.common.HudMod
    protected ClientEventsListener createForgeEventHandlerListener() {
        return new ClientEventsListener();
    }

    @Override // xaero.common.IXaeroMinimap
    public String getVersionsURL() {
        return "http://data.chocolateminecraft.com/Versions_" + Patreon.getKEY_VERSION2() + "/Minimap.dat";
    }

    @Override // xaero.common.IXaeroMinimap
    public String getUpdateLink() {
        return "http://chocolateminecraft.com/update/minimap.html";
    }

    @Override // xaero.common.HudMod
    protected ControlsRegister createControlsRegister() {
        return new XMinimapControlsRegister();
    }

    @Override // xaero.common.IXaeroMinimap
    public XaeroMinimapSession createSession() {
        return new XaeroMinimapStandaloneSession(this);
    }

    @Override // xaero.common.IXaeroMinimap
    public Object getSettingsKey() {
        return XMinimapKeyMappings.SETTINGS;
    }

    @Override // xaero.common.IXaeroMinimap
    public Object getServerSettingsKey() {
        return XMinimapKeyMappings.SERVER_PROFILES;
    }

    @Override // xaero.common.HudMod
    public Path getConfigSubFolder() {
        return Services.PLATFORM.getConfigDir().resolve("xaero").resolve(SupportXaeroWorldmap.MINIMAP_MW);
    }

    @Override // xaero.common.HudMod
    public Path getDefaultConfigsSubFolder() {
        return Services.PLATFORM.getConfigDir().resolveSibling("defaultconfigs").resolve("xaero").resolve(SupportXaeroWorldmap.MINIMAP_MW);
    }

    @Override // xaero.common.HudMod
    protected HudModConfigCommonRegistryHandler createConfigCommonRegistryHandler() {
        return new HudModConfigCommonRegistryHandler();
    }

    @Override // xaero.common.HudMod
    protected Supplier<IConfigChannelClientRegistryHandler> createConfigClientRegistryHandlerSupplier() {
        return HudModConfigClientRegistryHandler::new;
    }

    @Override // xaero.common.HudMod, xaero.common.IXaeroMinimap
    public boolean isStandalone() {
        return true;
    }
}
