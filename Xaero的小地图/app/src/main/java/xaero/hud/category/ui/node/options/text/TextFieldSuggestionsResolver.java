package xaero.hud.category.ui.node.options.text;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import xaero.common.misc.ListFactory;
import xaero.hud.category.ui.node.options.EditorOptionNode;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/text/TextFieldSuggestionsResolver.class */
public final class TextFieldSuggestionsResolver {
    private ListFactory listFactory;

    private TextFieldSuggestionsResolver(@Nonnull ListFactory listFactory) {
        this.listFactory = listFactory;
    }

    public List<EditorOptionNode<String>> getSuggestions(String input, List<EditorOptionNode<String>> allOptions) {
        if (input.isEmpty()) {
            return this.listFactory.get();
        }
        String lowerCaseInput = input.toLowerCase();
        Stream<EditorOptionNode<String>> streamLimit = allOptions.stream().filter(o -> {
            return o.getValue() != null && ((String) o.getValue()).toString().toLowerCase().contains(lowerCaseInput);
        }).sorted((o1, o2) -> {
            boolean firstStarts = ((String) o1.getValue()).toString().toLowerCase().startsWith(lowerCaseInput);
            boolean secondStarts = ((String) o2.getValue()).toString().toLowerCase().startsWith(lowerCaseInput);
            if (firstStarts == secondStarts) {
                return 0;
            }
            return firstStarts ? -1 : 1;
        }).limit(100L);
        ListFactory listFactory = this.listFactory;
        Objects.requireNonNull(listFactory);
        List<EditorOptionNode<String>> result = (List) streamLimit.collect(listFactory::get, (v0, v1) -> {
            v0.add(v1);
        }, (v0, v1) -> {
            v0.addAll(v1);
        });
        return result;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/text/TextFieldSuggestionsResolver$Builder.class */
    public static final class Builder {
        private final ListFactory listFactory;

        private Builder(ListFactory listFactory) {
            this.listFactory = listFactory;
        }

        public Builder setDefault() {
            return this;
        }

        public TextFieldSuggestionsResolver build() {
            if (this.listFactory == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return new TextFieldSuggestionsResolver(this.listFactory);
        }

        public static Builder begin(ListFactory listFactory) {
            return new Builder(listFactory).setDefault();
        }
    }
}
