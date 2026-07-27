package xaero.hud.minimap.info.render.compile;

import net.minecraft.core.BlockPos;
import xaero.hud.minimap.info.InfoDisplay;
import xaero.hud.minimap.module.MinimapSession;

@FunctionalInterface
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/render/compile/InfoDisplayOnCompile.class */
public interface InfoDisplayOnCompile<T> {
    void onCompile(InfoDisplay<T> infoDisplay, InfoDisplayCompiler infoDisplayCompiler, MinimapSession minimapSession, int i, BlockPos blockPos);
}
