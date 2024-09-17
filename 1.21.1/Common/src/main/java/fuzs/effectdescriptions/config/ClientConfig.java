package fuzs.effectdescriptions.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.api.config.v3.serialization.KeyedValueProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class ClientConfig implements ConfigCore {
    static final String KEY_ITEMS_CATEGORY = "items";
    static final String KEY_WIDGETS_CATEGORY = "widgets";

    @Config(name = "description", category = KEY_ITEMS_CATEGORY, description = "Add effect description to item tooltips.")
    public boolean itemDescription = true;
    @Config(name = "supported_items", category = KEY_ITEMS_CATEGORY, description = {"Items that should support descriptions for the effects on their inventory tooltip.", ConfigDataSet.CONFIG_DESCRIPTION})
    List<String> supportedItemsRaw = KeyedValueProvider.toString(Registries.ITEM, Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.TIPPED_ARROW, Items.SUSPICIOUS_STEW);
    @Config(category = KEY_ITEMS_CATEGORY, description = "Add effects to food tooltips.")
    public boolean foodEffects = true;
    @Config(description = "Amount of spaces to add at the beginning of an effect description.")
    @Config.IntRange(min = 0, max = 24)
    public int descriptionIndentation = 0;
    @Config(category = KEY_ITEMS_CATEGORY, description = "Only reveal effect description for items while any shift key is held.")
    public boolean shiftToReveal = false;
    @Config(name = "description", category = KEY_WIDGETS_CATEGORY, description = "Add effect description to effect widget tooltips in the survival and creative inventory screens.")
    public boolean widgetDescription = true;
    @Config(category = KEY_WIDGETS_CATEGORY, description = "Add the effect name and duration to large effect widget tooltips, even though the widget already contains both.")
    public EffectNameMode nameAndDuration = EffectNameMode.NAME_AND_DURATION;
    @Config(category = KEY_WIDGETS_CATEGORY, description = "Add attributes granted by an effect to effect widget tooltips.")
    public boolean attributes = true;
    @Config(category = KEY_WIDGETS_CATEGORY, description = "Add the internal id of an effect to effect widget tooltips.")
    public boolean internalId = false;
    @Config(category = KEY_WIDGETS_CATEGORY, description = "Add the name of the mod that added an effect to effect widget tooltips.")
    public boolean modName = false;

    public ConfigDataSet<Item> supportedItems;

    @Override
    public void afterConfigReload() {
        this.supportedItems = ConfigDataSet.from(Registries.ITEM, this.supportedItemsRaw);
    }

    public enum EffectNameMode {
        NAME_ONLY, NAME_AND_DURATION, NONE
    }
}
