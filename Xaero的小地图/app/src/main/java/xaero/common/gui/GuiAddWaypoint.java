package xaero.common.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointVisibilityType;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.misc.OptimizedMath;
import xaero.common.validator.NumericFieldValidator;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.primary.option.MinimapPrimaryClientConfigOptions;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointColor;
import xaero.hud.minimap.waypoint.WaypointPurpose;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.hud.path.XaeroPath;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.util.GuiUtils;
import xaero.lib.client.gui.widget.MySmallButton;
import xaero.lib.client.gui.widget.Tooltip;
import xaero.lib.client.gui.widget.dropdown.DropDownWidget;
import xaero.lib.client.gui.widget.dropdown.IDropDownWidgetCallback;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.single.SingleConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiAddWaypoint.class */
public class GuiAddWaypoint extends ScreenBase implements IDropDownWidgetCallback {
    private static final Tooltip VISIBILITY_TYPE_TOOLTIP = new Tooltip("gui.xaero_box_visibility_type");
    private static final Tooltip TYPE_TOOLTIP = new Tooltip("gui.xaero_box_waypoint_type");
    private final MinimapSession session;
    private MinimapWorldManager manager;
    protected String screenTitle;
    private Button leftButton;
    private Button rightButton;
    private Button modeSwitchButton;
    private Button resetButton;
    private EditBox nameTextField;
    private EditBox xTextField;
    private EditBox yTextField;
    private EditBox zTextField;
    private EditBox yawTextField;
    private EditBox initialTextField;
    private WaypointEditForm mutualForm;
    private ArrayList<WaypointEditForm> editForms;
    private int selectedWaypointIndex;
    private int defaultContainer;
    private MinimapWorld defaultWorld;
    private GuiWaypointContainers containers;
    private GuiWaypointWorlds worlds;
    private GuiWaypointSets sets;
    private DropDownWidget containersDD;
    private DropDownWidget worldsDD;
    private DropDownWidget setsDD;
    private DropDownWidget colorDD;
    private String fromSet;
    private ArrayList<Waypoint> waypointsEdited;
    private Button disableButton;
    private Button visibilityTypeButton;
    private NumericFieldValidator fieldValidator;
    private NumericFieldValidator fieldYValidator;
    private boolean adding;
    private boolean prefilled;
    private boolean startPrefilled;
    private String namePlaceholder;
    private String xPlaceholder;
    private String yPlaceholder;
    private String zPlaceholder;
    private String yawPlaceholder;
    private String initialPlaceholder;
    private String colorPlaceholder;
    private Button defaultYawButton;
    private Button defaultDisabledButton;
    private Button defaultVisibilityTypeButton;
    protected Button confirmButton;
    private boolean censorCoordsIfNeeded;
    private final XaeroPath frozenAutoWorldPath;
    private BiFunction<String, Integer, String> censoredTextFormatterString;
    private BiFunction<String, Integer, FormattedCharSequence> censoredTextFormatter;
    private boolean hasForcedPlayerPos;
    private int forcedPlayerX;
    private int forcedPlayerY;
    private int forcedPlayerZ;
    private double forcedPlayerScale;
    private MinimapWorld forcedCoordSrcWorld;
    private boolean ignoreEditBoxChanges;
    private boolean canBeLabyMod;
    private final HudMod modMain;

    @Deprecated
    public GuiAddWaypoint(IXaeroMinimap modMain, WaypointsManager manager, Screen par1GuiScreen, Screen escapeScreen, Waypoint point, String defaultParentContainer, WaypointWorld defaultWorld, String waypointSet) {
        this(modMain, manager, par1GuiScreen, escapeScreen, point, defaultParentContainer, defaultWorld, waypointSet, false, 0, 0, 0);
    }

    @Deprecated
    public GuiAddWaypoint(IXaeroMinimap modMain, WaypointsManager manager, Screen par1GuiScreen, Screen escapeScreen, Waypoint point, String defaultParentContainer, WaypointWorld defaultWorld, String waypointSet, boolean hasForcedPlayerPos, int forcedPlayerX, int forcedPlayerY, int forcedPlayerZ, double forcedPlayerScale, WaypointWorld forcedCoordSrcWorld) {
        this(modMain, manager, par1GuiScreen, escapeScreen, (ArrayList<Waypoint>) (point == null ? Lists.newArrayList() : Lists.newArrayList(new Waypoint[]{point})), defaultParentContainer, defaultWorld, waypointSet, point == null || point.getActualColor() == -1, hasForcedPlayerPos, forcedPlayerX, forcedPlayerY, forcedPlayerZ, forcedPlayerScale, forcedCoordSrcWorld);
    }

    @Deprecated
    public GuiAddWaypoint(IXaeroMinimap modMain, WaypointsManager manager, Screen par1GuiScreen, ArrayList<Waypoint> waypointsEdited, String defaultParentContainer, WaypointWorld defaultWorld, boolean adding) {
        this(modMain, manager, par1GuiScreen, (Screen) null, waypointsEdited, defaultParentContainer, defaultWorld, defaultWorld.getCurrentWaypointSetId(), adding);
    }

    @Deprecated
    public GuiAddWaypoint(IXaeroMinimap modMain, WaypointsManager manager, Screen par1GuiScreen, Screen escapeScreen, ArrayList<Waypoint> waypointsEdited, String defaultParentContainer, WaypointWorld defaultWorld, String waypointSet, boolean adding) {
        this(modMain, manager, par1GuiScreen, escapeScreen, waypointsEdited, defaultParentContainer, defaultWorld, waypointSet, adding, false, 0, 0, 0);
    }

    @Deprecated
    public GuiAddWaypoint(IXaeroMinimap modMain, WaypointsManager manager, Screen par1GuiScreen, Screen escapeScreen, ArrayList<Waypoint> waypointsEdited, String defaultParentContainer, WaypointWorld defaultWorld, String waypointSet, boolean adding, boolean hasForcedPlayerPos, int forcedPlayerX, int forcedPlayerY, int forcedPlayerZ, double forcedPlayerScale, WaypointWorld forcedCoordSrcWorld) {
        this((HudMod) modMain, manager, par1GuiScreen, escapeScreen, waypointsEdited, XaeroPath.root(defaultParentContainer), defaultWorld, waypointSet, adding, hasForcedPlayerPos, forcedPlayerX, forcedPlayerY, forcedPlayerZ, forcedPlayerScale, forcedCoordSrcWorld);
    }

