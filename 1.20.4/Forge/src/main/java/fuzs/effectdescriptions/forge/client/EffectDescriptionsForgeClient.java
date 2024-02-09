package fuzs.effectdescriptions.forge.client;

import fuzs.effectdescriptions.EffectDescriptions;
import fuzs.effectdescriptions.client.EffectDescriptionsClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = EffectDescriptions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EffectDescriptionsForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(EffectDescriptions.MOD_ID, EffectDescriptionsClient::new);
    }
}
