package fuzs.effectdescriptions.client.handler;

import com.google.common.collect.Lists;
import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.helper.EffectLinesHelper;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.puzzleslib.api.core.v1.Proxy;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DescriptionTooltipHandler {

    public static void onItemTooltip(ItemStack itemStack, List<Component> lines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        if (!EffectDescriptions.CONFIG.get(ClientConfig.class).itemDescription) return;
        if (!EffectDescriptions.CONFIG.get(ClientConfig.class).supportedItems.contains(itemStack.getItem()) && !itemStack.has(DataComponents.FOOD)) return;
        if (EffectDescriptions.CONFIG.get(ClientConfig.class).shiftToReveal && !Screen.hasShiftDown()) return;
        Set<String> ids = getMobEffects(itemStack).stream().map(MobEffectInstance::getDescriptionId).collect(Collectors.toSet());
        if (!ids.isEmpty()) {
            int indentation = EffectDescriptions.CONFIG.get(ClientConfig.class).descriptionIndentation;
            for (int i = 0; i < lines.size(); i++) {
                Component line = lines.get(i);
                while (line.getContents() instanceof PlainTextContents contents && contents.text().isBlank() && !line.getSiblings().isEmpty()) {
                    line = line.getSiblings().getFirst();
                }
                if (line.getContents() instanceof TranslatableContents contents) {
                    while (contents.getArgs().length != 0 && contents.getArgs()[0] instanceof Component component && component.getContents() instanceof TranslatableContents innerContents) {
                        contents = innerContents;
                    }
                    String id = contents.getKey();
                    if (ids.contains(id)) {
                        int index = i + 1;
                        EffectLinesHelper.getEffectDescriptionComponent(id).ifPresent((Component component) -> {
                            List<Component> tooltipLines = Proxy.INSTANCE.splitTooltipLines(component);
                            for (Component tooltipLine : tooltipLines.reversed()) {
                                if (indentation > 0) tooltipLine = Component.literal(StringUtils.repeat(" ", indentation)).append(tooltipLine);
                                lines.add(index, tooltipLine);
                            }
                        });
                    }
                }
            }
        }
    }

    private static List<MobEffectInstance> getMobEffects(ItemStack itemStack) {
        if (itemStack.has(DataComponents.SUSPICIOUS_STEW_EFFECTS)) {
            SuspiciousStewEffects suspiciousStewEffects = itemStack.getOrDefault(DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY);
            return Lists.transform(suspiciousStewEffects.effects(), SuspiciousStewEffects.Entry::createEffectInstance);
        } else if (itemStack.has(DataComponents.FOOD)) {
            return Lists.transform(itemStack.get(DataComponents.FOOD).effects(), FoodProperties.PossibleEffect::effect);
        } else {
            PotionContents potionContents = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            return Lists.newArrayList(potionContents.getAllEffects());
        }
    }
}