    @Deprecated
    public GuiAddWaypoint(IXaeroMinimap modMain, WaypointsManager manager, Screen par1GuiScreen, Screen escapeScreen, ArrayList<Waypoint> waypointsEdited, String defaultParentContainer, WaypointWorld defaultWorld, String waypointSet, boolean adding, boolean hasForcedPlayerPos, int forcedPlayerX, int forcedPlayerY, int forcedPlayerZ) {
        this(modMain, manager, par1GuiScreen, escapeScreen, waypointsEdited, defaultParentContainer, defaultWorld, waypointSet, adding, hasForcedPlayerPos, forcedPlayerX, forcedPlayerY, forcedPlayerZ, Minecraft.getInstance().level.dimensionType().coordinateScale(), (WaypointWorld) null);
    }

    @Deprecated
    public GuiAddWaypoint(IXaeroMinimap modMain, WaypointsManager manager, Screen par1GuiScreen, Screen escapeScreen, Waypoint point, String defaultParentContainer, WaypointWorld defaultWorld, String waypointSet, boolean hasForcedPlayerPos, int forcedPlayerX, int forcedPlayerY, int forcedPlayerZ) {
        this(modMain, manager, par1GuiScreen, escapeScreen, point, defaultParentContainer, defaultWorld, waypointSet, hasForcedPlayerPos, forcedPlayerX, forcedPlayerY, forcedPlayerZ, Minecraft.getInstance().level.dimensionType().coordinateScale(), null);
    }

    public GuiAddWaypoint(HudMod modMain, MinimapSession session, Screen par1GuiScreen, Screen escapeScreen, ArrayList<Waypoint> waypointsEdited, XaeroPath defaultParentContainer, MinimapWorld defaultWorld, String waypointSet, boolean adding, boolean hasForcedPlayerPos, int forcedPlayerX, int forcedPlayerY, int forcedPlayerZ, double forcedPlayerScale, MinimapWorld forcedCoordSrcWorld) {
        super(par1GuiScreen, escapeScreen, Component.literal(""));
        this.ignoreEditBoxChanges = true;
        this.canBeLabyMod = true;
        this.modMain = modMain;
        this.session = session;
        this.hasForcedPlayerPos = hasForcedPlayerPos;
        this.forcedPlayerX = forcedPlayerX;
        this.forcedPlayerY = forcedPlayerY;
        this.forcedPlayerZ = forcedPlayerZ;
        this.forcedPlayerScale = forcedPlayerScale;
        this.forcedCoordSrcWorld = forcedCoordSrcWorld;
        this.waypointsEdited = waypointsEdited;
        this.manager = session.getWorldManager();
        this.fromSet = waypointSet;
        this.defaultWorld = defaultWorld;
        this.frozenAutoWorldPath = session.getWorldState().getAutoWorldPath();
        this.containers = new GuiWaypointContainers(modMain, this.manager, defaultParentContainer, this.frozenAutoWorldPath);
        this.defaultContainer = this.containers.current;
        this.worlds = new GuiWaypointWorlds(this.manager.getRootWorldContainer(defaultParentContainer), session, defaultWorld.getFullPath(), this.frozenAutoWorldPath);
        this.sets = new GuiWaypointSets(false, defaultWorld, this.fromSet);
        boolean z = !waypointsEdited.isEmpty();
        this.prefilled = z;
        this.startPrefilled = z;
        createForms();
        this.fieldValidator = modMain.getFieldValidators().getNumericFieldValidator();
        this.fieldYValidator = modMain.getFieldValidators().getWpCoordFieldValidator();
        this.adding = adding;
        this.namePlaceholder = "- " + I18n.get("gui.xaero_waypoint_name", new Object[0]);
        this.xPlaceholder = "- x";
        this.yPlaceholder = "- y";
        this.zPlaceholder = "- z";
        this.yawPlaceholder = "- " + I18n.get("gui.xaero_yaw", new Object[0]);
        this.initialPlaceholder = "- " + I18n.get("gui.xaero_initial", new Object[0]);
        this.colorPlaceholder = "§8-";
        this.censorCoordsIfNeeded = true;
        this.censoredTextFormatterString = (p_195610_0_, p_195610_1_) -> {
            if (!this.censorCoordsIfNeeded) {
                return p_195610_0_;
            }
            return p_195610_0_.replaceAll(".", "#");
        };
        Style defaultTextStyle = Style.EMPTY;
        this.censoredTextFormatter = (s, i) -> {
            String censoredString = this.censoredTextFormatterString.apply(s, i);
            return cc -> {
                for (int j = 0; j < censoredString.length(); j++) {
                    cc.accept(j, defaultTextStyle, censoredString.charAt(j));
                }
                return true;
            };
        };
        this.canSkipWorldRender = true;
    }

    public GuiAddWaypoint(HudMod modMain, MinimapSession session, Screen par1GuiScreen, Screen escapeScreen, ArrayList<Waypoint> waypointsEdited, XaeroPath defaultParentContainer, MinimapWorld defaultWorld, String waypointSet, boolean adding) {
        this(modMain, session, par1GuiScreen, escapeScreen, waypointsEdited, defaultParentContainer, defaultWorld, waypointSet, adding, false, 0, 0, 0, Minecraft.getInstance().level.dimensionType().coordinateScale(), (MinimapWorld) null);
    }

    public GuiAddWaypoint(HudMod modMain, MinimapSession session, Screen par1GuiScreen, ArrayList<Waypoint> waypointsEdited, XaeroPath defaultParentContainer, MinimapWorld defaultWorld, boolean adding) {
        this(modMain, session, par1GuiScreen, (Screen) null, waypointsEdited, defaultParentContainer, defaultWorld, defaultWorld.getCurrentWaypointSetId(), adding);
    }

    private void fillFormWaypoint(WaypointEditForm form, Waypoint w) {
        form.name = w.getLocalizedName();
        form.xText = w.getX();
        form.yText = w.isYIncluded() ? w.getY() : "~";
        form.zText = w.getZ();
        form.yawText = w.isRotation() ? w.getYaw() : "";
        form.initial = w.getInitials();
        form.disabledOrTemporary = w.isDestination() ? 3 : w.isTemporary() ? 2 : w.isDisabled() ? 1 : 0;
        form.color = w.getWaypointColor();
        form.visibilityType = w.getVisibility();
        if (form.initial.length() == 0) {
            form.autoInitial = true;
        }
    }

    private double getDimDiv(double waypointDimScale) {
        double playerDimScale = this.hasForcedPlayerPos ? this.forcedPlayerScale : this.minecraft.cameraEntity.level().dimensionType().coordinateScale();
        return playerDimScale / waypointDimScale;
    }

    private int getAutomaticX(double waypointDimScale) {
        int playerX = this.hasForcedPlayerPos ? this.forcedPlayerX : OptimizedMath.myFloor(this.minecraft.cameraEntity.getX());
        return OptimizedMath.myFloor(playerX * getDimDiv(waypointDimScale));
    }

