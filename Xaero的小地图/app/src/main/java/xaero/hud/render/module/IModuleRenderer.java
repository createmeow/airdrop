package xaero.hud.render.module;

import net.minecraft.client.gui.GuiGraphics;
import xaero.hud.module.ModuleSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/render/module/IModuleRenderer.class */
public interface IModuleRenderer<MS extends ModuleSession<MS>> {
    void render(MS ms, ModuleRenderContext moduleRenderContext, GuiGraphics guiGraphics, float f);
}
