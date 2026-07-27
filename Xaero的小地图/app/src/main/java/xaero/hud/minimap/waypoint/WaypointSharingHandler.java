package xaero.hud.minimap.waypoint;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.common.HudMod;
import xaero.common.gui.GuiAddWaypoint;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.MinimapWorldContainerUtil;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/WaypointSharingHandler.class */
public abstract class WaypointSharingHandler {
    public static final String WAYPOINT_OLD_SHARE_PREFIX = "xaero_waypoint:";
    public static final String WAYPOINT_ADD_PREFIX = "xaero_waypoint_add:";
    public static final String WAYPOINT_SHARE_PREFIX = "xaero-waypoint:";
    private static final String DESTINATION_PREFIX_INTERNAL = "Internal";
    private static final String DESTINATION_PREFIX_INTERNAL_HYPHEN = "Internal-";
    private static final String DESTINATION_PREFIX_EXTERNAL = "External";
    private HudMod modMain;
    private MinimapSession session;
    private Screen confirmScreenParent;
    private Waypoint sharedWaypoint;
    private MinimapWorld minimapWorld;

    protected WaypointSharingHandler(HudMod modMain, MinimapSession session) {
        this.modMain = modMain;
        this.session = session;
    }

    public void shareWaypoint(Screen currentScreen, Waypoint waypoint, MinimapWorld minimapWorld) {
        this.confirmScreenParent = currentScreen;
        this.sharedWaypoint = waypoint;
        this.minimapWorld = minimapWorld;
        Minecraft.getInstance().setScreen(new ConfirmScreen(this::onShareConfirmationResult, Component.translatable("gui.xaero_share_msg1"), Component.translatable("gui.xaero_share_msg2")));
    }

    public void onShareConfirmationResult(boolean confirmed) {
        if (!confirmed) {
            Minecraft.getInstance().setScreen(this.confirmScreenParent);
            return;
        }
        String destinationDetails = getSharedDestinationDetails(this.minimapWorld.getContainer());
        String message = "xaero-waypoint:" + removeFormatting(this.sharedWaypoint.getNameSafe("^col^")) + ":" + removeFormatting(this.sharedWaypoint.getInitialsSafe("^col^")) + ":" + this.sharedWaypoint.getX() + ":" + String.valueOf(this.sharedWaypoint.isYIncluded() ? Integer.valueOf(this.sharedWaypoint.getY()) : "~") + ":" + this.sharedWaypoint.getZ() + ":" + this.sharedWaypoint.getWaypointColor().ordinal() + ":" + this.sharedWaypoint.isRotation() + ":" + this.sharedWaypoint.getYaw() + ":" + destinationDetails;
        Minecraft.getInstance().gui.getChat().addRecentChat(message);
        Minecraft.getInstance().player.connection.sendChat(message);
        Minecraft.getInstance().setScreen((Screen) null);
    }

    private String getSharedDestinationDetails(MinimapWorldContainer minimapWorldContainer) {
        MinimapWorldContainer rootContainer = minimapWorldContainer.getRoot();
        MinimapWorldContainer autoRootContainer = this.session.getWorldManager().getAutoWorld().getContainer().getRoot();
        if (rootContainer != autoRootContainer) {
            return DESTINATION_PREFIX_EXTERNAL;
        }
        XaeroPath containerPath = minimapWorldContainer.getPath();
        if (containerPath.getNodeCount() <= 1) {
            return "Internal-waypoints";
        }
        XaeroPath containerSubPath = containerPath.getSubPath(1);
        String dimKey = containerSubPath.getRoot().getLastNode();
        if (dimKey.equals("dim%0")) {
            dimKey = "overworld";
        } else if (dimKey.equals("dim%-1")) {
            dimKey = "the_nether";
        } else if (dimKey.equals("dim%1")) {
            dimKey = "the_end";
        }
        String subContainersString = XaeroPath.root(dimKey).resolve(containerSubPath.getSubPath(1)).toString().replace(":", "^col^");
        return "Internal-" + removeFormatting(subContainersString) + "-waypoints";
    }

