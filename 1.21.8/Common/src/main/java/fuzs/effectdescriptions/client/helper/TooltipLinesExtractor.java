package fuzs.effectdescriptions.client.helper;

import com.google.common.collect.ImmutableList;
import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.ClientComponentSplitter;
import fuzs.puzzleslib.api.core.v1.ModContainer;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.init.v3.registry.ResourceKeyHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public abstract class TooltipLinesExtractor<V, T, C extends ClientConfig.TooltipComponents> {
    public static final Component DEFAULT_TOOLTIP_DECORATIONS_COMPONENT = Component.literal(" \u25C6 ")
            .withStyle(ChatFormatting.GRAY);
    static final TooltipLinesExtractor<MobEffect, MobEffectInstance, ClientConfig.EffectTooltipComponents> MOB_EFFECT_DISPLAY_NAME = new MobEffectImpl() {
        @Override
        boolean isEnabled(ClientConfig.EffectTooltipComponents tooltipComponents) {
            return tooltipComponents.displayName;
        }

        @Override
        Stream<Component> modifyTooltipLines(List<Component> tooltipLines, int separatorIndex) {
            if (separatorIndex != -1) {
                return tooltipLines.subList(0, separatorIndex).stream();
            } else {
                return tooltipLines.stream();
            }
        }
    };
    static final TooltipLinesExtractor<MobEffect, MobEffectInstance, ? extends ClientConfig.TooltipComponents> MOB_EFFECT_DESCRIPTION = new DescriptionImpl<>() {
        @Override
        String getDescriptionId(MobEffectInstance mobEffect) {
            return mobEffect.getDescriptionId();
        }
    };
    static final TooltipLinesExtractor<Enchantment, EnchantmentWithLevel, ? extends ClientConfig.TooltipComponents> ENCHANTMENT_DESCRIPTION = new DescriptionImpl<>() {
        @Override
        String getDescriptionId(EnchantmentWithLevel enchantmentWithLevel) {
            ResourceKey<Enchantment> resourceKey = enchantmentWithLevel.enchantment().unwrapKey().orElseThrow();
            return ResourceKeyHelper.getTranslationKey(resourceKey);
        }
    };
    static final TooltipLinesExtractor<Enchantment, EnchantmentWithLevel, ClientConfig.EnchantmentTooltipComponents> ENCHANTMENT_COMPATIBLE_ITEMS = new TooltipLinesExtractor<Enchantment, EnchantmentWithLevel, ClientConfig.EnchantmentTooltipComponents>(
            true) {
        @Override
        boolean isEnabled(ClientConfig.EnchantmentTooltipComponents tooltipComponents) {
            return tooltipComponents.compatibleItems;
        }

        @Override
        Stream<Component> getTooltipLines(ResourceKey<? extends Enchantment> resourceKey, EnchantmentWithLevel enchantmentWithLevel) {
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
    static final TooltipLinesExtractor<MobEffect, MobEffectInstance, ClientConfig.EffectTooltipComponents> MOB_EFFECT_ATTRIBUTES = new MobEffectImpl() {
        @Override
        boolean isEnabled(ClientConfig.EffectTooltipComponents tooltipComponents) {
            return tooltipComponents.effectAttributes;
        }

        @Override
        Stream<Component> modifyTooltipLines(List<Component> tooltipLines, int separatorIndex) {
            if (separatorIndex != -1) {
                return tooltipLines.subList(separatorIndex, tooltipLines.size()).stream();
            } else {
                return Stream.empty();
            }
        }
    };
    static final TooltipLinesExtractor<?, ?, ? extends ClientConfig.TooltipComponents> MOD_NAME = new TooltipLinesExtractor<>(
            true) {
        @Override
        boolean isEnabled(ClientConfig.TooltipComponents tooltipComponents) {
            return tooltipComponents.modName;
        }

        @Override
        Stream<Component> getTooltipLines(ResourceKey<?> resourceKey, Object o) {
            return ModLoaderEnvironment.INSTANCE.getModContainer(resourceKey.location().getNamespace())
                    .map(ModContainer::getDisplayName)
                    .<Component>map((String s) -> Component.literal(s).withStyle(ChatFormatting.BLUE))
                    .stream();
        }
    };
    static final TooltipLinesExtractor<?, ?, ? extends ClientConfig.TooltipComponents> INTERNAL_NAME = new TooltipLinesExtractor<>(
            true) {
        @Override
        boolean isEnabled(ClientConfig.TooltipComponents tooltipComponents) {
            return tooltipComponents.internalName;
        }

        @Override
        Stream<Component> getTooltipLines(ResourceKey<?> resourceKey, Object o) {
            return Stream.of(Component.literal(resourceKey.location().toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
    };
    private static final List<TooltipLinesExtractor<MobEffect, MobEffectInstance, ClientConfig.EffectTooltipComponents>> MOB_EFFECT_WIDGET_SUPPLIERS = ImmutableList.of(
            MOB_EFFECT_DISPLAY_NAME,
            MOB_EFFECT_DESCRIPTION.cast(),
            MOD_NAME.cast(),
            INTERNAL_NAME.cast(),
            MOB_EFFECT_ATTRIBUTES);
    private static final List<TooltipLinesExtractor<MobEffect, MobEffectInstance, ClientConfig.TooltipComponents>> MOB_EFFECT_ITEM_SUPPLIERS = ImmutableList.of(
            MOB_EFFECT_DESCRIPTION.cast(),
            MOD_NAME.cast(),
            INTERNAL_NAME.cast());
    private static final List<TooltipLinesExtractor<Enchantment, EnchantmentWithLevel, ClientConfig.EnchantmentTooltipComponents>> ENCHANTMENT_ITEM_SUPPLIERS = ImmutableList.of(
            ENCHANTMENT_DESCRIPTION.cast(),
            ENCHANTMENT_COMPATIBLE_ITEMS,
            MOD_NAME.cast(),
            INTERNAL_NAME.cast());

    private final boolean supportsDecorations;

    TooltipLinesExtractor(boolean supportsDecorations) {
        this.supportsDecorations = supportsDecorations;
    }

    public static List<Component> getMobEffectWidgetTooltipLines(MobEffectInstance mobEffect) {
        ClientConfig.EffectTooltipComponents tooltipComponents = EffectDescriptions.CONFIG.get(ClientConfig.class).widgetEffectComponents;
        return getTooltipLines(MOB_EFFECT_WIDGET_SUPPLIERS,
                EffectDescriptions.CONFIG.get(ClientConfig.class).effectTooltipDecorations,
                mobEffect.getEffect(),
                mobEffect,
                tooltipComponents);
    }

    public static List<Component> getMobEffectItemTooltipLines(MobEffectInstance mobEffect) {
        ClientConfig.TooltipComponents tooltipComponents = EffectDescriptions.CONFIG.get(ClientConfig.class).itemEffectComponents;
        return getTooltipLines(MOB_EFFECT_ITEM_SUPPLIERS,
                EffectDescriptions.CONFIG.get(ClientConfig.class).effectTooltipDecorations,
                mobEffect.getEffect(),
                mobEffect,
                tooltipComponents);
    }

    public static List<Component> getEnchantmentItemTooltipLines(EnchantmentWithLevel enchantmentWithLevel) {
        ClientConfig.EnchantmentTooltipComponents tooltipComponents = EffectDescriptions.CONFIG.get(ClientConfig.class).itemEnchantmentComponents;
        return getTooltipLines(ENCHANTMENT_ITEM_SUPPLIERS,
                EffectDescriptions.CONFIG.get(ClientConfig.class).enchantmentTooltipDecorations,
                enchantmentWithLevel.enchantment(),
                enchantmentWithLevel,
                tooltipComponents);
    }

    private static <V, T, C extends ClientConfig.TooltipComponents> List<Component> getTooltipLines(List<TooltipLinesExtractor<V, T, C>> extractorList, String tooltipDecorations, Holder<? extends V> holder, T t, C tooltipComponents) {
        Component coloredPrefixComponent = ComponentHelper.getAsComponent(tooltipDecorations);
        Style style = getDecorativeStyle(tooltipDecorations, coloredPrefixComponent);
        // copy text only, without any style; also setting colour to black will make it barely visible on a tooltip background which we want
        // alternatively create an empty component consisting of spaces with the same width,
        // but that usually does not perfectly match up, so stick with the dying approach for now
        Component invisiblePrefixComponent = Component.literal(coloredPrefixComponent.getString())
                .withStyle(ChatFormatting.BLACK);
        MutableBoolean mutableBoolean = new MutableBoolean(true);
        List<Component> tooltipLines = new ArrayList<>();

        for (TooltipLinesExtractor<V, T, C> extractor : extractorList) {
            List<Component> list = extractor.getTooltipLines(tooltipComponents, holder.unwrapKey().orElseThrow(), t)
                    .toList();

            if (extractor.supportsDecorations) {
                for (Component component : list) {
                    Component newComponent;

                    if (mutableBoolean.isTrue()) {
                        mutableBoolean.setFalse();
                        newComponent = coloredPrefixComponent;
                    } else {
                        newComponent = invisiblePrefixComponent;
                    }

                    tooltipLines.add(Component.empty().append(newComponent).append(component).withStyle(style));
                }
            } else {
                tooltipLines.addAll(list);
            }
        }

        return tooltipLines;
    }

    private static Style getDecorativeStyle(String string, Component component) {
        if (!string.isEmpty() && component.getString().isEmpty()) {
            // A hack to get raw formatting without any non-formatting characters to apply.
            // We basically check if only formatting is present (when the component is empty),
            // then insert a temporary space and create a new component from that, which we only use the style from.
            int index = string.lastIndexOf(ChatFormatting.RESET.toString());
            StringBuilder stringBuilder = new StringBuilder(string);
            stringBuilder.insert(index != -1 ? index : string.length(), " ");
            return ComponentHelper.getAsComponent(stringBuilder.toString()).getStyle();
        } else {
            return component.getStyle();
        }
    }

    abstract boolean isEnabled(C tooltipComponents);

    abstract Stream<Component> getTooltipLines(ResourceKey<? extends V> resourceKey, T t);

    private Stream<Component> getTooltipLines(C tooltipComponents, ResourceKey<? extends V> resourceKey, T t) {
        if (this.isEnabled(tooltipComponents)) {
            return this.getTooltipLines(resourceKey, t);
        } else {
            return Stream.empty();
        }
    }

    private <E extends TooltipLinesExtractor<?, ?, ?>> E cast() {
        return (E) this;
    }

    static abstract class DescriptionImpl<V, T> extends TooltipLinesExtractor<V, T, ClientConfig.TooltipComponents> {

        DescriptionImpl() {
            super(true);
        }

        @Override
        final boolean isEnabled(ClientConfig.TooltipComponents tooltipComponents) {
            return tooltipComponents.valueDescription;
        }

        @Override
        final Stream<Component> getTooltipLines(ResourceKey<? extends V> resourceKey, T t) {
            String descriptionKey = this.getDescriptionTranslationKey(this.getDescriptionId(t));
            if (descriptionKey != null) {
                return ClientComponentSplitter.splitTooltipLines(Component.translatable(descriptionKey))
                        .map(ComponentHelper::getAsComponent);
            } else {
                return Stream.empty();
            }
        }

        @Nullable String getDescriptionTranslationKey(String translationKey) {
            if (Language.getInstance().has(translationKey + ".desc")) {
                // our own format, similar to Enchantment Descriptions mod format
                return translationKey + ".desc";
            } else if (Language.getInstance().has(translationKey + ".description")) {
                // Just Enough Effect Descriptions mod format
                return translationKey + ".description";
            } else if (Language.getInstance().has("description." + translationKey)) {
                // Potion Descriptions mod format
                return "description." + translationKey;
            } else {
                return null;
            }
        }

        abstract String getDescriptionId(T t);
    }

    static abstract class MobEffectImpl extends TooltipLinesExtractor<MobEffect, MobEffectInstance, ClientConfig.EffectTooltipComponents> {

        MobEffectImpl() {
            super(false);
        }

        @Override
        final Stream<Component> getTooltipLines(ResourceKey<? extends MobEffect> resourceKey, MobEffectInstance mobEffect) {
            List<Component> tooltipLines = new ArrayList<>();
            TickRateManager tickRateManager = Minecraft.getInstance().level.tickRateManager();
            PotionContents.addPotionTooltip(Collections.singleton(mobEffect),
                    tooltipLines::add,
                    1.0F,
                    tickRateManager.tickrate());
            int index = tooltipLines.indexOf(CommonComponents.EMPTY);
            return this.modifyTooltipLines(tooltipLines, index);
        }

        abstract Stream<Component> modifyTooltipLines(List<Component> tooltipLines, int separatorIndex);
    }
}
