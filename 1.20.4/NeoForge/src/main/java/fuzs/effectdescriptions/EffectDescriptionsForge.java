package fuzs.effectdescriptions;

import fuzs.effectdescriptions.data.ModLanguageProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EffectDescriptions.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EffectDescriptionsForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(EffectDescriptions.MOD_ID, EffectDescriptions::new);
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        final DataGenerator dataGenerator = evt.getGenerator();
        dataGenerator.addProvider(evt.includeClient(), new ModLanguageProvider(evt, EffectDescriptions.MOD_ID));
    }
}
