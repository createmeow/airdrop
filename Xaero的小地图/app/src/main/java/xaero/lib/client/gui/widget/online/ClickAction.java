package xaero.lib.client.gui.widget.online;

import java.net.URI;
import java.net.URISyntaxException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import xaero.lib.XaeroLib;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/ClickAction.class */
public enum ClickAction {
    NOTHING(null),
    URL(new WidgetClickHandler() { // from class: xaero.lib.client.gui.widget.online.WidgetUrlClickHandler
        private Screen clickedScreen;
        private String clickedURL;

        @Override // xaero.lib.client.gui.widget.online.WidgetClickHandler
        public void onClick(Screen screen, Widget widget) {
            this.clickedScreen = screen;
            this.clickedURL = widget.getUrl();
            Minecraft.getInstance().setScreen(new ConfirmLinkScreen(this::confirmLink, this.clickedURL, true));
        }

        private void confirmLink(boolean confirmed) {
            if (confirmed) {
                try {
                    Util.getPlatform().openUri(new URI(this.clickedURL));
                } catch (URISyntaxException e) {
                    XaeroLib.LOGGER.error("suppressed exception", e);
                }
            }
            Minecraft.getInstance().setScreen(this.clickedScreen);
        }
    });

    public final WidgetClickHandler clickHandler;

    ClickAction(WidgetClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }
}
