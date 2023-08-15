package fuzs.effectdescriptions.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class ClientConfig implements ConfigCore {
    @Config(description = "Add effect descriptions to item tooltips.")
    public boolean addDescriptionsToItemTooltips = true;
    @Config(description = {"Items that should support descriptions for the effects on their inventory tooltip.", ConfigDataSet.CONFIG_DESCRIPTION})
    List<String> effectDescriptionItemsRaw = ConfigDataSet.toString(Registry.ITEM_REGISTRY, Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.TIPPED_ARROW, Items.SUSPICIOUS_STEW);
    @Config(description = "Amount of spaces to add at the beginning of an effect description.")
    @Config.IntRange(min = 0)
    public int descriptionIndentation = 0;
    @Config(description = "Only reveal effect descriptions for items while any shift key is held.")
    public boolean holdShiftForItemDescriptions = false;
    @Config(description = "Add effect descriptions to effect widget tooltips in the survival and creative inventory screens.")
    public boolean addDescriptionsToWidgetTooltips = true;
    @Config(description = "Add the effect name and duration to every effect widget tooltip, even if the widget already contains both.")
    public boolean alwaysAddEffectNameToTooltips = true;
    @Config(description = "Add attributes granted by an effect to effect widget tooltips.")
    public boolean addAttributesToWidgetTooltips = true;
    @Config(description = "Add the internal id of an effect to effect widget tooltips.")
    public boolean addInternalIdToWidgetTooltips = false;
    @Config(description = "Add the name of the mod that added an effect to effect widget tooltips.")
    public boolean addModNameToWidgetTooltips = false;

    public ConfigDataSet<Item> effectDescriptionItems;

    @Override
    public void afterConfigReload() {
        this.effectDescriptionItems = ConfigDataSet.from(Registry.ITEM_REGISTRY, this.effectDescriptionItemsRaw);
    }
}
