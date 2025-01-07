package fuzs.effectdescriptions.fabric.services;

import fuzs.effectdescriptions.services.ClientAbstractions;
import net.minecraft.world.effect.MobEffectInstance;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public boolean shouldRenderEffect(MobEffectInstance effectInstance) {
        return true;
    }
}
