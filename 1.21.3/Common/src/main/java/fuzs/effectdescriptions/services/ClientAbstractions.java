package fuzs.effectdescriptions.services;

import fuzs.puzzleslib.api.core.v1.ServiceProviderHelper;
import net.minecraft.world.effect.MobEffectInstance;

public interface ClientAbstractions {
    ClientAbstractions INSTANCE = ServiceProviderHelper.load(ClientAbstractions.class);

    boolean shouldRenderEffect(MobEffectInstance effectInstance);
}