    private String getAutomaticYInput(MinimapWorld destinationWorld) {
        if (this.hasForcedPlayerPos) {
            if (this.forcedPlayerY == 32767) {
                return "~";
            }
            if (this.forcedCoordSrcWorld != null && this.forcedCoordSrcWorld != destinationWorld) {
                return "~";
            }
        }
        int playerY = this.hasForcedPlayerPos ? this.forcedPlayerY : OptimizedMath.myFloor(this.minecraft.cameraEntity.getY() + 0.0625d);
        return OptimizedMath.myFloor(playerY);
    }

    private int getAutomaticZ(double waypointDimScale) {
        int playerZ = this.hasForcedPlayerPos ? this.forcedPlayerZ : OptimizedMath.myFloor(this.minecraft.cameraEntity.getZ());
        return OptimizedMath.myFloor(playerZ * getDimDiv(waypointDimScale));
    }

    private void fillFormAutomatic(WaypointEditForm form) {
        form.xText = "";
        form.yText = "";
        form.zText = "";
        form.color = WaypointColor.getRandom();
        form.autoInitial = true;
    }

    private void createForms() {
        this.editForms = new ArrayList<>();
        this.mutualForm = new WaypointEditForm();
        for (int i = 0; i < this.waypointsEdited.size(); i++) {
            Waypoint w = this.waypointsEdited.get(i);
            WaypointEditForm form = new WaypointEditForm();
            fillFormWaypoint(form, w);
            this.editForms.add(form);
        }
        if (!this.startPrefilled) {
            WaypointEditForm createdForm = new WaypointEditForm();
            fillFormAutomatic(createdForm);
            this.editForms.add(createdForm);
        }
        updateMutual();
    }

    private void resetCurrentForm() {
        if (this.selectedWaypointIndex >= this.waypointsEdited.size()) {
            WaypointEditForm freshForm = new WaypointEditForm();
            fillFormAutomatic(freshForm);
            this.editForms.set(this.selectedWaypointIndex, freshForm);
        } else {
            Waypoint w = this.waypointsEdited.get(this.selectedWaypointIndex);
            WaypointEditForm freshForm2 = new WaypointEditForm();
            fillFormWaypoint(freshForm2, w);
            this.editForms.set(this.selectedWaypointIndex, freshForm2);
        }
    }

    private void updateMutual() {
        String nameTextMutual = "";
        String initialMutual = "";
        String yawMutual = "";
        int waypointDisabledOrTemporaryMutual = 0;
        WaypointVisibilityType waypointVisibilityTypeMutual = WaypointVisibilityType.LOCAL;
        WaypointColor colorMutual = null;
        String xTextMutual = "";
        String yTextMutual = "";
        String zTextMutual = "";
        WaypointEditForm firstForm = this.editForms.get(0);
        this.mutualForm.keepName = differentValues((v0) -> {
            return v0.getName();
        });
        this.mutualForm.keepXText = (this.editForms.size() > 1 && firstForm.xText.isEmpty()) || differentValues((v0) -> {
            return v0.getxText();
        });
        this.mutualForm.keepYText = (this.editForms.size() > 1 && firstForm.yText.isEmpty()) || differentValues((v0) -> {
            return v0.getyText();
        });
        this.mutualForm.keepZText = (this.editForms.size() > 1 && firstForm.zText.isEmpty()) || differentValues((v0) -> {
            return v0.getzText();
        });
        WaypointEditForm waypointEditForm = this.mutualForm;
        WaypointEditForm waypointEditForm2 = this.mutualForm;
        boolean zDifferentValues = differentValues((v0) -> {
            return v0.getYawText();
        });
        waypointEditForm2.keepYawText = zDifferentValues;
        waypointEditForm.defaultKeepYawText = zDifferentValues;
        this.mutualForm.keepInitial = differentValues((v0) -> {
            return v0.getInitial();
        });
        this.mutualForm.autoInitial = this.editForms.size() == 1 && firstForm.autoInitial;
        WaypointEditForm waypointEditForm3 = this.mutualForm;
        WaypointEditForm waypointEditForm4 = this.mutualForm;
        boolean zDifferentValues2 = differentValues((v0) -> {
            return v0.getDisabledOrTemporary();
        });
        waypointEditForm4.keepDisabledOrTemporary = zDifferentValues2;
        waypointEditForm3.defaultKeepDisabledOrTemporary = zDifferentValues2;
        WaypointEditForm waypointEditForm5 = this.mutualForm;
        WaypointEditForm waypointEditForm6 = this.mutualForm;
        boolean zDifferentValues3 = differentValues((v0) -> {
            return v0.getVisibilityType();
        });
        waypointEditForm6.keepVisibilityType = zDifferentValues3;
        waypointEditForm5.defaultKeepVisibilityType = zDifferentValues3;
        this.mutualForm.defaultKeepColor = differentValues((v0) -> {
            return v0.getColor();
        });
        if (!this.mutualForm.keepName) {
            nameTextMutual = firstForm.name;
        }
        if (!this.mutualForm.keepXText) {
            xTextMutual = firstForm.xText;
        }
        if (!this.mutualForm.keepYText) {
            yTextMutual = firstForm.yText;
        }
        if (!this.mutualForm.keepZText) {
            zTextMutual = firstForm.zText;
        }
        if (!this.mutualForm.keepYawText) {
            yawMutual = firstForm.yawText;
        }
        if (!this.mutualForm.keepInitial) {
            initialMutual = firstForm.initial;
        }
        if (!this.mutualForm.keepDisabledOrTemporary) {
            waypointDisabledOrTemporaryMutual = firstForm.disabledOrTemporary;
        }
        if (!this.mutualForm.keepVisibilityType) {
            waypointVisibilityTypeMutual = firstForm.visibilityType;
        }
        if (!this.mutualForm.defaultKeepColor) {
            colorMutual = firstForm.color;
        }
        this.mutualForm.name = nameTextMutual;
        this.mutualForm.xText = xTextMutual;
        this.mutualForm.yText = yTextMutual;
        this.mutualForm.zText = zTextMutual;
        this.mutualForm.yawText = yawMutual;
        this.mutualForm.initial = initialMutual;
        this.mutualForm.disabledOrTemporary = waypointDisabledOrTemporaryMutual;
        this.mutualForm.visibilityType = waypointVisibilityTypeMutual;
        this.mutualForm.color = colorMutual;
    }

