package fuzs.effectdescriptions.neoforge;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(EffectDescriptions.MOD_ID)
public class EffectDescriptionsNeoForge {

    public EffectDescriptionsNeoForge() {
        ModConstructor.construct(EffectDescriptions.MOD_ID, EffectDescriptions::new);
    }
}
