package xaero.lib;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xaero.lib.client.XaeroLibClient;
import xaero.lib.client.config.channel.register.handler.LibChannelClientRegistryHandler;
import xaero.lib.common.compat.ModCompatibility;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.channel.register.ConfigChannelRegistry;
import xaero.lib.common.config.channel.register.handler.LibChannelCommonRegistryHandler;
import xaero.lib.common.packet.IPacketHandler;
import xaero.lib.common.packet.PacketHandlerRegistry;
import xaero.lib.common.packet.PacketRegister;
import xaero.lib.common.permission.LibPermissionNodes;
import xaero.lib.common.permission.PermissionRegistry;
import xaero.lib.common.permission.system.PermissionSystemRegistry;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/XaeroLib.class */
public abstract class XaeroLib {
    public static final String MOD_ID = "xaerolib";
    public static XaeroLib INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger(XaeroLib.class);
    private XaeroLibClient client;
    private Throwable commonFirstStageError;
    private IPacketHandler packetHandler;
    private ConfigChannel libConfigChannel;
    private boolean loaded;

    public XaeroLib() {
        INSTANCE = this;
        ModCompatibility.getInstance().registerPermissionSystems();
        this.libConfigChannel = ConfigChannel.Builder.begin().setId(ResourceLocation.fromNamespaceAndPath(MOD_ID, "main")).setCommonRegistryHandler(new LibChannelCommonRegistryHandler()).setClientRegistryHandlerSupplier(LibChannelClientRegistryHandler::new).setLogger(LOGGER).setConfigPath(Services.PLATFORM.getConfigDir().resolve("xaero").resolve("lib")).setDefaultConfigsPath(Services.PLATFORM.getConfigDir().resolveSibling("defaultconfigs").resolve("xaero").resolve("lib")).setDefaultEnforcedServerProfileNodePath("xaero.lib.enforced_server_profile").build();
        ConfigChannelRegistry.INSTANCE.register(this.libConfigChannel);
        LibPermissionNodes.registerAll();
    }

    public void loadCommon() {
        try {
            LOGGER.info("Loading XaeroLib common 1/2!");
            this.packetHandler = PacketHandlerRegistry.INSTANCE.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "main"), 1000000, "1.0");
            new PacketRegister().register();
        } catch (Throwable t) {
            this.commonFirstStageError = t;
        }
    }

    public void loadCommonLater() {
        if (this.commonFirstStageError != null) {
            throw new RuntimeException(this.commonFirstStageError);
        }
        LOGGER.info("Loading XaeroLib common 2/2!");
        ConfigChannelRegistry.INSTANCE.freeze();
        PermissionRegistry.INSTANCE.freeze();
        PacketHandlerRegistry.INSTANCE.freeze();
        PermissionSystemRegistry.INSTANCE.freeze();
        ModCompatibility.getInstance().freeze();
    }

    public void loadServer() {
        if (this.commonFirstStageError != null) {
            return;
        }
        try {
            LOGGER.info("Loading XaeroLib server 1/2!");
        } catch (Throwable t) {
            this.commonFirstStageError = t;
        }
    }

    public void loadServerLater() {
        LOGGER.info("Loading XaeroLib server 2/2!");
        this.loaded = true;
    }

    public void loadClient() {
        if (this.commonFirstStageError != null) {
            return;
        }
        this.client = new XaeroLibClient();
        this.client.load();
    }

    public void loadClientLater() {
        this.client.loadLater();
        this.loaded = true;
    }

    public XaeroLibClient getClient() {
        return this.client;
    }

    public IPacketHandler getPacketHandler() {
        return this.packetHandler;
    }

    public ConfigChannel getLibConfigChannel() {
        return this.libConfigChannel;
    }

    public static boolean isLoaded() {
        return INSTANCE != null && INSTANCE.loaded;
    }
}
