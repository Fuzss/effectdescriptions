package fuzs.effectdescriptions.client.handler;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.gui.component.EnchantmentComponents;
import fuzs.effectdescriptions.client.gui.tooltip.EnchantmentTooltipLines;
import fuzs.effectdescriptions.client.util.EnchantmentWithLevel;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.effectdescriptions.config.ItemDescriptionMode;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class EnchantmentTooltipHandler extends TooltipDescriptionsHandler<EnchantmentWithLevel> {

    @Override
    protected ItemDescriptionMode getItemDescriptionMode() {
        return EffectDescriptions.CONFIG.get(ClientConfig.class).itemEnchantmentDescriptions;
    }

    @Override
    protected Map<String, EnchantmentWithLevel> getByDescriptionId(ItemStack itemStack) {
        // an item can contain the same effect multiple times, so make sure to include a merge function in our collect call
        return EnchantmentComponents.getAllEnchantments(itemStack)
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

    @Override
    protected Component getValueComponent(EnchantmentWithLevel enchantmentWithLevel) {
        // replace the enchantment name with our coloured variant
        return getFullname(enchantmentWithLevel.enchantment(), enchantmentWithLevel.level());
    }

    @Override
    protected List<Component> getItemTooltipLines(EnchantmentWithLevel enchantmentWithLevel) {
        return EnchantmentTooltipLines.getEnchantmentItemTooltipLines(enchantmentWithLevel);
    }

    /**
     * @see Enchantment#getFullname(Holder, int)
     */
    public static Component getFullname(Holder<Enchantment> enchantment, int level) {
        MutableComponent mutableComponent = enchantment.value().description().copy();
        mergeEnchantmentStyle(enchantment, mutableComponent);
        addLevelComponent(enchantment, level, mutableComponent);
        return mutableComponent;
    }

    private static void mergeEnchantmentStyle(Holder<Enchantment> enchantment, MutableComponent mutableComponent) {
        if (enchantment.is(EnchantmentTags.CURSE)) {
            ComponentUtils.mergeStyles(mutableComponent,
                    EffectDescriptions.CONFIG.get(ClientConfig.class).enchantmentTextStyling.curseStyle);
        } else if (enchantment.is(EnchantmentTags.TREASURE)) {
            ComponentUtils.mergeStyles(mutableComponent,
                    EffectDescriptions.CONFIG.get(ClientConfig.class).enchantmentTextStyling.treasureStyle);
        } else {
            ComponentUtils.mergeStyles(mutableComponent,
                    EffectDescriptions.CONFIG.get(ClientConfig.class).enchantmentTextStyling.defaultStyle);
        }
    }

    private static void addLevelComponent(Holder<Enchantment> enchantment, int level, MutableComponent mutableComponent) {
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
    }
}
