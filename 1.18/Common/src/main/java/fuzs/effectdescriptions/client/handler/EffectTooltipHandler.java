package fuzs.effectdescriptions.client.handler;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.core.ClientAbstractions;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.puzzleslib.api.client.screen.v2.DeferredTooltipRendering;
import fuzs.puzzleslib.api.client.screen.v2.ScreenHelper;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableBoolean;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

import java.util.Collection;
import java.util.List;

public class EffectTooltipHandler {
    private static int renderingMobEffects = -1;

    public static EventResult onInventoryMobEffects(Screen screen, int availableSpace, MutableBoolean smallWidgets, MutableInt horizontalOffset) {

        if (!EffectDescriptions.CONFIG.get(ClientConfig.class).addDescriptionsToWidgetTooltips || ModLoaderEnvironment.INSTANCE.isModLoaded("jeed")) return EventResult.PASS;

        Minecraft minecraft = ScreenHelper.INSTANCE.getMinecraft(screen);
        Collection<MobEffectInstance> activeEffects = minecraft.player.getActiveEffects();

        int verticalOffset = 33;
        if (activeEffects.size() > 5) {
            verticalOffset = 132 / (activeEffects.size() - 1);
        }

        final int mouseX = ScreenHelper.INSTANCE.getMouseX(minecraft);
        final int mouseY = ScreenHelper.INSTANCE.getMouseY(minecraft);

        List<MobEffectInstance> filteredEffects = activeEffects.stream().filter(ClientAbstractions.INSTANCE::shouldRenderEffect).sorted().toList();

        int widgetWidth = !smallWidgets.getAsBoolean() ? 121 : 33;

        if (mouseX >= horizontalOffset.getAsInt() && mouseX <= horizontalOffset.getAsInt() + widgetWidth) {
            int posY = ScreenHelper.INSTANCE.getTopPos((AbstractContainerScreen<?>) screen);
            MobEffectInstance mobeffectinstance = null;

            for (MobEffectInstance mobeffectinstance1 : filteredEffects) {
                if (mouseY >= posY && mouseY <= posY + verticalOffset) {
                    mobeffectinstance = mobeffectinstance1;
                }

                posY += verticalOffset;
            }

            if (mobeffectinstance != null) {

                List<FormattedCharSequence> lines = Lists.newArrayList();
                MobEffect mobEffect = mobeffectinstance.getEffect();

                if (smallWidgets.getAsBoolean() || EffectDescriptions.CONFIG.get(ClientConfig.class).alwaysAddEffectNameToTooltips) {

                    MutableComponent effectComponent = new TranslatableComponent(mobeffectinstance.getDescriptionId());
                    if (mobeffectinstance.getAmplifier() > 0) {
                        effectComponent = new TranslatableComponent("potion.withAmplifier", effectComponent, new TranslatableComponent("potion.potency." + mobeffectinstance.getAmplifier()));
                    }

                    if (smallWidgets.getAsBoolean()) {
                        renderingMobEffects = ScreenHelper.INSTANCE.getFont(screen).width(effectComponent);
                    }

                    Component durationComponent = new TextComponent("(").append(MobEffectUtil.formatDuration(mobeffectinstance, 1.0F)).append(")").withStyle(getMobEffectColor(mobEffect));
                    effectComponent.append(new TextComponent(" ")).append(durationComponent);
                    lines.add(effectComponent.getVisualOrderText());
                }

                Component descriptionComponent = ItemTooltipHandler.getEffectDescriptionComponent(mobeffectinstance.getDescriptionId(), true);
                if (descriptionComponent != null) lines.addAll(DeferredTooltipRendering.splitTooltip(minecraft, descriptionComponent));

                if (EffectDescriptions.CONFIG.get(ClientConfig.class).addInternalIdToWidgetTooltips) {

                    lines.add(new TextComponent(Registry.MOB_EFFECT.getKey(mobEffect).toString()).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
                }

                if (EffectDescriptions.CONFIG.get(ClientConfig.class).addModNameToWidgetTooltips) {

                    ModLoaderEnvironment.INSTANCE.getModName(Registry.MOB_EFFECT.getKey(mobEffect).getNamespace())
                            .map(s -> new TextComponent(s).withStyle(ChatFormatting.BLUE))
                            .ifPresent(component -> {
                                lines.add(component.getVisualOrderText());
                            });
                }

                if (!lines.isEmpty()) DeferredTooltipRendering.setTooltipForNextRenderPass(lines);
            }
        }

        return EventResult.PASS;
    }

    private static ChatFormatting getMobEffectColor(MobEffect mobEffect) {
        return switch (mobEffect.getCategory()) {
            case BENEFICIAL -> ChatFormatting.BLUE;
            case HARMFUL -> ChatFormatting.RED;
            default -> ChatFormatting.GOLD;
        };
    }

    public static EventResult onRenderTooltip(PoseStack poseStack, int mouseX, int mouseY, int screenWidth, int screenHeight, Font font, List<ClientTooltipComponent> components) {

        if (renderingMobEffects != -1) {

            if (components.size() == 2 && components.get(1) instanceof ClientTextTooltip) {

                if (components.get(0) instanceof ClientTextTooltip tooltip && tooltip.getWidth(font) == renderingMobEffects) {

                    return EventResult.INTERRUPT;
                }
            }

            renderingMobEffects = -1;
        }

        return EventResult.PASS;
    }
}
