package fuzs.effectdescriptions.client.handler;

import fuzs.effectdescriptions.config.ItemDescriptionMode;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class TooltipDescriptionsHandler<T> {

    public void onItemTooltip(ItemStack itemStack, List<Component> tooltipLines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        ItemDescriptionMode itemDescriptionMode = this.getItemDescriptionMode();

        if (itemDescriptionMode == ItemDescriptionMode.NEVER && !itemDescriptionMode.isActive()) {
            return;
        }

        Map<String, T> descriptionIds = this.getByDescriptionId(itemStack);

        if (!descriptionIds.isEmpty()) {
            MutableBoolean mutableBoolean = new MutableBoolean(true);

            for (int i = 0; i < tooltipLines.size(); i++) {
                TranslatableContents contents = getTranslatableContents(tooltipLines.get(i));

                if (contents != null && descriptionIds.containsKey(contents.getKey())) {
                    T t = descriptionIds.get(contents.getKey());
                    Component component = this.getValueComponent(t);

                    if (component != null) {
                        tooltipLines.set(i, component);
                    }

                    if (itemDescriptionMode.isActive()) {
                        List<Component> list = this.getItemTooltipLines(t);
                        tooltipLines.addAll(i + 1, list);
                        i += list.size();
                    } else if (mutableBoolean.isTrue()) {
                        // make sure the view description line is only added when there will actually be a description
                        mutableBoolean.setFalse();
                        itemDescriptionMode.processTooltipLines(itemStack, tooltipLines, tooltipFlag);

                        // if vanilla tooltip lines are not replaced, we can terminate early
                        if (component == null) {
                            break;
                        }
                    }
                }
            }
        }
    }

    protected abstract ItemDescriptionMode getItemDescriptionMode();

    protected abstract Map<String, T> getByDescriptionId(ItemStack itemStack);

    @Nullable
    protected Component getValueComponent(T t) {
        return null;
    }

    protected abstract List<Component> getItemTooltipLines(T t);

    @Nullable
    protected static TranslatableContents getTranslatableContents(Component component) {
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            return getNestedTranslatableContents(translatableContents);
        } else if (!component.getSiblings().isEmpty()) {
            return getTranslatableContents(component.getSiblings().getFirst());
        } else {
            return null;
        }
    }

    private static TranslatableContents getNestedTranslatableContents(TranslatableContents contents) {
        if (contents.getArgs().length != 0 && contents.getArgs()[0] instanceof Component component
                && component.getContents() instanceof TranslatableContents innerContents) {
            return getNestedTranslatableContents(innerContents);
        } else {
            return contents;
        }
    }

    public static Set<String> getAllTranslationKeys(List<Component> tooltipLines) {
        return tooltipLines.stream().mapMulti((Component component, Consumer<TranslatableContents> consumer) -> {
            TranslatableContents contents = TooltipDescriptionsHandler.getTranslatableContents(component);
            if (contents != null) {
                consumer.accept(contents);
            }
        }).map(TranslatableContents::getKey).collect(Collectors.toSet());
    }
}
