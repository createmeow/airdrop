package xaero.lib.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.lib.XaeroLib;
import xaero.lib.client.gui.widget.online.WidgetScreen;
import xaero.lib.patreon.Patreon;
import xaero.lib.patreon.PatreonMod;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/GuiUpdateAll.class */
public class GuiUpdateAll extends ConfirmScreen implements WidgetScreen {
    public GuiUpdateAll() {
        super(GuiUpdateAll::confirmResult, Component.literal("These mods are out-of-date: " + modListToNames(Patreon.getOutdatedMods())), Component.literal(Patreon.getHasAutoUpdates() ? "Would you like to automatically update them?" : "Would you like to update them (open the mod pages)?"));
        Patreon.setNotificationDisplayed(true);
    }

    private static String modListToNames(List<Object> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                builder.append(", ");
            }
            builder.append(((PatreonMod) list.get(i)).modName);
        }
        return builder.toString();
    }

    public void init() {
        super.init();
        if (Patreon.getHasAutoUpdates()) {
            addRenderableWidget(Button.builder(Component.translatable("Changelogs", new Object[0]), b -> {
                for (int i = 0; i < Patreon.getOutdatedMods().size(); i++) {
                    PatreonMod mod = (PatreonMod) Patreon.getOutdatedMods().get(i);
                    try {
                        Util.getPlatform().openUri(new URI(mod.changelogLink));
                    } catch (URISyntaxException e) {
                        XaeroLib.LOGGER.error("suppressed exception", e);
                    }
                }
            }).bounds((this.width / 2) - 100, (this.height / 6) + 120, 200, 20).build());
        }
        addRenderableWidget(Button.builder(Component.translatable("Don't show again for these updates", new Object[0]), b2 -> {
            for (int i = 0; i < Patreon.getOutdatedMods().size(); i++) {
                PatreonMod mod = (PatreonMod) Patreon.getOutdatedMods().get(i);
                if (mod.onVersionIgnore != null) {
                    mod.onVersionIgnore.run();
                }
            }
            this.minecraft.setScreen((Screen) null);
        }).bounds((this.width / 2) - 100, (this.height / 6) + 144, 200, 20).build());
        XaeroLib.INSTANCE.getClient().getWidgetScreenHandler().initialize(this, this.width, this.height);
    }

    private static void confirmResult(boolean p_confirmResult_1_) throws InterruptedException, NoSuchAlgorithmException, IOException {
        boolean shouldExit;
        if (p_confirmResult_1_) {
            if (Patreon.getHasAutoUpdates()) {
                for (Button button : Minecraft.getInstance().screen.children()) {
                    if (button instanceof Button) {
                        button.active = false;
                    }
                }
                shouldExit = autoUpdate();
            } else {
                shouldExit = true;
                for (int i = 0; i < Patreon.getOutdatedMods().size(); i++) {
                    PatreonMod m = (PatreonMod) Patreon.getOutdatedMods().get(i);
                    try {
                        Util.getPlatform().openUri(new URI(m.changelogLink));
                        if (m.modJar != null) {
                            Util.getPlatform().openFile(m.modJar.getParentFile());
                        }
                    } catch (Exception e) {
                        XaeroLib.LOGGER.error("suppressed exception", e);
                        shouldExit = false;
                    }
                }
            }
            if (shouldExit) {
                Minecraft.getInstance().stop();
            } else {
                Minecraft.getInstance().setScreen((Screen) null);
            }
            Minecraft.getInstance().stop();
            return;
        }
        Minecraft.getInstance().setScreen((Screen) null);
    }

    private static void download(BufferedOutputStream output, InputStream input, boolean closeInput) throws IOException {
        byte[] buffer = new byte[256];
        while (true) {
            int read = input.read(buffer, 0, buffer.length);
            if (read < 0) {
                break;
            } else {
                output.write(buffer, 0, read);
            }
        }
        output.flush();
        if (closeInput) {
            input.close();
        }
        output.close();
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x01c3, code lost:
    
        r0.add(r23);
        r0.add(r0.latestVersionLayout);
        r0.add(r0.currentVersion.split("_")[1]);
        r0.add(r0.latestVersion);
        r0.add(r0.currentVersion.split("_")[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x020e, code lost:
    
        if (r0.md5 != null) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0211, code lost:
    
        r1 = "null";
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0217, code lost:
    
        r1 = r0.md5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x021c, code lost:
    
        r0.add(r1);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean autoUpdate() throws java.lang.InterruptedException, java.security.NoSuchAlgorithmException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 603
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: xaero.lib.client.gui.GuiUpdateAll.autoUpdate():boolean");
    }

    public void renderBackground(GuiGraphics guiGraphics, int p_render_1_, int p_render_2_, float p_render_3_) {
        super.renderBackground(guiGraphics, p_render_1_, p_render_2_, p_render_3_);
        int mouseX = (int) ((this.minecraft.mouseHandler.xpos() * this.minecraft.getWindow().getGuiScaledWidth()) / this.minecraft.getWindow().getScreenWidth());
        int mouseY = (int) ((this.minecraft.mouseHandler.ypos() * this.minecraft.getWindow().getGuiScaledHeight()) / this.minecraft.getWindow().getScreenHeight());
        XaeroLib.INSTANCE.getClient().getWidgetScreenHandler().render(guiGraphics, this, this.width, this.height, mouseX, mouseY, this.minecraft.getWindow().getGuiScale());
    }

    public void render(GuiGraphics guiGraphics, int p_render_1_, int p_render_2_, float p_render_3_) {
        PoseStack matrixStack = guiGraphics.pose();
        super.render(guiGraphics, p_render_1_, p_render_2_, p_render_3_);
        matrixStack.pushPose();
        matrixStack.translate(0.0d, 0.0d, 0.1d);
        XaeroLib.INSTANCE.getClient().getWidgetScreenHandler().renderTooltips(guiGraphics, this, this.width, this.height, p_render_1_, p_render_2_, this.minecraft.getWindow().getGuiScale());
        matrixStack.popPose();
    }

    @Override // xaero.lib.client.gui.widget.online.WidgetScreen
    public void addButtonVisible(AbstractWidget button) {
        addRenderableWidget(button);
    }

    @Override // xaero.lib.client.gui.widget.online.WidgetScreen
    public <S extends Screen & WidgetScreen> S getScreen() {
        return this;
    }

    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        XaeroLib.INSTANCE.getClient().getWidgetScreenHandler().handleClick(this, this.width, this.height, (int) p_mouseClicked_1_, (int) p_mouseClicked_3_, this.minecraft.getWindow().getGuiScale());
        return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }
}