    private void confirmMutual() {
        for (int i = 0; i < this.editForms.size(); i++) {
            WaypointEditForm individualForm = this.editForms.get(i);
            if (!this.mutualForm.keepName) {
                individualForm.name = this.mutualForm.name;
            }
            if (!this.mutualForm.keepXText) {
                individualForm.xText = this.mutualForm.xText;
            }
            if (!this.mutualForm.keepYText) {
                individualForm.yText = this.mutualForm.yText;
            }
            if (!this.mutualForm.keepZText) {
                individualForm.zText = this.mutualForm.zText;
            }
            if (!this.mutualForm.keepYawText) {
                individualForm.yawText = this.mutualForm.yawText;
            }
            if (!this.mutualForm.keepInitial) {
                if (!individualForm.initial.equals(this.mutualForm.initial)) {
                    individualForm.autoInitial = false;
                }
                individualForm.initial = this.mutualForm.initial;
            }
            if (!this.mutualForm.keepDisabledOrTemporary) {
                individualForm.disabledOrTemporary = this.mutualForm.disabledOrTemporary;
            }
            if (!this.mutualForm.keepVisibilityType) {
                individualForm.visibilityType = this.mutualForm.visibilityType;
            }
            if (this.mutualForm.color != null) {
                individualForm.color = this.mutualForm.color;
            }
        }
    }

    private boolean differentValues(Function<WaypointEditForm, Object> s) {
        if (this.editForms.size() == 1) {
            return false;
        }
        WaypointEditForm testWaypoint = this.editForms.get(0);
        for (int i = 1; i < this.editForms.size(); i++) {
            WaypointEditForm w = this.editForms.get(i);
            if (!s.apply(w).equals(s.apply(testWaypoint))) {
                return true;
            }
        }
        return false;
    }

    public String[] createColorOptions() {
        boolean unchangedOption = getCurrent().defaultKeepColor;
        String[] options = new String[WaypointColor.values().length + (unchangedOption ? 1 : 0)];
        if (unchangedOption) {
            options[0] = this.colorPlaceholder;
        }
        for (int i = 0; i < WaypointColor.values().length; i++) {
            if (i == 0) {
                options[i + (unchangedOption ? 1 : 0)] = WaypointColor.values()[i].getName().getString();
            } else {
                options[i + (unchangedOption ? 1 : 0)] = "§" + WaypointColor.values()[i].getFormat() + WaypointColor.values()[i].getName().getString();
            }
        }
        return options;
    }

    private boolean getMutualEditConfig() {
        SingleConfigManager<Config> primaryConfigManager = HudMod.INSTANCE.getHudConfigs().getPrimaryClientConfigManager();
        return ((Boolean) primaryConfigManager.getEffective(MinimapPrimaryClientConfigOptions.WAYPOINT_MUTUAL_EDIT)).booleanValue();
    }

