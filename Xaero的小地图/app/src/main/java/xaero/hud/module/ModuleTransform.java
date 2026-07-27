package xaero.hud.module;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/module/ModuleTransform.class */
public class ModuleTransform {
    public int x;
    public int y;
    public boolean centered;
    public boolean fromRight;
    public boolean fromBottom;
    public boolean flippedHor;
    public boolean flippedVer;
    public boolean fromOldSystem;

    public ModuleTransform copy() {
        ModuleTransform copy = new ModuleTransform();
        copy.x = this.x;
        copy.y = this.y;
        copy.centered = this.centered;
        copy.fromRight = this.fromRight;
        copy.fromBottom = this.fromBottom;
        copy.flippedHor = this.flippedHor;
        copy.flippedVer = this.flippedVer;
        copy.fromOldSystem = this.fromOldSystem;
        return copy;
    }
}
