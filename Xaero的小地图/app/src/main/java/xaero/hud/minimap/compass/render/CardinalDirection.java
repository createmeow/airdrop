package xaero.hud.minimap.compass.render;

import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/compass/render/CardinalDirection.class */
public enum CardinalDirection {
    NORTH(Component.translatable("gui.xaero_compass_north")),
    EAST(Component.translatable("gui.xaero_compass_east")),
    SOUTH(Component.translatable("gui.xaero_compass_south")),
    WEST(Component.translatable("gui.xaero_compass_west"));

    private final Component initials;

    CardinalDirection(Component initials) {
        this.initials = initials;
    }

    public Component getInitials() {
        return this.initials;
    }
}
