package fuzs.effectdescriptions.client.helper;

import com.google.common.collect.Lists;
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
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public final class MobEffectSuppliers {
    private static final Impl<PotionContents> POTION_CONTENTS = new Impl<>(() -> EffectDescriptions.CONFIG.get(
            ClientConfig.class).itemDescriptionTargets.potionContents,
            DataComponents.POTION_CONTENTS,
            (PotionContents potionContents) -> {
                return StreamSupport.stream(potionContents.getAllEffects().spliterator(), false).toList();
            });
    private static final Impl<Consumable> CONSUMABLE = new Impl<>(() -> EffectDescriptions.CONFIG.get(ClientConfig.class).itemDescriptionTargets.consumable,
            DataComponents.CONSUMABLE,
            (Consumable consumable) -> {
                List<MobEffectInstance> list = new ArrayList<>();
                for (ConsumeEffect consumeEffect : consumable.onConsumeEffects()) {
                    if (consumeEffect instanceof ApplyStatusEffectsConsumeEffect applyStatusEffectsConsumeEffect) {
                        list.addAll(applyStatusEffectsConsumeEffect.effects());
                    }
                }
                return list;
            });
    private static final Impl<OminousBottleAmplifier> OMINOUS_BOTTLE_AMPLIFIER = new Impl<>(() -> EffectDescriptions.CONFIG.get(
            ClientConfig.class).itemDescriptionTargets.ominousBottle,
            DataComponents.OMINOUS_BOTTLE_AMPLIFIER,
            (OminousBottleAmplifier ominousBottleAmplifier) -> {
                // copied from OminousBottleAmplifier implementation
                return Collections.singletonList(new MobEffectInstance(MobEffects.BAD_OMEN,
                        120000,
                        ominousBottleAmplifier.value(),
                        false,
                        false,
                        true));
            });
    private static final Impl<SuspiciousStewEffects> SUSPICIOUS_STEW_EFFECTS = new Impl<>(() -> EffectDescriptions.CONFIG.get(
            ClientConfig.class).itemDescriptionTargets.suspiciousStew,
            DataComponents.SUSPICIOUS_STEW_EFFECTS,
            (SuspiciousStewEffects suspiciousStewEffects) -> {
                return Lists.transform(suspiciousStewEffects.effects(),
                        SuspiciousStewEffects.Entry::createEffectInstance);
            });
    private static final List<Impl<?>> MOB_EFFECTS_SUPPLIERS = List.of(SUSPICIOUS_STEW_EFFECTS,
            OMINOUS_BOTTLE_AMPLIFIER,
            CONSUMABLE,
            POTION_CONTENTS);

    private MobEffectSuppliers() {
        // NO-OP
    }

    public static List<MobEffectInstance> getMobEffects(ItemStack itemStack) {
        for (Impl<?> impl : MOB_EFFECTS_SUPPLIERS) {
            List<MobEffectInstance> list = impl.getMobEffects(itemStack);

            if (!list.isEmpty()) {
                return list;
            }
        }

        return Collections.emptyList();
    }

    private record Impl<T>(BooleanSupplier isEnabled,
                           DataComponentType<T> dataComponentType,
                           Function<T, List<MobEffectInstance>> extractor) {

        public List<MobEffectInstance> getMobEffects(ItemStack itemStack) {
            if (this.isEnabled.getAsBoolean() && itemStack.has(this.dataComponentType)) {
                T dataComponent = itemStack.get(this.dataComponentType);
                return this.extractor.apply(dataComponent);
            } else {
                return Collections.emptyList();
            }
        }
    }
}
