package fuzs.effectdescriptions.client.handler;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.helper.EffectTooltipSuppliers;
import fuzs.effectdescriptions.client.helper.MobEffectSuppliers;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.effectdescriptions.config.ItemEffectDescription;
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
        ItemEffectDescription itemEffectDescription = EffectDescriptions.CONFIG.get(ClientConfig.class).itemDescriptions;
        if (itemEffectDescription == ItemEffectDescription.NEVER && !itemEffectDescription.isActive()) {
            return;
        }

        // an item can contain the same effect multiple times, so make sure to include a merge function in our collect call
        Map<String, MobEffectInstance> mobEffects = MobEffectSuppliers.getMobEffects(itemStack)
                .stream()
                .collect(Collectors.toMap(MobEffectInstance::getDescriptionId,
                        Function.identity(),
                        (MobEffectInstance o1, MobEffectInstance o2) -> o1));

        if (!mobEffects.isEmpty()) {
            for (int i = 0; i < tooltipLines.size(); i++) {
                TranslatableContents contents = getTranslatableContents(tooltipLines.get(i));

                if (contents != null) {
                    String translationKey = contents.getKey();

                    if (mobEffects.containsKey(translationKey)) {
                        if (itemEffectDescription.isActive()) {
                            List<? extends Component> list = EffectTooltipSuppliers.DESCRIPTION.getRawMobEffectComponents(
                                    mobEffects.get(translationKey));
                            tooltipLines.addAll(i + 1, list);
                        } else {
                            // make sure the view description line is only added when there will actually be a description
                            itemEffectDescription.processTooltipLines(itemStack, tooltipLines, tooltipFlag);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Nullable
    private static TranslatableContents getTranslatableContents(Component component) {
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
