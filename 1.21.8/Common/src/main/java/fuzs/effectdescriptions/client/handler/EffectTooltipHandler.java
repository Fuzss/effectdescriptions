package fuzs.effectdescriptions.client.handler;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.helper.DataComponentExtractor;
import fuzs.effectdescriptions.client.helper.TooltipLinesExtractor;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.effectdescriptions.config.ItemDescriptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EffectTooltipHandler {

    public static void onItemTooltip(ItemStack itemStack, List<Component> tooltipLines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        ItemDescriptions itemDescriptions = EffectDescriptions.CONFIG.get(ClientConfig.class).itemEffectDescriptions;
        if (itemDescriptions == ItemDescriptions.NEVER && !itemDescriptions.isActive()) {
            return;
        }

        Map<String, MobEffectInstance> mobEffects = getEffectsByDescriptionId(itemStack);

        if (!mobEffects.isEmpty()) {
            for (int i = 0; i < tooltipLines.size(); i++) {
                TranslatableContents contents = getTranslatableContents(tooltipLines.get(i));

                if (contents != null) {
                    String translationKey = contents.getKey();

                    if (mobEffects.containsKey(translationKey)) {
                        if (itemDescriptions.isActive()) {
                            MobEffectInstance mobEffect = mobEffects.get(translationKey);
                            List<Component> list = TooltipLinesExtractor.getMobEffectItemTooltipLines(mobEffect);
                            tooltipLines.addAll(i + 1, list);
                            i += list.size();
                        } else {
                            // make sure the view description line is only added when there will actually be a description
                            itemDescriptions.processTooltipLines(itemStack, tooltipLines, tooltipFlag);
                            break;
                        }
                    }
                }
            }
        }
    }

    private static Map<String, MobEffectInstance> getEffectsByDescriptionId(ItemStack itemStack) {
        // an item can contain the same effect multiple times, so make sure to include a merge function in our collect call
        return DataComponentExtractor.getAllMobEffects(itemStack)
                .collect(Collectors.toMap(MobEffectInstance::getDescriptionId,
                        Function.identity(),
                        (MobEffectInstance o1, MobEffectInstance o2) -> o1));
    }

    @Nullable
    public static TranslatableContents getTranslatableContents(Component component) {
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            return getNestedTranslatableContents(translatableContents);
        } else if (!component.getSiblings().isEmpty()) {
            return getTranslatableContents(component.getSiblings().getFirst());
        } else {
            return null;
        }
    }

    private static TranslatableContents getNestedTranslatableContents(TranslatableContents contents) {
        if (contents.getArgs().length != 0 && contents.getArgs()[0] instanceof Component component
                && component.getContents() instanceof TranslatableContents innerContents) {
            return getNestedTranslatableContents(innerContents);
        } else {
            return contents;
        }
    }

    public static Set<String> getAllTranslationKeys(List<Component> tooltipLines) {
        return tooltipLines.stream().mapMulti((Component component, Consumer<TranslatableContents> consumer) -> {
            TranslatableContents contents = EffectTooltipHandler.getTranslatableContents(component);
            if (contents != null) {
                consumer.accept(contents);
            }
        }).map(TranslatableContents::getKey).collect(Collectors.toSet());
    }
}
