package fuzs.effectdescriptions.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.BooleanSupplier;

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

    public enum ItemEffectDescription {
        NEVER(false),
        SHIFT(Screen::hasShiftDown),
        ALWAYS(true);

        public final BooleanSupplier isActive;

        ItemEffectDescription(boolean isActive) {
            this(() -> isActive);
        }

        ItemEffectDescription(BooleanSupplier isActive) {
            this.isActive = isActive;
        }
    }
}
