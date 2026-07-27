package xaero.hud.render.module;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/render/module/ModuleRenderContext.class */
public class ModuleRenderContext {
    public int x;
    public int y;
    public int w;
    public int h;
    public boolean flippedVertically;
    public boolean flippedHorizontally;
    public final int screenWidth;
    public final int screenHeight;
    public final double screenScale;

    public ModuleRenderContext(int screenWidth, int screenHeight, double screenScale) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenScale = screenScale;
    }
}
