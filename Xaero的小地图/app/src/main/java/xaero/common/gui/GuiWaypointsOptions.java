package xaero.common.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.apache.commons.io.FileUtils;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.MinimapWorldContainerUtil;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.widget.MyTinyButton;
import xaero.lib.client.gui.widget.Tooltip;
import xaero.lib.client.gui.widget.dropdown.DropDownWidget;
import xaero.lib.common.config.io.ConfigIO;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiWaypointsOptions.class */
public class GuiWaypointsOptions extends ScreenBase {
    private MinimapSession session;
    private MinimapWorldManager manager;
    private Button automaticButton;
    private Button subAutomaticButton;
    private Button deleteButton;
    private Button subDeleteButton;
    private Button connectButton;
    private boolean buttonTest;
    private MinimapWorld minimapWorld;
    private MinimapWorld automaticMinimapWorld;
    private MinimapWorldRootContainer rootContainer;
    private boolean teleportationOptionShown;
    private boolean selectedWorldIsConnected;
    public Tooltip mwTooltip;
    public Tooltip teleportationTooltip;
    public Tooltip connectionTooltip;
    private final IXaeroMinimap modMain;

    public GuiWaypointsOptions(IXaeroMinimap modMain, MinimapSession session, Screen parent, Screen escapeScreen, MinimapWorld minimapWorld, XaeroPath frozenAutoWorldPath) {
        super(parent, escapeScreen, Component.translatable("gui.xaero_options"));
        this.mwTooltip = new Tooltip("gui.xaero_use_multiworld_tooltip");
        this.teleportationTooltip = new Tooltip("gui.xaero_teleportation_tooltip", Style.EMPTY.withColor(ChatFormatting.RED));
        this.connectionTooltip = new Tooltip("gui.xaero_world_connection_tooltip");
        this.modMain = modMain;
        this.session = session;
        this.manager = this.session.getWorldManager();
        this.minimapWorld = minimapWorld;
        this.rootContainer = minimapWorld.getContainer().getRoot();
        this.automaticMinimapWorld = this.manager.getWorld(frozenAutoWorldPath);
        this.teleportationOptionShown = this.rootContainer.getConfig().isTeleportationEnabled();
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void init() {
        super.init();
        this.parent.resize(this.minecraft, this.width, this.height);
        this.selectedWorldIsConnected = this.rootContainer.getSubWorldConnections().isConnected(this.automaticMinimapWorld, this.minimapWorld);
        addRenderableWidget(new MyTinyButton((this.width / 2) - 203, 32, Component.translatable("gui.xaero_close", new Object[0]), b -> {
            actionPerformed(b, 5);
        }));
        addRenderableWidget(new MyBigButton(6, (this.width / 2) - 203, 57, Component.translatable("gui.xaero_transfer", new Object[0]), b2 -> {
            actionPerformed(b2, 6);
        }));
        MyBigButton myBigButton = new MyBigButton(7, (this.width / 2) - 203, 82, Component.translatable("gui.xaero_make_automatic", new Object[0]), b3 -> {
            actionPerformed(b3, 7);
        });
        this.automaticButton = myBigButton;
        addRenderableWidget(myBigButton);
        MyBigButton myBigButton2 = new MyBigButton(8, (this.width / 2) - 203, 107, Component.translatable("gui.xaero_make_multi_automatic", new Object[0]), b4 -> {
            actionPerformed(b4, 8);
        });
        this.subAutomaticButton = myBigButton2;
        addRenderableWidget(myBigButton2);
        MyBigButton myBigButton3 = new MyBigButton(9, (this.width / 2) - 203, 132, Component.translatable("gui.xaero_delete_world", new Object[0]), b5 -> {
            actionPerformed(b5, 9);
        });
        this.deleteButton = myBigButton3;
        addRenderableWidget(myBigButton3);
        MyBigButton myBigButton4 = new MyBigButton(10, (this.width / 2) - 203, 157, Component.translatable("gui.xaero_delete_multi_world", new Object[0]), b6 -> {
            actionPerformed(b6, 10);
        });
        this.subDeleteButton = myBigButton4;
        addRenderableWidget(myBigButton4);
        addRenderableWidget(new MyBigButton(200, (this.width / 2) + 3, 57, Component.literal(getConfigButtonName(0)), b7 -> {
            onConfigButtonClick((MyBigButton) b7);
        }));
        MyBigButton teleportationEnabledButton = addRenderableWidget(new MyBigButton(201, (this.width / 2) + 3, 82, Component.literal(getConfigButtonName(1)), b8 -> {
            onConfigButtonClick((MyBigButton) b8);
        }));
        teleportationEnabledButton.active = this.teleportationOptionShown;
        addRenderableWidget(new MyBigButton(13, (this.width / 2) + 3, 107, Component.translatable("gui.xaero_world_teleport_command"), b9 -> {
            actionPerformed(b9, 13);
        }));
        MyBigButton myBigButton5 = new MyBigButton(14, (this.width / 2) + 3, 132, Component.literal(getConfigButtonName(4)), b10 -> {
            actionPerformed(b10, 14);
        });
        this.connectButton = myBigButton5;
        addRenderableWidget(myBigButton5);
        this.connectButton.active = MinimapWorldContainerUtil.isMultiplayer(this.rootContainer.getPath()) && this.rootContainer == this.automaticMinimapWorld.getContainer().getRoot();
        addRenderableWidget(new MyBigButton(202, (this.width / 2) + 3, 182, Component.literal(getConfigButtonName(2)), b11 -> {
            onConfigButtonClick((MyBigButton) b11);
        }));
        addRenderableWidget(new MyBigButton(203, (this.width / 2) + 3, 207, Component.literal(getConfigButtonName(3)), b12 -> {
            onConfigButtonClick((MyBigButton) b12);
        }));
    }

    private String getConfigButtonName(int buttonId) {
        switch (buttonId) {
            case 0:
                return I18n.get("gui.xaero_use_multiworld", new Object[0]) + ": " + ModSettings.getTranslation(this.rootContainer.getConfig().isUsingMultiworldDetection());
            case 1:
                return I18n.get("gui.xaero_teleportation", new Object[0]) + ": " + ModSettings.getTranslation(this.rootContainer.getConfig().isTeleportationEnabled());
            case 2:
                return I18n.get("gui.xaero_sort", new Object[0]) + ": " + I18n.get(this.rootContainer.getConfig().getSortType().optionName, new Object[0]);
            case 3:
                return I18n.get("gui.xaero_sort_reversed", new Object[0]) + ": " + ModSettings.getTranslation(this.rootContainer.getConfig().isSortReversed());
            case 4:
                return this.selectedWorldIsConnected ? I18n.get("gui.xaero_disconnect_from_auto", new Object[0]) : I18n.get("gui.xaero_connect_with_auto", new Object[0]);
            default:
                return "";
        }
    }

    private void onConfigButtonClick(MyBigButton button) {
        this.buttonTest = true;
        MinimapWorldRootContainer wc = this.rootContainer;
        switch (button.getId() - 200) {
            case 0:
                wc.getConfig().setUsingMultiworldDetection(!this.rootContainer.getConfig().isUsingMultiworldDetection());
                wc.getConfig().setDefaultMultiworldId(null);
                break;
            case 1:
                wc.getConfig().setTeleportationEnabled(!wc.getConfig().isTeleportationEnabled());
                break;
            case 2:
                this.rootContainer.getConfig().toggleSortType();
                this.parent.init(this.minecraft, this.width, this.height);
                break;
            case 3:
                this.rootContainer.getConfig().toggleSortReversed();
                this.parent.init(this.minecraft, this.width, this.height);
                break;
        }
        this.session.getWorldManagerIO().getRootConfigIO().save(wc);
        button.setMessage(Component.literal(getConfigButtonName(button.getId() - 200)));
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public boolean mouseClicked(double par1, double par2, int par3) {
        this.buttonTest = false;
        boolean toReturn = super.mouseClicked(par1, par2, par3);
        if (!this.buttonTest) {
            goBack();
        }
        return toReturn;
    }

    protected void actionPerformed(Button p_146284_1_, int id) {
        this.buttonTest = true;
        if (p_146284_1_.active) {
            switch (id) {
                case 5:
                    goBack();
                    break;
                case 6:
                    this.minecraft.setScreen(new GuiTransfer(this.modMain, this.session, this.parent, this.escape));
                    break;
                case 7:
                    this.minecraft.setScreen(new ConfirmScreen(result -> {
                        confirmResult(result, id);
                    }, Component.translatable("gui.xaero_make_automatic_msg1"), Component.translatable("gui.xaero_make_automatic_msg2")));
                    break;
                case 8:
                    this.minecraft.setScreen(new ConfirmScreen(result2 -> {
                        confirmResult(result2, id);
                    }, Component.translatable("gui.xaero_make_multi_automatic_msg1"), Component.translatable("gui.xaero_make_multi_automatic_msg2")));
                    break;
                case 9:
                    this.minecraft.setScreen(new ConfirmScreen(result3 -> {
                        confirmResult(result3, id);
                    }, Component.translatable("gui.xaero_delete_world_msg1"), Component.translatable("gui.xaero_delete_world_msg2")));
                    break;
                case ConfigIO.IO_ATTEMPTS /* 10 */:
                    this.minecraft.setScreen(new ConfirmScreen(result4 -> {
                        confirmResult(result4, id);
                    }, Component.translatable("gui.xaero_delete_multi_world_msg1"), Component.translatable("gui.xaero_delete_multi_world_msg2")));
                    break;
                case DropDownWidget.LINE_HEIGHT /* 11 */:
                    this.minecraft.setScreen(new ConfirmScreen(result5 -> {
                        confirmResult(result5, id);
                    }, Component.translatable("gui.xaero_multiply_msg1"), Component.translatable("gui.xaero_multiply_msg2")));
                    break;
                case 12:
                    this.minecraft.setScreen(new ConfirmScreen(result6 -> {
                        confirmResult(result6, id);
                    }, Component.translatable("gui.xaero_multiply_msg1"), Component.translatable("gui.xaero_divide_msg2")));
                    break;
                case 13:
                    this.minecraft.setScreen(new GuiWorldTpCommand(this.modMain, this, this.escape, this.minimapWorld.getContainer().getRoot()));
                    break;
                case 14:
                    MinimapWorldContainer autoContainer = this.automaticMinimapWorld.getContainer();
                    MinimapWorldContainer selectedContainer = this.minimapWorld.getContainer();
                    String autoWorldName = autoContainer.getFullWorldName(this.automaticMinimapWorld.getNode(), autoContainer.getSubName()) + " (auto)";
                    String selectedWorldName = selectedContainer.getFullWorldName(this.minimapWorld.getNode(), selectedContainer.getSubName());
                    String connectionDisplayString = autoWorldName + "   §e<=>§r   " + selectedWorldName;
                    if (this.selectedWorldIsConnected) {
                        this.minecraft.setScreen(new ConfirmScreen(result7 -> {
                            confirmResult(result7, id);
                        }, Component.translatable("gui.xaero_disconnect_from_auto_msg"), Component.literal(connectionDisplayString)));
                        break;
                    } else {
                        this.minecraft.setScreen(new ConfirmScreen(result8 -> {
                            confirmResult(result8, id);
                        }, Component.translatable("gui.xaero_connect_with_auto_msg"), Component.literal(connectionDisplayString)));
                        break;
                    }
            }
        }
    }

    public void confirmResult(boolean result, int id) throws IOException {
        boolean differentRoot = isDifferentRootContainer();
        boolean differentSub = isDifferentSubWorld(differentRoot);
        if (result) {
            switch (id) {
                case 7:
                    if (differentRoot) {
                        MinimapWorldRootContainer selected = this.rootContainer;
                        MinimapWorldRootContainer auto = this.manager.getAutoRootContainer();
                        if (selected != null && auto != null) {
                            XaeroPath buKey = selected.getPath();
                            this.manager.removeContainer(selected.getPath());
                            this.manager.removeContainer(auto.getPath());
                            selected.setPath(auto.getPath());
                            auto.setPath(buKey);
                            this.manager.addRootWorldContainer(selected);
                            this.manager.addRootWorldContainer(auto);
                            selected.updateConnectionsField(this.session.getWaypointSession());
                            auto.updateConnectionsField(this.session.getWaypointSession());
                            Path selectedPath = selected.getDirectoryPath();
                            Path autoPath = auto.getDirectoryPath();
                            Path tempFolder = this.modMain.getWaypointsFolder().resolve("temp_to_add");
                            try {
                                Files.createDirectories(tempFolder, new FileAttribute[0]);
                                Path selectedTemp = tempFolder.resolve(selectedPath.getFileName());
                                if (Files.exists(selectedPath, new LinkOption[0])) {
                                    Files.move(selectedPath, selectedTemp, new CopyOption[0]);
                                }
                                if (Files.exists(autoPath, new LinkOption[0])) {
                                    Files.move(autoPath, selectedPath, new CopyOption[0]);
                                }
                                if (Files.exists(selectedTemp, new LinkOption[0])) {
                                    Files.move(selectedTemp, autoPath, new CopyOption[0]);
                                }
                                Files.deleteIfExists(tempFolder);
                                this.session.getWorldManagerIO().getRootConfigIO().load(selected);
                                this.session.getWorldManagerIO().getRootConfigIO().load(auto);
                            } catch (Throwable e) {
                                this.modMain.getInterfaces().getMinimapInterface().setCrashedWith(e);
                            }
                            this.session.getWorldState().setCustomWorldPath(null);
                            break;
                        }
                    }
                    break;
                case 8:
                    if (differentSub) {
                        MinimapWorld autoWorld = this.automaticMinimapWorld;
                        MinimapWorld selectedWorld = this.minimapWorld;
                        try {
                            Path autoFile = this.session.getWorldManagerIO().getWorldFile(autoWorld);
                            Path selectedFile = this.session.getWorldManagerIO().getWorldFile(selectedWorld);
                            Path autoTempFile = autoFile.getParent().resolve("temp_to_add").resolve(autoFile.getFileName());
                            Files.createDirectories(autoTempFile.getParent(), new FileAttribute[0]);
                            if (!Files.exists(autoFile, new LinkOption[0])) {
                                Files.createFile(autoFile, new FileAttribute[0]);
                            }
                            Files.move(autoFile, autoTempFile, new CopyOption[0]);
                            if (!Files.exists(selectedFile, new LinkOption[0])) {
                                Files.createFile(selectedFile, new FileAttribute[0]);
                            }
                            Files.move(selectedFile, autoFile, new CopyOption[0]);
                            if (Files.exists(autoTempFile, new LinkOption[0])) {
                                Files.move(autoTempFile, selectedFile, new CopyOption[0]);
                            }
                            Files.deleteIfExists(autoTempFile.getParent());
                            MinimapWorldContainer autoWc = autoWorld.getContainer();
                            MinimapWorldContainer selectedWc = selectedWorld.getContainer();
                            autoWorld.setContainer(selectedWc);
                            selectedWorld.setContainer(autoWc);
                            selectedWc.removeWorld(selectedWorld.getNode());
                            autoWc.removeWorld(autoWorld.getNode());
                            String buSelected = selectedWorld.getNode();
                            selectedWorld.setNode(autoWorld.getNode());
                            autoWorld.setNode(buSelected);
                            selectedWc.addWorld(autoWorld);
                            autoWc.addWorld(selectedWorld);
                            ResourceKey<Level> buDimId = selectedWorld.getDimId();
                            selectedWorld.setDimId(autoWorld.getDimId());
                            autoWorld.setDimId(buDimId);
                            this.rootContainer.getSubWorldConnections().swapConnections(autoWorld, selectedWorld);
                            this.session.getWorldManagerIO().getRootConfigIO().save(this.rootContainer);
                            this.session.getWorldState().setCustomWorldPath(null);
                            break;
                        } catch (Throwable e2) {
                            this.modMain.getInterfaces().getMinimapInterface().setCrashedWith(e2);
                            break;
                        }
                    }
                    break;
                case 9:
                    if (differentRoot) {
                        XaeroPath selectedRootContainerId = this.rootContainer.getPath();
                        try {
                            File directory = selectedRootContainerId.applyToFilePath(this.modMain.getWaypointsFolder()).toFile();
                            if (directory.exists()) {
                                FileUtils.deleteDirectory(directory);
                            }
                            this.manager.removeContainer(selectedRootContainerId);
                            this.session.getWorldState().setCustomWorldPath(null);
                            break;
                        } catch (Throwable e3) {
                            this.modMain.getInterfaces().getMinimapInterface().setCrashedWith(e3);
                            break;
                        }
                    }
                    break;
                case ConfigIO.IO_ATTEMPTS /* 10 */:
                    if (differentSub) {
                        MinimapWorld selectedWorld2 = this.minimapWorld;
                        try {
                            Files.deleteIfExists(this.session.getWorldManagerIO().getWorldFile(selectedWorld2));
                        } catch (IOException e4) {
                            MinimapLogs.LOGGER.error("suppressed exception", e4);
                        }
                        selectedWorld2.getContainer().removeWorld(selectedWorld2.getNode());
                        selectedWorld2.getContainer().removeName(selectedWorld2.getNode());
                        this.session.getWorldState().setCustomWorldPath(null);
                        break;
                    }
                    break;
                case DropDownWidget.LINE_HEIGHT /* 11 */:
                    multiplyWaypoints(this.minimapWorld, 8.0d);
                    break;
                case 12:
                    multiplyWaypoints(this.minimapWorld, 0.125d);
                    break;
                case 14:
                    if (!this.selectedWorldIsConnected) {
                        this.rootContainer.getSubWorldConnections().addConnection(this.automaticMinimapWorld, this.minimapWorld);
                    } else {
                        this.rootContainer.getSubWorldConnections().removeConnection(this.automaticMinimapWorld, this.minimapWorld);
                    }
                    this.session.getWorldManagerIO().getRootConfigIO().save(this.rootContainer);
                    break;
            }
            if (1 != 0) {
                if (this.parent instanceof GuiWaypoints) {
                    this.minecraft.setScreen(new GuiWaypoints((HudMod) this.modMain, this.session, ((GuiWaypoints) this.parent).parent, this.escape));
                    return;
                } else {
                    goBack();
                    return;
                }
            }
            return;
        }
        this.minecraft.setScreen(this);
    }

    private void multiplyWaypoints(MinimapWorld world, double factor) {
        for (WaypointSet set : world.getIterableWaypointSets()) {
            for (Waypoint wp : set.getWaypoints()) {
                wp.setX((int) Math.floor(wp.getX() * factor));
                wp.setZ((int) Math.floor(wp.getZ() * factor));
            }
        }
        try {
            this.session.getWorldManagerIO().saveWorld(world);
        } catch (IOException e) {
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
    }

    private boolean isDifferentRootContainer() {
        return (this.session.getWorldState().getAutoRootContainerPath() == null || this.session.getWorldState().getAutoRootContainerPath().equals(this.rootContainer.getPath())) ? false : true;
    }

    private boolean isDifferentSubWorld(boolean differentRoot) {
        return (differentRoot || this.minimapWorld == this.automaticMinimapWorld) ? false : true;
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int par1, int par2, float par3) {
        PoseStack matrixStack = guiGraphics.pose();
        Button button = this.automaticButton;
        Button button2 = this.deleteButton;
        boolean zIsDifferentRootContainer = isDifferentRootContainer();
        button2.active = zIsDifferentRootContainer;
        button.active = zIsDifferentRootContainer;
        Button button3 = this.subAutomaticButton;
        Button button4 = this.subDeleteButton;
        boolean zIsDifferentSubWorld = isDifferentSubWorld(this.automaticButton.active);
        button4.active = zIsDifferentSubWorld;
        button3.active = zIsDifferentSubWorld;
        this.parent.render(guiGraphics, 0, 0, par3);
        guiGraphics.flush();
        GlStateManager._clear(256, Minecraft.ON_OSX);
        super.render(guiGraphics, par1, par2, par3);
        matrixStack.pushPose();
        matrixStack.translate(0.0d, 0.0d, 0.1d);
        for (MyBigButton myBigButton : children()) {
            if (myBigButton instanceof AbstractWidget) {
                MyBigButton myBigButton2 = (AbstractWidget) myBigButton;
                if (myBigButton2 instanceof MyBigButton) {
                    MyBigButton b = myBigButton2;
                    if (par1 >= b.getX() && par2 >= b.getY() && par1 < b.getX() + b.getWidth() && par2 < b.getY() + 20) {
                        if (b.getId() >= 200) {
                            switch (b.getId() - 200) {
                                case 0:
                                    this.mwTooltip.drawBox(guiGraphics, par1, par2, this.width, this.height);
                                    break;
                                case 1:
                                    this.teleportationTooltip.drawBox(guiGraphics, par1, par2, this.width, this.height);
                                    break;
                            }
                        } else if (b.getId() == 14 && b.active) {
                            this.connectionTooltip.drawBox(guiGraphics, par1, par2, this.width, this.height);
                        }
                    }
                }
            }
        }
        matrixStack.popPose();
    }
}
