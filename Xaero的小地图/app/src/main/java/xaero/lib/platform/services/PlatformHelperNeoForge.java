package xaero.lib.platform.services;

import java.nio.file.Path;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.LoadingModList;
import xaero.lib.client.controls.util.IKeyMappingHelper;
import xaero.lib.client.controls.util.KeyMappingHelperNeoForge;
import xaero.lib.client.graphics.util.IPlatformRenderHelper;
import xaero.lib.client.graphics.util.RenderHelperNeoForge;
import xaero.lib.common.compat.NeoForgeModCompatibility;
import xaero.lib.common.compat.PlatformModCompatibility;
import xaero.lib.common.packet.IPacketHandler;
import xaero.lib.common.packet.PacketHandlerNeoForge;
import xaero.lib.common.reflection.util.ObfuscatedReflectionNeoForge;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/platform/services/PlatformHelperNeoForge.class */
public class PlatformHelperNeoForge implements IPlatformHelper {
    private final KeyMappingHelperNeoForge keyMappingHelper = new KeyMappingHelperNeoForge();
    private final RenderHelperNeoForge renderHelper = new RenderHelperNeoForge();
    private final ObfuscatedReflectionNeoForge obfuscatedReflection = new ObfuscatedReflectionNeoForge();

    @Override // xaero.lib.platform.services.IPlatformHelper
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override // xaero.lib.platform.services.IPlatformHelper
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override // xaero.lib.platform.services.IPlatformHelper
    public boolean checkModForMixin(String modId) {
        return LoadingModList.get().getModFileById(modId) != null;
    }

    @Override // xaero.lib.platform.services.IPlatformHelper
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override // xaero.lib.platform.services.IPlatformHelper
    public boolean isDedicatedServer() {
        return FMLLoader.getDist() == Dist.DEDICATED_SERVER;
    }

    @Override // xaero.lib.platform.services.IPlatformHelper
    public IKeyMappingHelper getKeyMappingHelper() {
        return this.keyMappingHelper;
    }

    @Override // xaero.lib.platform.services.IPlatformHelper
    public IPlatformRenderHelper getPlatformRenderHelper() {
        return this.renderHelper;
    }

    @Override // xaero.lib.platform.services.IPlatformHelper
    public ObfuscatedReflectionNeoForge getObfuscatedReflection() {
        return this.obfuscatedReflection;
    }

    @Override // xaero.lib.platform.services.IPlatformHelper
    public IPacketHandler createPacketHandler(ResourceLocation channelId, int protocolVersion, String protocolVersionString) {
        return PacketHandlerNeoForge.Builder.begin().setChannelId(channelId).setProtocolVersion(protocolVersionString).build();
    }

    @Override // xaero.lib.platform.services.IPlatformHelper
    public PlatformModCompatibility createPlatformModCompatibility() {
        return new NeoForgeModCompatibility();
    }

    @Override // xaero.lib.platform.services.IPlatformHelper
    public Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override // xaero.lib.platform.services.IPlatformHelper
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
