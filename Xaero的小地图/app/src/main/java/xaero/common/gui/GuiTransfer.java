package xaero.common.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.hud.path.XaeroPath;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.widget.MySmallButton;
import xaero.lib.client.gui.widget.dropdown.DropDownWidget;
import xaero.lib.client.gui.widget.dropdown.IDropDownWidgetCallback;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiTransfer.class */
public class GuiTransfer extends ScreenBase implements IDropDownWidgetCallback {
    private MySmallButton transferButton;
    private GuiWaypointContainers containers1;
    private GuiWaypointWorlds worlds1;
    private GuiWaypointContainers containers2;
    private GuiWaypointWorlds worlds2;
    private DropDownWidget containers1DD;
    private DropDownWidget worlds1DD;
    private DropDownWidget containers2DD;
    private DropDownWidget worlds2DD;
    private MinimapSession session;
    private MinimapWorldManager manager;
    private final XaeroPath frozenAutoWorldPath;
    private boolean dropped;
    private final IXaeroMinimap modMain;

    public GuiTransfer(IXaeroMinimap modMain, MinimapSession session, Screen par1, Screen escapeScreen) {
        super(par1, escapeScreen, Component.translatable("gui.xaero_transfer_all"));
        this.dropped = false;
        this.modMain = modMain;
        this.session = session;
        this.manager = session.getWorldManager();
        this.frozenAutoWorldPath = session.getWorldState().getAutoWorldPath();
        XaeroPath currentWorldPath = session.getWorldState().getCurrentWorldPath(this.frozenAutoWorldPath);
        XaeroPath currentContainerPath = currentWorldPath.getRoot();
        this.containers1 = new GuiWaypointContainers((HudMod) modMain, this.manager, currentContainerPath, this.frozenAutoWorldPath);
        this.containers2 = new GuiWaypointContainers((HudMod) modMain, this.manager, currentContainerPath, this.frozenAutoWorldPath);
        this.worlds1 = new GuiWaypointWorlds(this.manager.getRootWorldContainer(this.containers1.getCurrentKey()), session, currentWorldPath, this.frozenAutoWorldPath);
        this.worlds2 = new GuiWaypointWorlds(this.manager.getRootWorldContainer(this.containers2.getCurrentKey()), session, currentWorldPath, this.frozenAutoWorldPath);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void init() {
        super.init();
        MySmallButton mySmallButton = new MySmallButton(5, (this.width / 2) - 155, (this.height / 7) + 120, Component.translatable("gui.xaero_transfer", new Object[0]), b -> {
            transfer();
        });
        this.transferButton = mySmallButton;
        addRenderableWidget(mySmallButton);
        this.transferButton.active = false;
        addRenderableWidget(new MySmallButton(6, (this.width / 2) + 5, (this.height / 7) + 120, Component.translatable("gui.xaero_cancel", new Object[0]), b2 -> {
            openParent();
        }));
        DropDownWidget dropDownWidgetCreateWorlds1DD = createWorlds1DD();
        this.worlds1DD = dropDownWidgetCreateWorlds1DD;
        addWidget(dropDownWidgetCreateWorlds1DD);
        DropDownWidget dropDownWidgetCreateWorlds2DD = createWorlds2DD();
        this.worlds2DD = dropDownWidgetCreateWorlds2DD;
        addWidget(dropDownWidgetCreateWorlds2DD);
        DropDownWidget dropDownWidgetCreateContainers1DD = createContainers1DD();
        this.containers1DD = dropDownWidgetCreateContainers1DD;
        addWidget(dropDownWidgetCreateContainers1DD);
        DropDownWidget dropDownWidgetCreateContainers2DD = createContainers2DD();
        this.containers2DD = dropDownWidgetCreateContainers2DD;
        addWidget(dropDownWidgetCreateContainers2DD);
    }

    private DropDownWidget createWorlds1DD() {
        return DropDownWidget.Builder.begin().setOptions(this.worlds1.options).setX((this.width / 2) + 2).setY((this.height / 7) + 20).setW(200).setSelected(Integer.valueOf(this.worlds1.current)).setCallback(this).setContainer(this).setNarrationTitle(Component.translatable("gui.xaero_dropdown_transfer_worlds1")).build();
    }

    private DropDownWidget createWorlds2DD() {
        return DropDownWidget.Builder.begin().setOptions(this.worlds2.options).setX((this.width / 2) + 2).setY((this.height / 7) + 50).setW(200).setSelected(Integer.valueOf(this.worlds2.current)).setCallback(this).setContainer(this).setNarrationTitle(Component.translatable("gui.xaero_dropdown_transfer_worlds2")).build();
    }

    private DropDownWidget createContainers1DD() {
        return DropDownWidget.Builder.begin().setOptions(this.containers1.options).setX((this.width / 2) - 202).setY((this.height / 7) + 20).setW(200).setSelected(Integer.valueOf(this.containers1.current)).setCallback(this).setContainer(this).setNarrationTitle(Component.translatable("gui.xaero_dropdown_transfer_containers1")).build();
    }

    private DropDownWidget createContainers2DD() {
        return DropDownWidget.Builder.begin().setOptions(this.containers2.options).setX((this.width / 2) - 202).setY((this.height / 7) + 50).setW(200).setSelected(Integer.valueOf(this.containers2.current)).setCallback(this).setContainer(this).setNarrationTitle(Component.translatable("gui.xaero_dropdown_transfer_containers2")).build();
    }

    private void openParent() {
        if (this.parent instanceof GuiWaypoints) {
            this.minecraft.setScreen(new GuiWaypoints((HudMod) this.modMain, this.session, ((GuiWaypoints) this.parent).parent, this.escape));
        } else {
            goBack();
        }
    }

    public void transfer() {
        try {
            XaeroPath keys1 = this.worlds1.getCurrentKey();
            XaeroPath keys2 = this.worlds2.getCurrentKey();
            MinimapWorld from = this.manager.getWorld(keys1);
            MinimapWorld to = this.manager.getWorld(keys2);
            for (WaypointSet fromSet : from.getIterableWaypointSets()) {
                WaypointSet toSet = to.getWaypointSet(fromSet.getName());
                if (toSet == null) {
                    toSet = WaypointSet.Builder.begin().setName(fromSet.getName()).build();
                }
                for (Waypoint w : fromSet.getWaypoints()) {
                    Waypoint copy = new Waypoint(w.getX(), w.getY(), w.getZ(), w.getName(), w.getInitials(), w.getWaypointColor(), w.getPurpose(), w.isTemporary(), w.isYIncluded());
                    copy.setRotation(w.isRotation());
                    copy.setDisabled(w.isDisabled());
                    copy.setYaw(w.getYaw());
                    copy.setVisibility(w.getVisibility());
                    toSet.add(copy);
                }
                to.addWaypointSet(toSet);
            }
            if (keys2 != null) {
                this.session.getWorldState().setCustomWorldPath(keys2);
            }
            openParent();
            this.session.getWorldManagerIO().saveWorld(to);
        } catch (Exception e) {
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
    }

    @Override // xaero.lib.client.gui.ScreenBase
    protected void renderPreDropdown(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        super.renderPreDropdown(guiGraphics, mouseX, mouseY, partial);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 5, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_from", new Object[0]).replace("§§", ":") + ":", this.width / 2, (this.height / 7) + 10, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_to", new Object[0]).replace("§§", ":") + ":", this.width / 2, (this.height / 7) + 40, 16777215);
    }

