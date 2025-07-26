package fuzs.effectdescriptions.config;

import fuzs.effectdescriptions.client.helper.ComponentHelper;
import fuzs.effectdescriptions.client.helper.EffectTooltipSuppliers;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ClientConfig implements ConfigCore {
    @Config(
            description = "Add effect descriptions to item tooltips."
    )
    public ItemEffectDescription itemDescriptions = ItemEffectDescription.ALWAYS;
    @Config
    public final ItemDescriptionTargets itemDescriptionTargets = new ItemDescriptionTargets();
    @Config(
            description = "Add rich tooltips including effect descriptions to effect widgets in inventory screens."
    )
    public boolean widgetTooltips = true;
    @Config
    public final WidgetTooltipComponents widgetTooltipComponents = new WidgetTooltipComponents();
    @Config(
            description = {
                    "Apply a fixed string before every effect description line.",
                    "This option supports formatting codes which will also apply to the description for setting custom text colors and styles.",
                    "https://minecraft.wiki/w/Formatting_codes"
            }
    )
    public String descriptionDecorator = ComponentHelper.getAsString(EffectTooltipSuppliers.DEFAULT_DESCRIPTION_DECORATOR_COMPONENT);

    public static class ItemDescriptionTargets implements ConfigCore {
        @Config(description = "Add effect descriptions to potion items, e.g. potion, splash potion, lingering potion, and tipped arrow.")
        public boolean potionContents = true;
        @Config(description = "Add effect descriptions to food items, e.g. rotten flesh and raw chicken.")
        public boolean consumable = true;
        @Config(description = "Add effect descriptions to ominous bottle items.")
        public boolean ominousBottle = true;
        @Config(description = "Add effect descriptions to suspicious stew items.")
        public boolean suspiciousStew = true;
    }

    public static class WidgetTooltipComponents implements ConfigCore {
        @Config(
                description = "Add the effect name and duration to effect widget tooltips."
        )
        public boolean effectName = true;
        @Config(
                description = "Add the effect description to effect widget tooltips."
        )
        public boolean effectDescription = true;
        @Config(
                description = "Add attributes granted by an effect to effect widget tooltips."
        )
        public boolean effectAttributes = true;
        @Config(
                description = "Add the name of the mod that added an effect to effect widget tooltips."
        )
        public boolean modName = false;
        @Config(
                description = "Add the internal id of an effect to effect widget tooltips."
        )
        public boolean internalEffectName = false;
    }
}
