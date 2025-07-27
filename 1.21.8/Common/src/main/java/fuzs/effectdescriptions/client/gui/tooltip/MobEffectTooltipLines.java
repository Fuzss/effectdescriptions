package fuzs.effectdescriptions.client.gui.tooltip;

import com.google.common.collect.ImmutableList;
import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.config.ClientConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;
import java.util.stream.Stream;

public final class MobEffectTooltipLines {
    static final TooltipLinesExtractor<MobEffectInstance, ClientConfig.EffectTooltipComponents> DISPLAY_NAME = new PotionContentsLines() {
        @Override
        protected boolean isEnabled(ClientConfig.EffectTooltipComponents tooltipComponents) {
            return tooltipComponents.displayName;
        }

        @Override
        protected Stream<Component> modifyTooltipLines(List<Component> tooltipLines, int separatorIndex) {
            if (separatorIndex != -1) {
                return tooltipLines.subList(0, separatorIndex).stream();
            } else {
                return tooltipLines.stream();
            }
        }
    };
    static final TooltipLinesExtractor<MobEffectInstance, ClientConfig.TooltipComponents> DESCRIPTION = new DescriptionLines<>() {
        @Override
        protected String getDescriptionId(MobEffectInstance mobEffect) {
            return mobEffect.getDescriptionId();
        }
    };
    static final TooltipLinesExtractor<MobEffectInstance, ClientConfig.TooltipComponents> MOD_NAME = new ModNameLines<>() {
        @Override
        protected ResourceKey<?> getResourceKey(MobEffectInstance mobEffect) {
            return mobEffect.getEffect().unwrapKey().orElseThrow();
        }
    };
    static final TooltipLinesExtractor<MobEffectInstance, ClientConfig.TooltipComponents> INTERNAL_NAME = new InternalNameLines<>() {
        @Override
        protected ResourceKey<?> getResourceKey(MobEffectInstance mobEffect) {
            return mobEffect.getEffect().unwrapKey().orElseThrow();
        }
    };
    static final TooltipLinesExtractor<MobEffectInstance, ClientConfig.EffectTooltipComponents> ATTRIBUTES = new PotionContentsLines() {
        @Override
        protected boolean isEnabled(ClientConfig.EffectTooltipComponents tooltipComponents) {
            return tooltipComponents.effectAttributes;
        }

        @Override
        protected Stream<Component> modifyTooltipLines(List<Component> tooltipLines, int separatorIndex) {
            if (separatorIndex != -1) {
                return tooltipLines.subList(separatorIndex, tooltipLines.size()).stream();
            } else {
                return Stream.empty();
            }
        }
    };
    static final List<TooltipLinesExtractor<MobEffectInstance, ClientConfig.TooltipComponents>> ITEM_SUPPLIERS = ImmutableList.of(
            DESCRIPTION,
            MOD_NAME,
            INTERNAL_NAME);
    static final List<TooltipLinesExtractor<MobEffectInstance, ClientConfig.EffectTooltipComponents>> WIDGET_SUPPLIERS = ImmutableList.of(
            DISPLAY_NAME,
            DESCRIPTION.cast(),
            MOD_NAME.cast(),
            INTERNAL_NAME.cast(),
            ATTRIBUTES);

    private MobEffectTooltipLines() {
        // NO-OP
    }

    public static List<Component> getMobEffectWidgetTooltipLines(MobEffectInstance mobEffect) {
        return TooltipLinesExtractor.getTooltipLines(WIDGET_SUPPLIERS,
                EffectDescriptions.CONFIG.get(ClientConfig.class).effectWidgetTooltips.decorationComponent,
                EffectDescriptions.CONFIG.get(ClientConfig.class).effectWidgetTooltips.decorationStyle,
                mobEffect,
                EffectDescriptions.CONFIG.get(ClientConfig.class).effectWidgetTooltips.widgetTooltipLines);
    }

    public static List<Component> getMobEffectItemTooltipLines(MobEffectInstance mobEffect) {
        return TooltipLinesExtractor.getTooltipLines(ITEM_SUPPLIERS,
                EffectDescriptions.CONFIG.get(ClientConfig.class).effectItemTooltips.decorationComponent,
                EffectDescriptions.CONFIG.get(ClientConfig.class).effectItemTooltips.decorationStyle,
                mobEffect,
                EffectDescriptions.CONFIG.get(ClientConfig.class).enchantmentItemTooltips.itemTooltipLines);
    }
}
