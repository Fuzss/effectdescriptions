package fuzs.effectdescriptions.client.handler;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.helper.EffectLinesHelper;
import fuzs.effectdescriptions.config.ClientConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
                    int index = i + 1;
                    EffectLinesHelper.getEffectDescriptionComponent(id, false).ifPresent(component -> lines.add(index, component));
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
        if (compoundTag != null && compoundTag.contains(SuspiciousStewItem.EFFECTS_TAG, Tag.TAG_LIST)) {
            SuspiciousEffectHolder.EffectEntry.LIST_CODEC.parse(NbtOps.INSTANCE, compoundTag.getList("effects", 10)).result().ifPresent((list) -> {
                list.forEach(effectEntry -> consumer.accept(effectEntry.createEffectInstance()));
            });
        }
    }
}
