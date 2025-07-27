package fuzs.effectdescriptions.client.gui.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

public abstract class TooltipComponentExtractor<T, C> {
    private final DataComponentType<C> dataComponentType;

    TooltipComponentExtractor(DataComponentType<C> dataComponentType) {
        this.dataComponentType = dataComponentType;
    }

    protected abstract boolean isEnabled();

    protected abstract Stream<T> extractFromComponent(C dataComponent);

    public Stream<T> extractFromItemStack(ItemStack itemStack) {
        if (this.isEnabled() && itemStack.has(this.dataComponentType)) {
            C dataComponent = itemStack.get(this.dataComponentType);
            return this.extractFromComponent(dataComponent);
        } else {
            return Stream.empty();
        }
    }
}
