package xaero.common.graphics;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import java.lang.reflect.Field;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import xaero.hud.minimap.MinimapLogs;
import xaero.lib.common.reflection.util.ReflectionUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/graphics/ImprovedFramebuffer.class */
public class ImprovedFramebuffer extends TextureTarget {
    private static Field MAIN_RENDER_TARGET_FIELD = ReflectionUtils.getFieldReflection(Minecraft.class, "mainRenderTarget", "field_1689", "Lnet/minecraft/class_276;", "f_91042_");
    private static RenderTarget mainRenderTargetBackup;
    private int type;
    public int colorAttachment;
    private int depthAttachment;
    private boolean superConstructorWorks;
    private static boolean optifineChecked;
    private static boolean forceMainFBO;
    private static int forcedMainFBO;
    private static final int GL_FB_INCOMPLETE_ATTACHMENT = 36054;
    private static final int GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
    private static final int GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
    private static final int GL_FB_INCOMPLETE_READ_BUFFER = 36060;

    public ImprovedFramebuffer(int width, int height, boolean useDepthIn) {
        super(width, height, useDepthIn, Minecraft.ON_OSX);
        if (!this.superConstructorWorks) {
            resize(width, height, Minecraft.ON_OSX);
        }
    }

    public static void detectOptifineFBOs() {
        int actualResult = GL11.glGetInteger(36006);
        if (actualResult != Minecraft.getInstance().getMainRenderTarget().frameBufferId) {
            MinimapLogs.LOGGER.info("(Minimap) Detected main FBO: " + actualResult);
            forceMainFBO = true;
            forcedMainFBO = actualResult;
        }
    }

    public void resize(int width, int height, boolean isMac) {
        if (!optifineChecked) {
            detectOptifineFBOs();
            optifineChecked = true;
        }
        this.superConstructorWorks = true;
        GlStateManager._enableDepthTest();
        if (this.frameBufferId >= 0) {
            destroyBuffers();
        }
        createBuffers(width, height, isMac);
        beginWrite(this.type, 36160, 0);
    }

    public void createBuffers(int width, int height, boolean isMac) {
        this.viewWidth = width;
        this.viewHeight = height;
        this.width = width;
        this.height = height;
        this.frameBufferId = genFrameBuffers();
        if (this.frameBufferId == -1) {
            clear(isMac);
            return;
        }
        this.colorTextureId = TextureUtil.generateTextureId();
        if (this.colorTextureId == -1) {
            clear(isMac);
            return;
        }
        if (this.useDepth) {
            this.depthBufferId = genRenderbuffers();
            if (this.depthBufferId == -1) {
                clear(isMac);
                return;
            }
        }
        setFilterMode(9728);
        GlStateManager._bindTexture(this.colorTextureId);
        GlStateManager._texImage2D(3553, 0, 32856, this.width, this.height, 0, 6408, 5121, (IntBuffer) null);
        beginWrite(this.type, 36160, this.frameBufferId);
        framebufferTexture2D(this.type, 36160, 36064, 3553, this.colorTextureId, 0);
        if (this.useDepth) {
            bindRenderbuffer(this.type, 36161, this.depthBufferId);
            renderbufferStorage(this.type, 36161, 33190, this.width, this.height);
            framebufferRenderbuffer(this.type, 36160, 36096, 36161, this.depthBufferId);
        }
        checkStatus();
        clear(isMac);
        unbindRead();
    }

    private int genFrameBuffers() {
        int fbo = GlStateManager.glGenFramebuffers();
        this.type = 0;
        return fbo;
    }

    public int genRenderbuffers() {
        int rbo = -1;
        switch (this.type) {
            case 0:
                rbo = GlStateManager.glGenRenderbuffers();
                break;
        }
        return rbo;
    }

    public void destroyBuffers() {
        unbindRead();
        unbindWrite();
        if (this.depthBufferId > -1) {
            deleteRenderbuffers(this.depthBufferId);
            this.depthBufferId = -1;
        }
        if (this.colorTextureId > -1) {
            TextureUtil.releaseTextureId(this.colorTextureId);
            this.colorTextureId = -1;
        }
        if (this.frameBufferId > -1) {
            beginWrite(this.type, 36160, 0);
            deleteFramebuffers(this.frameBufferId);
            this.frameBufferId = -1;
        }
    }

    private void deleteFramebuffers(int framebufferIn) {
        switch (this.type) {
            case 0:
                GlStateManager._glDeleteFramebuffers(framebufferIn);
                break;
        }
    }

    private void deleteRenderbuffers(int renderbuffer) {
        switch (this.type) {
            case 0:
                GlStateManager._glDeleteRenderbuffers(renderbuffer);
                break;
        }
    }

