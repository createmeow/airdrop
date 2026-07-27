package xaero.hud.minimap.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.info.config.InfoDisplayConfigData;
import xaero.hud.minimap.common.config.info.config.InfoDisplayManagerConfigData;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/InfoDisplayIO.class */
public class InfoDisplayIO {
    public String encode(InfoDisplayManagerConfigData data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("infoDisplayOrder");
        data.getOrderStream().forEach(id -> {
            stringBuilder.append(":");
            stringBuilder.append(id);
        });
        stringBuilder.append("\n");
        data.getOrderStream().forEach(id2 -> {
            InfoDisplayConfigData displayConfig = data.get(id2);
            if (displayConfig == null) {
                MinimapLogs.LOGGER.warn("Info display mentioned in the order list being encoded doesn't have a config: " + id2);
            } else {
                stringBuilder.append(getInfoDisplayLine(id2, displayConfig)).append("\n");
            }
        });
        return stringBuilder.toString();
    }

    private String getInfoDisplayLine(String id, InfoDisplayConfigData data) {
        String stateToWrite = data.getState() == null ? "-" : data.getState();
        String textColorToWrite = data.getTextColor() == null ? "-" : data.getTextColor();
        String backgroundColorToWrite = data.getBackgroundColor() == null ? "-" : data.getBackgroundColor();
        return "infoDisplay:" + id + ":" + stateToWrite + ":" + textColorToWrite + ":" + backgroundColorToWrite;
    }

    public InfoDisplayManagerConfigData decode(String encodedData) {
        List<String> order = new ArrayList<>();
        Map<String, InfoDisplayConfigData> configs = new HashMap<>();
        encodedData.lines().forEach(line -> {
            String[] args = line.split(":");
            if (args[0].equals("infoDisplayOrder")) {
                loadInfoDisplayOrderLine(args, order);
            } else if (args[0].equals("infoDisplay")) {
                loadInfoDisplay(args, configs);
            }
        });
        return new InfoDisplayManagerConfigData(order, configs);
    }

    private void loadInfoDisplayOrderLine(String[] args, List<String> destination) {
        if (args.length <= 1) {
            return;
        }
        for (int i = 1; i < args.length; i++) {
            if (!destination.contains(args[i])) {
                destination.add(args[i]);
            }
        }
    }

    private void loadInfoDisplay(String[] args, Map<String, InfoDisplayConfigData> destination) {
        String id = args[1];
        String state = args[2].equals("-") ? null : args[2];
        Integer textColorIndex = args[3].equals("-") ? null : Integer.valueOf(Integer.parseInt(args[3]));
        Integer backgroundColorIndex = args[4].equals("-") ? null : Integer.valueOf(Integer.parseInt(args[4]));
        InfoDisplayConfigData config = new InfoDisplayConfigData(backgroundColorIndex, textColorIndex, state);
        destination.put(id, config);
    }
}
