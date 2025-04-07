package fuzs.effectdescriptions.client;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.handler.EffectTooltipHandler;
import fuzs.effectdescriptions.client.handler.EffectWidgetHandler;
import fuzs.effectdescriptions.client.handler.FoodTooltipHandler;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.gui.GatherEffectScreenTooltipCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.InventoryMobEffectsCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.ItemTooltipCallback;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class EffectDescriptionsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ItemTooltipCallback.EVENT.register(EventPhase.LAST, EffectTooltipHandler::onItemTooltip);
        ItemTooltipCallback.EVENT.register(EventPhase.AFTER, FoodTooltipHandler::onItemTooltip);
        if (!ModLoaderEnvironment.INSTANCE.isModLoaded("stylisheffects") &&
                !ModLoaderEnvironment.INSTANCE.isModLoaded("jeed")) {
            InventoryMobEffectsCallback.EVENT.register(EventPhase.LAST, EffectWidgetHandler::onInventoryMobEffects);
            GatherEffectScreenTooltipCallback.EVENT.register((AbstractContainerScreen<?> screen, MobEffectInstance mobEffectInstance, List<Component> tooltipLines) -> {
                if (EffectDescriptions.CONFIG.get(ClientConfig.class).widgetTooltips) {
                    tooltipLines.clear();
                }
            });
        }
    }
}
