package fuzs.effectdescriptions.client.handler;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.config.ClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FoodTooltipHandler {

    public static void onItemTooltip(ItemStack itemStack, List<Component> lines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        if (!EffectDescriptions.CONFIG.get(ClientConfig.class).foodEffects) return;
        if (itemStack.has(DataComponents.FOOD)) {
            FoodProperties foodProperties = itemStack.get(DataComponents.FOOD);
            List<Component> effectLines = new ArrayList<>();
            for (FoodProperties.PossibleEffect effect : foodProperties.effects()) {
                float[] effectProbability = new float[]{effect.probability()};
                PotionContents.addPotionTooltip(Collections.singleton(effect.effect()),
                        (Component component) -> {
                    if (effectProbability[0] != 1.0F) {
                        int intEffectProbability = Mth.floor(effectProbability[0] * 100.0F);
                        component = Component.empty().append(component).append(" (" + intEffectProbability + "%)").withStyle(
                                ChatFormatting.GOLD);
                        effectProbability[0] = 1.0F;
                    }
                            effectLines.add(component);
                        }, 1.0F, tooltipContext.tickRate());
            }
            if (lines.isEmpty()) {
                lines.addAll(effectLines);
            } else {
                lines.addAll(1, effectLines);
            }
        }
    }
}
