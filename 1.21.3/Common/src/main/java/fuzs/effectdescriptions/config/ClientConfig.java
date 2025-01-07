package fuzs.effectdescriptions.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.api.config.v3.serialization.KeyedValueProvider;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.function.BooleanSupplier;

public class ClientConfig implements ConfigCore {
    static final String KEY_ITEMS_CATEGORY = "items";
    static final String KEY_WIDGETS_CATEGORY = "widgets";

    @Config(
            description = "Add effect description to item tooltips."
    )
    public ItemEffectDescription itemDescriptions = ItemEffectDescription.ALWAYS;
    @Config(
            name = "supported_items", category = KEY_ITEMS_CATEGORY, description = {
            "Items that should support descriptions for the effects on their inventory tooltip.",
            ConfigDataSet.CONFIG_DESCRIPTION
    }
    )
    List<String> supportedItemsRaw = KeyedValueProvider.tagAppender(Registries.ITEM)
            .add(Items.POTION,
                    Items.SPLASH_POTION,
                    Items.LINGERING_POTION,
                    Items.TIPPED_ARROW,
                    Items.SUSPICIOUS_STEW,
                    Items.OMINOUS_BOTTLE)
            .asStringList();
    @Config(
            category = KEY_ITEMS_CATEGORY,
            description = "Add effects to food tooltips. The option is separate from adding effect descriptions."
    )
    public boolean foodEffects = true;
    @Config(
            description = "Add effect description to effect widget tooltips in the survival and creative inventory screens."
    )
    public boolean widgetTooltips = true;
    @Config(
            category = KEY_WIDGETS_CATEGORY,
            description = "Add the effect name and duration to effect widget tooltips."
    )
    public boolean name = true;
    @Config(
            category = KEY_WIDGETS_CATEGORY,
            description = "Add the effect description to effect widget tooltips."
    )
    public boolean description = true;
    @Config(
            category = KEY_WIDGETS_CATEGORY,
            description = "Add attributes granted by an effect to effect widget tooltips."
    )
    public boolean attributes = true;
    @Config(
            category = KEY_WIDGETS_CATEGORY, description = "Add the internal id of an effect to effect widget tooltips."
    )
    public boolean internalName = false;
    @Config(
            category = KEY_WIDGETS_CATEGORY,
            description = "Add the name of the mod that added an effect to effect widget tooltips."
    )
    public boolean modName = false;

    public ConfigDataSet<Item> supportedItems;

    @Override
    public void afterConfigReload() {
        this.supportedItems = ConfigDataSet.from(Registries.ITEM, this.supportedItemsRaw);
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
