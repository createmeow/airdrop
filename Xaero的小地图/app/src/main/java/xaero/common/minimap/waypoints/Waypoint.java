package xaero.common.minimap.waypoints;

import net.minecraft.client.Camera;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import xaero.hud.minimap.waypoint.WaypointColor;
import xaero.hud.minimap.waypoint.WaypointPurpose;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/Waypoint.class */
public class Waypoint implements Comparable<Waypoint> {
    public static final int ONEOFF_DESTINATION_SAFE_FOR = 5000;
    public static final int ONEOFF_DESTINATION_REMOVE_DISTANCE = 4;
    public static Vec3 RENDER_SORTING_POS = new Vec3(0.0d, 0.0d, 0.0d);
    private int x;
    private int y;
    private int z;
    private String name;
    private String initials;
    private WaypointColor color;
    private WaypointVisibilityType visibility;
    private boolean disabled;
    private WaypointPurpose purpose;
    private boolean rotation;
    private int yaw;
    private boolean temporary;
    private boolean yIncluded;
    private final long createdAt;

    @Deprecated
    private int actualColor;

    @Deprecated
    public Waypoint(int x, int y, int z, String name, String initials, int color) {
        this(x, y, z, name, initials, color, 0, false);
    }

    @Deprecated
    public Waypoint(int x, int y, int z, String name, String initials, int color, int type) {
        this(x, y, z, name, initials, color, type, false);
    }

    @Deprecated
    public Waypoint(int x, int y, int z, String name, String initials, int color, int type, boolean temp) {
        this(x, y, z, name, initials, color, type, temp, true);
    }

    @Deprecated
    public Waypoint(int x, int y, int z, String name, String initials, int color, int type, boolean temp, boolean yIncluded) {
        this(x, y, z, name, initials, WaypointColor.fromIndex(color), WaypointPurpose.values()[type], temp, yIncluded);
        this.actualColor = color;
    }

    public Waypoint(int x, int y, int z, String name, String initials, WaypointColor color) {
        this(x, y, z, name, initials, color, WaypointPurpose.NORMAL, false);
    }

    public Waypoint(int x, int y, int z, String name, String initials, WaypointColor color, WaypointPurpose purpose) {
        this(x, y, z, name, initials, color, purpose, false);
    }

    public Waypoint(int x, int y, int z, String name, String initials, WaypointColor color, WaypointPurpose purpose, boolean temp) {
        this(x, y, z, name, initials, color, purpose, temp, true);
    }

