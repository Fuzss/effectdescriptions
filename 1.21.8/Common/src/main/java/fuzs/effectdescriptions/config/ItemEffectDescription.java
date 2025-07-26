package fuzs.effectdescriptions.config;

import fuzs.effectdescriptions.EffectDescriptions;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public enum ItemEffectDescription {
    NEVER {
        @Override
        public boolean isActive() {
            return false;
        }
    },
    SHIFT {
        @Override
        public boolean isActive() {
            return Screen.hasShiftDown();
        }

        @Override
        public void processTooltipLines(ItemStack itemStack, List<Component> tooltipLines, TooltipFlag tooltipFlag) {
            tooltipLines.add(this.getLineIndex(itemStack, tooltipLines, tooltipFlag), VIEW_DESCRIPTIONS_COMPONENT);
        }

        private int getLineIndex(ItemStack itemStack, List<Component> tooltipLines, TooltipFlag tooltipFlag) {
            int lineIndex = -1;

            if (tooltipFlag.isAdvanced()) {
                // add this just before the 'dev-only' tooltip lines
                Component component = Component.literal(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString())
                        .withStyle(ChatFormatting.DARK_GRAY);
                // also this is probably the most reliable way instead of using fixes indices regarding mod interference
                lineIndex = tooltipLines.lastIndexOf(component);
            }

            if (lineIndex == -1) {
                return tooltipLines.size();
            } else {
                return lineIndex;
            }
        }
    },
    ALWAYS {
        @Override
        public boolean isActive() {
            return true;
        }
    };

    public static final Component SHIFT_COMPONENT = Component.translatable(Util.makeDescriptionId("gui",
            EffectDescriptions.id("tooltip.shift"))).withStyle(ChatFormatting.LIGHT_PURPLE);
    public static final Component VIEW_DESCRIPTIONS_COMPONENT = Component.translatable(Util.makeDescriptionId("gui",
            EffectDescriptions.id("tooltip.view_descriptions")), SHIFT_COMPONENT).withStyle(ChatFormatting.GRAY);

    public abstract boolean isActive();

    public void processTooltipLines(ItemStack itemStack, List<Component> tooltipLines, TooltipFlag tooltipFlag) {
        // NO-OP
    }
}
