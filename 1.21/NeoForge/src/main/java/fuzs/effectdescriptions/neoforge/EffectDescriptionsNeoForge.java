package fuzs.effectdescriptions.neoforge;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.data.client.ModLanguageProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EffectDescriptions.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EffectDescriptionsNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(EffectDescriptions.MOD_ID, EffectDescriptions::new);
        DataProviderHelper.registerDataProviders(EffectDescriptions.MOD_ID, ModLanguageProvider::new);
    }
}
