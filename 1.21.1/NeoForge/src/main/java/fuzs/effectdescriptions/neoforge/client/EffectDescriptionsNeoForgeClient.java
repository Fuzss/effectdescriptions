package fuzs.effectdescriptions.neoforge.client;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.EffectDescriptionsClient;
import fuzs.effectdescriptions.data.client.ModLanguageProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = EffectDescriptions.MOD_ID, dist = Dist.CLIENT)
public class EffectDescriptionsNeoForgeClient {

    public EffectDescriptionsNeoForgeClient() {
        ClientModConstructor.construct(EffectDescriptions.MOD_ID, EffectDescriptionsClient::new);
        DataProviderHelper.registerDataProviders(EffectDescriptions.MOD_ID, ModLanguageProvider::new);
    }
}
