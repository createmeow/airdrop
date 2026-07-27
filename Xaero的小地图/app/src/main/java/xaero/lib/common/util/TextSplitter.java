package xaero.lib.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/util/TextSplitter.class */
public class TextSplitter {
    public static int splitTextIntoLines(List<Component> dest, int minWidth, int widthLimit, FormattedText formattedText, StringBuilder plainTextBuilder) {
        SplitProgress progress = new SplitProgress();
        int spaceWidth = Minecraft.getInstance().font.width(" ");
        progress.resultWidth = minWidth;
        FormattedText.StyledContentConsumer<Object> consumer = (style, text) -> {
            boolean isEnd = style == null;
            if (!isEnd && plainTextBuilder != null) {
                plainTextBuilder.append(text);
            }
            boolean endsWithSpace = text.endsWith(" ");
            if (endsWithSpace) {
                text = text + ".";
            }
            String[] parts = text.split(" ");
            int i = 0;
            while (i < parts.length) {
                boolean canAddMultiword = isEnd || i < parts.length - 1;
                String part = (isEnd || (endsWithSpace && i == parts.length - 1)) ? "" : parts[i];
                int partWidth = Minecraft.getInstance().font.width(part);
                if (!canAddMultiword) {
                    progress.buildMultiword(part, partWidth, style);
                } else {
                    int wordWidth = partWidth + progress.multiwordWidth;
                    int wordTakesWidth = wordWidth + (!progress.firstWord ? spaceWidth : 0);
                    if (progress.lineWidth + wordTakesWidth <= widthLimit) {
                        progress.resultWidth = Math.max(progress.resultWidth, Math.min(widthLimit, progress.lineWidth + wordTakesWidth));
                    }
                    if (progress.firstWord && progress.lineWidth + wordTakesWidth > progress.resultWidth) {
                        progress.resultWidth = progress.lineWidth + wordTakesWidth;
                    }
                    boolean isNewLine = progress.multiword == null && part.equals("\n");
                    if (!isNewLine && progress.lineWidth + wordTakesWidth <= progress.resultWidth) {
                        progress.confirmWord(part, style, wordTakesWidth);
                    } else {
                        progress.confirmComponent();
                        dest.add(progress.line);
                        progress.nextLine();
                        if (!isNewLine) {
                            i--;
                        }
                    }
                }
                i++;
            }
            return Optional.empty();
        };
        formattedText.visit(consumer, Style.EMPTY.withColor(ChatFormatting.WHITE));
        if (progress.multiword != null) {
            consumer.accept((Style) null, "end");
        } else if (progress.stringBuilder.length() > 0) {
            progress.confirmComponent();
        }
        if (progress.line != null) {
            dest.add(progress.line);
        }
        if (progress.resultWidth > minWidth) {
            progress.resultWidth--;
        }
        return progress.resultWidth;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/util/TextSplitter$SplitProgress.class */
    public static class SplitProgress {
        int multiwordWidth;
        List<MutableComponent> multiword = null;
        boolean firstWord = true;
        Component line = null;
        StringBuilder stringBuilder = new StringBuilder();
        int lineWidth;
        Style lastStyle;
        int resultWidth;

        public void buildMultiword(String wordPart, int width, Style style) {
            MutableComponent wordPartComponent = Component.literal(wordPart).withStyle(style);
            if (this.multiword == null) {
                this.multiword = new ArrayList();
            }
            this.multiword.add(wordPartComponent);
            this.multiwordWidth += width;
        }

        private void confirmWordPart(String part, Style style) {
            if (this.lastStyle != null && !Objects.equals(style, this.lastStyle)) {
                confirmComponent();
            }
            this.stringBuilder.append(part);
            this.lastStyle = style;
        }

        public void confirmWord(String lastPart, Style lastPartStyle, int width) {
            if (!this.firstWord) {
                this.stringBuilder.append(" ");
            }
            if (this.multiword != null) {
                for (Component component : this.multiword) {
                    String text = component.getContents().text();
                    Style style = component.getStyle();
                    confirmWordPart(text, style);
                }
                this.multiword = null;
                this.multiwordWidth = 0;
            }
            confirmWordPart(lastPart, lastPartStyle);
            this.lineWidth += width;
            this.firstWord = false;
        }

        public void confirmComponent() {
            MutableComponent mutableComponentWithStyle = Component.literal(this.stringBuilder.toString()).withStyle(this.lastStyle == null ? Style.EMPTY : this.lastStyle);
            if (this.line != null) {
                if (this.stringBuilder.length() > 0) {
                    this.line.getSiblings().add(mutableComponentWithStyle);
                }
            } else {
                this.line = mutableComponentWithStyle;
            }
            this.stringBuilder.delete(0, this.stringBuilder.length());
        }

        public void nextLine() {
            this.firstWord = true;
            this.line = null;
            this.lastStyle = null;
            this.lineWidth = 0;
        }
    }
}
