package fuzs.effectdescriptions.client;

import fuzs.effectdescriptions.client.handler.InventoryTooltipHandler;
import fuzs.effectdescriptions.client.handler.ItemTooltipHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.gui.InventoryMobEffectsCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.ItemTooltipCallback;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;

public class EffectDescriptionsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ItemTooltipCallback.EVENT.register(ItemTooltipHandler::onItemTooltip);
        if (!ModLoaderEnvironment.INSTANCE.isModLoaded("stylisheffects") && !ModLoaderEnvironment.INSTANCE.isModLoaded("jeed")) {
            InventoryMobEffectsCallback.EVENT.register(EventPhase.LAST, InventoryTooltipHandler::onInventoryMobEffects);
        }
    }
}
