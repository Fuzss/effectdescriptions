package fuzs.effectdescriptions.config;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.util.v1.ComponentHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class ClientConfig implements ConfigCore {
    static final Component DEFAULT_TOOLTIP_DECORATIONS_COMPONENT = Component.literal(" \u25C6 ")
            .withStyle(ChatFormatting.GRAY);
    static final String EFFECTS_CATEGORY = "effects";
    static final String ITEMS_CATEGORY = "items";
    static final String WIDGETS_CATEGORY = "widgets";
    static final String ENCHANTMENTS_CATEGORY = "enchantments";
    static final String TOOLTIP_DECORATIONS_DESCRIPTION_1 = "Apply a fixed string before every description line.";
    static final String TOOLTIP_DECORATIONS_DESCRIPTION_2 = "Supports formatting codes which will also apply to the description for setting custom text colors and styles.";
    static final String FORMATTING_CODES_DESCRIPTION_LINK = "https://minecraft.wiki/w/Formatting_codes";

    @Config(
            name = "item_descriptions",
            category = {EFFECTS_CATEGORY, ITEMS_CATEGORY},
            description = "Add effect descriptions to item tooltips."
    )
    public ItemDescriptionMode itemEffectDescriptions = ItemDescriptionMode.ALWAYS;
    @Config(
            name = "item_descriptions",
            category = ENCHANTMENTS_CATEGORY,
            description = "Add enchantment descriptions to item tooltips."
    )
    public ItemDescriptionMode itemEnchantmentDescriptions = ItemDescriptionMode.ALWAYS;
    @Config(name = "item_description_targets", category = {EFFECTS_CATEGORY, ITEMS_CATEGORY})
    public final EffectDescriptionTargets effectDescriptionTargets = new EffectDescriptionTargets();
    @Config(name = "item_description_targets", category = ENCHANTMENTS_CATEGORY)
    public final EnchantmentDescriptionTargets enchantmentDescriptionTargets = new EnchantmentDescriptionTargets();
    @Config(
            category = {EFFECTS_CATEGORY, WIDGETS_CATEGORY},
            description = "Add rich tooltips including effect descriptions to effect widgets in inventory screens."
    )
    public boolean widgetTooltips = true;
    @Config(
            category = {EFFECTS_CATEGORY, ITEMS_CATEGORY},
            description = "Display potion effects for food item tooltips."
    )
    public boolean foodEffectTooltips = true;
    @Config(name = "widget_tooltip_lines", category = {EFFECTS_CATEGORY, WIDGETS_CATEGORY})
    public final EffectTooltipComponents widgetEffectComponents = new EffectTooltipComponents();
    @Config(name = "item_tooltip_lines", category = {EFFECTS_CATEGORY, ITEMS_CATEGORY})
    public final TooltipComponents itemEffectComponents = new TooltipComponents();
    @Config(name = "item_tooltip_lines", category = ENCHANTMENTS_CATEGORY)
    public final EnchantmentTooltipComponents itemEnchantmentComponents = new EnchantmentTooltipComponents();
    @Config(
            name = "tooltip_line_decorations", category = EFFECTS_CATEGORY, description = {
            TOOLTIP_DECORATIONS_DESCRIPTION_1, TOOLTIP_DECORATIONS_DESCRIPTION_2, FORMATTING_CODES_DESCRIPTION_LINK
    }
    )
    String effectTooltipDecorations = ComponentHelper.getAsString(DEFAULT_TOOLTIP_DECORATIONS_COMPONENT);
    @Config(
            name = "tooltip_line_decorations", category = ENCHANTMENTS_CATEGORY, description = {
            TOOLTIP_DECORATIONS_DESCRIPTION_1, TOOLTIP_DECORATIONS_DESCRIPTION_2, FORMATTING_CODES_DESCRIPTION_LINK
    }
    )
    String enchantmentTooltipDecorations = ComponentHelper.getAsString(DEFAULT_TOOLTIP_DECORATIONS_COMPONENT);
    @Config(name = "enchantment_name_styling", category = ENCHANTMENTS_CATEGORY)
    public final EnchantmentTextStyling enchantmentTextStyling = new EnchantmentTextStyling();

    public Component effectDecorationComponent;
    public Style effectDecorationStyle;
    public Component enchantmentDecorationComponent;
    public Style enchantmentDecorationStyle;

    @Override
    public void afterConfigReload() {
        this.effectDecorationComponent = ComponentHelper.getAsComponent(this.effectTooltipDecorations);
        this.effectDecorationStyle = ComponentHelper.getDefaultStyle(this.effectTooltipDecorations);
        this.enchantmentDecorationComponent = ComponentHelper.getAsComponent(this.enchantmentTooltipDecorations);
        this.enchantmentDecorationStyle = ComponentHelper.getDefaultStyle(this.enchantmentTooltipDecorations);
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
                    && EffectDescriptions.CONFIG.get(ClientConfig.class).itemEnchantmentDescriptions.isActive();
        }
    }
}
