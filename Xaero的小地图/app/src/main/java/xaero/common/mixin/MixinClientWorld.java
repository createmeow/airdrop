package xaero.common.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import xaero.common.minimap.mcworld.IXaeroMinimapClientWorld;
import xaero.common.minimap.mcworld.MinimapClientWorldData;

@Mixin({ClientLevel.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinClientWorld.class */
public class MixinClientWorld implements IXaeroMinimapClientWorld {
    private MinimapClientWorldData xaero_minimapData;

    @Override // xaero.common.minimap.mcworld.IXaeroMinimapClientWorld
    public MinimapClientWorldData getXaero_minimapData() {
        return this.xaero_minimapData;
    }

    @Override // xaero.common.minimap.mcworld.IXaeroMinimapClientWorld
    public void setXaero_minimapData(MinimapClientWorldData xaero_minimapData) {
        this.xaero_minimapData = xaero_minimapData;
    }
}
