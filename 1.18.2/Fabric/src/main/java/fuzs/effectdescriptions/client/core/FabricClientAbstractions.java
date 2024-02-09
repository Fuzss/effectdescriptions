package fuzs.effectdescriptions.client.core;

import net.minecraft.world.effect.MobEffectInstance;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public boolean shouldRenderEffect(MobEffectInstance effectInstance) {
        return true;
    }
}
