package xaero.hud.compat.mods;

import net.minecraft.client.gui.GuiGraphics;
import xaero.lib.client.graphics.util.ImmediateRenderUtil;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/compat/mods/ImmediatelyFastHelper.class */
public class ImmediatelyFastHelper {
    public static void triggerBatchingBuffersFlush(GuiGraphics guiGraphics) {
        ImmediateRenderUtil.coloredRectangle(guiGraphics.pose().last().pose(), 0.0f, 0.0f, 0.0f, 0.0f, 0);
    }
}
