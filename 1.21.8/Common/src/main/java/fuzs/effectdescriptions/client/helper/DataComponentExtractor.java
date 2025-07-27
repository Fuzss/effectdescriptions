package fuzs.effectdescriptions.client.helper;

import com.google.common.collect.ImmutableList;
import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.config.ClientConfig;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class DataComponentExtractor<T, C> {
    static final DataComponentExtractor<MobEffectInstance, PotionContents> POTION_CONTENTS = new DataComponentExtractor<>(
            DataComponents.POTION_CONTENTS) {
        @Override
        boolean isEnabled() {
            return EffectDescriptions.CONFIG.get(ClientConfig.class).effectDescriptionTargets.potionContents;
        }

        @Override
        Stream<MobEffectInstance> extractFromComponent(PotionContents potionContents) {
            return StreamSupport.stream(potionContents.getAllEffects().spliterator(), false);
        }
    };
    static final DataComponentExtractor<MobEffectInstance, Consumable> CONSUMABLE = new DataComponentExtractor<>(
            DataComponents.CONSUMABLE) {
        @Override
        boolean isEnabled() {
            return EffectDescriptions.CONFIG.get(ClientConfig.class).effectDescriptionTargets.consumable;
        }

        @Override
        Stream<MobEffectInstance> extractFromComponent(Consumable consumable) {
            return consumable.onConsumeEffects()
                    .stream()
                    .filter(ApplyStatusEffectsConsumeEffect.class::isInstance)
                    .map(ApplyStatusEffectsConsumeEffect.class::cast)
                    .map(ApplyStatusEffectsConsumeEffect::effects)
                    .flatMap(Collection::stream);
        }
    };
    static final DataComponentExtractor<MobEffectInstance, OminousBottleAmplifier> OMINOUS_BOTTLE_AMPLIFIER = new DataComponentExtractor<>(
            DataComponents.OMINOUS_BOTTLE_AMPLIFIER) {
        @Override
        boolean isEnabled() {
            return EffectDescriptions.CONFIG.get(ClientConfig.class).effectDescriptionTargets.ominousBottle;
        }

        @Override
        Stream<MobEffectInstance> extractFromComponent(OminousBottleAmplifier ominousBottleAmplifier) {
            // copied from OminousBottleAmplifier implementation
            return Stream.of(new MobEffectInstance(MobEffects.BAD_OMEN,
                    120_000,
                    ominousBottleAmplifier.value(),
                    false,
                    false,
                    true));
        }
    };
    static final DataComponentExtractor<MobEffectInstance, SuspiciousStewEffects> SUSPICIOUS_STEW_EFFECTS = new DataComponentExtractor<>(
            DataComponents.SUSPICIOUS_STEW_EFFECTS) {
        @Override
        boolean isEnabled() {
            return EffectDescriptions.CONFIG.get(ClientConfig.class).effectDescriptionTargets.suspiciousStew;
        }

        @Override
        Stream<MobEffectInstance> extractFromComponent(SuspiciousStewEffects suspiciousStewEffects) {
            return suspiciousStewEffects.effects().stream().map(SuspiciousStewEffects.Entry::createEffectInstance);
        }
    };
    static final DataComponentExtractor<EnchantmentWithLevel, ItemEnchantments> ENCHANTMENTS = new DataComponentExtractor<>(
            DataComponents.ENCHANTMENTS) {
        @Override
        boolean isEnabled() {
            return EffectDescriptions.CONFIG.get(ClientConfig.class).enchantmentDescriptionTargets.enchantments;
        }

        @Override
        Stream<EnchantmentWithLevel> extractFromComponent(ItemEnchantments itemEnchantments) {
            return itemEnchantments.entrySet().stream().map(EnchantmentWithLevel::new);
        }
    };
    static final DataComponentExtractor<EnchantmentWithLevel, ItemEnchantments> STORED_ENCHANTMENTS = new DataComponentExtractor<>(
            DataComponents.STORED_ENCHANTMENTS) {
        @Override
        boolean isEnabled() {
            return EffectDescriptions.CONFIG.get(ClientConfig.class).enchantmentDescriptionTargets.storedEnchantments;
        }

        @Override
        Stream<EnchantmentWithLevel> extractFromComponent(ItemEnchantments itemEnchantments) {
            return itemEnchantments.entrySet().stream().map(EnchantmentWithLevel::new);
        }
    };
    private static final List<DataComponentExtractor<MobEffectInstance, ?>> MOB_EFFECTS_SUPPLIERS = ImmutableList.of(
            SUSPICIOUS_STEW_EFFECTS,
            OMINOUS_BOTTLE_AMPLIFIER,
            CONSUMABLE,
            POTION_CONTENTS);
    private static final List<DataComponentExtractor<EnchantmentWithLevel, ?>> ENCHANTMENT_SUPPLIERS = ImmutableList.of(
            ENCHANTMENTS,
            STORED_ENCHANTMENTS);

    private final DataComponentType<C> dataComponentType;

    DataComponentExtractor(DataComponentType<C> dataComponentType) {
        this.dataComponentType = dataComponentType;
    }

    public static Stream<MobEffectInstance> getAllMobEffects(ItemStack itemStack) {
        return MOB_EFFECTS_SUPPLIERS.stream()
                .flatMap((DataComponentExtractor<MobEffectInstance, ?> supplier) -> supplier.extractFromItemStack(
                        itemStack));
    }

    public static Stream<EnchantmentWithLevel> getAllEnchantments(ItemStack itemStack) {
        return ENCHANTMENT_SUPPLIERS.stream()
                .flatMap((DataComponentExtractor<EnchantmentWithLevel, ?> supplier) -> supplier.extractFromItemStack(
                        itemStack));
    }

    abstract boolean isEnabled();

    abstract Stream<T> extractFromComponent(C dataComponent);

    private Stream<T> extractFromItemStack(ItemStack itemStack) {
        if (this.isEnabled() && itemStack.has(this.dataComponentType)) {
            C dataComponent = itemStack.get(this.dataComponentType);
            return this.extractFromComponent(dataComponent);
        } else {
            return Stream.empty();
        }
    }
}
