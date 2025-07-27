package fuzs.effectdescriptions.config;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.util.v1.ComponentHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class ClientConfig implements ConfigCore {
    static final String EFFECTS_CATEGORY = "effects";
    static final String ENCHANTMENTS_CATEGORY = "enchantments";
    static final String FORMATTING_CODES_DESCRIPTION_LINK = "https://minecraft.wiki/w/Formatting_codes";

    @Config(category = EFFECTS_CATEGORY)
    public final EffectWidgetTooltips effectWidgetTooltips = new EffectWidgetTooltips();
    @Config(category = EFFECTS_CATEGORY)
    public final EffectItemTooltips effectItemTooltips = new EffectItemTooltips();
    @Config(category = ENCHANTMENTS_CATEGORY)
    public final EnchantmentItemTooltips enchantmentItemTooltips = new EnchantmentItemTooltips();

    public static abstract class StyledTooltips implements ConfigCore {
        @Config(
                description = {
                        "Apply a fixed string before every description line.",
                        "Supports formatting codes which will also apply to the description for setting custom text colors and styles.",
                        FORMATTING_CODES_DESCRIPTION_LINK
                }
        )
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

    public static class EffectWidgetTooltips extends StyledTooltips {
        @Config(description = "Add rich tooltips including effect descriptions to effect widgets in inventory screens.")
        public boolean widgetTooltips = true;
        @Config
        public final EffectTooltipComponents widgetTooltipLines = new EffectTooltipComponents();
    }

    public static abstract class ItemTooltips extends StyledTooltips {
        @Config(description = "Add descriptions to item tooltips.")
        public ItemDescriptionMode itemDescriptions = ItemDescriptionMode.ALWAYS;
    }

    public static class EffectItemTooltips extends ItemTooltips {
        @Config
        public final EffectDescriptionTargets itemDescriptionTargets = new EffectDescriptionTargets();
        @Config
        public final TooltipComponents itemTooltipLines = new TooltipComponents();
        @Config(description = "Display potion effects for food item tooltips.")
        public boolean foodEffectTooltips = true;
    }

    public static class EnchantmentItemTooltips extends ItemTooltips {
        @Config
        public final EnchantmentDescriptionTargets itemDescriptionTargets = new EnchantmentDescriptionTargets();
        @Config
        public final EnchantmentTooltipComponents itemTooltipLines = new EnchantmentTooltipComponents();
        @Config
        public final EnchantmentTextStyling enchantmentNameStyling = new EnchantmentTextStyling();
    }

    public static class EffectDescriptionTargets implements ConfigCore {
        @Config(description = "Add effect descriptions to potion items, e.g. potion, splash potion, lingering potion, and tipped arrow.")
        public boolean potionContents = true;
        @Config(description = "Add effect descriptions to food items, e.g. rotten flesh and raw chicken.")
        public boolean consumable = true;
        @Config(description = "Add effect descriptions to ominous bottle items.")
        public boolean ominousBottle = true;
        @Config(description = "Add effect descriptions to suspicious stew items.")
        public boolean suspiciousStew = true;
    }

    public static class EnchantmentDescriptionTargets implements ConfigCore {
        @Config(description = "Add enchantment descriptions to enchanted items.")
        public boolean enchantments = true;
        @Config(description = "Add enchantment descriptions to enchanted books.")
        public boolean storedEnchantments = true;
    }

    public static class EnchantmentTextStyling implements ConfigCore {
        @Config(
                description = {
                        "Formatting codes for setting custom text colors and styles for normal enchantments.",
                        FORMATTING_CODES_DESCRIPTION_LINK
                }
        )
        String defaultFormatting = ComponentHelper.getAsString(Style.EMPTY.applyFormat(ChatFormatting.GREEN));
        @Config(
                description = {
                        "Formatting codes for setting custom text colors and styles for treasure enchantments.",
                        FORMATTING_CODES_DESCRIPTION_LINK
                }
        )
        String treasureFormatting = ComponentHelper.getAsString(Style.EMPTY.applyFormat(ChatFormatting.GOLD));
        @Config(
                description = {
                        "Formatting codes for setting custom text colors and styles for curses.",
                        FORMATTING_CODES_DESCRIPTION_LINK
                }
        )
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

    public static class TooltipComponents implements ConfigCore {
        @Config(
                description = "Add the description to tooltips."
        )
        public boolean valueDescription = true;
        @Config(
                description = "Add the name of the source mod to tooltips."
        )
        public boolean modName = false;
        @Config(
                description = "Add the internal id to tooltips."
        )
        public boolean internalName = false;
    }

    public static class EffectTooltipComponents extends TooltipComponents {
        @Config(
                description = "Add the effect name and duration to tooltips."
        )
        public boolean displayName = true;
        @Config(
                description = "Add attributes granted by an effect to tooltips."
        )
        public boolean effectAttributes = true;
    }

    public static class EnchantmentTooltipComponents extends TooltipComponents {
        @Config(
                description = "Add the maximum enchantment level as part of the name to tooltips."
        )
        public boolean maximumLevel = true;
        @Config(
                description = "Add tags for primary and other supported items to tooltips."
        )
        public boolean compatibleItems = true;

        public boolean maximumLevel() {
            return this.maximumLevel
                    && EffectDescriptions.CONFIG.get(ClientConfig.class).enchantmentItemTooltips.itemDescriptions.isActive();
        }
    }
}
