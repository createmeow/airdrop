package xaero.hud.minimap.info.render.compile;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import xaero.hud.minimap.info.InfoDisplay;
import xaero.hud.minimap.module.MinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/render/compile/InfoDisplayCompiler.class */
public final class InfoDisplayCompiler {
    private boolean compiling;
    private int size;
    private List<Component> compiledLines;

    private InfoDisplayCompiler(List<Component> compiledLines) {
        this.compiledLines = compiledLines;
    }

    public <T> List<Component> compile(InfoDisplay<T> infoDisplay, MinimapSession minimapSession, int size, BlockPos playerPos) {
        if (this.compiling) {
            throw new IllegalStateException();
        }
        this.compiling = true;
        this.size = size;
        this.compiledLines.clear();
        infoDisplay.getCompiler().onCompile(infoDisplay, this, minimapSession, size, playerPos);
        this.compiling = false;
        return this.compiledLines;
    }

    public void addWords(String text) {
        if (!this.compiling) {
            throw new IllegalStateException();
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.font.width(text) <= this.size) {
            this.compiledLines.add(Component.literal(text));
            return;
        }
        String[] words = text.split(" ");
        StringBuilder lineBuilder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            int wordStart = lineBuilder.length();
            if (i > 0) {
                lineBuilder.append(' ');
            }
            lineBuilder.append(words[i]);
            if (i != 0) {
                int lineWidth = mc.font.width(lineBuilder.toString());
                if (lineWidth > this.size) {
                    lineBuilder.delete(wordStart, lineBuilder.length());
                    this.compiledLines.add(Component.literal(lineBuilder.toString()));
                    lineBuilder.delete(0, lineBuilder.length());
                    lineBuilder.append(words[i]);
                }
            }
        }
        this.compiledLines.add(Component.literal(lineBuilder.toString()));
    }

    public void addLine(Component line) {
        if (!this.compiling) {
            throw new IllegalStateException();
        }
        this.compiledLines.add(line);
    }

    public void addLine(String line) {
        addLine((Component) Component.literal(line));
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/render/compile/InfoDisplayCompiler$Builder.class */
    public static final class Builder {
        private Builder() {
        }

        private Builder setDefault() {
            return this;
        }

        public InfoDisplayCompiler build() {
            return new InfoDisplayCompiler(new ArrayList());
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
