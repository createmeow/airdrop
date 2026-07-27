package xaero.common.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xaero.common.core.IXaeroMinimapMinecraftClient;

@Mixin({Minecraft.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinMinecraftClient.class */
public class MixinMinecraftClient implements IXaeroMinimapMinecraftClient {

    @Shadow
    private static int fps;

    @Override // xaero.common.core.IXaeroMinimapMinecraftClient
    public int getXaeroMinimap_fps() {
        return fps;
    }
}
