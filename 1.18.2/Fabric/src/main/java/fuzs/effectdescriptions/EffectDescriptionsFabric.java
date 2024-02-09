package fuzs.effectdescriptions;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class EffectDescriptionsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(EffectDescriptions.MOD_ID, EffectDescriptions::new);
    }
}
