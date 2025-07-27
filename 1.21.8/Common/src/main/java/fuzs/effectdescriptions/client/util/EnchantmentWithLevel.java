package fuzs.effectdescriptions.client.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

public record EnchantmentWithLevel(Holder<Enchantment> enchantment, int level) {

    public EnchantmentWithLevel(Object2IntMap.Entry<Holder<Enchantment>> entry) {
        this(entry.getKey(), entry.getIntValue());
    }
}
