package xaero.lib.client.gui.widget;

import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import xaero.common.minimap.write.MinimapWriter;
import xaero.lib.common.gui.widget.TooltipInfo;
import xaero.lib.common.util.TextSplitter;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/Tooltip.class */
public class Tooltip implements Supplier<Tooltip> {
    private static final int BOX_OFFSET_X = 12;
    private static final int BOX_OFFSET_Y = 10;
    private static final int START_WIDTH = 20;
    private static final int USUAL_WIDTH = 200;
    private ArrayList<Component> strings;
    private Component directText;
    private boolean directTextReady;
    private String language;
    private String fullCode;
    private Style codeStyle;
    private String plainText;
    private int boxWidth;
    private int startWidth;
    private static final int color = -939524096;
    private boolean customLines;
    private boolean flippedByDefault;
    private boolean autoLinebreak;

    public Tooltip(String code) {
        this(code, Style.EMPTY);
    }

    public Tooltip(String code, Style codeStyle) {
        this(code, codeStyle, false);
    }

    public Tooltip(String code, Style codeStyle, boolean flippedByDefault) {
        this.boxWidth = START_WIDTH;
        this.startWidth = START_WIDTH;
        this.fullCode = code;
        this.codeStyle = codeStyle;
        this.flippedByDefault = flippedByDefault;
        this.autoLinebreak = true;
    }

    public Tooltip(Component directText) {
        this(directText, false);
    }

    public Tooltip(Component directText, boolean flippedByDefault) {
        this.boxWidth = START_WIDTH;
        this.startWidth = START_WIDTH;
        this.directText = directText;
        this.flippedByDefault = flippedByDefault;
        this.autoLinebreak = true;
    }

    public Tooltip(int size) {
        this.boxWidth = START_WIDTH;
        this.startWidth = START_WIDTH;
        this.strings = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.strings.add(Component.literal(""));
        }
        this.customLines = true;
    }

    public Tooltip(TooltipInfo info) {
        this(info.text, info.flippedByDefault);
        this.autoLinebreak = info.autoLineBreak;
    }

    public void setStartWidth(int startWidth) {
        this.startWidth = startWidth;
    }

    private String currentLanguage() {
        return Minecraft.getInstance().getLanguageManager().getSelected();
    }

    public void createLines(Component text) {
        try {
            this.language = currentLanguage();
        } catch (NullPointerException e) {
            this.language = "en_us";
        }
        this.strings = new ArrayList<>();
        splitWords(this.strings, text);
    }

    public void splitWords(ArrayList<Component> dest, FormattedText formattedText) {
        StringBuilder plainTextBuilder = new StringBuilder();
        this.boxWidth = START_WIDTH + TextSplitter.splitTextIntoLines(dest, this.startWidth - START_WIDTH, (this.autoLinebreak ? USUAL_WIDTH : MinimapWriter.NO_Y_VALUE) - START_WIDTH, formattedText, plainTextBuilder);
        this.plainText = plainTextBuilder.toString().replaceAll("(§[0-9a-g])+", "");
    }

    public Component getLine(int line) {
        return this.strings.get(line);
    }

    private void ensure() {
        try {
            if (!this.customLines && ((this.fullCode == null && !this.directTextReady) || this.language == null || !this.language.equals(currentLanguage()))) {
                if (this.fullCode != null) {
                    createLines(Component.translatable(this.fullCode).withStyle(this.codeStyle));
                } else {
                    createLines(this.directText);
                    this.directTextReady = true;
                }
            }
        } catch (Exception e) {
        }
    }

    public void drawBox(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        ensure();
        int drawX = x + BOX_OFFSET_X;
        int drawY = y + 10;
        int overEdgeX = (drawX + this.boxWidth) - width;
        if (this.flippedByDefault || overEdgeX > 9) {
            drawX = (x - BOX_OFFSET_X) - this.boxWidth;
        } else if (overEdgeX > 0) {
            drawX -= overEdgeX;
        }
        if (drawX < 0) {
            drawX = 0;
        }
        int h = 5 + (this.strings.size() * 10) + 5;
        int overEdgeY = (drawY + h) - height;
        if (overEdgeY > h / 2) {
            drawY = (y - 10) - h;
        } else if (overEdgeY > 0) {
            drawY -= overEdgeY;
        }
        if (drawY < 0) {
            drawY = 0;
        }
        guiGraphics.fill(drawX, drawY, drawX + this.boxWidth, drawY + h, -939524096);
        for (int i = 0; i < this.strings.size(); i++) {
            Component s = getLine(i);
            guiGraphics.drawString(Minecraft.getInstance().font, s, drawX + 10, drawY + 6 + (10 * i), 16777215);
        }
    }

    public Tooltip withWidth(int boxWidth) {
        this.boxWidth = boxWidth;
        return this;
    }

    public void setAutoLinebreak(boolean autoLinebreak) {
        this.autoLinebreak = autoLinebreak;
    }

    public String getPlainText() {
        ensure();
        return this.plainText;
    }

    public String getFullCode() {
        return this.fullCode;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.function.Supplier
    public Tooltip get() {
        return this;
    }
}
