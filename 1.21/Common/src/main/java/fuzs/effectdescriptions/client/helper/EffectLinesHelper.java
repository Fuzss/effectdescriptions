package fuzs.effectdescriptions.client.helper;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.puzzleslib.api.core.v1.ModContainer;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class EffectLinesHelper {

    public static Optional<Component> getEffectDescriptionComponent(String id) {

        String descriptionKey = getDescriptionTranslationKey(id);
        if (descriptionKey != null) {

            Component component = Component.translatable(descriptionKey).withStyle(ChatFormatting.GRAY);
            return Optional.of(component);
        }

        return Optional.empty();
    }

    @Nullable
    private static String getDescriptionTranslationKey(String id) {
        if (Language.getInstance().has(id + ".desc")) {
            // our own format, similar to Enchantment Descriptions mod format
            return id + ".desc";
        } else if (Language.getInstance().has(id + ".description")) {
            // Just Enough Effect Descriptions mod format
            return id + ".description";
        } else if (Language.getInstance().has("description." + id)) {
            // Potion Descriptions mod format
            return "description." + id;
        } else  {
            return null;
        }
    }

    public static void tryAddDisplayName(Minecraft minecraft, List<Component> lines, MobEffectInstance mobEffectInstance, boolean smallWidgets) {

        if (smallWidgets || EffectDescriptions.CONFIG.get(ClientConfig.class).nameAndDuration != ClientConfig.EffectNameMode.NONE) {

            MutableComponent effectComponent = Component.translatable(mobEffectInstance.getDescriptionId());
            if (mobEffectInstance.getAmplifier() > 0) {
                effectComponent = Component.translatable("potion.withAmplifier", effectComponent, Component.translatable("potion.potency." + mobEffectInstance.getAmplifier()));
            }

            if (smallWidgets || EffectDescriptions.CONFIG.get(ClientConfig.class).nameAndDuration != ClientConfig.EffectNameMode.NAME_ONLY) {

                if (!mobEffectInstance.isInfiniteDuration()) {
                    float tickrate = minecraft.level.tickRateManager().tickrate();
                    Component durationComponent = Component.literal("(").append(MobEffectUtil.formatDuration(mobEffectInstance, 1.0F,
                            tickrate
                    )).append(")");
                    effectComponent.append(CommonComponents.SPACE).append(durationComponent);
                }
            }

            effectComponent.withStyle(mobEffectInstance.getEffect().value().getCategory().getTooltipFormatting());
            lines.add(effectComponent);
        }
    }

    public static void tryAddAttributes(List<Component> lines, MobEffectInstance mobEffectInstance) {
        if (!EffectDescriptions.CONFIG.get(ClientConfig.class).attributes) return;
        List<Pair<Attribute, AttributeModifier>> attributes = getAttributesFromEffects(List.of(mobEffectInstance));
        if (!attributes.isEmpty()) {
            lines.add(CommonComponents.EMPTY);
            lines.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
            for (Pair<Attribute, AttributeModifier> pair : attributes) {
                AttributeModifier attributeModifier = pair.getSecond();
                double d0 = attributeModifier.amount();
                double d1;
                if (attributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE && attributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    d1 = attributeModifier.amount();
                } else {
                    d1 = attributeModifier.amount() * 100.0D;
                }

                if (d0 > 0.0D) {
                    lines.add(Component.translatable("attribute.modifier.plus." + attributeModifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId())).withStyle(ChatFormatting.BLUE));
                } else if (d0 < 0.0D) {
                    d1 *= -1.0D;
                    lines.add(Component.translatable("attribute.modifier.take." + attributeModifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId())).withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    private static List<Pair<Attribute, AttributeModifier>> getAttributesFromEffects(List<MobEffectInstance> effects) {
        List<Pair<Attribute, AttributeModifier>> attributes = Lists.newArrayList();
        for (MobEffectInstance mobeffectinstance : effects) {
            mobeffectinstance.getEffect().value().createModifiers(mobeffectinstance.getAmplifier(), (holder, attributeModifier) -> {
                attributes.add(new Pair<>(holder.value(), attributeModifier));
            });
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

        if (EffectDescriptions.CONFIG.get(ClientConfig.class).modName) {

            ModLoaderEnvironment.INSTANCE.getModContainer(BuiltInRegistries.MOB_EFFECT.getKey(mobEffectInstance.getEffect().value()).getNamespace())
                    .map(ModContainer::getDisplayName)
                    .map(s -> Component.literal(s).withStyle(ChatFormatting.BLUE))
                    .ifPresent(lines::add);
        }
    }

    public static void tryAddInternalName(List<Component> lines, MobEffectInstance mobEffectInstance) {

        if (EffectDescriptions.CONFIG.get(ClientConfig.class).internalId) {

            lines.add(Component.literal(BuiltInRegistries.MOB_EFFECT.getKey(mobEffectInstance.getEffect().value()).toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
