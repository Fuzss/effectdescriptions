package fuzs.effectinsights.client;

import fuzs.effectinsights.client.handler.EffectTooltipHandler;
import fuzs.effectinsights.client.handler.EffectWidgetHandler;
import fuzs.effectinsights.client.handler.FoodTooltipHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.entity.player.ClientPlayerNetworkEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ContainerScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.GatherEffectScreenTooltipCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.ItemTooltipCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.PrepareInventoryMobEffectsCallback;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;
import fuzs.tooltipinsights.api.v1.client.gui.tooltip.DescriptionLines;
import fuzs.tooltipinsights.impl.TooltipInsights;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Function;

public class EffectInsightsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ItemTooltipCallback.EVENT.register(EventPhase.LAST, new EffectTooltipHandler()::onItemTooltip);
        ItemTooltipCallback.EVENT.register(EventPhase.AFTER, FoodTooltipHandler::onItemTooltip);
        PrepareInventoryMobEffectsCallback.EVENT.register(EventPhase.LAST, EffectWidgetHandler::onInventoryMobEffects);
        ContainerScreenEvents.FOREGROUND.register(EffectWidgetHandler::onDrawForeground);
        GatherEffectScreenTooltipCallback.EVENT.register(EffectWidgetHandler::onGatherEffectScreenTooltip);
    }

    @Override
    public void onClientSetup() {
        printMissingDescriptionWarnings(Registries.MOB_EFFECT,
                (Holder.Reference<MobEffect> holder) -> holder.value().getDescriptionId());
    }

    /**
     * TODO replace with original method from Tooltip Insights
     */
    @Deprecated(forRemoval = true)
    public static <T> void printMissingDescriptionWarnings(ResourceKey<? extends Registry<? extends T>> registryKey, Function<Holder.Reference<T>, String> descriptionIdGetter) {
        ClientPlayerNetworkEvents.LOGGED_IN.register((LocalPlayer player, MultiPlayerGameMode multiPlayerGameMode, Connection connection) -> {
            player.registryAccess().lookupOrThrow(registryKey).listElements().forEach((Holder.Reference<T> holder) -> {
                String translationKey = descriptionIdGetter.apply(holder);
                if (DescriptionLines.getDescriptionTranslationKey(translationKey) == null) {
                    TooltipInsights.LOGGER.warn("Missing description for {}: {}",
                            holder.key(),
                            translationKey + ".desc");
                }
            });
        });
    }
}
