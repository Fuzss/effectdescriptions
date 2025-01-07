package fuzs.effectdescriptions.client.helper;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public final class EffectTooltipSuppliers {
    public static final MobEffectComponentProvider NAME = register(() -> EffectDescriptions.CONFIG.get(ClientConfig.class).name,
            (List<? extends Component> list, Integer index) -> {
                if (index != -1) {
                    return list.subList(0, index);
                } else {
                    return list;
                }
            });
    public static final MobEffectComponentProvider DESCRIPTION = new MobEffectComponentProviderImpl(() -> EffectDescriptions.CONFIG.get(
            ClientConfig.class).description, (MobEffectInstance mobEffectInstance) -> {
        String translationKey = getDescriptionTranslationKey(mobEffectInstance.getDescriptionId());
        if (translationKey != null) {
            return Collections.singletonList((Component) Component.translatable(translationKey)
                    .withStyle(ChatFormatting.GRAY));
        } else {
            return Collections.emptyList();
        }
    });
    public static final MobEffectComponentProvider ATTRIBUTES = register(() -> EffectDescriptions.CONFIG.get(
            ClientConfig.class).attributes, (List<? extends Component> list, Integer index) -> {
        if (index != -1) {
            return list.subList(index, list.size());
        } else {
            return Collections.emptyList();
        }
    });
    public static final MobEffectComponentProvider MOD_NAME = register(() -> EffectDescriptions.CONFIG.get(ClientConfig.class).modName,
            (ResourceLocation resourceLocation) -> {
                return ModLoaderEnvironment.INSTANCE.getModContainer(resourceLocation.getNamespace())
                        .map(ModContainer::getDisplayName)
                        .map((String s) -> Component.literal(s).withStyle(ChatFormatting.BLUE))
                        .map(Collections::singletonList)
                        .orElseGet(Collections::emptyList);
            });
    public static final MobEffectComponentProvider INTERNAL_NAME = register(() -> EffectDescriptions.CONFIG.get(
            ClientConfig.class).internalName, (ResourceLocation resourceLocation) -> {
        return Collections.singletonList(Component.literal(resourceLocation.toString())
                .withStyle(ChatFormatting.DARK_GRAY));
    });
    private static final List<MobEffectComponentProvider> EFFECT_TOOLTIP_SUPPLIERS = List.of(NAME,
            DESCRIPTION,
            ATTRIBUTES,
            MOD_NAME,
            INTERNAL_NAME);

    private EffectTooltipSuppliers() {
        // NO-OP
    }

    @Nullable
    private static String getDescriptionTranslationKey(String translationKey) {
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

    private static MobEffectComponentProviderImpl register(BooleanSupplier isEnabled, BiFunction<List<? extends Component>, Integer, List<? extends Component>> extractor) {
        return new MobEffectComponentProviderImpl(isEnabled, (MobEffectInstance mobEffectInstance) -> {
            List<Component> list = new ArrayList<>();
            TickRateManager tickRateManager = Minecraft.getInstance().level.tickRateManager();
            PotionContents.addPotionTooltip(Collections.singleton(mobEffectInstance),
                    list::add,
                    1.0F,
                    tickRateManager.tickrate());
            int index = list.indexOf(CommonComponents.EMPTY);
            return extractor.apply(list, index);
        });
    }

    private static MobEffectComponentProviderImpl register(BooleanSupplier isEnabled, Function<ResourceLocation, List<? extends Component>> extractor) {
        return new MobEffectComponentProviderImpl(isEnabled, (MobEffectInstance mobEffectInstance) -> {
            ResourceLocation resourceLocation = BuiltInRegistries.MOB_EFFECT.getKey(mobEffectInstance.getEffect()
                    .value());
            return extractor.apply(resourceLocation);
        });
    }

    public static List<Component> getMobEffectComponents(MobEffectInstance mobEffectInstance) {
        List<Component> list = new ArrayList<>();
        for (MobEffectComponentProvider componentProvider : EFFECT_TOOLTIP_SUPPLIERS) {
            list.addAll(componentProvider.getMobEffectComponents(mobEffectInstance));
        }
        return list;
    }

    public interface MobEffectComponentProvider {

        List<? extends Component> getRawMobEffectComponents(MobEffectInstance mobEffectInstance);

        List<? extends Component> getMobEffectComponents(MobEffectInstance mobEffectInstance);
    }

    private record MobEffectComponentProviderImpl(BooleanSupplier isEnabled,
                                                  Function<MobEffectInstance, List<? extends Component>> extractor) implements MobEffectComponentProvider {

        @Override
        public List<? extends Component> getRawMobEffectComponents(MobEffectInstance mobEffectInstance) {
            return this.extractor.apply(mobEffectInstance);
        }

        @Override
        public List<? extends Component> getMobEffectComponents(MobEffectInstance mobEffectInstance) {
            if (this.isEnabled.getAsBoolean()) {
                return this.getRawMobEffectComponents(mobEffectInstance);
            } else {
                return Collections.emptyList();
            }
        }
    }
}
