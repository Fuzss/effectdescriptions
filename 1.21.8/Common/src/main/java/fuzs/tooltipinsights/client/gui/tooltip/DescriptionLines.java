package fuzs.tooltipinsights.client.gui.tooltip;

import fuzs.puzzleslib.api.client.gui.v2.tooltip.ClientComponentSplitter;
import fuzs.puzzleslib.api.util.v1.ComponentHelper;
import fuzs.tooltipinsights.config.AbstractClientConfig;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public abstract class DescriptionLines<T> extends TooltipLinesExtractor<T, AbstractClientConfig.TooltipComponents> {

    public DescriptionLines() {
        super(true);
    }

    @Override
    protected final boolean isEnabled(AbstractClientConfig.TooltipComponents tooltipComponents) {
        return tooltipComponents.valueDescription;
    }

    @Override
    protected final Stream<Component> getTooltipLines(T t) {
        String descriptionKey = this.getDescriptionTranslationKey(this.getDescriptionId(t));
        if (descriptionKey != null) {
            return ClientComponentSplitter.splitTooltipLines(Component.translatable(descriptionKey))
                    .map(ComponentHelper::getAsComponent);
        } else {
            return Stream.empty();
        }
    }

    public static @Nullable String getDescriptionTranslationKey(String translationKey) {
        if (Language.getInstance().has(translationKey + ".desc")) {
            // our own format, similar to Enchantment Descriptions mod format
            return translationKey + ".desc";
        } else if (Language.getInstance().has(translationKey + ".description")) {
            // Just Enough Effect Descriptions mod format
            return translationKey + ".description";
        } else if (Language.getInstance().has("description." + translationKey)) {
            // Potion Descriptions mod format
            return "description." + translationKey;
        } else {
            return null;
        }
    }

    protected abstract String getDescriptionId(T t);
}
