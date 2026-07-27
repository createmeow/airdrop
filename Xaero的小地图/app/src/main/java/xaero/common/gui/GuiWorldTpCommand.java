package xaero.common.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointWorldRootContainer;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.widget.MySmallButton;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiWorldTpCommand.class */
public class GuiWorldTpCommand extends ScreenBase {
    private MySmallButton confirmButton;
    private EditBox commandFormatTextField;
    private EditBox rotationCommandFormatTextField;
    private boolean usingDefault;
    private String commandFormat;
    private String rotationCommandFormat;
    private MinimapWorldRootContainer rootContainer;

    @Deprecated
    public GuiWorldTpCommand(IXaeroMinimap modMain, Screen parent, Screen escape, WaypointWorld world) {
        this(modMain, parent, escape, world.getContainer().getRoot());
    }

    @Deprecated
    public GuiWorldTpCommand(IXaeroMinimap modMain, Screen parent, Screen escape, WaypointWorldRootContainer rootContainer) {
        this(modMain, parent, escape, (MinimapWorldRootContainer) rootContainer);
    }

    public GuiWorldTpCommand(IXaeroMinimap modMain, Screen parent, Screen escape, MinimapWorldRootContainer rootContainer) {
        super(parent, escape, Component.translatable("gui.xaero_world_teleport_command"));
        this.rootContainer = rootContainer;
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        String defaultWaypointTPCommandFormatConfig = (String) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_FORMAT);
        String defaultWaypointTPCommandRotationFormatConfig = (String) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_ROTATION_FORMAT);
        this.commandFormat = rootContainer.getConfig().getServerTeleportCommandFormat() == null ? defaultWaypointTPCommandFormatConfig : rootContainer.getConfig().getServerTeleportCommandFormat();
        this.rotationCommandFormat = rootContainer.getConfig().getServerTeleportCommandRotationFormat() == null ? defaultWaypointTPCommandRotationFormatConfig : rootContainer.getConfig().getServerTeleportCommandRotationFormat();
        this.usingDefault = rootContainer.getConfig().isUsingDefaultTeleportCommand();
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void init() {
        super.init();
        this.parent.resize(this.minecraft, this.width, this.height);
        this.commandFormatTextField = new EditBox(this, this.font, (this.width / 2) - 100, (this.height / 7) + 50, 200, 20, Component.translatable("gui.xaero_world_teleport_command")) { // from class: xaero.common.gui.GuiWorldTpCommand.1
            public void insertText(String textToWrite) {
                if (this.active) {
                    super.insertText(textToWrite);
                }
            }

            public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
                if (this.active) {
                    return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
                }
                return false;
            }

            public void deleteChars(int p_146175_1_) {
                if (this.active) {
                    super.deleteChars(p_146175_1_);
                }
            }

            public void deleteWords(int p_146177_1_) {
                if (this.active) {
                    super.deleteWords(p_146177_1_);
                }
            }
        };
        this.commandFormatTextField.setMaxLength(128);
        this.rotationCommandFormatTextField = new EditBox(this, this.font, (this.width / 2) - 100, (this.height / 7) + 98, 200, 20, Component.translatable("gui.xaero_world_teleport_command_with_rotation")) { // from class: xaero.common.gui.GuiWorldTpCommand.2
            public void insertText(String textToWrite) {
                if (this.active) {
                    super.insertText(textToWrite);
                }
            }

            public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
                if (this.active) {
                    return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
                }
                return false;
            }

            public void deleteChars(int p_146175_1_) {
                if (this.active) {
                    super.deleteChars(p_146175_1_);
                }
            }

            public void deleteWords(int p_146177_1_) {
                if (this.active) {
                    super.deleteWords(p_146177_1_);
                }
            }
        };
        this.rotationCommandFormatTextField.setMaxLength(128);
        this.commandFormatTextField.active = !this.usingDefault;
        this.rotationCommandFormatTextField.active = !this.usingDefault;
        this.commandFormatTextField.setValue(this.commandFormat);
        this.rotationCommandFormatTextField.setValue(this.rotationCommandFormat);
        addWidget(this.commandFormatTextField);
        addWidget(this.rotationCommandFormatTextField);
        MySmallButton mySmallButton = new MySmallButton(200, (this.width / 2) - 155, (this.height / 6) + 168, Component.translatable("gui.xaero_confirm", new Object[0]), b -> {
            ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
            String defaultWaypointTPCommandFormatConfig = (String) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_FORMAT);
            String defaultWaypointTPCommandRotationFormatConfig = (String) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_ROTATION_FORMAT);
            if (this.commandFormat.equals(defaultWaypointTPCommandFormatConfig) && this.rotationCommandFormat.equals(defaultWaypointTPCommandRotationFormatConfig)) {
                this.usingDefault = true;
                this.commandFormat = null;
                this.rotationCommandFormat = null;
            }
            this.rootContainer.getConfig().setUsingDefaultTeleportCommand(this.usingDefault);
            this.rootContainer.getConfig().setServerTeleportCommandFormat(this.commandFormat);
            this.rootContainer.getConfig().setServerTeleportCommandRotationFormat(this.rotationCommandFormat);
            this.rootContainer.getSession().getWorldManagerIO().getRootConfigIO().save(this.rootContainer);
            goBack();
        });
        this.confirmButton = mySmallButton;
        addRenderableWidget(mySmallButton);
        addRenderableWidget(new MySmallButton(201, (this.width / 2) + 5, (this.height / 6) + 168, Component.translatable("gui.xaero_cancel", new Object[0]), b2 -> {
            goBack();
        }));
        addRenderableWidget(new MySmallButton(202, (this.width / 2) - 75, (this.height / 7) + 8, Component.literal(I18n.get("gui.xaero_use_default", new Object[0]) + ": " + ModSettings.getTranslation(this.usingDefault)), b3 -> {
            this.usingDefault = !this.usingDefault;
            this.commandFormatTextField.active = !this.usingDefault;
            this.rotationCommandFormatTextField.active = !this.usingDefault;
            init(this.minecraft, this.width, this.height);
        }));
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderBackground(guiGraphics, i, j, f);
        super.renderBackground(guiGraphics, i, j, f);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);
        guiGraphics.drawCenteredString(this.font, "{x} {y} {z} {name}", this.width / 2, (this.height / 7) + 36, -5592406);
        guiGraphics.drawCenteredString(this.font, "{x} {y} {z} {name} {yaw}", this.width / 2, (this.height / 7) + 84, -5592406);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (this.parent instanceof GuiWaypointsOptions) {
            ((GuiWaypointsOptions) this.parent).parent.render(guiGraphics, 0, 0, partial);
            guiGraphics.flush();
        }
        GlStateManager._clear(256, Minecraft.ON_OSX);
        super.render(guiGraphics, mouseX, mouseY, partial);
        if (this.usingDefault) {
            ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
            String defaultWaypointTPCommandFormatConfig = (String) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_FORMAT);
            String defaultWaypointTPCommandRotationFormatConfig = (String) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_ROTATION_FORMAT);
            this.commandFormatTextField.setValue(defaultWaypointTPCommandFormatConfig);
            this.rotationCommandFormatTextField.setValue(defaultWaypointTPCommandRotationFormatConfig);
            this.commandFormatTextField.setTextColor(-11184811);
            this.rotationCommandFormatTextField.setTextColor(-11184811);
        }
        this.commandFormatTextField.render(guiGraphics, mouseX, mouseY, partial);
        this.rotationCommandFormatTextField.render(guiGraphics, mouseX, mouseY, partial);
        if (this.usingDefault) {
            this.commandFormatTextField.setValue(this.commandFormat);
            this.rotationCommandFormatTextField.setValue(this.rotationCommandFormat);
            this.commandFormatTextField.setTextColor(-1);
            this.rotationCommandFormatTextField.setTextColor(-1);
        }
    }

    public void tick() {
        this.commandFormat = this.commandFormatTextField.getValue();
        this.rotationCommandFormat = this.rotationCommandFormatTextField.getValue();
        this.confirmButton.active = (this.commandFormat != null && this.commandFormat.length() > 0 && this.rotationCommandFormat != null && this.rotationCommandFormat.length() > 0) || this.usingDefault;
    }

    public boolean keyPressed(int par1, int par2, int par3) {
        if (par1 == 257 && ((this.commandFormatTextField.isFocused() || this.rotationCommandFormatTextField.isFocused()) && this.commandFormat != null && this.commandFormat.length() > 0 && this.rotationCommandFormat != null && this.rotationCommandFormat.length() > 0)) {
            this.confirmButton.onClick(0.0d, 0.0d);
        }
        return super.keyPressed(par1, par2, par3);
    }
}
