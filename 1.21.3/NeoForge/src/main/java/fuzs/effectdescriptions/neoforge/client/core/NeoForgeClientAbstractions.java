package fuzs.effectdescriptions.neoforge.client.core;

import fuzs.effectdescriptions.client.core.ClientAbstractions;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.client.ClientHooks;

public class NeoForgeClientAbstractions implements ClientAbstractions {

    @Override
    public boolean shouldRenderEffect(MobEffectInstance effectInstance) {
        return ClientHooks.shouldRenderEffect(effectInstance);
    }
}
