package fuzs.effectdescriptions.client;

import fuzs.effectdescriptions.client.handler.EffectTooltipHandler;
import fuzs.effectdescriptions.client.handler.EffectWidgetHandler;
import fuzs.effectdescriptions.client.handler.EnchantmentTooltipHandler;
import fuzs.effectdescriptions.client.handler.FoodTooltipHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.gui.ContainerScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.GatherEffectScreenTooltipCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.ItemTooltipCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.PrepareInventoryMobEffectsCallback;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;

public class EffectDescriptionsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ItemTooltipCallback.EVENT.register(EventPhase.LAST, new EnchantmentTooltipHandler()::onItemTooltip);
        ItemTooltipCallback.EVENT.register(EventPhase.LAST, new EffectTooltipHandler()::onItemTooltip);
        ItemTooltipCallback.EVENT.register(EventPhase.AFTER, FoodTooltipHandler::onItemTooltip);
        if (!ModLoaderEnvironment.INSTANCE.isModLoaded("stylisheffects") && !ModLoaderEnvironment.INSTANCE.isModLoaded(
                "jeed")) {
            PrepareInventoryMobEffectsCallback.EVENT.register(EventPhase.LAST,
                    EffectWidgetHandler::onInventoryMobEffects);
            ContainerScreenEvents.FOREGROUND.register(EffectWidgetHandler::onDrawForeground);
            GatherEffectScreenTooltipCallback.EVENT.register(EffectWidgetHandler::onGatherEffectScreenTooltip);
        }
    }
}
