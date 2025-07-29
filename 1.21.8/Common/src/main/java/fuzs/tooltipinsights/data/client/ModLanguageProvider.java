package fuzs.tooltipinsights.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.tooltipinsights.config.ItemDescriptionMode;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ItemDescriptionMode.SHIFT_COMPONENT, "\u21E7 Shift");
        translationBuilder.add(ItemDescriptionMode.VIEW_DESCRIPTIONS_COMPONENT, "Hold %s to view descriptions.");
    }
}
