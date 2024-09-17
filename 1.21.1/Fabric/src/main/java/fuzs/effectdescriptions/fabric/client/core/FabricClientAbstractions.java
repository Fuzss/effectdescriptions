package fuzs.effectdescriptions.fabric.client.core;

import fuzs.effectdescriptions.client.core.ClientAbstractions;
import net.minecraft.world.effect.MobEffectInstance;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public boolean shouldRenderEffect(MobEffectInstance effectInstance) {
        return true;
    }
}
