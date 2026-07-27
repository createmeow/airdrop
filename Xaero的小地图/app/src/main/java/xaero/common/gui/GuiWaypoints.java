package xaero.common.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Predicate;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsSort;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.config.primary.option.MinimapPrimaryClientConfigOptions;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.hud.path.XaeroPath;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.client.controls.util.KeyMappingUtils;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.widget.MyTinyButton;
import xaero.lib.client.gui.widget.dropdown.DropDownWidget;
import xaero.lib.client.gui.widget.dropdown.IDropDownWidgetCallback;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.single.SingleConfigManager;
import xaero.lib.common.util.KeySortableByOther;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiWaypoints.class */
public class GuiWaypoints extends ScreenBase implements IDropDownWidgetCallback {
    private static final int FRAME_TOP_SIZE = 58;
    private static final int FRAME_BOTTOM_SIZE = 61;
    public static double distanceDivided;
    private List list;
    private MinimapWorld displayedWorld;
    private ConcurrentSkipListSet<Integer> selectedListSet;
    private GuiWaypointContainers containers;
    private GuiWaypointWorlds worlds;
    private GuiWaypointSets sets;
    private DropDownWidget containersDD;
    private DropDownWidget worldsDD;
    private DropDownWidget setsDD;
    private MinimapSession session;
    private MinimapWorldManager manager;
    private int draggingFromX;
    private int draggingFromY;
    private int draggingFromSlot;
    private Waypoint draggingWaypoint;
    private boolean displayingTeleportableWorld;
    private int shiftSelectFirst;
    private ArrayList<Waypoint> waypointsSorted;
    private final XaeroPath frozenAutoWorldPath;
    private Button deleteButton;
    private Button editButton;
    private Button teleportButton;
    private Button disableEnableButton;
    private Button clearButton;
    private Button shareButton;
    private final HudMod modMain;

    @Deprecated
    public GuiWaypoints(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, Screen par1GuiScreen) {
        this(modMain, minimapSession, par1GuiScreen, (Screen) null);
    }

