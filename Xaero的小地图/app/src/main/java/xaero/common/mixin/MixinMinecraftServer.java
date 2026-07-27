package xaero.common.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import xaero.common.server.IMinecraftServer;
import xaero.common.server.MinecraftServerData;

@Mixin({MinecraftServer.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinMinecraftServer.class */
public class MixinMinecraftServer implements IMinecraftServer {
    private MinecraftServerData xaeroMinimapServerData;

    @Override // xaero.common.server.IMinecraftServer
    public MinecraftServerData getXaeroMinimapServerData() {
        return this.xaeroMinimapServerData;
    }

    @Override // xaero.common.server.IMinecraftServer
    public void setXaeroMinimapServerData(MinecraftServerData data) {
        this.xaeroMinimapServerData = data;
    }
}
