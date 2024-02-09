package fuzs.effectdescriptions.forge.client.core;

import fuzs.effectdescriptions.client.core.ClientAbstractions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.ForgeHooksClient;

public class ForgeClientAbstractions implements ClientAbstractions {

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public boolean shouldRenderEffect(MobEffectInstance effectInstance) {
        return ForgeHooksClient.shouldRenderEffect(effectInstance);
    }
}