    private void setMutualEditConfig(boolean value) {
        SingleConfigManager<Config> primaryConfigManager = HudMod.INSTANCE.getHudConfigs().getPrimaryClientConfigManager();
        primaryConfigManager.getConfig().set(MinimapPrimaryClientConfigOptions.WAYPOINT_MUTUAL_EDIT, Boolean.valueOf(value));
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void init() {
        String str;
        super.init();
        this.ignoreEditBoxChanges = true;
        this.screenTitle = this.adding ? I18n.get("gui.xaero_new_waypoint", new Object[0]) : I18n.get("gui.xaero_edit_waypoint", new Object[0]);
        if (this.editForms.size() > 1) {
            String str2 = this.screenTitle;
            if (this.editForms.size() > 1) {
                str = " (" + (getMutualEditConfig() ? "" : (this.selectedWaypointIndex + 1) + "/") + this.editForms.size() + ")";
            } else {
                str = "";
            }
            this.screenTitle = str2 + str;
        }
        this.nameTextField = applyEditBoxResponder(new EditBox(this.font, (this.width / 2) - 100, 104, 200, 20, Component.translatable("gui.xaero_waypoint_name")));
        this.xTextField = applyEditBoxResponder(new EditBox(this.font, (this.width / 2) - 109, 134, 50, 20, Component.literal("x")));
        this.yTextField = applyEditBoxResponder(new EditBox(this.font, (this.width / 2) - 53, 134, 50, 20, Component.literal("y")));
        this.zTextField = applyEditBoxResponder(new EditBox(this.font, (this.width / 2) + 3, 134, 50, 20, Component.literal("z")));
        ClientConfigManager configManager = this.modMain.getHudConfigs().getClientConfigManager();
        boolean hideWaypointCoordinatesConfig = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.HIDE_WAYPOINT_COORDINATES)).booleanValue();
        if (hideWaypointCoordinatesConfig) {
            this.xTextField.setFormatter(this.censoredTextFormatter);
            this.yTextField.setFormatter(this.censoredTextFormatter);
            this.zTextField.setFormatter(this.censoredTextFormatter);
        }
        this.yawTextField = applyEditBoxResponder(new EditBox(this.font, (this.width / 2) + 59, 134, 50, 20, Component.translatable("gui.xaero_yaw")));
        this.initialTextField = applyEditBoxResponder(new EditBox(this.font, (this.width / 2) - 25, 164, 50, 20, Component.translatable("gui.xaero_initial")));
        addWidget(this.nameTextField);
        addWidget(this.xTextField);
        addWidget(this.yTextField);
        addWidget(this.zTextField);
        addWidget(this.yawTextField);
        addWidget(this.initialTextField);
        MySmallButton mySmallButton = new MySmallButton(0, (this.width / 2) - 155, (this.height / 6) + 168, Component.translatable("gui.xaero_confirm"), b -> {
            Waypoint w;
            if (getMutualEditConfig()) {
                confirmMutual();
            }
            boolean creatingAWaypoint = this.adding && this.waypointsEdited.size() < this.editForms.size();
            XaeroPath destinationWorldKeys = this.worlds.getCurrentKey();
            MinimapWorld destinationWorld = this.manager.getWorld(destinationWorldKeys);
            double waypointDimScale = this.session.getDimensionHelper().getDimCoordinateScale(destinationWorld);
            int initialEditedWaypointsSize = this.waypointsEdited.size();
            int i = 0;
            while (i < this.editForms.size()) {
                boolean shouldCreate = i >= initialEditedWaypointsSize;
                if (!creatingAWaypoint && shouldCreate) {
                    break;
                }
                WaypointEditForm waypointForm = this.editForms.get(i);
                String nameString = waypointForm.name;
                String xString = waypointForm.xText;
                String yString = waypointForm.yText;
                if (yString.equals("-") || yString.isEmpty()) {
                    yString = getAutomaticYInput(destinationWorld);
                }
                String zString = waypointForm.zText;
                String initialString = waypointForm.initial;
                WaypointColor color = waypointForm.color;
                boolean yIncluded = !yString.equals("~");
                int x = (xString.equals("-") || xString.isEmpty()) ? getAutomaticX(waypointDimScale) : Integer.parseInt(xString);
                int y = !yIncluded ? 0 : Integer.parseInt(yString);
                int z = (zString.equals("-") || zString.isEmpty()) ? getAutomaticZ(waypointDimScale) : Integer.parseInt(zString);
                if (shouldCreate) {
                    w = new Waypoint(x, y, z, nameString, initialString, color, WaypointPurpose.NORMAL, false, yIncluded);
                    this.waypointsEdited.add(w);
                } else {
                    w = this.waypointsEdited.get(i);
                    if (w.getPurpose() != WaypointPurpose.DEATH || !nameString.equals(I18n.get("gui.xaero_deathpoint", new Object[0]))) {
                        w.setName(nameString);
                        if (w.getPurpose() != WaypointPurpose.NORMAL) {
                            w.setPurpose(WaypointPurpose.NORMAL);
                        }
                    }
                    w.setX(x);
                    w.setY(y);
                    w.setZ(z);
                    w.setInitials(initialString);
                    w.setWaypointColor(color);
                    w.setYIncluded(yIncluded);
                }
                String yawText = waypointForm.yawText;
                int disableOrTemporary = waypointForm.disabledOrTemporary;
                boolean yawIsUsable = yawText.length() > 0 && !yawText.equals("-");
                w.setRotation(yawIsUsable);
                if (yawIsUsable) {
                    w.setYaw(Integer.parseInt(yawText));
                }
                if (w.isDestination() != (disableOrTemporary == 3)) {
                    w.setPurpose(disableOrTemporary == 3 ? WaypointPurpose.DESTINATION : WaypointPurpose.NORMAL);
                }
                w.setDisabled(disableOrTemporary == 1);
                if (disableOrTemporary == 2) {
                    w.setTemporary(true);
                }
                w.setVisibility(waypointForm.visibilityType);
                i++;
            }
            MinimapWorld sourceWorld = this.defaultWorld;
            WaypointSet sourceSet = sourceWorld.getWaypointSet(this.fromSet);
            String destinationSetKey = this.sets.getCurrentSetKey();
            WaypointSet destinationSet = destinationWorld.getWaypointSet(destinationSetKey);
            if (this.adding || sourceSet != destinationSet) {
                if (!((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.NEW_WAYPOINTS_TO_BOTTOM)).booleanValue()) {
                    destinationSet.addAll(this.waypointsEdited, true);
                } else {
                    destinationSet.addAll(this.waypointsEdited);
                }
            }
            if (sourceSet != destinationSet) {
                sourceSet.removeAll(this.waypointsEdited);
            }
            try {
                this.session.getWorldManagerIO().saveWorld(sourceWorld);
                if (destinationWorld != sourceWorld) {
                    this.session.getWorldManagerIO().saveWorld(destinationWorld);
                }
            } catch (IOException e) {
                MinimapLogs.LOGGER.error("suppressed exception", e);
            }
            goBack();
        });
        this.confirmButton = mySmallButton;
        addRenderableWidget(mySmallButton);
        addRenderableWidget(new MySmallButton(0, (this.width / 2) + 5, (this.height / 6) + 168, Component.translatable("gui.xaero_cancel", new Object[0]), b2 -> {
            goBack();
        }));
        this.leftButton = Button.builder(Component.literal("<"), b3 -> {
            this.selectedWaypointIndex--;
            if (this.selectedWaypointIndex < 0) {
                this.selectedWaypointIndex = 0;
            }
            boolean restoreFocus = getFocused() == b3;
            init(this.minecraft, this.width, this.height);
            if (restoreFocus) {
                boolean activeBU = this.leftButton.active;
                this.leftButton.active = true;
                setFocused(this.leftButton);
                this.leftButton.active = activeBU;
            }
        }).bounds((this.width / 2) - 203, 104, 20, 20).build();
        this.rightButton = Button.builder(Component.literal(">"), b4 -> {
            this.selectedWaypointIndex++;
            if (this.selectedWaypointIndex >= this.editForms.size()) {
                this.selectedWaypointIndex = this.editForms.size() - 1;
            }
            boolean restoreFocus = getFocused() == b4;
            init(this.minecraft, this.width, this.height);
            if (restoreFocus) {
                boolean activeBU = this.rightButton.active;
                this.rightButton.active = true;
                setFocused(this.rightButton);
                this.rightButton.active = activeBU;
            }
        }).bounds((this.width / 2) + 183, 104, 20, 20).build();
        this.modeSwitchButton = Button.builder(Component.translatable(getMutualEditConfig() ? "gui.xaero_waypoints_edit_mode_all" : "gui.xaero_waypoints_edit_mode_individually"), b5 -> {
            setMutualEditConfig(!getMutualEditConfig());
            HudMod.INSTANCE.getHudConfigs().getPrimaryClientConfigManagerIO().save();
            if (getMutualEditConfig()) {
                this.prefilled = true;
                updateMutual();
            } else {
                confirmMutual();
            }
            boolean restoreFocus = getFocused() == b5;
            init(this.minecraft, this.width, this.height);
            if (restoreFocus) {
                boolean activeBU = this.modeSwitchButton.active;
                this.modeSwitchButton.active = true;
                setFocused(this.modeSwitchButton);
                this.modeSwitchButton.active = activeBU;
            }
        }).bounds((this.width / 2) + 106, 56, 99, 20).build();
        if (this.editForms.size() > 1) {
            addRenderableWidget(this.leftButton);
            addRenderableWidget(this.rightButton);
            addRenderableWidget(this.modeSwitchButton);
        }
        Button buttonBuild = Button.builder(Component.translatable("gui.xaero_waypoints_edit_reset"), b6 -> {
            if (getMutualEditConfig()) {
                createForms();
                boolean restoreFocus = getFocused() == b6;
                init(this.minecraft, this.width, this.height);
                if (restoreFocus) {
                    boolean activeBU = this.resetButton.active;
                    this.resetButton.active = true;
                    setFocused(this.resetButton);
                    this.resetButton.active = activeBU;
                    return;
                }
                return;
            }
            resetCurrentForm();
            boolean restoreFocus2 = getFocused() == b6;
            init(this.minecraft, this.width, this.height);
            if (restoreFocus2) {
                boolean activeBU2 = this.resetButton.active;
                this.resetButton.active = true;
                setFocused(this.resetButton);
                this.resetButton.active = activeBU2;
            }
        }).bounds((this.width / 2) - 204, 56, 99, 20).build();
        this.resetButton = buttonBuild;
        addRenderableWidget(buttonBuild);
        this.nameTextField.setValue(getCurrent().name);
        this.xTextField.setValue(getCurrent().xText);
        this.yTextField.setValue(getCurrent().yText);
        this.zTextField.setValue(getCurrent().zText);
        this.yawTextField.setValue(getCurrent().yawText);
        this.initialTextField.setValue(getCurrent().initial);
        TooltipButton tooltipButton = new TooltipButton((this.width / 2) + 31, 164, 79, 20, getDisableButtonText(), b7 -> {
            getCurrent().disabledOrTemporary = (getCurrent().disabledOrTemporary + 1) % 4;
            this.disableButton.setMessage(getDisableButtonText());
            getCurrent().keepDisabledOrTemporary = false;
            if (this.defaultDisabledButton != null) {
                this.defaultDisabledButton.active = true;
            }
        }, () -> {
            return TYPE_TOOLTIP;
        });
        this.disableButton = tooltipButton;
        addRenderableWidget(tooltipButton);
        TooltipButton tooltipButton2 = new TooltipButton((this.width / 2) - 109, 164, 79, 20, getCurrent().visibilityType.getTranslation(), b8 -> {
            getCurrent().visibilityType = WaypointVisibilityType.values()[(getCurrent().visibilityType.ordinal() + 1) % WaypointVisibilityType.values().length];
            this.visibilityTypeButton.setMessage(getCurrent().visibilityType.getTranslation());
            getCurrent().keepVisibilityType = false;
            if (this.defaultVisibilityTypeButton != null) {
                this.defaultVisibilityTypeButton.active = true;
            }
        }, () -> {
            return VISIBILITY_TYPE_TOOLTIP;
        });
        this.visibilityTypeButton = tooltipButton2;
        addRenderableWidget(tooltipButton2);
        if (getCurrent().defaultKeepYawText) {
            Button buttonBuild2 = Button.builder(Component.literal("-"), b9 -> {
                getCurrent().keepYawText = true;
                getCurrent().yawText = "";
                this.yawTextField.setValue(getCurrent().yawText);
                b9.active = false;
            }).bounds((this.width / 2) + 111, 134, 20, 20).build();
            this.defaultYawButton = buttonBuild2;
            addRenderableWidget(buttonBuild2);
            this.defaultYawButton.active = !getCurrent().keepYawText;
        }
        if (getCurrent().defaultKeepDisabledOrTemporary) {
            Button buttonBuild3 = Button.builder(Component.literal("-"), b10 -> {
                getCurrent().keepDisabledOrTemporary = true;
                getCurrent().disabledOrTemporary = 0;
                this.disableButton.setMessage(getDisableButtonText());
                b10.active = false;
            }).bounds((this.width / 2) + 110, 164, 20, 20).build();
            this.defaultDisabledButton = buttonBuild3;
            addRenderableWidget(buttonBuild3);
            this.defaultDisabledButton.active = !getCurrent().keepDisabledOrTemporary;
        }
        if (getCurrent().defaultKeepVisibilityType) {
            Button buttonBuild4 = Button.builder(Component.literal("-"), b11 -> {
                getCurrent().keepVisibilityType = true;
                getCurrent().visibilityType = WaypointVisibilityType.LOCAL;
                this.visibilityTypeButton.setMessage(getCurrent().visibilityType.getTranslation());
                b11.active = false;
            }).bounds((this.width / 2) - 130, 164, 20, 20).build();
            this.defaultVisibilityTypeButton = buttonBuild4;
            addRenderableWidget(buttonBuild4);
            this.defaultVisibilityTypeButton.active = !getCurrent().keepVisibilityType;
        }
        if (hideWaypointCoordinatesConfig) {
            addRenderableWidget(new MySuperTinyButton((this.width / 2) + 115, 134, Component.translatable(this.censorCoordsIfNeeded ? "gui.xaero_waypoints_edit_show" : "gui.xaero_waypoints_edit_hide"), b12 -> {
                this.censorCoordsIfNeeded = !this.censorCoordsIfNeeded;
                b12.setMessage(Component.translatable(this.censorCoordsIfNeeded ? "gui.xaero_waypoints_edit_show" : "gui.xaero_waypoints_edit_hide"));
            }));
        }
        WaypointColor currentColor = getCurrent().color;
        this.colorDD = DropDownWidget.Builder.begin().setOptions(createColorOptions()).setX((this.width / 2) - 60).setY(82).setW(120).setSelected(Integer.valueOf((currentColor == null ? -1 : currentColor.ordinal()) + (getCurrent().defaultKeepColor ? 1 : 0))).setCallback(this).setContainer(this).setNarrationTitle(Component.translatable("gui.xaero_dropdown_waypoint_color")).build();
        addWidget(this.colorDD);
        DropDownWidget dropDownWidgetCreateSetsDropdown = createSetsDropdown();
        this.setsDD = dropDownWidgetCreateSetsDropdown;
        addWidget(dropDownWidgetCreateSetsDropdown);
        DropDownWidget dropDownWidgetCreateContainersDropdown = createContainersDropdown();
        this.containersDD = dropDownWidgetCreateContainersDropdown;
        addWidget(dropDownWidgetCreateContainersDropdown);
        DropDownWidget dropDownWidgetCreateWorldsDropdown = createWorldsDropdown();
        this.worldsDD = dropDownWidgetCreateWorldsDropdown;
        addWidget(dropDownWidgetCreateWorldsDropdown);
        setFocused(this.nameTextField);
        this.nameTextField.setFocused(true);
        updateConfirmButton();
    }

    protected void setInitialFocus() {
    }

    public EditBox applyEditBoxResponder(EditBox box) {
        box.setResponder(s -> {
            if (!this.ignoreEditBoxChanges) {
                postType(box);
            }
        });
        return box;
    }

    private DropDownWidget createSetsDropdown() {
        return DropDownWidget.Builder.begin().setOptions(this.sets.getOptions()).setX((this.width / 2) - 101).setY(60).setW(201).setSelected(Integer.valueOf(this.sets.getCurrentSet())).setCallback(this).setContainer(this).setNarrationTitle(Component.translatable("gui.xaero_dropdown_waypoint_set")).build();
    }

    private DropDownWidget createContainersDropdown() {
        return DropDownWidget.Builder.begin().setOptions(this.containers.options).setX((this.width / 2) - 203).setY(38).setW(200).setSelected(Integer.valueOf(this.containers.current)).setCallback(this).setContainer(this).setNarrationTitle(Component.translatable("gui.xaero_dropdown_waypoint_container")).build();
    }

    private DropDownWidget createWorldsDropdown() {
        return DropDownWidget.Builder.begin().setOptions(this.worlds.options).setX((this.width / 2) + 2).setY(38).setW(200).setSelected(Integer.valueOf(this.worlds.current)).setCallback(this).setContainer(this).setNarrationTitle(Component.translatable("gui.xaero_dropdown_waypoint_world")).build();
    }

    private Component getDisableButtonText() {
        return Component.translatable(getCurrent().disabledOrTemporary == 3 ? "gui.xaero_destination" : getCurrent().disabledOrTemporary == 1 ? "gui.xaero_toggle_disabled" : getCurrent().disabledOrTemporary == 0 ? "gui.xaero_toggle_enabled" : "gui.xaero_temporary2");
    }

    private WaypointEditForm getCurrent() {
        return getMutualEditConfig() ? this.mutualForm : this.editForms.get(this.selectedWaypointIndex);
    }

    public boolean keyPressed(int par1, int par2, int par3) {
        GuiEventListener focused = getFocused();
        preType(focused);
        this.ignoreEditBoxChanges = false;
        boolean result = super.keyPressed(par1, par2, par3);
        if (this.ignoreEditBoxChanges) {
            this.canBeLabyMod = false;
        }
        if ((focused instanceof EditBox) && canConfirm() && (par1 == 257 || par1 == 335)) {
            this.confirmButton.onClick(0.0d, 0.0d);
            return true;
        }
        return result;
    }

    public boolean charTyped(char par1, int par2) {
        GuiEventListener focused = getFocused();
        preType(focused);
        this.ignoreEditBoxChanges = false;
        boolean result = super.charTyped(par1, par2);
        if (this.ignoreEditBoxChanges) {
            this.canBeLabyMod = false;
        }
        return result;
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.ignoreEditBoxChanges = false;
        boolean result = super.mouseClicked(mouseX, mouseY, button);
        if (this.ignoreEditBoxChanges) {
            this.canBeLabyMod = false;
        }
        return result;
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.ignoreEditBoxChanges = false;
        boolean result = super.mouseReleased(mouseX, mouseY, button);
        if (this.ignoreEditBoxChanges) {
            this.canBeLabyMod = false;
        }
        return result;
    }

    private void preType(GuiEventListener focused) {
        if (focused == null) {
        }
    }

    private void postType(GuiEventListener focused) {
        this.ignoreEditBoxChanges = true;
        if (focused == null) {
            return;
        }
        if (this.nameTextField == focused) {
            if (getCurrent().autoInitial && this.nameTextField.getValue().length() > 0 && (!getCurrent().keepInitial || !getMutualEditConfig())) {
                this.initialTextField.setValue(this.nameTextField.getValue().substring(0, 1).toUpperCase());
            }
        } else if (this.initialTextField == focused) {
            getCurrent().autoInitial = false;
        }
        checkFields(focused);
        updateConfirmButton();
    }

    public void setFocused(GuiEventListener l) {
        preType(l);
        EditBox focused = getFocused();
        if (focused != null && focused != l && (focused instanceof EditBox)) {
            focused.setFocused(false);
        }
        super.setFocused(l);
    }

    private boolean canConfirm() {
        WaypointEditForm current = getCurrent();
        return (current.keepName || current.name.length() > 0) && (current.keepInitial || current.initial.length() > 0);
    }

    private void updateConfirmButton() {
        Button button = this.confirmButton;
        Button button2 = this.modeSwitchButton;
        boolean zCanConfirm = canConfirm();
        button2.active = zCanConfirm;
        button.active = zCanConfirm;
        this.leftButton.active = !getMutualEditConfig() && canConfirm() && this.selectedWaypointIndex > 0;
        this.rightButton.active = !getMutualEditConfig() && canConfirm() && this.selectedWaypointIndex < this.editForms.size() - 1;
    }

    private void handleCoordinateInputSpaces(EditBox coordinateBox, EditBox nextBox) {
        String startingBoxValue = coordinateBox.getValue();
        int indexOfFirstSpace = startingBoxValue.indexOf(32);
        if (indexOfFirstSpace != -1) {
            String subStringToCut = startingBoxValue.substring(indexOfFirstSpace + 1);
            coordinateBox.setValue(startingBoxValue.substring(0, indexOfFirstSpace));
            coordinateBox.moveCursorToStart(false);
            nextBox.setValue(nextBox.getValue() + subStringToCut);
            if (getFocused() == coordinateBox) {
                coordinateBox.setFocused(false);
                nextBox.setFocused(true);
                setFocused(nextBox);
                nextBox.moveCursorToEnd(false);
            }
        }
    }

    protected void checkFields(GuiEventListener focused) {
        handleCoordinateInputSpaces(this.xTextField, this.yTextField);
        handleCoordinateInputSpaces(this.yTextField, this.zTextField);
        handleCoordinateInputSpaces(this.zTextField, this.yawTextField);
        this.fieldValidator.validate(this.yawTextField);
        if (this.yawTextField == focused) {
            getCurrent().keepYawText = false;
            if (this.defaultYawButton != null) {
                this.defaultYawButton.active = true;
            }
        }
        this.fieldValidator.validate(this.xTextField);
        this.fieldYValidator.validate(this.yTextField);
        this.fieldValidator.validate(this.zTextField);
        WaypointEditForm current = getCurrent();
        current.name = this.nameTextField.getValue();
        current.xText = this.xTextField.getValue();
        current.yText = this.yTextField.getValue();
        current.zText = this.zTextField.getValue();
        current.yawText = this.yawTextField.getValue();
        current.initial = this.initialTextField.getValue();
        if (current.initial.length() > 2) {
            current.initial = current.initial.substring(0, 2);
            this.initialTextField.setValue(current.initial);
        }
        if (current.yawText.length() > 4) {
            current.yawText = current.yawText.substring(0, 4);
            this.yawTextField.setValue(current.yawText);
        }
        if (this.prefilled && this.editForms.size() > 1 && getMutualEditConfig()) {
            current.keepName = current.name.isEmpty();
            current.keepXText = current.xText.isEmpty();
            current.keepYText = current.yText.isEmpty();
            current.keepZText = current.zText.isEmpty();
            current.keepInitial = current.initial.isEmpty();
        }
    }

    public void tick() {
        if (this.minecraft.cameraEntity == null) {
            this.minecraft.setScreen((Screen) null);
        }
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int par1, int par2, float par3) {
        renderEscapeScreen(guiGraphics, par1, par2, par3);
        super.render(guiGraphics, par1, par2, par3);
        super.renderTooltips(guiGraphics, par1, par2, par3);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    protected void renderPreDropdown(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        super.renderPreDropdown(guiGraphics, mouseX, mouseY, partial);
        guiGraphics.drawCenteredString(this.font, this.screenTitle, this.width / 2, 20, 16777215);
        WaypointEditForm current = getCurrent();
        if (!this.canBeLabyMod) {
            this.ignoreEditBoxChanges = true;
        }
        if (this.ignoreEditBoxChanges) {
            if (!this.nameTextField.isFocused() && current.keepName) {
                GuiUtils.setFieldText(this.nameTextField, this.namePlaceholder, -11184811);
                this.nameTextField.moveCursorTo(0, false);
            }
            XaeroPath destinationWorldKeys = this.worlds.getCurrentKey();
            MinimapWorld destinationWorld = this.manager.getWorld(destinationWorldKeys);
            double waypointDimScale = this.session.getDimensionHelper().getDimCoordinateScale(destinationWorld);
            if (current.keepXText) {
                if (!this.xTextField.isFocused()) {
                    GuiUtils.setFieldText(this.xTextField, this.xPlaceholder, -11184811);
                }
            } else if (current.xText.isEmpty()) {
                GuiUtils.setFieldText(this.xTextField, getAutomaticX(waypointDimScale), -11184811);
                this.xTextField.moveCursorTo(0, false);
            }
            if (current.keepYText) {
                if (!this.yTextField.isFocused()) {
                    GuiUtils.setFieldText(this.yTextField, this.yPlaceholder, -11184811);
                }
            } else if (current.yText.isEmpty()) {
                GuiUtils.setFieldText(this.yTextField, getAutomaticYInput(destinationWorld), -11184811);
                this.yTextField.moveCursorTo(0, false);
            }
            if (current.keepZText) {
                if (!this.zTextField.isFocused()) {
                    GuiUtils.setFieldText(this.zTextField, this.zPlaceholder, -11184811);
                }
            } else if (current.zText.isEmpty()) {
                GuiUtils.setFieldText(this.zTextField, getAutomaticZ(waypointDimScale), -11184811);
                this.zTextField.moveCursorTo(0, false);
            }
            if (!this.yawTextField.isFocused() && current.yawText.isEmpty()) {
                if (current.keepYawText) {
                    GuiUtils.setFieldText(this.yawTextField, this.yawPlaceholder, -11184811);
                } else {
                    GuiUtils.setFieldText(this.yawTextField, I18n.get("gui.xaero_yaw", new Object[0]), -11184811);
                }
                this.yawTextField.moveCursorTo(0, false);
            }
            if (!this.initialTextField.isFocused() && current.initial.isEmpty()) {
                if (current.keepInitial) {
                    GuiUtils.setFieldText(this.initialTextField, this.initialPlaceholder, -11184811);
                } else {
                    GuiUtils.setFieldText(this.initialTextField, I18n.get("gui.xaero_initial", new Object[0]), -11184811);
                }
                this.initialTextField.moveCursorTo(0, false);
            }
        }
        this.nameTextField.render(guiGraphics, mouseX, mouseY, partial);
        this.xTextField.render(guiGraphics, mouseX, mouseY, partial);
        this.yTextField.render(guiGraphics, mouseX, mouseY, partial);
        this.zTextField.render(guiGraphics, mouseX, mouseY, partial);
        this.yawTextField.render(guiGraphics, mouseX, mouseY, partial);
        this.initialTextField.render(guiGraphics, mouseX, mouseY, partial);
        if (this.ignoreEditBoxChanges) {
            GuiUtils.setFieldText(this.nameTextField, current.name);
            GuiUtils.setFieldText(this.xTextField, current.xText);
            GuiUtils.setFieldText(this.yTextField, current.yText);
            GuiUtils.setFieldText(this.zTextField, current.zText);
            GuiUtils.setFieldText(this.yawTextField, current.yawText);
            GuiUtils.setFieldText(this.initialTextField, current.initial);
        }
        this.ignoreEditBoxChanges = true;
    }

    @Override // xaero.lib.client.gui.widget.dropdown.IDropDownWidgetCallback
    public boolean onSelected(DropDownWidget menu, int selected) {
        MinimapWorld currentWorld;
        if (menu == this.setsDD) {
            this.sets.setCurrentSet(selected);
            if (this.session.getWorldState().getCurrentWorldPath().equals(this.worlds.getCurrentKey())) {
                this.manager.getCurrentWorld().setCurrentWaypointSetId(this.sets.getCurrentSetKey());
                try {
                    this.session.getWorldManagerIO().saveWorld(this.manager.getCurrentWorld());
                    return true;
                } catch (IOException e) {
                    MinimapLogs.LOGGER.error("suppressed exception", e);
                    return true;
                }
            }
            return true;
        }
        if (menu == this.colorDD) {
            getCurrent().color = !getCurrent().defaultKeepColor ? WaypointColor.fromIndex(selected) : selected == 0 ? null : WaypointColor.fromIndex(selected - 1);
            return true;
        }
        if (menu != this.containersDD) {
            if (menu == this.worldsDD) {
                this.worlds.current = selected;
                XaeroPath worldKeys = this.worlds.getCurrentKey();
                MinimapWorld currentWorld2 = this.manager.getWorld(worldKeys);
                this.sets = new GuiWaypointSets(false, currentWorld2, currentWorld2 == this.defaultWorld ? this.fromSet : currentWorld2.getCurrentWaypointSetId());
                AbstractWidget abstractWidget = this.setsDD;
                DropDownWidget dropDownWidgetCreateSetsDropdown = createSetsDropdown();
                this.setsDD = dropDownWidgetCreateSetsDropdown;
                replaceWidget(abstractWidget, dropDownWidgetCreateSetsDropdown);
                return true;
            }
            return true;
        }
        this.containers.current = selected;
        if (this.containers.current != this.defaultContainer) {
            currentWorld = this.manager.getRootWorldContainer(this.containers.getCurrentKey()).getFirstWorld();
        } else {
            currentWorld = this.defaultWorld;
        }
        this.sets = new GuiWaypointSets(false, currentWorld, this.containers.current == this.defaultContainer ? this.fromSet : currentWorld.getCurrentWaypointSetId());
        this.worlds = new GuiWaypointWorlds(this.manager.getRootWorldContainer(this.containers.getCurrentKey()), this.session, currentWorld.getFullPath(), this.frozenAutoWorldPath);
        AbstractWidget abstractWidget2 = this.setsDD;
        DropDownWidget dropDownWidgetCreateSetsDropdown2 = createSetsDropdown();
        this.setsDD = dropDownWidgetCreateSetsDropdown2;
        replaceWidget(abstractWidget2, dropDownWidgetCreateSetsDropdown2);
        AbstractWidget abstractWidget3 = this.worldsDD;
        DropDownWidget dropDownWidgetCreateWorldsDropdown = createWorldsDropdown();
        this.worldsDD = dropDownWidgetCreateWorldsDropdown;
        replaceWidget(abstractWidget3, dropDownWidgetCreateWorldsDropdown);
        return true;
    }
}
