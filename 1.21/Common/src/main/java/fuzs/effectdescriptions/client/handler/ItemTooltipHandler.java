package fuzs.effectdescriptions.client.handler;

import com.google.common.collect.Lists;
import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.helper.EffectLinesHelper;
import fuzs.effectdescriptions.config.ClientConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemTooltipHandler {

    public static void onItemTooltip(ItemStack itemStack, List<Component> lines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        if (!EffectDescriptions.CONFIG.get(ClientConfig.class).itemDescription) return;
        if (!EffectDescriptions.CONFIG.get(ClientConfig.class).supportedItems.contains(itemStack.getItem())) return;
        if (EffectDescriptions.CONFIG.get(ClientConfig.class).shiftToReveal && !Screen.hasShiftDown()) return;
        Set<String> ids = getMobEffects(itemStack).stream().map(MobEffectInstance::getDescriptionId).collect(Collectors.toSet());
        for (int i = 0; i < lines.size(); i++) {
            Component line = lines.get(i);
            if (line.getContents() instanceof TranslatableContents contents) {
                while (contents.getArgs().length != 0 && contents.getArgs()[0] instanceof Component component && component.getContents() instanceof TranslatableContents innerContents) {
                    contents = innerContents;
                }
                String id = contents.getKey();
                if (ids.contains(id)) {
                    int index = i + 1;
                    EffectLinesHelper.getEffectDescriptionComponent(id, false).ifPresent(component -> lines.add(index, component));
                }
            }
        }
    }

    private static List<MobEffectInstance> getMobEffects(ItemStack itemStack) {
        if (itemStack.has(DataComponents.SUSPICIOUS_STEW_EFFECTS)) {
            SuspiciousStewEffects suspiciousStewEffects = itemStack.getOrDefault(DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY);
            return Lists.transform(suspiciousStewEffects.effects(), SuspiciousStewEffects.Entry::createEffectInstance);
        } else {
            PotionContents potionContents = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            return Lists.newArrayList(potionContents.getAllEffects());
        }
    }
}
