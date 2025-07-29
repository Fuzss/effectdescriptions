package fuzs.tooltipinsights.client.handler;

import fuzs.puzzleslib.api.client.event.v1.AddResourcePackReloadListenersCallback;
import fuzs.puzzleslib.api.init.v3.registry.LookupHelper;
import fuzs.tooltipinsights.client.gui.tooltip.DescriptionLines;
import fuzs.tooltipinsights.config.ItemDescriptionMode;
import fuzs.tooltipinsights.impl.TooltipInsights;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
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

    public static <T> void printMissingDescriptionsWarning(ResourceKey<? extends Registry<? super T>> registryKey, Function<T, String> descriptionIdGetter) {
        Registry<T> registry = LookupHelper.getRegistry(registryKey).orElseThrow();
        AddResourcePackReloadListenersCallback.EVENT.register((BiConsumer<ResourceLocation, PreparableReloadListener> consumer) -> {
            ResourceLocation resourceLocation = TooltipInsights.id(registryKey.location().toString().replace(':', '/'));
            consumer.accept(resourceLocation, (ResourceManagerReloadListener) (ResourceManager resourceManager) -> {
                registry.listElements().forEach((Holder.Reference<T> holder) -> {
                    String translationKey = descriptionIdGetter.apply(holder.value());
                    if (DescriptionLines.getDescriptionTranslationKey(translationKey) == null) {
                        TooltipInsights.LOGGER.warn("Missing description for {}: {}",
                                holder.key(),
                                translationKey + ".desc");
                    }
                });
            });
        });
    }
}
