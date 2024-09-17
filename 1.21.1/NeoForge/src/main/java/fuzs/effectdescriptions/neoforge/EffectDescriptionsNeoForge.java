package fuzs.effectdescriptions.neoforge;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.data.client.ModLanguageProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(EffectDescriptions.MOD_ID)
public class EffectDescriptionsNeoForge {

    public EffectDescriptionsNeoForge() {
        ModConstructor.construct(EffectDescriptions.MOD_ID, EffectDescriptions::new);
        DataProviderHelper.registerDataProviders(EffectDescriptions.MOD_ID, ModLanguageProvider::new);
    }
}