    @Override // xaero.lib.client.gui.widget.dropdown.IDropDownWidgetCallback
    public boolean onSelected(DropDownWidget menu, int selected) {
        if (menu == this.containers1DD) {
            this.containers1.current = selected;
            this.worlds1 = new GuiWaypointWorlds(this.manager.getRootWorldContainer(this.containers1.getCurrentKey()), this.session, this.session.getWorldState().getCurrentWorldPath(this.frozenAutoWorldPath), this.frozenAutoWorldPath);
            AbstractWidget abstractWidget = this.worlds1DD;
            DropDownWidget dropDownWidgetCreateWorlds1DD = createWorlds1DD();
            this.worlds1DD = dropDownWidgetCreateWorlds1DD;
            replaceWidget(abstractWidget, dropDownWidgetCreateWorlds1DD);
        } else if (menu == this.containers2DD) {
            this.containers2.current = selected;
            this.worlds2 = new GuiWaypointWorlds(this.manager.getRootWorldContainer(this.containers2.getCurrentKey()), this.session, this.session.getWorldState().getCurrentWorldPath(this.frozenAutoWorldPath), this.frozenAutoWorldPath);
            AbstractWidget abstractWidget2 = this.worlds2DD;
            DropDownWidget dropDownWidgetCreateWorlds2DD = createWorlds2DD();
            this.worlds2DD = dropDownWidgetCreateWorlds2DD;
            replaceWidget(abstractWidget2, dropDownWidgetCreateWorlds2DD);
        } else if (menu == this.worlds1DD) {
            this.worlds1.current = selected;
        } else if (menu == this.worlds2DD) {
            this.worlds2.current = selected;
        }
        this.transferButton.active = (this.containers1.current == this.containers2.current && this.worlds1.current == this.worlds2.current) ? false : true;
        return true;
    }
}
