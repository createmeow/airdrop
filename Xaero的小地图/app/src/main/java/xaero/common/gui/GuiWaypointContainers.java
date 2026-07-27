package xaero.common.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;
import xaero.hud.path.XaeroPathReader;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.util.KeySortableByOther;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiWaypointContainers.class */
public class GuiWaypointContainers extends GuiDropdownHelper<String> {

    @Deprecated
    private static final XaeroPathReader pathReader = new XaeroPathReader();

    @Deprecated
    public GuiWaypointContainers(IXaeroMinimap modMain, WaypointsManager waypointsManager, String currentContainer, String autoContainer) {
        this((HudMod) modMain, waypointsManager.getWorldManager(), currentContainer == null ? null : pathReader.read(currentContainer), autoContainer == null ? null : pathReader.read(autoContainer + "/old"));
    }

    /* JADX WARN: Type inference failed for: r1v9, types: [T[], java.lang.Object[]] */
    public GuiWaypointContainers(HudMod modMain, MinimapWorldManager manager, XaeroPath currentContainer, XaeroPath autoWorldPath) {
        String sortName;
        List<KeySortableByOther<String>> sortableKeyList = new ArrayList<>();
        ClientConfigManager configManager = modMain.getHudConfigs().getClientConfigManager();
        int hideWorldNamesConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.HIDE_WORLD_NAMES)).intValue();
        for (MinimapWorldRootContainer rootContainer : manager.getRootContainers()) {
            String rootContainerNode = rootContainer.getPath().getLastNode();
            String[] details = rootContainerNode.split("_");
            if (details.length > 1 && details[0].equals("Realms")) {
                sortName = "Realm ID " + details[1].substring(details[1].indexOf(".") + 1);
            } else {
                sortName = details[details.length - 1].replace("%us%", "_").replace("%fs%", "/").replace("%bs%", "\\").replace("§", ":").replace("%lb%", "[").replace("%rb%", "]").replace(",", ".");
            }
            if (hideWorldNamesConfig == 1 && details.length > 1 && details[0].equals("Multiplayer")) {
                String[] dotSplit = sortName.split("(\\.|:+)");
                StringBuilder builder = new StringBuilder();
                for (int o = 0; o < dotSplit.length; o++) {
                    if (o < dotSplit.length - 2) {
                        builder.append("-.");
                    } else if (o < dotSplit.length - 1) {
                        builder.append(dotSplit[o].isEmpty() ? "" : Character.valueOf(dotSplit[o].charAt(0))).append("-.");
                    } else {
                        builder.append(dotSplit[o]);
                    }
                }
                sortName = builder.toString();
            }
            Comparable[] comparableArr = new Comparable[3];
            comparableArr[0] = Integer.valueOf(rootContainerNode.startsWith("Multiplayer_") ? 1 : rootContainerNode.startsWith("Realms_") ? 2 : 0);
            comparableArr[1] = sortName.toLowerCase();
            comparableArr[2] = sortName;
            sortableKeyList.add(new KeySortableByOther<>(rootContainerNode, comparableArr));
        }
        Collections.sort(sortableKeyList);
        this.current = -1;
        this.auto = -1;
        List<String> keyList = new ArrayList<>();
        List<String> optionList = new ArrayList<>();
        String currentRoot = currentContainer == null ? null : currentContainer.getLastNode();
        String autoRoot = autoWorldPath == null ? null : autoWorldPath.getRoot().getLastNode();
        for (int i = 0; i < sortableKeyList.size(); i++) {
            KeySortableByOther<String> k = sortableKeyList.get(i);
            String containerKey = k.getKey();
            if (this.current == -1 && containerKey.equals(currentRoot)) {
                this.current = i;
            }
            String option = (String) k.getDataToSortBy()[2];
            option = (hideWorldNamesConfig == 2 || option.isEmpty()) ? "hidden " + optionList.size() : option;
            if (this.auto == -1 && containerKey.equals(autoRoot)) {
                this.auto = i;
                option = option + " (auto)";
            }
            keyList.add(containerKey);
            optionList.add(option);
        }
        this.keys = keyList.toArray(new String[0]);
        this.options = (String[]) optionList.toArray(new String[0]);
    }
}