    public Waypoint(int x, int y, int z, String name, String initials, WaypointColor color, WaypointPurpose purpose, boolean temp, boolean yIncluded) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.initials = initials;
        this.color = color;
        this.purpose = purpose;
        this.name = name;
        this.temporary = temp;
        this.visibility = WaypointVisibilityType.LOCAL;
        if (this.purpose.isDeath()) {
            this.visibility = WaypointVisibilityType.GLOBAL;
        }
        this.yIncluded = yIncluded;
        this.createdAt = System.currentTimeMillis();
        this.actualColor = color.ordinal();
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX(double dimDiv) {
        if (dimDiv == 1.0d) {
            return this.x;
        }
        return (int) Math.floor(this.x / dimDiv);
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getZ(double dimDiv) {
        if (dimDiv == 1.0d) {
            return this.z;
        }
        return (int) Math.floor(this.z / dimDiv);
    }

    public String getName() {
        return this.name;
    }

    public String getLocalizedName() {
        return I18n.get(this.name, new Object[0]);
    }

    public String getNameSafe(String replacement) {
        return getName().replace(":", replacement);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitials() {
        return this.initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    @Deprecated
    public String getSymbol() {
        return getInitials();
    }

    @Deprecated
    public void setSymbol(String symbol) {
        setInitials(symbol);
    }

    @Deprecated
    public String getSymbolSafe(String replacement) {
        return getInitialsSafe(replacement);
    }

    public String getInitialsSafe(String replacement) {
        return getInitials().replace(":", replacement);
    }

    @Deprecated
    public int getColor() {
        return getWaypointColor().ordinal();
    }

    @Deprecated
    public int getActualColor() {
        return this.actualColor;
    }

    @Deprecated
    public void setColor(int c) {
        setWaypointColor(WaypointColor.fromIndex(c));
        this.actualColor = c;
    }

    public WaypointColor getWaypointColor() {
        return this.color;
    }

    public void setWaypointColor(WaypointColor c) {
        this.color = c;
        this.actualColor = c.ordinal();
    }

    public boolean isGlobal() {
        return this.visibility.isGlobal();
    }

    @Deprecated
    public int getVisibilityType() {
        return getVisibility().ordinal();
    }

    public WaypointVisibilityType getVisibility() {
        return this.visibility;
    }

    @Deprecated
    public void setVisibilityType(int visibility) {
        setVisibility(WaypointVisibilityType.values()[visibility]);
    }

    public void setVisibility(WaypointVisibilityType visibility) {
        if (this.purpose != WaypointPurpose.DEATH) {
            this.visibility = visibility;
        }
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean b) {
        this.temporary = false;
        this.disabled = b;
    }

    @Deprecated
    public int getWaypointType() {
        return this.purpose.ordinal();
    }

    @Deprecated
    public void setType(int type) {
        setPurpose(WaypointPurpose.values()[type]);
    }

    public WaypointPurpose getPurpose() {
        return this.purpose;
    }

    public void setPurpose(WaypointPurpose purpose) {
        this.purpose = purpose;
        if (this.purpose.isDeath()) {
            this.visibility = WaypointVisibilityType.GLOBAL;
        }
    }

    public boolean isRotation() {
        return this.rotation;
    }

    public void setRotation(boolean rotation) {
        this.rotation = rotation;
    }

    public int getYaw() {
        return this.yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public boolean isTemporary() {
        return this.temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
        this.disabled = false;
    }

    public boolean isYIncluded() {
        return this.yIncluded;
    }

    public void setYIncluded(boolean yIncluded) {
        this.yIncluded = yIncluded;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    @Deprecated
    public boolean isOneoffDestination() {
        return this.purpose.isDestination();
    }

    @Deprecated
    public void setOneoffDestination(boolean oneoffDestination) {
        if (oneoffDestination) {
            if (this.purpose == WaypointPurpose.NORMAL) {
                this.purpose = WaypointPurpose.DESTINATION;
            }
        } else {
            if (this.purpose != WaypointPurpose.DESTINATION) {
                return;
            }
            this.purpose = WaypointPurpose.NORMAL;
        }
    }

    public boolean isDestination() {
        return this.purpose.isDestination();
    }

    public double getDistanceSq(double x, double y, double z) {
        double offX = this.x - x;
        double offY = this.yIncluded ? this.y - y : 0.0d;
        double offZ = this.z - z;
        return (offX * offX) + (offY * offY) + (offZ * offZ);
    }

    public static String getStringFromStringSafe(String stringSafe, String replacement) {
        return stringSafe.replace(replacement, ":");
    }

    public boolean isServerWaypoint() {
        return false;
    }

    public String getComparisonName() {
        String comparisonName = getLocalizedName().toLowerCase().trim();
        if (comparisonName.startsWith("the ")) {
            return comparisonName.substring(4);
        }
        if (comparisonName.startsWith("a ")) {
            return comparisonName.substring(2);
        }
        return comparisonName;
    }

    public double getComparisonDistance(Camera camera, double dimDiv) {
        Vec3 cameraPos = camera.getPosition();
        double offX = getX(dimDiv) - cameraPos.x;
        double offY = !isYIncluded() ? 0.0d : getY() - cameraPos.y;
        double offZ = getZ(dimDiv) - cameraPos.z;
        return (offX * offX) + (offY * offY) + (offZ * offZ);
    }

    public double getComparisonAngleCos(Camera camera, double dimDiv) {
        Vector3f lookVector = camera.getLookVector();
        Vec3 cameraPos = camera.getPosition();
        double offX = getX(dimDiv) - cameraPos.x;
        double offY = !isYIncluded() ? 0.0d : getY() - cameraPos.y;
        double offZ = getZ(dimDiv) - cameraPos.z;
        double distance = Math.sqrt((offX * offX) + (offY * offY) + (offZ * offZ));
        return (((offX * lookVector.x()) + (offY * lookVector.y())) + (offZ * lookVector.z())) / distance;
    }

    private double getRenderSortingDistanceSquared() {
        double fromCameraX = this.x - RENDER_SORTING_POS.x;
        double fromCameraY = this.yIncluded ? this.y - RENDER_SORTING_POS.y : 0.0d;
        double fromCameraZ = this.z - RENDER_SORTING_POS.z;
        return (fromCameraX * fromCameraX) + (fromCameraY * fromCameraY) + (fromCameraZ * fromCameraZ);
    }

    @Override // java.lang.Comparable
    public int compareTo(Waypoint other) {
        boolean isDeath = this.purpose.isDeath();
        if (isDeath != other.purpose.isDeath()) {
            return isDeath ? 1 : -1;
        }
        double rsds = getRenderSortingDistanceSquared();
        double otherRsds = other.getRenderSortingDistanceSquared();
        if (rsds > otherRsds) {
            return -1;
        }
        return rsds == otherRsds ? 0 : 1;
    }
}
