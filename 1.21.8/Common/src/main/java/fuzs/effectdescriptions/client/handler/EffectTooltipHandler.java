package fuzs.effectdescriptions.client.handler;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.gui.component.EffectComponents;
import fuzs.effectdescriptions.client.gui.tooltip.MobEffectTooltipLines;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.effectdescriptions.config.ItemDescriptionMode;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class EffectTooltipHandler extends TooltipDescriptionsHandler<MobEffectInstance> {

    @Override
    protected ItemDescriptionMode getItemDescriptionMode() {
        return EffectDescriptions.CONFIG.get(ClientConfig.class).effectItemTooltips.itemDescriptions;
    }

    @Override
    protected Map<String, MobEffectInstance> getByDescriptionId(ItemStack itemStack) {
        // an item can contain the same effect multiple times, so make sure to include a merge function in our collect call
        return EffectComponents.getAllMobEffects(itemStack)
                .collect(Collectors.toMap(MobEffectInstance::getDescriptionId,
                        Function.identity(),
                        (MobEffectInstance o1, MobEffectInstance o2) -> o1));
    }

    @Override
    protected List<Component> getItemTooltipLines(MobEffectInstance mobEffectInstance) {
        return MobEffectTooltipLines.getMobEffectItemTooltipLines(mobEffectInstance);
    }
}
