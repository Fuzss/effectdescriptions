package fuzs.tooltipinsights.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.util.v1.ComponentHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public abstract class AbstractClientConfig implements ConfigCore {
    protected static final String FORMATTING_CODES_DESCRIPTION_LINK = "https://minecraft.wiki/w/Formatting_codes";

    public static abstract class StyledTooltips implements ConfigCore {
        @Config(description = {
                "Apply a fixed string before every description line.",
                "Supports formatting codes which will also apply to the description for setting custom text colors and styles.",
                FORMATTING_CODES_DESCRIPTION_LINK
        })
        String tooltipLineDecorations = ComponentHelper.getAsString(Component.literal(" \u25C6 ")
                .withStyle(ChatFormatting.GRAY));

        public Component decorationComponent;
        public Style decorationStyle;

        @Override
        public void afterConfigReload() {
            this.decorationComponent = ComponentHelper.getAsComponent(this.tooltipLineDecorations);
            this.decorationStyle = ComponentHelper.getDefaultStyle(this.tooltipLineDecorations);
        }
    }

    public static abstract class ItemTooltips extends StyledTooltips {
        @Config(description = "Add descriptions to item tooltips.")
        public ItemDescriptionMode itemDescriptions = ItemDescriptionMode.ALWAYS;
    }

    public static class TooltipComponents implements ConfigCore {
        @Config(description = "Add the description to tooltips.")
        public boolean valueDescription = true;
        @Config(description = "Add the name of the source mod to tooltips.")
        public boolean modName = false;
        @Config(description = "Add the internal id to tooltips.")
        public boolean internalName = false;
    }
}
