package fuzs.enchantmentinsights.client.gui.tooltip;

import com.google.common.collect.ImmutableList;
import fuzs.enchantmentinsights.EnchantmentInsights;
import fuzs.enchantmentinsights.client.util.EnchantmentWithLevel;
import fuzs.enchantmentinsights.config.ClientConfig;
import fuzs.puzzleslib.api.init.v3.registry.ResourceKeyHelper;
import fuzs.tooltipinsights.client.gui.tooltip.DescriptionLines;
import fuzs.tooltipinsights.client.gui.tooltip.InternalNameLines;
import fuzs.tooltipinsights.client.gui.tooltip.ModNameLines;
import fuzs.tooltipinsights.client.gui.tooltip.TooltipLinesExtractor;
import fuzs.tooltipinsights.config.AbstractClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.stream.Stream;

public final class EnchantmentTooltipLines {
    static final TooltipLinesExtractor<EnchantmentWithLevel, AbstractClientConfig.TooltipComponents> DESCRIPTION = new DescriptionLines<>() {
        @Override
        protected String getDescriptionId(EnchantmentWithLevel enchantmentWithLevel) {
            ResourceKey<Enchantment> resourceKey = enchantmentWithLevel.enchantment().unwrapKey().orElseThrow();
            return ResourceKeyHelper.getTranslationKey(resourceKey);
        }
    };
    static final TooltipLinesExtractor<EnchantmentWithLevel, fuzs.enchantmentinsights.config.ClientConfig.EnchantmentTooltipComponents> COMPATIBLE_ITEMS = new TooltipLinesExtractor<>(
            true) {
        @Override
        protected boolean isEnabled(fuzs.enchantmentinsights.config.ClientConfig.EnchantmentTooltipComponents tooltipComponents) {
            return tooltipComponents.compatibleItems;
        }

        @Override
        protected Stream<Component> getTooltipLines(EnchantmentWithLevel enchantmentWithLevel) {
            Enchantment.EnchantmentDefinition enchantmentDefinition = enchantmentWithLevel.enchantment()
                    .value()
                    .definition();
            Stream.Builder<Component> builder = Stream.builder();
            enchantmentDefinition.primaryItems()
                    .flatMap(HolderSet::unwrapKey)
                    .map(this::getTagKeyAsComponent)
                    .ifPresent(builder);
            enchantmentDefinition.supportedItems().unwrapKey().map(this::getTagKeyAsComponent).ifPresent(builder);
            return builder.build();
        }

        private Component getTagKeyAsComponent(TagKey<?> tagKey) {
            return Component.literal("#" + tagKey.location()).withStyle(ChatFormatting.LIGHT_PURPLE);
        }
    };
    static final TooltipLinesExtractor<EnchantmentWithLevel, AbstractClientConfig.TooltipComponents> MOD_NAME = new ModNameLines<>() {
        @Override
        protected ResourceKey<?> getResourceKey(EnchantmentWithLevel enchantmentWithLevel) {
            return enchantmentWithLevel.enchantment().unwrapKey().orElseThrow();
        }
    };
    static final TooltipLinesExtractor<EnchantmentWithLevel, AbstractClientConfig.TooltipComponents> INTERNAL_NAME = new InternalNameLines<>() {
        @Override
        protected ResourceKey<?> getResourceKey(EnchantmentWithLevel enchantmentWithLevel) {
            return enchantmentWithLevel.enchantment().unwrapKey().orElseThrow();
        }
    };
    static final List<TooltipLinesExtractor<EnchantmentWithLevel, fuzs.enchantmentinsights.config.ClientConfig.EnchantmentTooltipComponents>> ITEM_SUPPLIERS = ImmutableList.of(
            DESCRIPTION.cast(),
            COMPATIBLE_ITEMS,
            MOD_NAME.cast(),
            INTERNAL_NAME.cast());

    private EnchantmentTooltipLines() {
        // NO-OP
    }

    public static List<Component> getEnchantmentItemTooltipLines(EnchantmentWithLevel enchantmentWithLevel) {
        return TooltipLinesExtractor.getTooltipLines(ITEM_SUPPLIERS,
                EnchantmentInsights.CONFIG.get(ClientConfig.class).enchantmentItemTooltips.decorationComponent,
                EnchantmentInsights.CONFIG.get(ClientConfig.class).enchantmentItemTooltips.decorationStyle,
                enchantmentWithLevel,
                EnchantmentInsights.CONFIG.get(ClientConfig.class).enchantmentItemTooltips.itemTooltipLines);
    }
}
