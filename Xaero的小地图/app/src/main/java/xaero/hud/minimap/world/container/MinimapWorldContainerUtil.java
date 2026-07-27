package xaero.hud.minimap.world.container;

import xaero.hud.path.XaeroPath;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/container/MinimapWorldContainerUtil.class */
public class MinimapWorldContainerUtil {
    public static boolean isMultiplayer(XaeroPath containerPath) {
        String rootNode = containerPath.getRoot().getLastNode();
        return rootNode.startsWith("Multiplayer_") || rootNode.startsWith("Realms_");
    }

    public static String convertWorldFolderToContainerNode(String worldFolder) {
        return convertWorldFolderToContainerNode(worldFolder, 4);
    }

    public static String convertWorldFolderToContainerNode(String worldFolder, int version) {
        String result = worldFolder.replace("_", "%us%").replace("/", "%fs%").replace("\\", "%bs%");
        if (version >= 2) {
            result = result.replace("[", "%lb%").replace("]", "%rb%");
        }
        return result;
    }
}
