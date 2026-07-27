package xaero.common.interfaces.render;

import xaero.common.IXaeroMinimap;
import xaero.common.graphics.CustomVertexConsumers;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/interfaces/render/InterfaceRenderer.class */
public class InterfaceRenderer {
    private final IXaeroMinimap modMain;

    public InterfaceRenderer(IXaeroMinimap modMain) {
        this.modMain = modMain;
    }

    public CustomVertexConsumers getCustomVertexConsumers() {
        return this.modMain.getHudRenderer().getCustomVertexConsumers();
    }
}
