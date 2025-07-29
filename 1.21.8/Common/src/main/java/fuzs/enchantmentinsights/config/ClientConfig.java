package fuzs.enchantmentinsights.config;

import fuzs.enchantmentinsights.EnchantmentInsights;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.util.v1.ComponentHelper;
import fuzs.tooltipinsights.config.AbstractClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;

public class ClientConfig extends AbstractClientConfig {
    @Config
    public final EnchantmentItemTooltips enchantmentItemTooltips = new EnchantmentItemTooltips();

    public static class EnchantmentItemTooltips extends ItemTooltips {
        @Config
        public final EnchantmentDescriptionTargets itemDescriptionTargets = new EnchantmentDescriptionTargets();
        @Config
        public final EnchantmentTooltipComponents itemTooltipLines = new EnchantmentTooltipComponents();
        @Config
        public final EnchantmentTextStyling enchantmentNameStyling = new EnchantmentTextStyling();
    }

    public static class EnchantmentDescriptionTargets implements ConfigCore {
        @Config(description = "Add enchantment descriptions to enchanted items.")
        public boolean enchantments = true;
        @Config(description = "Add enchantment descriptions to enchanted books.")
        public boolean storedEnchantments = true;
    }

    public static class EnchantmentTextStyling implements ConfigCore {
        @Config(description = {
                "Formatting codes for setting custom text colors and styles for normal enchantments.",
                FORMATTING_CODES_DESCRIPTION_LINK
        })
        String defaultFormatting = ComponentHelper.getAsString(Style.EMPTY.applyFormat(ChatFormatting.GREEN));
        @Config(description = {
                "Formatting codes for setting custom text colors and styles for treasure enchantments.",
                FORMATTING_CODES_DESCRIPTION_LINK
        })
        String treasureFormatting = ComponentHelper.getAsString(Style.EMPTY.applyFormat(ChatFormatting.GOLD));
        @Config(description = {
                "Formatting codes for setting custom text colors and styles for curses.",
                FORMATTING_CODES_DESCRIPTION_LINK
        })
        String curseFormatting = ComponentHelper.getAsString(Style.EMPTY.applyFormat(ChatFormatting.RED));

        public Style defaultStyle;
        public Style treasureStyle;
        public Style curseStyle;

        @Override
        public void afterConfigReload() {
            this.defaultStyle = ComponentHelper.getDefaultStyle(this.defaultFormatting);
            this.treasureStyle = ComponentHelper.getDefaultStyle(this.treasureFormatting);
            this.curseStyle = ComponentHelper.getDefaultStyle(this.curseFormatting);
        }
    }

    public static class EnchantmentTooltipComponents extends TooltipComponents {
        @Config(description = "Add the maximum enchantment level as part of the name to tooltips.")
        public boolean maximumLevel = true;
        @Config(description = "Add tags for primary and other supported items to tooltips.")
        public boolean compatibleItems = true;

        public boolean maximumLevel() {
            return this.maximumLevel
                    && EnchantmentInsights.CONFIG.get(ClientConfig.class).enchantmentItemTooltips.itemDescriptions.isActive();
        }
    }
}
