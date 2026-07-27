package xaero.common.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.core.XaeroMinimapCore;

@Mixin({Gui.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinOptionalInGameHud.class */
public class MixinOptionalInGameHud {
    @Inject(at = {@At("HEAD")}, method = {"renderHotbarAndDecorations"})
    public void onRenderHotbarAndDecorations(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        XaeroMinimapCore.handleRenderModOverlay(guiGraphics, deltaTracker);
    }

    @Inject(at = {@At("RETURN")}, method = {"render"})
    public void onRenderEnd(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        XaeroMinimapCore.afterIngameGuiRender(guiGraphics, deltaTracker);
    }

    @Inject(at = {@At("HEAD")}, method = {"renderCrosshair"}, cancellable = true)
    public void onRenderCrosshair(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        if (XaeroMinimapCore.onRenderCrosshair(guiGraphics)) {
            info.cancel();
        }
    }

    @Inject(at = {@At("RETURN")}, method = {"renderEffects"})
    public void postRenderStatusEffectOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        XaeroMinimapCore.onRenderStatusEffectOverlayPost(guiGraphics);
    }

    @Inject(at = {@At("HEAD")}, method = {"render"})
    public void onRenderStart(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        XaeroMinimapCore.beforeIngameGuiRender(guiGraphics, deltaTracker);
    }

    @Inject(at = {@At("HEAD")}, method = {"renderEffects"}, cancellable = true)
    public void onRenderStatusEffectOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        if (XaeroMinimapCore.onRenderStatusEffectOverlay(guiGraphics)) {
            info.cancel();
        }
    }
}
