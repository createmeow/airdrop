package xaero.common.icon;

import com.mojang.blaze3d.platform.GlStateManager;
import java.util.List;
import xaero.common.icon.XaeroIconAtlas;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/icon/XaeroIconAtlasManager.class */
public class XaeroIconAtlasManager {
    private final int iconWidth;
    private final int atlasTextureSize;
    private final List<XaeroIconAtlas> atlases;
    private int currentAtlasIndex = -1;

    public XaeroIconAtlasManager(int iconWidth, int atlasTextureSize, List<XaeroIconAtlas> atlases) {
        this.iconWidth = iconWidth;
        this.atlasTextureSize = atlasTextureSize;
        this.atlases = atlases;
    }

    public void clearAtlases() {
        for (XaeroIconAtlas entityIconAtlas : this.atlases) {
            GlStateManager._deleteTexture(entityIconAtlas.getTextureId());
        }
        this.currentAtlasIndex = -1;
        this.atlases.clear();
    }

    public XaeroIconAtlas getCurrentAtlas() {
        if (this.currentAtlasIndex < 0 || this.atlases.get(this.currentAtlasIndex).isFull()) {
            this.atlases.add(XaeroIconAtlas.Builder.begin().setWidth(this.atlasTextureSize).setIconWidth(this.iconWidth).build());
            this.currentAtlasIndex = this.atlases.size() - 1;
        }
        return this.atlases.get(this.currentAtlasIndex);
    }
}
