package fuzs.effectdescriptions.client.handler;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.helper.EffectTooltipSuppliers;
import fuzs.effectdescriptions.config.ClientConfig;
import fuzs.puzzleslib.api.client.gui.v2.ScreenHelper;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.ClientComponentSplitter;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableBoolean;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class EffectWidgetHandler {
    @Nullable
    private static List<FormattedCharSequence> tooltipLines;

    public static EventResult onInventoryMobEffects(Screen screen, int availableSpace, MutableBoolean smallWidgets, MutableInt horizontalOffset) {
        if (!EffectDescriptions.CONFIG.get(ClientConfig.class).widgetTooltips) return EventResult.PASS;
        if (!(screen instanceof AbstractContainerScreen<?> abstractContainerScreen)) return EventResult.PASS;
        if (isInWidgetsColumn(horizontalOffset.getAsInt(), smallWidgets.getAsBoolean())) {
            MobEffectInstance mobEffect = getMobEffectInRow(abstractContainerScreen);
            if (mobEffect != null) {
                List<Component> list = EffectTooltipSuppliers.getMobEffectComponents(mobEffect);
                tooltipLines = ClientComponentSplitter.splitTooltipLines(list).toList();
            }
        }
        return EventResult.PASS;
    }

    public static void onDrawForeground(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (tooltipLines != null) {
            guiGraphics.setTooltipForNextFrame(screen.getFont(),
                    tooltipLines,
                    DefaultTooltipPositioner.INSTANCE,
                    mouseX,
                    mouseY,
                    true);
            tooltipLines = null;
        }
    }

    private static boolean isInWidgetsColumn(int horizontalOffset, boolean smallWidgets) {
        int mouseX = ScreenHelper.getMouseX();
        int widgetWidth = !smallWidgets ? 121 : 33;
        return mouseX >= horizontalOffset && mouseX <= horizontalOffset + widgetWidth;
    }

    private static MobEffectInstance getMobEffectInRow(AbstractContainerScreen<?> screen) {
        int mouseY = ScreenHelper.getMouseY();
        int posY = screen.topPos;
        Collection<MobEffectInstance> mobEffects = getVisibleMobEffects();
        int verticalOffset = getVerticalOffset(mobEffects);
        MobEffectInstance hoveredMobEffect = null;
        for (MobEffectInstance mobEffect : mobEffects) {
            if (mouseY >= posY && mouseY <= posY + verticalOffset) {
                hoveredMobEffect = mobEffect;
            }
            posY += verticalOffset;
        }
        return hoveredMobEffect;
    }

    private static Collection<MobEffectInstance> getVisibleMobEffects() {
        return Minecraft.getInstance().player.getActiveEffects()
                .stream()
                .filter(ScreenHelper::isEffectVisibleInInventory)
                .sorted()
                .toList();
    }

    private static int getVerticalOffset(Collection<MobEffectInstance> mobEffects) {
        if (mobEffects.size() > 5) {
            return 132 / (mobEffects.size() - 1);
        } else {
            return 33;
        }
    }

    public static void onGatherEffectScreenTooltip(AbstractContainerScreen<?> screen, MobEffectInstance mobEffectInstance, List<Component> tooltipLines) {
        if (EffectDescriptions.CONFIG.get(ClientConfig.class).widgetTooltips) {
            tooltipLines.clear();
        }
    }
}
