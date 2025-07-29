package fuzs.tooltipinsights.client.gui.tooltip;

import fuzs.tooltipinsights.config.AbstractClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class TooltipLinesExtractor<T, C extends AbstractClientConfig.TooltipComponents> {
    private final boolean supportsDecorations;

    public TooltipLinesExtractor(boolean supportsDecorations) {
        this.supportsDecorations = supportsDecorations;
    }

    public static <T, C extends AbstractClientConfig.TooltipComponents> List<Component> getTooltipLines(List<TooltipLinesExtractor<T, C>> extractorList, Component decorationComponent, Style style, T t, C tooltipComponents) {
        // copy text only, without any style; also setting colour to black will make it barely visible on a tooltip background which we want
        // alternatively create an empty component consisting of spaces with the same width,
        // but that usually does not perfectly match up, so stick with the dying approach for now
        Component indentComponent = Component.literal(decorationComponent.getString()).withStyle(ChatFormatting.BLACK);
        MutableBoolean mutableBoolean = new MutableBoolean(true);
        List<Component> tooltipLines = new ArrayList<>();

        for (TooltipLinesExtractor<T, C> extractor : extractorList) {
            List<Component> list = extractor.getTooltipLines(tooltipComponents, t).toList();

            if (extractor.supportsDecorations) {
                for (Component tooltipLine : list) {
                    Component component;

                    if (mutableBoolean.isTrue()) {
                        mutableBoolean.setFalse();
                        component = decorationComponent;
                    } else {
                        component = indentComponent;
                    }

                    tooltipLines.add(Component.empty().append(component).append(tooltipLine).withStyle(style));
                }
            } else {
                tooltipLines.addAll(list);
            }
        }

        return tooltipLines;
    }

    protected abstract boolean isEnabled(C tooltipComponents);

    protected abstract Stream<Component> getTooltipLines(T t);

    private Stream<Component> getTooltipLines(C tooltipComponents, T t) {
        if (this.isEnabled(tooltipComponents)) {
            return this.getTooltipLines(t);
        } else {
            return Stream.empty();
        }
    }

    public <E extends TooltipLinesExtractor<?, ?>> E cast() {
        return (E) this;
    }
}
