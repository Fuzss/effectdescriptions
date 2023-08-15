package fuzs.effectdescriptions.client.handler;

import com.google.common.collect.Lists;
import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.core.ClientAbstractions;
import fuzs.effectdescriptions.client.helper.EffectLinesHelper;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.puzzleslib.api.client.screen.v2.ScreenHelper;
import fuzs.puzzleslib.api.client.screen.v2.ScreenTooltipFactory;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableBoolean;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Collection;
import java.util.List;

public class InventoryTooltipHandler {
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

        int widgetWidth = !smallWidgets.getAsBoolean() ? 121 : 33;

        if (mouseX >= horizontalOffset.getAsInt() && mouseX <= horizontalOffset.getAsInt() + widgetWidth) {

            int posY = ScreenHelper.INSTANCE.getTopPos((AbstractContainerScreen<?>) screen);
            MobEffectInstance hovered = null;
            List<MobEffectInstance> effects = activeEffects.stream().filter(ClientAbstractions.INSTANCE::shouldRenderEffect).sorted().toList();
            for (MobEffectInstance mobEffectInstance : effects) {

                if (mouseY >= posY && mouseY <= posY + verticalOffset) {
                    hovered = mobEffectInstance;
                }

                posY += verticalOffset;
            }

            if (hovered != null) {

                List<Component> lines = Lists.newArrayList();
                EffectLinesHelper.tryAddDisplayName(lines, hovered, smallWidgets.getAsBoolean()).ifPresent(component -> renderingMobEffects = ScreenHelper.INSTANCE.getFont(screen).width(component));
                EffectLinesHelper.getEffectDescriptionComponent(hovered.getDescriptionId(), true).ifPresent(lines::add);
                EffectLinesHelper.tryAddAttributes(lines, hovered);
                EffectLinesHelper.tryAddInternalName(lines, hovered);
                EffectLinesHelper.tryAddModName(lines, hovered);

                if (!lines.isEmpty()) screen.setTooltipForNextRenderPass(ScreenTooltipFactory.create(lines), DefaultTooltipPositioner.INSTANCE, true);
            }
        }

        return EventResult.PASS;
    }

    public static EventResult onRenderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, int screenWidth, int screenHeight, Font font, List<ClientTooltipComponent> components, ClientTooltipPositioner positioner) {

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