    @Deprecated
    public GuiWaypoints(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, Screen par1GuiScreen, Screen escapeScreen) {
        this((HudMod) modMain, (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession(), par1GuiScreen, escapeScreen);
    }

    public GuiWaypoints(HudMod modMain, MinimapSession session, Screen par1GuiScreen, Screen escapeScreen) {
        super(par1GuiScreen, escapeScreen, Component.translatable("gui.xaero_waypoints"));
        this.modMain = modMain;
        this.session = session;
        this.manager = session.getWorldManager();
        this.frozenAutoWorldPath = session.getWorldState().getAutoWorldPath();
        this.displayedWorld = this.manager.getCurrentWorld(this.frozenAutoWorldPath);
        if (this.displayedWorld == null) {
            return;
        }
        this.selectedListSet = new ConcurrentSkipListSet<>();
        this.draggingFromX = -1;
        this.draggingFromY = -1;
        this.draggingFromSlot = -1;
        XaeroPath currentContainer = this.displayedWorld.getContainer().getRoot().getPath();
        this.containers = new GuiWaypointContainers(modMain, this.manager, currentContainer, this.frozenAutoWorldPath);
        this.worlds = new GuiWaypointWorlds(this.manager.getRootWorldContainer(this.containers.getCurrentKey()), session, this.displayedWorld.getFullPath(), this.frozenAutoWorldPath);
        this.displayingTeleportableWorld = session.getWaypointSession().getTeleport().isWorldTeleportable(this.displayedWorld);
        this.waypointsSorted = new ArrayList<>();
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void init() {
        super.init();
        if (this.displayedWorld == null) {
            return;
        }
        updateSortedList();
        this.list = new List();
        this.sets = new GuiWaypointSets(true, this.displayedWorld, this.displayedWorld.getCurrentWaypointSetId());
        addWidget(this.list);
        MyTinyButton myTinyButton = new MyTinyButton((this.width / 2) + 129, this.height - 53, Component.translatable("gui.xaero_delete"), b -> {
            if (!isSomethingSelected()) {
                return;
            }
            undrag();
            boolean shouldRestore = true;
            Iterator<Integer> it = this.selectedListSet.iterator();
            while (it.hasNext()) {
                int i = it.next().intValue();
                Waypoint w = this.list.getWaypoint(i);
                if (!w.isTemporary()) {
                    shouldRestore = false;
                    w.setTemporary(true);
                }
            }
            if (shouldRestore) {
                Iterator<Integer> it2 = this.selectedListSet.iterator();
                while (it2.hasNext()) {
                    int i2 = it2.next().intValue();
                    this.list.getWaypoint(i2).setTemporary(false);
                }
            }
            try {
                this.session.getWorldManagerIO().saveWorld(this.displayedWorld);
            } catch (IOException e) {
                MinimapLogs.LOGGER.error("suppressed exception", e);
            }
        });
        this.deleteButton = myTinyButton;
        addRenderableWidget(myTinyButton);
        addRenderableWidget(Button.builder(Component.translatable("gui.done", new Object[0]), b2 -> {
            goBack();
        }).bounds((this.width / 2) - 100, this.height - 29, 200, 20).build());
        MyTinyButton myTinyButton2 = new MyTinyButton((this.width / 2) - 203, this.height - 53, Component.translatable("gui.xaero_add_edit", new Object[0]), b3 -> {
            if (!isAddEditEnabled()) {
                return;
            }
            ArrayList<Waypoint> selectedWaypoints = (ArrayList) getSelectedWaypointsList().stream().filter(w -> {
                return !w.isServerWaypoint();
            }).collect(ArrayList::new, (v0, v1) -> {
                v0.add(v1);
            }, (v0, v1) -> {
                v0.addAll(v1);
            });
            this.minecraft.setScreen(new GuiAddWaypoint(this.modMain, this.session, this, this.escape, selectedWaypoints, this.displayedWorld.getContainer().getRoot().getPath(), this.displayedWorld, this.displayedWorld.getCurrentWaypointSetId(), selectedWaypoints.isEmpty()));
            this.list.setSelected((List.WaypointEntry) null);
        });
        this.editButton = myTinyButton2;
        addRenderableWidget(myTinyButton2);
        MyTinyButton myTinyButton3 = new MyTinyButton((this.width / 2) - 120, this.height - 53, Component.literal(I18n.get("gui.xaero_waypoint_teleport", new Object[0]) + " (T)"), b4 -> {
            if (!canTeleport()) {
                return;
            }
            this.displayingTeleportableWorld = this.session.getWaypointSession().getTeleport().isWorldTeleportable(this.displayedWorld);
            this.session.getWaypointSession().getTeleport().teleportToWaypoint(this.list.getWaypoint(this.selectedListSet.first().intValue()), this.displayedWorld, this);
        });
        this.teleportButton = myTinyButton3;
        addRenderableWidget(myTinyButton3);
        MyTinyButton myTinyButton4 = new MyTinyButton((this.width / 2) + 46, this.height - 53, Component.translatable("gui.xaero_disable_enable", new Object[0]), b5 -> {
            if (!isSomethingSelected()) {
                return;
            }
            ArrayList<Waypoint> selectedWaypoints = getSelectedWaypointsList();
            if (allWaypointsAre(selectedWaypoints, (v0) -> {
                return v0.isTemporary();
            })) {
                Iterator<Waypoint> it = selectedWaypoints.iterator();
                while (it.hasNext()) {
                    Waypoint selected = it.next();
                    this.displayedWorld.getCurrentWaypointSet().remove(selected);
                }
                this.selectedListSet.clear();
            } else if (allWaypointsAre(selectedWaypoints, (v0) -> {
                return v0.isDisabled();
            })) {
                Iterator<Waypoint> it2 = selectedWaypoints.iterator();
                while (it2.hasNext()) {
                    Waypoint selected2 = it2.next();
                    selected2.setDisabled(false);
                }
            } else {
                Iterator<Waypoint> it3 = selectedWaypoints.iterator();
                while (it3.hasNext()) {
                    Waypoint selected3 = it3.next();
                    selected3.setDisabled(true);
                }
            }
            updateSortedList();
            try {
                this.session.getWorldManagerIO().saveWorld(this.displayedWorld);
            } catch (IOException e) {
                MinimapLogs.LOGGER.error("suppressed exception", e);
            }
        });
        this.disableEnableButton = myTinyButton4;
        addRenderableWidget(myTinyButton4);
        MyTinyButton myTinyButton5 = new MyTinyButton((this.width / 2) + 130, 32, Component.translatable("gui.xaero_clear", new Object[0]), b6 -> {
            XaeroPath worldKeys = this.worlds.getCurrentKey();
            String name = this.sets.getOptions()[this.sets.getCurrentSet()];
            if (shouldDeleteSet()) {
                this.minecraft.setScreen(new GuiDeleteSet(I18n.get(name, new Object[0]), worldKeys, name, this, this.escape, this.modMain, this.session));
            } else {
                this.minecraft.setScreen(new GuiClearSet(I18n.get(name, new Object[0]), worldKeys, name, this, this.escape, this.modMain, this.session));
            }
        });
        this.clearButton = myTinyButton5;
        addRenderableWidget(myTinyButton5);
        addRenderableWidget(new MyTinyButton((this.width / 2) - 203, 32, Component.translatable("gui.xaero_options", new Object[0]), b7 -> {
            this.minecraft.setScreen(new GuiWaypointsOptions(this.modMain, this.session, this, this.escape, this.displayedWorld, this.frozenAutoWorldPath));
        }));
        MyTinyButton myTinyButton6 = new MyTinyButton((this.width / 2) - 37, this.height - 53, Component.translatable("gui.xaero_share", new Object[0]), b8 -> {
            if (!isOneSelected()) {
                return;
            }
            Waypoint selected = this.selectedListSet.isEmpty() ? null : this.list.getWaypoint(this.selectedListSet.first().intValue());
            if (selected != null) {
                this.session.getWaypointSession().getSharing().shareWaypoint(this, selected, this.displayedWorld);
            }
        });
        this.shareButton = myTinyButton6;
        addRenderableWidget(myTinyButton6);
        DropDownWidget dropDownWidgetCreateContainersDropdown = createContainersDropdown();
        this.containersDD = dropDownWidgetCreateContainersDropdown;
        addWidget(dropDownWidgetCreateContainersDropdown);
        DropDownWidget dropDownWidgetCreateWorldsDropdown = createWorldsDropdown();
        this.worldsDD = dropDownWidgetCreateWorldsDropdown;
        addWidget(dropDownWidgetCreateWorldsDropdown);
        DropDownWidget dropDownWidgetCreateSetsDropdown = createSetsDropdown();
        this.setsDD = dropDownWidgetCreateSetsDropdown;
        addWidget(dropDownWidgetCreateSetsDropdown);
    }

    private DropDownWidget createSetsDropdown() {
        return DropDownWidget.Builder.begin().setOptions(this.sets.getOptions()).setX((this.width / 2) - 100).setY(33).setW(200).setSelected(Integer.valueOf(this.sets.getCurrentSet())).setCallback(this).setContainer(this).setNarrationTitle(Component.translatable("gui.xaero_dropdown_waypoint_set")).build();
    }

    private DropDownWidget createContainersDropdown() {
        return DropDownWidget.Builder.begin().setOptions(this.containers.options).setX((this.width / 2) - 202).setY(17).setW(200).setSelected(Integer.valueOf(this.containers.current)).setCallback(this).setContainer(this).setNarrationTitle(Component.translatable("gui.xaero_dropdown_waypoint_container")).build();
    }

    private DropDownWidget createWorldsDropdown() {
        return DropDownWidget.Builder.begin().setOptions(this.worlds.options).setX((this.width / 2) + 2).setY(17).setW(200).setSelected(Integer.valueOf(this.worlds.current)).setCallback(this).setContainer(this).setNarrationTitle(Component.translatable("gui.xaero_dropdown_waypoint_world")).build();
    }

    private ArrayList<Waypoint> getSelectedWaypointsList() {
        return (ArrayList) this.selectedListSet.stream().map(i -> {
            return this.list.getWaypoint(i.intValue());
        }).collect(ArrayList::new, (v0, v1) -> {
            v0.add(v1);
        }, (v0, v1) -> {
            v0.addAll(v1);
        });
    }

    public static boolean allWaypointsAre(ArrayList<Waypoint> waypoints, Predicate<Waypoint> predicate) {
        boolean allTrue = true;
        Iterator<Waypoint> it = waypoints.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Waypoint w = it.next();
            if (!predicate.test(w)) {
                allTrue = false;
                break;
            }
        }
        return allTrue;
    }

    public boolean shouldDeleteSet() {
        return !this.sets.getOptions()[this.sets.getCurrentSet()].equals("gui.xaero_default") && this.displayedWorld.getCurrentWaypointSet().isEmpty();
    }

    private void undrag() {
        this.draggingFromX = -1;
        this.draggingFromY = -1;
        this.draggingFromSlot = -1;
        this.draggingWaypoint = null;
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public boolean mouseClicked(double par1, double par2, int par3) {
        if (this.openDropdown == null) {
            if (KeyMappingUtils.inputMatches(InputConstants.Type.MOUSE, par3, ModSettings.keyWaypoints, 0)) {
                goBack();
                return true;
            }
            if (par3 != 0) {
                this.list.setSelected((List.WaypointEntry) null);
            } else if (par2 >= 58.0d && par2 < this.height - FRAME_BOTTOM_SIZE && this.displayedWorld.getRootConfig().getSortType() == WaypointsSort.NONE) {
                this.draggingFromX = (int) par1;
                this.draggingFromY = (int) par2;
                this.draggingFromSlot = this.list.getEntryAt(par1, par2);
                if (this.draggingFromSlot >= this.displayedWorld.getCurrentWaypointSet().size()) {
                    this.draggingFromSlot = -1;
                }
            }
        }
        return super.mouseClicked(par1, par2, par3);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public boolean mouseReleased(double par1, double par2, int par3) {
        try {
            if (this.draggingWaypoint != null) {
                this.session.getWorldManagerIO().saveWorld(this.displayedWorld);
            }
        } catch (IOException e) {
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
        undrag();
        if (!super.mouseReleased(par1, par2, par3)) {
            return this.list.mouseReleased(par1, par2, par3);
        }
        return true;
    }

    public boolean keyReleased(int par1, int par2, int par3) {
        switch (par1) {
            case 84:
                if (this.teleportButton.active) {
                    this.teleportButton.onClick(0.0d, 0.0d);
                    break;
                }
                break;
            case 261:
                if (this.disableEnableButton.active) {
                    Iterator<Integer> it = this.selectedListSet.iterator();
                    while (it.hasNext()) {
                        int i = it.next().intValue();
                        this.list.getWaypoint(i).setTemporary(true);
                    }
                    this.disableEnableButton.onClick(0.0d, 0.0d);
                    break;
                }
                break;
        }
        return true;
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void renderBackground(GuiGraphics guiGraphics, int par1, int par2, float par3) {
        super.renderBackground(guiGraphics, par1, par2, par3);
        this.list.render(guiGraphics, par1, par2, par3);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int par1, int par2, float par3) {
        if (this.displayedWorld == null) {
            this.minecraft.setScreen(this.parent);
        } else if (this.minecraft.player == null) {
            this.minecraft.setScreen((Screen) null);
        } else {
            updateButtons();
            super.render(guiGraphics, par1, par2, par3);
        }
    }

    @Override // xaero.lib.client.gui.ScreenBase
    protected void renderPreDropdown(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        super.renderPreDropdown(guiGraphics, mouseX, mouseY, partial);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_world_server", new Object[0]), (this.width / 2) - 102, 5, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_subworld_dimension", new Object[0]), (this.width / 2) + 102, 5, 16777215);
        if (this.draggingFromSlot != -1) {
            int distance = (int) Math.sqrt(Math.pow(mouseX - this.draggingFromX, 2.0d) + Math.pow(mouseY - this.draggingFromY, 2.0d));
            int toSlot = Math.min(this.displayedWorld.getCurrentWaypointSet().size() - 1, this.list.getEntryAt(mouseX, mouseY));
            if (distance > 4 && this.draggingWaypoint == null) {
                this.draggingWaypoint = this.displayedWorld.getCurrentWaypointSet().get(this.draggingFromSlot);
                this.list.setSelected((List.WaypointEntry) null);
            }
            if (this.draggingWaypoint != null && this.draggingFromSlot != toSlot && toSlot != -1) {
                int direction = toSlot > this.draggingFromSlot ? 1 : -1;
                int i = this.draggingFromSlot;
                while (true) {
                    int i2 = i;
                    if (i2 == toSlot) {
                        break;
                    }
                    this.displayedWorld.getCurrentWaypointSet().set(i2, this.displayedWorld.getCurrentWaypointSet().get(i2 + direction));
                    i = i2 + direction;
                }
                this.displayedWorld.getCurrentWaypointSet().set(toSlot, this.draggingWaypoint);
                this.draggingFromSlot = toSlot;
                updateSortedList();
            }
            int fromCenter = this.draggingFromX - (this.list.getWidth() / 2);
            this.list.drawWaypointSlot(guiGraphics, this.draggingWaypoint, (mouseX - 108) - fromCenter, mouseY - (this.list.getItemHeight() / 4));
        }
    }

    private void updateButtons() {
        Button button = this.deleteButton;
        Button button2 = this.disableEnableButton;
        boolean zIsSomethingSelected = isSomethingSelected();
        button2.active = zIsSomethingSelected;
        button.active = zIsSomethingSelected;
        this.shareButton.active = isOneSelected();
        this.teleportButton.active = canTeleport();
        this.editButton.active = isAddEditEnabled();
        this.clearButton.setMessage(Component.translatable(shouldDeleteSet() ? "gui.xaero_delete_set" : "gui.xaero_clear", new Object[0]));
        ArrayList<Waypoint> selectedWaypointsList = getSelectedWaypointsList();
        if (isSomethingSelected() && allWaypointsAre(selectedWaypointsList, (v0) -> {
            return v0.isTemporary();
        })) {
            this.disableEnableButton.setMessage(Component.translatable("gui.xaero_delete"));
            this.deleteButton.setMessage(Component.translatable("gui.xaero_restore"));
        } else {
            this.deleteButton.setMessage(Component.translatable("gui.xaero_delete"));
            String[] enabledisable = I18n.get("gui.xaero_disable_enable", new Object[0]).split("/");
            this.disableEnableButton.setMessage(Component.literal(enabledisable[!allWaypointsAre(selectedWaypointsList, (v0) -> {
                return v0.isDisabled();
            }) ? (char) 0 : (char) 1]));
        }
    }

    private boolean isAddEditEnabled() {
        ArrayList<Waypoint> selectedWaypointsList = getSelectedWaypointsList();
        return selectedWaypointsList.isEmpty() || !allWaypointsAre(selectedWaypointsList, (v0) -> {
            return v0.isServerWaypoint();
        });
    }

    private boolean isSomethingSelected() {
        return !this.selectedListSet.isEmpty();
    }

    private boolean isOneSelected() {
        return this.selectedListSet.size() == 1;
    }

    private boolean canTeleport() {
        if (!isOneSelected() || !this.displayedWorld.getRootConfig().isTeleportationEnabled()) {
            return false;
        }
        if (this.displayingTeleportableWorld) {
            return true;
        }
        ClientConfigManager configManager = this.modMain.getHudConfigs().getClientConfigManager();
        SingleConfigManager<Config> primaryConfigManager = configManager.getPrimaryConfigManager();
        return ((Boolean) primaryConfigManager.getEffective(MinimapPrimaryClientConfigOptions.WRONG_WORLD_TELEPORT)).booleanValue();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiWaypoints$List.class */
    class List extends ObjectSelectionList<WaypointEntry> {
        private int createdCount;

        public List() {
            super(GuiWaypoints.this.minecraft, GuiWaypoints.this.width, Math.max(4, (GuiWaypoints.this.height - GuiWaypoints.FRAME_BOTTOM_SIZE) - GuiWaypoints.FRAME_TOP_SIZE), GuiWaypoints.FRAME_TOP_SIZE, 18);
            createEntries(getWaypointCount());
        }

        protected int getWaypointCount() {
            int size = GuiWaypoints.this.displayedWorld.getCurrentWaypointSet().size();
            return size + GuiWaypoints.this.displayedWorld.getContainer().getServerWaypointManager().size();
        }

        private Waypoint getWaypoint(int slotIndex) {
            if (slotIndex < GuiWaypoints.this.displayedWorld.getCurrentWaypointSet().size()) {
                return GuiWaypoints.this.waypointsSorted.get(slotIndex);
            }
            int serverWPIndex = slotIndex - GuiWaypoints.this.displayedWorld.getCurrentWaypointSet().size();
            if (serverWPIndex < GuiWaypoints.this.displayedWorld.getContainer().getServerWaypointManager().size()) {
                return GuiWaypoints.this.displayedWorld.getContainer().getServerWaypointManager().getBySlot(serverWPIndex);
            }
            return null;
        }

        protected boolean isSelectedItem(int p_148131_1_) {
            return !GuiWaypoints.this.selectedListSet.isEmpty() && GuiWaypoints.this.selectedListSet.contains(Integer.valueOf(p_148131_1_));
        }

        private void createEntries(int count) {
            clearEntries();
            this.createdCount = count;
            for (int i = 0; i < count; i++) {
                WaypointEntry entry = new WaypointEntry(i);
                addEntry(entry);
            }
        }

        public void renderWidget(GuiGraphics guiGraphics, int p_render_1_, int p_render_2_, float p_render_3_) {
            int currentCount = getWaypointCount();
            if (currentCount != this.createdCount) {
                createEntries(currentCount);
                setScrollAmount(getScrollAmount());
            }
            super.renderWidget(guiGraphics, p_render_1_, p_render_2_, p_render_3_);
        }

        public boolean isFocused() {
            return GuiWaypoints.this.openDropdown == null && GuiWaypoints.this.draggingWaypoint == null && GuiWaypoints.this.getFocused() == this;
        }

        public void setSelected(WaypointEntry e) {
            if (e == null) {
                GuiWaypoints.this.selectedListSet.clear();
                GuiWaypoints.this.shiftSelectFirst = -1;
                return;
            }
            getWaypoint(e.index);
            int currentSize = GuiWaypoints.this.selectedListSet.size();
            boolean shiftPressed = Screen.hasShiftDown();
            if ((currentSize > 1 || (currentSize == 1 && GuiWaypoints.this.selectedListSet.first().intValue() != e.index)) && !Screen.hasControlDown() && !shiftPressed) {
                GuiWaypoints.this.selectedListSet.clear();
            }
            if (currentSize > 0 && shiftPressed) {
                int direction = e.index > GuiWaypoints.this.shiftSelectFirst ? 1 : -1;
                GuiWaypoints.this.selectedListSet.clear();
                int i = GuiWaypoints.this.shiftSelectFirst;
                while (true) {
                    int i2 = i;
                    if (i2 == e.index + direction) {
                        break;
                    }
                    GuiWaypoints.this.selectedListSet.add(Integer.valueOf(i2));
                    i = i2 + direction;
                }
            } else if (GuiWaypoints.this.selectedListSet.contains(Integer.valueOf(e.index))) {
                GuiWaypoints.this.selectedListSet.remove(Integer.valueOf(e.index));
            } else {
                GuiWaypoints.this.shiftSelectFirst = e.index;
                GuiWaypoints.this.selectedListSet.add(Integer.valueOf(e.index));
            }
            super.setSelected(GuiWaypoints.this.selectedListSet.isEmpty() ? null : e);
        }

        public int getItemHeight() {
            return this.itemHeight;
        }

        public void drawWaypointSlot(GuiGraphics guiGraphics, Waypoint w, int p_180791_2_, int p_180791_3_) {
            PoseStack matrixStack = guiGraphics.pose();
            if (w == null) {
                return;
            }
            matrixStack.pushPose();
            matrixStack.translate(0.0f, 0.0f, 1.0f);
            guiGraphics.drawCenteredString(GuiWaypoints.this.font, w.getLocalizedName() + (w.isDisabled() ? " §4" + I18n.get("gui.xaero_disabled", new Object[0]) : w.isTemporary() ? " §4" + I18n.get("gui.xaero_temporary", new Object[0]) : ""), p_180791_2_ + 110, p_180791_3_ + 1, 16777215);
            int rectX = p_180791_2_ + 8 + 4;
            int rectY = p_180791_3_ + 6;
            if (w.isGlobal()) {
                guiGraphics.drawCenteredString(GuiWaypoints.this.font, "*", rectX - 25, rectY - 3, 16777215);
            }
            MultiBufferSource.BufferSource renderTypeBuffers = GuiWaypoints.this.modMain.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
            GuiWaypoints.this.modMain.getMinimap().getWaypointGuiRenderer().drawIconOnGUI(guiGraphics, GuiWaypoints.this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().getHelper(), w, rectX, rectY, 90, renderTypeBuffers, renderTypeBuffers.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS), renderTypeBuffers.getBuffer(CustomRenderTypes.GUI_NEAREST));
            renderTypeBuffers.endBatch();
            matrixStack.popPose();
        }

        public int getEntryAt(double x, double y) {
            WaypointEntry entry = getEntryAtPosition(x, y);
            if (entry == null) {
                return -1;
            }
            return entry.index;
        }

        /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiWaypoints$List$WaypointEntry.class */
        public class WaypointEntry extends ObjectSelectionList.Entry<WaypointEntry> {
            private int index;

            public WaypointEntry(int index) {
                this.index = index;
            }

            public void render(GuiGraphics guiGraphics, int index, int p_render_2_, int p_render_3_, int p_render_4_, int p_render_5_, int p_render_6_, int p_render_7_, boolean p_render_8_, float p_render_9_) {
                Waypoint w = List.this.getWaypoint(index);
                if (w == GuiWaypoints.this.draggingWaypoint) {
                    return;
                }
                List.this.drawWaypointSlot(guiGraphics, w, p_render_3_, p_render_2_);
            }

            public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
                if (p_mouseClicked_5_ == 0) {
                    return true;
                }
                return false;
            }

            public Component getNarration() {
                Waypoint w = List.this.getWaypoint(this.index);
                String narration = "";
                if (w != null) {
                    narration = narration + I18n.get("narrator.select", new Object[]{w.getName()}) + (w.isDisabled() ? " " + I18n.get("gui.xaero_disabled", new Object[0]) : "") + (w.isTemporary() ? " " + I18n.get("gui.xaero_temporary", new Object[0]) : "") + ", ";
                }
                if (GuiWaypoints.this.selectedListSet.size() != 1) {
                    narration = narration + I18n.get("narrator.select", new Object[]{I18n.get("gui.xaero_waypoints", new Object[0]) + " " + GuiWaypoints.this.selectedListSet.size()});
                }
                return Component.literal(narration);
            }
        }
    }

    @Override // xaero.lib.client.gui.widget.dropdown.IDropDownWidgetCallback
    public boolean onSelected(DropDownWidget menu, int selectedIndex) {
        if (menu == this.containersDD || menu == this.worldsDD) {
            if (menu == this.containersDD) {
                this.containers.current = selectedIndex;
                if (this.containers.current != this.containers.auto) {
                    MinimapWorld firstWorld = this.manager.getRootWorldContainer(this.containers.getCurrentKey()).getFirstWorld();
                    this.session.getWorldState().setCustomWorldPath(firstWorld.getFullPath());
                } else {
                    this.session.getWorldState().setCustomWorldPath(null);
                }
                this.displayedWorld = this.manager.getCurrentWorld(this.frozenAutoWorldPath);
                updateSortedList();
                this.worlds = new GuiWaypointWorlds(this.manager.getRootWorldContainer(this.containers.getCurrentKey()), this.session, this.displayedWorld.getFullPath(), this.frozenAutoWorldPath);
                AbstractWidget abstractWidget = this.worldsDD;
                DropDownWidget dropDownWidgetCreateWorldsDropdown = createWorldsDropdown();
                this.worldsDD = dropDownWidgetCreateWorldsDropdown;
                replaceWidget(abstractWidget, dropDownWidgetCreateWorldsDropdown);
            } else {
                this.worlds.current = selectedIndex;
                if (this.worlds.current != this.worlds.auto) {
                    XaeroPath selectedWorldPath = this.worlds.getCurrentKey();
                    this.session.getWorldState().setCustomWorldPath(selectedWorldPath);
                } else {
                    this.session.getWorldState().setCustomWorldPath(null);
                }
                this.displayedWorld = this.manager.getCurrentWorld(this.frozenAutoWorldPath);
                updateSortedList();
            }
            this.displayingTeleportableWorld = this.session.getWaypointSession().getTeleport().isWorldTeleportable(this.displayedWorld);
            this.list.setSelected((List.WaypointEntry) null);
            this.sets = new GuiWaypointSets(true, this.displayedWorld, this.displayedWorld.getCurrentWaypointSetId());
            AbstractWidget abstractWidget2 = this.setsDD;
            DropDownWidget dropDownWidgetCreateSetsDropdown = createSetsDropdown();
            this.setsDD = dropDownWidgetCreateSetsDropdown;
            replaceWidget(abstractWidget2, dropDownWidgetCreateSetsDropdown);
            return true;
        }
        if (menu == this.setsDD) {
            this.list.setSelected((List.WaypointEntry) null);
            if (selectedIndex == menu.size() - 1) {
                MinimapLogs.LOGGER.info("New waypoint set gui");
                this.minecraft.setScreen(new GuiNewSet(this.modMain, this.session, this, this.escape, this.displayedWorld));
                return false;
            }
            this.sets.setCurrentSet(selectedIndex);
            this.displayedWorld.setCurrentWaypointSetId(this.sets.getCurrentSetKey());
            updateSortedList();
            try {
                this.session.getWorldManagerIO().saveWorld(this.displayedWorld);
                return true;
            } catch (IOException e) {
                MinimapLogs.LOGGER.error("suppressed exception", e);
                return true;
            }
        }
        return false;
    }

    private void updateSortedList() {
        WaypointsSort sortType = this.displayedWorld.getRootConfig().getSortType();
        this.waypointsSorted = new ArrayList<>();
        if (sortType == WaypointsSort.NONE) {
            for (Waypoint waypoint : this.displayedWorld.getCurrentWaypointSet().getWaypoints()) {
                this.waypointsSorted.add(waypoint);
            }
            return;
        }
        distanceDivided = this.session.getDimensionHelper().getDimensionDivision(this.displayedWorld);
        ArrayList<KeySortableByOther<Waypoint>> sortableKeys = new ArrayList<>();
        Camera camera = this.minecraft.gameRenderer.getMainCamera();
        for (Waypoint w : this.displayedWorld.getCurrentWaypointSet().getWaypoints()) {
            Comparable[] comparableArr = new Comparable[1];
            comparableArr[0] = sortType == WaypointsSort.COLOR ? w.getWaypointColor() : sortType == WaypointsSort.ANGLE ? Double.valueOf(-w.getComparisonAngleCos(camera, distanceDivided)) : sortType == WaypointsSort.NAME ? w.getComparisonName() : sortType == WaypointsSort.SYMBOL ? w.getInitials() : Double.valueOf(w.getComparisonDistance(camera, distanceDivided));
            sortableKeys.add(new KeySortableByOther<>(w, comparableArr));
        }
        Collections.sort(sortableKeys);
        Iterator<KeySortableByOther<Waypoint>> it = sortableKeys.iterator();
        while (it.hasNext()) {
            KeySortableByOther<Waypoint> k = it.next();
            this.waypointsSorted.add(k.getKey());
        }
        if (this.displayedWorld.getRootConfig().isSortReversed()) {
            Collections.reverse(this.waypointsSorted);
        }
    }

    public boolean keyPressed(int par1, int par2, int par3) {
        if (super.keyPressed(par1, par2, par3)) {
            return true;
        }
        if (KeyMappingUtils.inputMatches(par1 != -1 ? InputConstants.Type.KEYSYM : InputConstants.Type.SCANCODE, par1 != -1 ? par1 : par2, ModSettings.keyWaypoints, 0)) {
            goBack();
            return true;
        }
        return false;
    }
}
