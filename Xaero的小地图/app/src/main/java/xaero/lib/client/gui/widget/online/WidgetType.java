package xaero.lib.client.gui.widget.online;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.commons.codec.binary.Hex;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import xaero.hud.io.HudIO;
import xaero.lib.XaeroLib;
import xaero.lib.client.gui.util.graphics.GuiGraphicsUtils;
import xaero.lib.client.gui.widget.online.Alignment;
import xaero.lib.client.gui.widget.online.ButtonWidget;
import xaero.lib.client.gui.widget.online.ButtonWidgetBuilder;
import xaero.lib.client.gui.widget.online.ImageWidget;
import xaero.lib.client.gui.widget.online.ImageWidgetBuilder;
import xaero.lib.client.gui.widget.online.TextWidget;
import xaero.lib.client.gui.widget.online.TextWidgetBuilder;
import xaero.lib.client.gui.widget.online.Widget;
import xaero.lib.client.gui.widget.online.WidgetScreen;
import xaero.lib.client.gui.widget.online.init.WidgetInitializer;
import xaero.lib.client.gui.widget.online.loader.ScalableWidgetLoader;
import xaero.lib.client.gui.widget.online.loader.WidgetLoader;
import xaero.lib.client.gui.widget.online.render.ScalableWidgetRenderer;
import xaero.lib.client.gui.widget.online.render.WidgetRenderer;
import xaero.lib.common.util.InternetUtils;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/WidgetType.class */
public enum WidgetType {
    IMAGE(new ScalableWidgetLoader() { // from class: xaero.lib.client.gui.widget.online.loader.ImageWidgetLoader
        /* JADX WARN: Finally extract failed */
        @Override // xaero.lib.client.gui.widget.online.loader.WidgetLoader
        public Widget load(Map<String, String> parsedArgs) throws NoSuchAlgorithmException, IOException {
            ImageWidgetBuilder builder = new ImageWidgetBuilder();
            commonLoad(builder, parsedArgs);
            String image = parsedArgs.get("image");
            String image_url = parsedArgs.get("image_url");
            int textureId = 0;
            if (image != null) {
                if (!image.replaceAll("[^a-zA-Z0-9_]+", "").equals(image)) {
                    XaeroLib.LOGGER.info("Invalid widget image id!");
                    return null;
                }
                String image_md5 = parsedArgs.get("image_md5");
                if (image_md5 == null) {
                    XaeroLib.LOGGER.info("No image md5.");
                    RenderSystem.bindTexture(0);
                    return null;
                }
                try {
                    MessageDigest digestMD5 = MessageDigest.getInstance("MD5");
                    builder.setImageId(image);
                    textureId = GL11.glGenTextures();
                    if (textureId <= 0) {
                        return null;
                    }
                    builder.setGlTexture(textureId);
                    RenderSystem.bindTexture(textureId);
                    String tex_base_level = parsedArgs.get("tex_base_level");
                    String tex_max_level = parsedArgs.get("tex_max_level");
                    String tex_min_lod = parsedArgs.get("tex_min_lod");
                    String tex_max_lod = parsedArgs.get("tex_max_lod");
                    String tex_lod_bias = parsedArgs.get("tex_lod_bias");
                    String tex_mag_filter = parsedArgs.get("tex_mag_filter");
                    String tex_min_filter = parsedArgs.get("tex_min_filter");
                    String tex_wrap_s = parsedArgs.get("tex_wrap_s");
                    String tex_wrap_t = parsedArgs.get("tex_wrap_t");
                    GL11.glTexParameteri(3553, 33084, tex_base_level != null ? Integer.parseInt(tex_base_level) : 0);
                    GL11.glTexParameteri(3553, 33085, tex_max_level != null ? Integer.parseInt(tex_max_level) : 1);
                    GL11.glTexParameterf(3553, 33082, tex_min_lod != null ? Float.parseFloat(tex_min_lod) : 0.0f);
                    GL11.glTexParameterf(3553, 33083, tex_max_lod != null ? Float.parseFloat(tex_max_lod) : 1.0f);
                    GL11.glTexParameterf(3553, 34049, tex_lod_bias != null ? Float.parseFloat(tex_lod_bias) : 0.0f);
                    GL11.glTexParameteri(3553, 10240, tex_mag_filter != null ? Integer.parseInt(tex_mag_filter) : 9728);
                    GL11.glTexParameteri(3553, 10241, tex_min_filter != null ? Integer.parseInt(tex_min_filter) : 9729);
                    GL11.glTexParameteri(3553, 10242, tex_wrap_s != null ? Integer.parseInt(tex_wrap_s) : 33071);
                    GL11.glTexParameteri(3553, 10243, tex_wrap_t != null ? Integer.parseInt(tex_wrap_t) : 33071);
                    File cacheFolder = Services.PLATFORM.getGameDir().resolve("XaeroCache").toFile();
                    Path cacheFolderPath = cacheFolder.toPath();
                    if (!Files.exists(cacheFolderPath, new LinkOption[0])) {
                        Files.createDirectories(cacheFolderPath, new FileAttribute[0]);
                    }
                    ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath("xaerobetterpvp", "gui/" + image + ".png");
                    InputStream inputStream = null;
                    DigestInputStream digestStream = null;
                    try {
                        try {
                            Resource resource = (Resource) Minecraft.getInstance().getResourceManager().getResource(resourceLocation).get();
                            inputStream = resource.open();
                        } catch (NoSuchElementException e) {
                            XaeroLib.LOGGER.info("Widget image not included in jar. Checking cache...");
                            Path cacheFilePath = cacheFolderPath.resolve(image + ".cache");
                            if (!Files.exists(cacheFilePath, new LinkOption[0])) {
                                XaeroLib.LOGGER.info("Widget image not in cache. Downloading...");
                                if (image_url == null) {
                                    XaeroLib.LOGGER.info("No image URL.");
                                    RenderSystem.bindTexture(0);
                                    GL11.glDeleteTextures(textureId);
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                    if (0 != 0) {
                                        digestStream.close();
                                    }
                                    return null;
                                }
                                URL url = new URL(image_url);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setReadTimeout(900);
                                conn.setConnectTimeout(900);
                                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
                                if (conn.getContentLengthLong() > 5242880) {
                                    throw new IOException("Image too big to trust!");
                                }
                                InputStream input = null;
                                BufferedOutputStream output = null;
                                try {
                                    input = conn.getInputStream();
                                    output = new BufferedOutputStream(new FileOutputStream(cacheFilePath.toFile()));
                                    InternetUtils.download(output, input);
                                    if (input != null) {
                                        input.close();
                                    }
                                    if (output != null) {
                                        output.close();
                                    }
                                } catch (Throwable th) {
                                    if (input != null) {
                                        input.close();
                                    }
                                    if (output != null) {
                                        output.close();
                                    }
                                    throw th;
                                }
                            }
                            inputStream = new FileInputStream(cacheFilePath.toFile());
                        }
                        InputStream inputStream2 = new BufferedInputStream(inputStream);
                        DigestInputStream digestStream2 = new DigestInputStream(inputStream2, digestMD5);
                        BufferedImage bufferedImage = ImageIO.read(digestStream2);
                        while (digestStream2.read() != -1) {
                        }
                        byte[] digest = digestMD5.digest();
                        String fileMD5 = Hex.encodeHexString(digest);
                        if (!image_md5.equals(fileMD5)) {
                            XaeroLib.LOGGER.info("Invalid image MD5: " + fileMD5);
                            bufferedImage.flush();
                            bufferedImage = null;
                        }
                        if (inputStream2 != null) {
                            inputStream2.close();
                        }
                        if (digestStream2 != null) {
                            digestStream2.close();
                        }
                        if (bufferedImage == null) {
                            RenderSystem.bindTexture(0);
                            GL11.glDeleteTextures(textureId);
                            return null;
                        }
                        int imageW = bufferedImage.getWidth();
                        int imageH = bufferedImage.getHeight();
                        ByteBuffer buffer = BufferUtils.createByteBuffer(imageW * imageH * 4);
                        for (int y = 0; y < imageH; y++) {
                            for (int x = 0; x < imageW; x++) {
                                int color = bufferedImage.getRGB(x, y);
                                buffer.putInt(color);
                            }
                        }
                        buffer.flip();
                        bufferedImage.flush();
                        GL11.glTexImage2D(3553, 0, 6408, imageW, imageH, 0, 32993, 33639, buffer);
                        GL30.glGenerateMipmap(3553);
                        RenderSystem.bindTexture(0);
                        builder.setImageW(imageW);
                        builder.setImageH(imageH);
                    } catch (Throwable th2) {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (0 != 0) {
                            digestStream.close();
                        }
                        throw th2;
                    }
                } catch (NoSuchAlgorithmException e2) {
                    XaeroLib.LOGGER.info("No algorithm for MD5.");
                    RenderSystem.bindTexture(0);
                    return null;
                }
            }
            if (builder.validate()) {
                return builder.build();
            }
            if (textureId > 0) {
                GL11.glDeleteTextures(textureId);
                return null;
            }
            return null;
        }
    }, new ScalableWidgetRenderer<ImageWidget>() { // from class: xaero.lib.client.gui.widget.online.render.ImageWidgetRenderer
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.lib.client.gui.widget.online.render.ScalableWidgetRenderer
        public void renderScaled(GuiGraphics guiGraphics, int width, int height, int mouseX, int mouseY, double guiScale, ImageWidget widget) {
            guiGraphics.flush();
            RenderSystem.setShaderTexture(0, widget.getGlTexture());
            RenderSystem.enableBlend();
            GuiGraphicsUtils.blit(guiGraphics.pose(), 0, 0, 0, 0.0f, 0.0f, widget.getW(), widget.getH(), widget.getW(), widget.getH());
        }
    }, null),
    BUTTON(new WidgetLoader() { // from class: xaero.lib.client.gui.widget.online.loader.ButtonWidgetLoader
        @Override // xaero.lib.client.gui.widget.online.loader.WidgetLoader
        public Widget load(Map<String, String> parsedArgs) {
            ButtonWidgetBuilder builder = new ButtonWidgetBuilder();
            commonLoad(builder, parsedArgs);
            String button_text = parsedArgs.get("button_text");
            String button_w = parsedArgs.get("button_w");
            String button_h = parsedArgs.get("button_h");
            if (button_text != null) {
                builder.setButtonText(button_text.replace("%semi%", HudIO.SEPARATOR));
            }
            if (button_w != null) {
                builder.setButtonW(Integer.parseInt(button_w));
            }
            if (button_h != null) {
                builder.setButtonH(Integer.parseInt(button_h));
            }
            if (builder.validate()) {
                return builder.build();
            }
            return null;
        }
    }, null, new WidgetInitializer() { // from class: xaero.lib.client.gui.widget.online.init.ButtonWidgetInitializer
        @Override // xaero.lib.client.gui.widget.online.init.WidgetInitializer
        public void init(WidgetScreen screen, int width, int height, Widget widget) {
            ButtonWidget buttonWidget = (ButtonWidget) widget;
            screen.addButtonVisible(Button.builder(Component.literal(buttonWidget.getButtonText()), b -> {
                widget.getOnClick().clickHandler.onClick(toScreen(screen), widget);
            }).bounds(widget.getX(width), widget.getY(height), buttonWidget.getW(), buttonWidget.getH()).build());
        }

        private Screen toScreen(WidgetScreen screen) {
            Screen result = screen.getScreen();
            if (result == screen) {
                return result;
            }
            throw new RuntimeException("Incorrect usage of " + String.valueOf(getClass()));
        }
    }),
    TEXT(new ScalableWidgetLoader() { // from class: xaero.lib.client.gui.widget.online.loader.TextWidgetLoader
        @Override // xaero.lib.client.gui.widget.online.loader.WidgetLoader
        public Widget load(Map<String, String> parsedArgs) throws IOException {
            TextWidgetBuilder builder = new TextWidgetBuilder();
            commonLoad(builder, parsedArgs);
            String text = parsedArgs.get("text");
            String alignment = parsedArgs.get("alignment");
            if (text != null) {
                builder.setText(text.replace("%semi%", HudIO.SEPARATOR));
            }
            if (alignment != null) {
                builder.setAlignment(Alignment.valueOf(alignment));
            }
            if (builder.validate()) {
                return builder.build();
            }
            return null;
        }
    }, new ScalableWidgetRenderer<TextWidget>() { // from class: xaero.lib.client.gui.widget.online.render.TextWidgetRenderer
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.lib.client.gui.widget.online.render.ScalableWidgetRenderer
        public void renderScaled(GuiGraphics guiGraphics, int width, int height, int mouseX, int mouseY, double guiScale, TextWidget widget) {
            guiGraphics.drawString(Minecraft.getInstance().font, widget.getText(), 0, 0, 16777215);
        }
    }, null);

    public final WidgetLoader widgetLoader;
    public final WidgetRenderer widgetRenderer;
    public final WidgetInitializer widgetInit;

    WidgetType(WidgetLoader widgetLoader, WidgetRenderer widgetRenderer, WidgetInitializer widgetInit) {
        this.widgetLoader = widgetLoader;
        this.widgetRenderer = widgetRenderer;
        this.widgetInit = widgetInit;
    }
}
