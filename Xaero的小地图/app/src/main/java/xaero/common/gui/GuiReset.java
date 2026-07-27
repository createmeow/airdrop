package xaero.common.gui;

import java.util.Objects;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiReset.class */
public class GuiReset extends ConfirmScreen {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public GuiReset(IXaeroConfirmScreenCallback callback, Screen parent, Screen escScreen) {
        super(callback::accept, Component.translatable("gui.xaero_reset_config_profile_default_message"), Component.translatable("gui.xaero_reset_config_profile_default_message2"));
        Objects.requireNonNull(callback);
    }
}
