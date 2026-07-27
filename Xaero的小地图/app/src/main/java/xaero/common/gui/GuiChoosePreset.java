package xaero.common.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.common.IXaeroMinimap;
import xaero.hud.preset.HudPreset;
import xaero.lib.client.gui.GuiSettings;
import xaero.lib.client.gui.ISettingEntry;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiChoosePreset.class */
public class GuiChoosePreset extends GuiSettings {
    public GuiChoosePreset(IXaeroMinimap modMain, Screen back, Screen escape) {
        super(Component.translatable("gui.xaero_choose_a_preset"), back, escape);
        List<ISettingEntry> entryList = new ArrayList<>();
        for (HudPreset preset : modMain.getHud().getPresetManager().getPresets()) {
            entryList.add(new HudPresetSettingEntry(modMain.getHud().getPresetManager(), preset));
        }
        this.entries = (ISettingEntry[]) entryList.toArray(new ISettingEntry[0]);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void goBack() {
        super.goBack();
    }
}
