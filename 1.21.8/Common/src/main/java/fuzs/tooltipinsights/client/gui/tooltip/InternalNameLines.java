package fuzs.tooltipinsights.client.gui.tooltip;

import fuzs.tooltipinsights.config.AbstractClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;

import java.util.stream.Stream;

public abstract class InternalNameLines<T> extends TooltipLinesExtractor<T, AbstractClientConfig.TooltipComponents> {

    public InternalNameLines() {
        super(true);
    }

    @Override
    protected boolean isEnabled(AbstractClientConfig.TooltipComponents tooltipComponents) {
        return tooltipComponents.internalName;
    }

    @Override
    protected Stream<Component> getTooltipLines(T t) {
        ResourceKey<?> resourceKey = this.getResourceKey(t);
        return Stream.of(Component.literal(resourceKey.location().toString()).withStyle(ChatFormatting.DARK_GRAY));
    }

    protected abstract ResourceKey<?> getResourceKey(T t);
}
