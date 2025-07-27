package fuzs.effectdescriptions.client.handler;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.helper.DataComponentExtractor;
import fuzs.effectdescriptions.client.helper.EnchantmentWithLevel;
import fuzs.effectdescriptions.client.helper.TooltipLinesExtractor;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.effectdescriptions.config.ItemDescriptions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EnchantmentTooltipHandler {

    public static void onItemTooltip(ItemStack itemStack, List<Component> tooltipLines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        ItemDescriptions itemDescriptions = EffectDescriptions.CONFIG.get(ClientConfig.class).itemEnchantmentDescriptions;
        if (itemDescriptions == ItemDescriptions.NEVER && !itemDescriptions.isActive()) {
            return;
        }

        Map<String, EnchantmentWithLevel> enchantments = getEnchantmentsByDescriptionId(itemStack);

        if (!enchantments.isEmpty()) {
            MutableBoolean mutableBoolean = new MutableBoolean(true);

            for (int i = 0; i < tooltipLines.size(); i++) {
                TranslatableContents contents = EffectTooltipHandler.getTranslatableContents(tooltipLines.get(i));

                if (contents != null) {
                    String translationKey = contents.getKey();

                    if (enchantments.containsKey(translationKey)) {
                        EnchantmentWithLevel enchantmentWithLevel = enchantments.get(translationKey);
                        // replace the enchantment name with our coloured variant
                        tooltipLines.set(i,
                                getFullname(enchantmentWithLevel.enchantment(), enchantmentWithLevel.level()));

                        if (itemDescriptions.isActive()) {
                            List<Component> list = TooltipLinesExtractor.getEnchantmentItemTooltipLines(
                                    enchantmentWithLevel);
                            tooltipLines.addAll(i + 1, list);
                            i += list.size();
                        } else {
                            // make sure the view description line is only added when there will actually be a description
                            if (mutableBoolean.isTrue()) {
                                mutableBoolean.setFalse();
                                itemDescriptions.processTooltipLines(itemStack, tooltipLines, tooltipFlag);
                            }
                        }
                    }
                }
            }
        }
    }

    private static Map<String, EnchantmentWithLevel> getEnchantmentsByDescriptionId(ItemStack itemStack) {
        // an item can contain the same effect multiple times, so make sure to include a merge function in our collect call
        return DataComponentExtractor.getAllEnchantments(itemStack)
                .mapMulti((EnchantmentWithLevel enchantmentWithLevel, Consumer<Map.Entry<String, EnchantmentWithLevel>> consumer) -> {
                    Component component = enchantmentWithLevel.enchantment().value().description();
                    TranslatableContents contents = EffectTooltipHandler.getTranslatableContents(component);
                    if (contents != null) {
                        consumer.accept(Map.entry(contents.getKey(), enchantmentWithLevel));
                    }
                })
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (EnchantmentWithLevel o1, EnchantmentWithLevel o2) -> o1));
    }

    /**
     * @see Enchantment#getFullname(Holder, int)
     */
    public static Component getFullname(Holder<Enchantment> enchantment, int level) {
        MutableComponent mutableComponent = enchantment.value().description().copy();

        if (enchantment.is(EnchantmentTags.CURSE)) {
            ComponentUtils.mergeStyles(mutableComponent, Style.EMPTY.withColor(ChatFormatting.RED));
        } else if (enchantment.is(EnchantmentTags.TREASURE)) {
            ComponentUtils.mergeStyles(mutableComponent, Style.EMPTY.withColor(ChatFormatting.GOLD));
        } else {
            ComponentUtils.mergeStyles(mutableComponent, Style.EMPTY.withColor(ChatFormatting.GREEN));
        }

        boolean maximumLevel = EffectDescriptions.CONFIG.get(ClientConfig.class).itemEnchantmentComponents.maximumLevel();

        if (maximumLevel || level != 1 || enchantment.value().getMaxLevel() != 1) {
            mutableComponent.append(CommonComponents.SPACE)
                    .append(Component.translatable("enchantment.level." + level));

            if (maximumLevel) {
                int maxLevel = enchantment.value().getMaxLevel();
                mutableComponent.append(CommonComponents.SPACE)
                        .append("(")
                        .append(Component.translatable("enchantment.level." + maxLevel))
                        .append(")");
            }
        }

        return mutableComponent;
    }
}
