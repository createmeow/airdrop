package xaero.lib.platform.services;

import java.nio.file.Path;
import net.minecraft.resources.ResourceLocation;
import xaero.lib.client.controls.util.IKeyMappingHelper;
import xaero.lib.client.graphics.util.IPlatformRenderHelper;
import xaero.lib.common.compat.PlatformModCompatibility;
import xaero.lib.common.packet.IPacketHandler;
import xaero.lib.common.reflection.util.IObfuscatedReflection;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/platform/services/IPlatformHelper.class */
public interface IPlatformHelper {
    String getPlatformName();

    boolean isModLoaded(String str);

    boolean isDevelopmentEnvironment();

    boolean isDedicatedServer();

    IPacketHandler createPacketHandler(ResourceLocation resourceLocation, int i, String str);

    PlatformModCompatibility createPlatformModCompatibility();

    IKeyMappingHelper getKeyMappingHelper();

    IPlatformRenderHelper getPlatformRenderHelper();

    IObfuscatedReflection getObfuscatedReflection();

    Path getGameDir();

    Path getConfigDir();

    default boolean checkModForMixin(String modId) {
        return isModLoaded(modId);
    }

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