    public void onWaypointReceived(String playerName, String text) {
        String text2 = text.replaceAll("§.", "");
        boolean newFormat = text2.contains("xaero-waypoint:");
        String sharePrefix = newFormat ? "xaero-waypoint:" : "xaero_waypoint:";
        String[] args = text2.substring(text2.indexOf(sharePrefix)).split(":");
        if (args.length < 9) {
            MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 0");
            return;
        }
        if (newFormat) {
            args[1] = restoreFormatting(args[1]);
            args[2] = restoreFormatting(args[2]);
        }
        MutableComponent mutableComponentTranslatable = Component.translatable(Waypoint.getStringFromStringSafe(args[1], "^col^"));
        Component dimensionName = null;
        if (args.length > 9) {
            if (args[9].equals(DESTINATION_PREFIX_INTERNAL)) {
                XaeroPath potentialContainerPath = this.session.getWorldStateUpdater().getPotentialContainerPath();
                args[9] = getSharedDestinationDetails(this.session.getWorldManager().getWorldContainer(potentialContainerPath));
            }
            if (args[9].startsWith(DESTINATION_PREFIX_INTERNAL_HYPHEN)) {
                dimensionName = getReceivedDimensionName(args[9]);
            }
        }
        MutableComponent mainComponent = Component.translatable(dimensionName != null ? "gui.xaero_waypoint_shared_dimension2" : "gui.xaero_waypoint_shared2", new Object[]{playerName, mutableComponentTranslatable, dimensionName});
        StringBuilder addCommandBuilder = new StringBuilder();
        addCommandBuilder.append("xaero_waypoint_add:");
        addCommandBuilder.append(args[1]);
        for (int i = 2; i < args.length; i++) {
            addCommandBuilder.append(':').append(args[i]);
        }
        String addCommand = addCommandBuilder.toString();
        MutableComponent hoverComponent = Component.literal(args[3] + ", " + args[4] + ", " + args[5]);
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + addCommand);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent);
        MutableComponent addComponent = Component.translatable("gui.xaero_waypoint_shared_add").withStyle(ChatFormatting.DARK_GREEN).withStyle(ChatFormatting.UNDERLINE);
        mainComponent.getSiblings().add(addComponent);
        mainComponent.setStyle(mainComponent.getStyle().applyFormat(ChatFormatting.GRAY).withClickEvent(clickEvent).withHoverEvent(hoverEvent));
        Minecraft.getInstance().gui.getChat().addMessage(mainComponent);
    }

    private Component getReceivedDimensionName(String destinationDetails) {
        String strSubstring;
        int lastMinus = destinationDetails.lastIndexOf("-");
        if (lastMinus == -1) {
            return null;
        }
        if (lastMinus == DESTINATION_PREFIX_INTERNAL.length()) {
            strSubstring = destinationDetails.substring(DESTINATION_PREFIX_INTERNAL_HYPHEN.length());
        } else {
            strSubstring = destinationDetails.substring(DESTINATION_PREFIX_INTERNAL_HYPHEN.length(), lastMinus);
        }
        String containerPathRaw = strSubstring;
        String containerPathString = restoreFormatting(containerPathRaw.replace("^col^", ":"));
        if (containerPathString.contains("/")) {
            return Component.literal(containerPathString);
        }
        if (!containerPathString.startsWith("dim%")) {
            return Component.literal(containerPathString);
        }
        if (containerPathString.length() == 4) {
            return Component.translatable("gui.xaero_waypoint_unknown_dimension");
        }
        ResourceKey<Level> dimId = this.session.getDimensionHelper().getDimensionKeyForDirectoryName(containerPathString);
        if (dimId == null) {
            return Component.translatable("gui.xaero_waypoint_unknown_dimension");
        }
        return Component.literal(dimId.location().getPath());
    }

    public void onWaypointAdd(String[] args) {
        String waypointName = Waypoint.getStringFromStringSafe(args[1], "^col^");
        if (waypointName.length() < 1 || waypointName.length() > 32) {
            MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 1");
            return;
        }
        String waypointSymbol = Waypoint.getStringFromStringSafe(args[2], "^col^");
        if (waypointSymbol.length() < 1 || waypointSymbol.length() > 3) {
            MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 2");
            return;
        }
        if (this.session.getWorldState().getAutoWorldPath() == null) {
            MinimapLogs.LOGGER.info("Can't add a waypoint at this time!");
            return;
        }
        boolean yIsIncluded = !args[4].equals("~");
        try {
            int x = Integer.parseInt(args[3]);
            int y = yIsIncluded ? Integer.parseInt(args[4]) : 0;
            int z = Integer.parseInt(args[5]);
            int colorIndex = Integer.parseInt(args[6]);
            if (colorIndex < 0) {
                colorIndex = 0;
            }
            WaypointColor color = WaypointColor.fromIndex(colorIndex % WaypointColor.values().length);
            String yawString = args[8];
            if (yawString.length() > 4) {
                MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 4");
                return;
            }
            int yaw = Integer.parseInt(yawString);
            boolean rotation = args[7].equals("true");
            Waypoint waypoint = new Waypoint(x, y, z, waypointName, waypointSymbol, color, WaypointPurpose.NORMAL, false, yIsIncluded);
            waypoint.setRotation(rotation);
            waypoint.setYaw(yaw);
            MinimapWorld externalWorld = this.session.getWorldManager().getCurrentWorld();
            MinimapWorld destinationWorld = externalWorld;
            if (args.length > 9) {
                destinationWorld = getReceivedDestinationWorld(args[9], externalWorld);
                if (destinationWorld == null) {
                    return;
                }
            }
            Minecraft.getInstance().setScreen(new GuiAddWaypoint(this.modMain, this.session, (Screen) null, (Screen) null, (ArrayList<Waypoint>) Lists.newArrayList(new Waypoint[]{waypoint}), destinationWorld.getContainer().getRoot().getPath(), destinationWorld, destinationWorld.getCurrentWaypointSetId(), true));
        } catch (NumberFormatException e) {
            MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 3");
        }
    }

    private MinimapWorld getReceivedDestinationWorld(String destinationDetails, MinimapWorld externalWorld) {
        MinimapWorld destinationWorld;
        if (destinationDetails.equals(DESTINATION_PREFIX_EXTERNAL)) {
            return externalWorld;
        }
        if (!destinationDetails.startsWith(DESTINATION_PREFIX_INTERNAL_HYPHEN) || destinationDetails.equals(DESTINATION_PREFIX_INTERNAL_HYPHEN)) {
            MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 12");
            return null;
        }
        int divider = destinationDetails.lastIndexOf(45);
        if (divider == DESTINATION_PREFIX_INTERNAL.length()) {
            divider = destinationDetails.length();
        }
        String containerPathString = restoreFormatting(destinationDetails.substring(DESTINATION_PREFIX_INTERNAL_HYPHEN.length(), divider).replace("^col^", ":"));
        if (containerPathString.contains("\\")) {
            MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 13");
            return null;
        }
        String[] containerPathNodes = containerPathString.split("/");
        if (containerPathNodes.length != 1) {
            MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 8");
            return null;
        }
        for (String s : containerPathNodes) {
            if (s.isEmpty()) {
                MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 11");
                return null;
            }
        }
        Optional<ResourceKey<Level>> receivedDimId = getReceivedDimId(containerPathNodes);
        if (receivedDimId == null) {
            return null;
        }
        if (receivedDimId.isEmpty()) {
            return externalWorld;
        }
        ResourceKey<Level> dimId = receivedDimId.get();
        containerPathNodes[0] = this.session.getDimensionHelper().getDimensionDirectoryName(dimId);
        XaeroPath containerPath = this.session.getWorldState().getAutoRootContainerPath();
        for (String node : containerPathNodes) {
            containerPath = containerPath.resolve(node);
        }
        try {
            XaeroPath subContainerPath = containerPath.getSubPath(1);
            Path rootContainerFilePath = this.modMain.getMinimapFolder().resolve(containerPath.getRoot().getLastNode());
            Path rootContainerCanonicalPath = rootContainerFilePath.toFile().getCanonicalFile().toPath();
            Path securityTest = subContainerPath.applyToFilePath(rootContainerCanonicalPath).resolve("test_1.txt");
            if (!securityTest.equals(securityTest.toFile().getCanonicalFile().toPath())) {
                MinimapLogs.LOGGER.info("Dangerously incorrect format of the shared waypoint! Error: 10");
                return null;
            }
            MinimapWorldRootContainer rootContainer = this.session.getWorldManager().getAutoRootContainer();
            rootContainer.renameOldContainer(containerPath);
            MinimapWorldContainer worldContainer = this.session.getWorldManager().getWorldContainer(containerPath);
            MinimapWorld autoWorld = this.session.getWorldManager().getAutoWorld();
            if (worldContainer == autoWorld.getContainer()) {
                destinationWorld = autoWorld;
            } else {
                destinationWorld = worldContainer.getFirstWorldConnectedTo(autoWorld);
                if (destinationWorld == null) {
                    destinationWorld = worldContainer.getFirstWorld();
                }
                if (destinationWorld == null) {
                    destinationWorld = worldContainer.addWorld(this.session.getWorldStateUpdater().getPotentialWorldNode(dimId, false));
                }
            }
            if (!this.modMain.getSupportMods().worldmap()) {
                return destinationWorld;
            }
            if (!MinimapWorldContainerUtil.isMultiplayer(containerPath)) {
                return destinationWorld;
            }
            List<String> worldmapMultiworldIds = this.modMain.getSupportMods().worldmapSupport.getMultiworldIds(dimId);
            for (String mw : worldmapMultiworldIds) {
                this.session.getWorldManager().addWorld(containerPath.resolve(mw));
            }
            return destinationWorld;
        } catch (IOException e) {
            MinimapLogs.LOGGER.error("IO exception during file path check when adding a shared waypoint!", e);
            return null;
        } catch (InvalidPathException ipe) {
            MinimapLogs.LOGGER.error("Invalid path exception during file path check when adding a shared waypoint!", ipe);
            return null;
        }
    }

    private Optional<ResourceKey<Level>> getReceivedDimId(String[] containerPathNodes) {
        ResourceKey<Level> dimId;
        String dimensionNode = containerPathNodes[0];
        if (!dimensionNode.startsWith("dim%")) {
            if (!dimensionNode.replaceAll("[^a-zA-Z0-9_]+", "").equals(dimensionNode)) {
                MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 18");
                return null;
            }
            dimId = this.session.getDimensionHelper().findDimensionKeyForOldName(Minecraft.getInstance().player, dimensionNode);
        } else {
            dimId = this.session.getDimensionHelper().getDimensionKeyForDirectoryName(dimensionNode);
        }
        if (dimId == null) {
            MinimapLogs.LOGGER.info("Destination dimension doesn't exist! Handling waypoint as external.");
            return Optional.empty();
        }
        return Optional.of(dimId);
    }

    private String removeFormatting(String s) {
        return s.replace("-", "^min^").replace("_", "-").replace("*", "^ast^");
    }

    private String restoreFormatting(String s) {
        return s.replace("^ast^", "*").replace("-", "_").replace("^min^", "-");
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/WaypointSharingHandler$Builder.class */
    public static final class Builder {
        private HudMod modMain;
        private MinimapSession session;

        private Builder() {
        }

        public Builder setDefault() {
            setModMain(null);
            setSession(null);
            return this;
        }

        public Builder setModMain(HudMod modMain) {
            this.modMain = modMain;
            return this;
        }

        public Builder setSession(MinimapSession session) {
            this.session = session;
            return this;
        }

        public WaypointSharingHandler build() {
            if (this.modMain == null || this.session == null) {
                throw new IllegalStateException();
            }
            return new xaero.common.minimap.waypoints.WaypointSharingHandler(this.modMain, this.session);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
