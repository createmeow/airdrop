package xaero.lib.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import xaero.lib.client.level.ClientLevelData;
import xaero.lib.client.level.IClientLevel;

@Mixin({ClientLevel.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/mixin/MixinClientLevel.class */
public class MixinClientLevel implements IClientLevel {
    private ClientLevelData xaerolib_data;

    @Override // xaero.lib.client.level.IClientLevel
    public ClientLevelData xaerolib_getData() {
        return this.xaerolib_data;
    }

    @Override // xaero.lib.client.level.IClientLevel
    public void xaerolib_setData(ClientLevelData data) {
        this.xaerolib_data = data;
    }
}
