package fuzs.effectdescriptions.client.helper;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EffectLinesHelper {

    public static Optional<Component> getEffectDescriptionComponent(String id, boolean preventIndentation) {
        String description;
        if (Language.getInstance().has(id + ".desc")) {
            // our own format, similar to Enchantment Descriptions mod format
            description = id + ".desc";
        } else if (Language.getInstance().has(id + ".description")) {
            // Just Enough Effect Descriptions mod format
            description = id + ".description";
        } else if (Language.getInstance().has("description." + id)) {
            // Potion Descriptions mod format
            description = "description." + id;
        } else  {
            description = null;
        }
        if (description != null) {

            MutableComponent component = Component.translatable(description);
            if (!preventIndentation) {

                int indentation = EffectDescriptions.CONFIG.get(ClientConfig.class).descriptionIndentation;
                if (indentation > 0) component = Component.literal(StringUtils.repeat(" ", indentation)).append(component);
            }

            return Optional.of(component.withStyle(ChatFormatting.GRAY));
        }
        return Optional.empty();
    }

    public static Optional<Component> tryAddDisplayName(List<Component> lines, MobEffectInstance mobEffectInstance, boolean smallWidgets) {

        if (smallWidgets || EffectDescriptions.CONFIG.get(ClientConfig.class).alwaysAddEffectNameToTooltips) {

            MutableComponent effectComponent = Component.translatable(mobEffectInstance.getDescriptionId());
            if (mobEffectInstance.getAmplifier() > 0) {
                effectComponent = Component.translatable("potion.withAmplifier", effectComponent, Component.translatable("potion.potency." + mobEffectInstance.getAmplifier()));
            }

            Component durationComponent = Component.literal("(").append(MobEffectUtil.formatDuration(mobEffectInstance, 1.0F)).append(")");
            effectComponent.append(CommonComponents.SPACE).append(durationComponent);
            effectComponent.withStyle(mobEffectInstance.getEffect().getCategory().getTooltipFormatting());
            lines.add(effectComponent);

            return smallWidgets ? Optional.of(effectComponent) : Optional.empty();
        }

        return Optional.empty();
    }

    public static void tryAddAttributes(List<Component> lines, MobEffectInstance mobEffectInstance) {
        if (!EffectDescriptions.CONFIG.get(ClientConfig.class).addAttributesToWidgetTooltips) return;
        List<Pair<Attribute, AttributeModifier>> attributes = getAttributesFromEffects(List.of(mobEffectInstance));
        if (!attributes.isEmpty()) {
            lines.add(CommonComponents.EMPTY);
            lines.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
            for (Pair<Attribute, AttributeModifier> pair : attributes) {
                AttributeModifier attributeModifier = pair.getSecond();
                double d0 = attributeModifier.getAmount();
                double d1;
                if (attributeModifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributeModifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    d1 = attributeModifier.getAmount();
                } else {
                    d1 = attributeModifier.getAmount() * 100.0D;
                }

                if (d0 > 0.0D) {
                    lines.add(Component.translatable("attribute.modifier.plus." + attributeModifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId())).withStyle(ChatFormatting.BLUE));
                } else if (d0 < 0.0D) {
                    d1 *= -1.0D;
                    lines.add(Component.translatable("attribute.modifier.take." + attributeModifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId())).withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    private static List<Pair<Attribute, AttributeModifier>> getAttributesFromEffects(List<MobEffectInstance> effects) {
        List<Pair<Attribute, AttributeModifier>> attributes = Lists.newArrayList();
        for (MobEffectInstance mobeffectinstance : effects) {
            for (Map.Entry<Attribute, AttributeModifier> entry : mobeffectinstance.getEffect().getAttributeModifiers().entrySet()) {
                AttributeModifier attributeModifier = entry.getValue();
                double attributeModifierValue = mobeffectinstance.getEffect().getAttributeModifierValue(mobeffectinstance.getAmplifier(), attributeModifier);
                AttributeModifier newAttributeModifier = new AttributeModifier(attributeModifier.getName(), attributeModifierValue, attributeModifier.getOperation());
                attributes.add(new Pair<>(entry.getKey(), newAttributeModifier));
            }
        }
        return attributes;
    }

    private static ChatFormatting getMobEffectColor(MobEffect mobEffect) {
        return switch (mobEffect.getCategory()) {
            case BENEFICIAL -> ChatFormatting.BLUE;
            case HARMFUL -> ChatFormatting.RED;
            default -> ChatFormatting.GOLD;
        };
    }

    public static void tryAddModName(List<Component> lines, MobEffectInstance mobEffectInstance) {

        if (EffectDescriptions.CONFIG.get(ClientConfig.class).addModNameToWidgetTooltips) {

            ModLoaderEnvironment.INSTANCE.getModName(BuiltInRegistries.MOB_EFFECT.getKey(mobEffectInstance.getEffect()).getNamespace())
                    .map(s -> Component.literal(s).withStyle(ChatFormatting.BLUE))
                    .ifPresent(lines::add);
        }
    }

    public static void tryAddInternalName(List<Component> lines, MobEffectInstance mobEffectInstance) {

        if (EffectDescriptions.CONFIG.get(ClientConfig.class).addInternalIdToWidgetTooltips) {

            lines.add(Component.literal(BuiltInRegistries.MOB_EFFECT.getKey(mobEffectInstance.getEffect()).toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
