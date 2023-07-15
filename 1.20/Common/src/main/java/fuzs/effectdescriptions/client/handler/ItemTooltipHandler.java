package fuzs.effectdescriptions.client.handler;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.config.ClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemTooltipHandler {

    public static void onItemTooltip(ItemStack stack, @Nullable Player player, List<Component> lines, TooltipFlag context) {
        if (!EffectDescriptions.CONFIG.get(ClientConfig.class).addDescriptionsToItemTooltips) return;
        if (!EffectDescriptions.CONFIG.get(ClientConfig.class).effectDescriptionItems.contains(stack.getItem())) return;
        if (EffectDescriptions.CONFIG.get(ClientConfig.class).holdShiftForItemDescriptions && !Screen.hasShiftDown()) return;
        Set<String> ids = getMobEffects(stack).stream().map(MobEffectInstance::getDescriptionId).collect(Collectors.toSet());
        for (int i = 0; i < lines.size(); i++) {
            Component line = lines.get(i);
            if (line.getContents() instanceof TranslatableContents contents) {
                while (contents.getArgs().length != 0 && contents.getArgs()[0] instanceof Component component && component.getContents() instanceof TranslatableContents innerContents) {
                    contents = innerContents;
                }
                String id = contents.getKey();
                if (ids.contains(id)) {
                    Component component = getEffectDescriptionComponent(id, false);
                    if (component != null) {
                        lines.add(i + 1, component);
                    }
                }
            }
        }
    }

    private static List<MobEffectInstance> getMobEffects(ItemStack stack) {
        if (stack.is(Items.SUSPICIOUS_STEW)) {
            List<MobEffectInstance> suspiciousStewEffects = Lists.newArrayList();
            getSuspiciousStewPotionEffects(stack, suspiciousStewEffects::add);
            return suspiciousStewEffects;
        }
        return PotionUtils.getMobEffects(stack);
    }

    private static void getSuspiciousStewPotionEffects(ItemStack itemStack, Consumer<MobEffectInstance> consumer) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null && compoundTag.contains("Effects", 9)) {
            ListTag listTag = compoundTag.getList("Effects", 10);

            for(int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundTag2 = listTag.getCompound(i);
                int j;
                if (compoundTag2.contains("EffectDuration", 99)) {
                    j = compoundTag2.getInt("EffectDuration");
                } else {
                    j = 160;
                }

                MobEffect mobEffect = MobEffect.byId(compoundTag2.getInt("EffectId"));
                if (mobEffect != null) {
                    consumer.accept(new MobEffectInstance(mobEffect, j));
                }
            }
        }
    }

    @Nullable
    public static Component getEffectDescriptionComponent(String id, boolean preventIndentation) {
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

            return component.withStyle(ChatFormatting.GRAY);
        }
        return null;
    }
}
