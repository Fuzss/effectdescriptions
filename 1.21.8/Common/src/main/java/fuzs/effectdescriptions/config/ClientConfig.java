package fuzs.effectdescriptions.config;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.helper.ComponentHelper;
import fuzs.effectdescriptions.client.helper.TooltipLinesExtractor;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ClientConfig implements ConfigCore {
    static final String EFFECTS_CATEGORY = "effects";
    static final String ENCHANTMENTS_CATEGORY = "enchantments";
    static final String TOOLTIP_DECORATIONS_DESCRIPTION_1 = "Apply a fixed string before every description line.";
    static final String TOOLTIP_DECORATIONS_DESCRIPTION_2 = "This option supports formatting codes which will also apply to the description for setting custom text colors and styles.";
    static final String TOOLTIP_DECORATIONS_DESCRIPTION_3 = "https://minecraft.wiki/w/Formatting_codes";

    @Config(
            name = "item_descriptions",
            category = EFFECTS_CATEGORY,
            description = "Add effect descriptions to item tooltips."
    )
    public ItemDescriptions itemEffectDescriptions = ItemDescriptions.ALWAYS;
    @Config(
            name = "item_descriptions",
            category = ENCHANTMENTS_CATEGORY,
            description = "Add enchantment descriptions to item tooltips."
    )
    public ItemDescriptions itemEnchantmentDescriptions = ItemDescriptions.ALWAYS;
    @Config(name = "description_targets", category = EFFECTS_CATEGORY)
    public final EffectDescriptionTargets effectDescriptionTargets = new EffectDescriptionTargets();
    @Config(name = "description_targets", category = ENCHANTMENTS_CATEGORY)
    public final EnchantmentDescriptionTargets enchantmentDescriptionTargets = new EnchantmentDescriptionTargets();
    @Config(
            category = EFFECTS_CATEGORY,
            description = "Add rich tooltips including effect descriptions to effect widgets in inventory screens."
    )
    public boolean widgetTooltips = true;
    @Config(name = "widget_components", category = EFFECTS_CATEGORY)
    public final EffectTooltipComponents widgetEffectComponents = new EffectTooltipComponents();
    @Config(name = "item_components", category = EFFECTS_CATEGORY)
    public final TooltipComponents itemEffectComponents = new TooltipComponents();
    @Config(name = "item_components", category = ENCHANTMENTS_CATEGORY)
    public final EnchantmentTooltipComponents itemEnchantmentComponents = new EnchantmentTooltipComponents();
    @Config(
            name = "tooltip_decorations", category = EFFECTS_CATEGORY, description = {
            TOOLTIP_DECORATIONS_DESCRIPTION_1, TOOLTIP_DECORATIONS_DESCRIPTION_2, TOOLTIP_DECORATIONS_DESCRIPTION_3
    }
    )
    public String effectTooltipDecorations = ComponentHelper.getAsString(TooltipLinesExtractor.DEFAULT_TOOLTIP_DECORATIONS_COMPONENT);
    @Config(
            name = "tooltip_decorations", category = ENCHANTMENTS_CATEGORY, description = {
            TOOLTIP_DECORATIONS_DESCRIPTION_1, TOOLTIP_DECORATIONS_DESCRIPTION_2, TOOLTIP_DECORATIONS_DESCRIPTION_3
    }
    )
    public String enchantmentTooltipDecorations = ComponentHelper.getAsString(TooltipLinesExtractor.DEFAULT_TOOLTIP_DECORATIONS_COMPONENT);

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