    public void checkStatus() {
        int i = checkFramebufferStatus(36160);
        if (i != 36053) {
            if (i == GL_FB_INCOMPLETE_ATTACHMENT) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
            }
            if (i == GL_FB_INCOMPLETE_MISS_ATTACH) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
            }
            if (i == GL_FB_INCOMPLETE_DRAW_BUFFER) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
            }
            if (i == GL_FB_INCOMPLETE_READ_BUFFER) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
            }
            throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
        }
    }

    private int checkFramebufferStatus(int target) {
        switch (this.type) {
            case 0:
                return GlStateManager.glCheckFramebufferStatus(target);
            default:
                return -1;
        }
    }

    private static void beginWrite(int type, int target, int framebufferIn) {
        if (framebufferIn == -1) {
            framebufferIn = 0;
        }
        switch (type) {
            case 0:
                GlStateManager._glBindFramebuffer(target, framebufferIn);
                break;
        }
    }

    public void bindDefaultFramebuffer(Minecraft mc) throws IllegalAccessException, IllegalArgumentException {
        restoreMainRenderTarget();
        beginWrite(getType(), 36160, forceMainFBO ? forcedMainFBO : mc.getMainRenderTarget().frameBufferId);
        mc.getMainRenderTarget().bindWrite(false);
    }

    public static void framebufferTexture2D(int type, int target, int attachment, int textarget, int texture, int level) {
        switch (type) {
            case 0:
                GlStateManager._glFramebufferTexture2D(target, attachment, textarget, texture, level);
                break;
        }
    }

    public static void bindRenderbuffer(int type, int target, int renderbuffer) {
        switch (type) {
            case 0:
                GlStateManager._glBindRenderbuffer(target, renderbuffer);
                break;
        }
    }

    public static void renderbufferStorage(int type, int target, int internalFormat, int width, int height) {
        switch (type) {
            case 0:
                GlStateManager._glRenderbufferStorage(target, internalFormat, width, height);
                break;
        }
    }

    public static void framebufferRenderbuffer(int type, int target, int attachment, int renderBufferTarget, int renderBuffer) {
        switch (type) {
            case 0:
                GlStateManager._glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
                break;
        }
    }

    public void bindWrite(boolean p_147610_1_) {
        beginWrite(this.type, 36160, this.frameBufferId);
        if (p_147610_1_) {
            GlStateManager._viewport(0, 0, this.viewWidth, this.viewHeight);
        }
    }

    public void unbindWrite() {
        beginWrite(this.type, 36160, 0);
    }

    public void bindRead() {
        GlStateManager._bindTexture(this.colorTextureId);
        RenderSystem.setShaderTexture(0, this.colorTextureId);
    }

    public void unbindRead() {
        GlStateManager._bindTexture(0);
    }

    public void setFilterMode(int framebufferFilterIn) {
        this.filterMode = framebufferFilterIn;
        GlStateManager._bindTexture(this.colorTextureId);
        GlStateManager._texParameter(3553, 10241, framebufferFilterIn);
        GlStateManager._texParameter(3553, 10240, framebufferFilterIn);
        GlStateManager._texParameter(3553, 10242, 33071);
        GlStateManager._texParameter(3553, 10243, 33071);
        GlStateManager._bindTexture(0);
    }

    public int getFramebufferTexture() {
        return this.colorTextureId;
    }

    public void setFramebufferTexture(int textureId) {
        if (textureId != this.colorTextureId) {
            this.colorTextureId = textureId;
            if (textureId != 0) {
                framebufferTexture2D(this.type, 36160, 36064, 3553, this.colorTextureId, 0);
            }
        }
    }

    public void generateMipmaps() {
        switch (this.type) {
            case 0:
                GL30.glGenerateMipmap(3553);
                break;
        }
    }

    public int getType() {
        return this.type;
    }

    private void forceAsMainRenderTarget() throws IllegalAccessException, IllegalArgumentException {
        if (mainRenderTargetBackup == null) {
            mainRenderTargetBackup = (RenderTarget) ReflectionUtils.getReflectFieldValue(Minecraft.getInstance(), MAIN_RENDER_TARGET_FIELD);
        }
        ReflectionUtils.setReflectFieldValue(Minecraft.getInstance(), MAIN_RENDER_TARGET_FIELD, this);
    }

    public static void restoreMainRenderTarget() throws IllegalAccessException, IllegalArgumentException {
        if (mainRenderTargetBackup != null) {
            ReflectionUtils.setReflectFieldValue(Minecraft.getInstance(), MAIN_RENDER_TARGET_FIELD, mainRenderTargetBackup);
            mainRenderTargetBackup = null;
        }
    }

    public void bindAsMainTarget(boolean viewport) throws IllegalAccessException, IllegalArgumentException {
        bindWrite(viewport);
        forceAsMainRenderTarget();
    }
}
