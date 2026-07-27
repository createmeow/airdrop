package xaero.common.icon;

import com.mojang.blaze3d.platform.GlStateManager;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import xaero.common.exception.OpenGLException;
import xaero.hud.minimap.MinimapLogs;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/icon/XaeroIconAtlas.class */
public final class XaeroIconAtlas {
    private final int textureId;
    private final int width;
    private int currentIndex;
    private final int iconWidth;
    private final int sideIconCount;
    private final int maxIconCount;

    private XaeroIconAtlas(int textureId, int width, int iconWidth) {
        this.textureId = textureId;
        this.width = width;
        this.iconWidth = iconWidth;
        this.sideIconCount = width / iconWidth;
        this.maxIconCount = this.sideIconCount * this.sideIconCount;
    }

    public int getTextureId() {
        return this.textureId;
    }

    public int getWidth() {
        return this.width;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public boolean isFull() {
        return this.currentIndex >= this.maxIconCount;
    }

    public XaeroIcon createIcon() {
        if (!isFull()) {
            int offsetX = (this.currentIndex % this.sideIconCount) * this.iconWidth;
            int offsetY = (this.currentIndex / this.sideIconCount) * this.iconWidth;
            this.currentIndex++;
            return new XaeroIcon(this, offsetX, offsetY);
        }
        return null;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/icon/XaeroIconAtlas$Builder.class */
    public static class Builder {
        private int width;
        private int preparedTexture;
        private int iconWidth;

        private Builder() {
        }

        public Builder setDefault() {
            setIconWidth(64);
            return this;
        }

        public Builder setPreparedTexture(int preparedTexture) {
            this.preparedTexture = preparedTexture;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setIconWidth(int iconWidth) {
            this.iconWidth = iconWidth;
            return this;
        }

        private int createGlTexture(int actualWidth) throws OpenGLException {
            int texture = GlStateManager._genTexture();
            OpenGLException.checkGLError();
            if (texture == 0) {
                return 0;
            }
            GlStateManager._bindTexture(texture);
            GL11.glTexParameteri(3553, 33085, 0);
            GL11.glTexParameterf(3553, 33082, 0.0f);
            GL11.glTexParameterf(3553, 33083, 0.0f);
            GL11.glTexParameterf(3553, 34049, 0.0f);
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10242, 33071);
            GL11.glTexParameteri(3553, 10243, 33071);
            GlStateManager._texImage2D(3553, 0, 32856, actualWidth, actualWidth, 0, 32993, 32821, (IntBuffer) null);
            GlStateManager._bindTexture(0);
            OpenGLException.checkGLError();
            return texture;
        }

        public XaeroIconAtlas build() {
            if (this.width == 0 || this.iconWidth <= 0) {
                throw new IllegalStateException();
            }
            if ((this.width / this.iconWidth) * this.iconWidth != this.width) {
                throw new IllegalArgumentException();
            }
            int texture = this.preparedTexture == 0 ? createGlTexture(this.width) : this.preparedTexture;
            if (texture == 0) {
                MinimapLogs.LOGGER.error("Failed to create a GL texture for a new xaero icon atlas!");
                return null;
            }
            return new XaeroIconAtlas(texture, this.width, this.iconWidth);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
