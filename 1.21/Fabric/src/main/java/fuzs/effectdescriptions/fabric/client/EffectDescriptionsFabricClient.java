package fuzs.effectdescriptions.fabric.client;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.EffectDescriptionsClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class EffectDescriptionsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EffectDescriptions.MOD_ID, EffectDescriptionsClient::new);
    }
}
