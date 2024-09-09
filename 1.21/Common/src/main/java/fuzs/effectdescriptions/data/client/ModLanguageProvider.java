package fuzs.effectdescriptions.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.effect.MobEffects;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.add(MobEffects.MOVEMENT_SPEED, "desc", "Increases walking speed.");
        builder.add(MobEffects.MOVEMENT_SLOWDOWN, "desc", "Decreases walking speed.");
        builder.add(MobEffects.DIG_SPEED, "desc", "Increases mining and attack speed.");
        builder.add(MobEffects.DIG_SLOWDOWN, "desc", "Decreases mining and attack speed.");
        builder.add(MobEffects.DAMAGE_BOOST, "desc", "Increases dealt melee damage.");
        builder.add(MobEffects.HEAL, "desc", "Heals living entities, damages undead.");
        builder.add(MobEffects.HARM, "desc", "Damages living entities, heals undead.");
        builder.add(MobEffects.JUMP, "desc", "Increases jump height and reduces fall damage.");
        builder.add(MobEffects.CONFUSION, "desc", "Wobbles and warps the screen.");
        builder.add(MobEffects.REGENERATION, "desc", "Regenerates health over time.");
        builder.add(MobEffects.DAMAGE_RESISTANCE, "desc", "Reduces incoming damage.");
        builder.add(MobEffects.FIRE_RESISTANCE, "desc", "Prevents taking damage from fire sources, like fire and lava.");
        builder.add(MobEffects.WATER_BREATHING, "desc", "Prevents losing air underwater and drowning.");
        builder.add(MobEffects.INVISIBILITY, "desc", "Grants invisibility, and reduces other mobs' detection range.");
        builder.add(MobEffects.BLINDNESS, "desc", "Impairs vision and disables the ability to sprint and critical hit.");
        builder.add(MobEffects.NIGHT_VISION, "desc", "Lets the player see well in darkness and underwater.");
        builder.add(MobEffects.HUNGER, "desc", "Increases food exhaustion.");
        builder.add(MobEffects.WEAKNESS, "desc", "Decreases dealt melee damage.");
        builder.add(MobEffects.POISON, "desc", "Inflicts damage over time, but doesn't kill.");
        builder.add(MobEffects.WITHER, "desc", "Inflicts potentially deadly damage over time.");
        builder.add(MobEffects.HEALTH_BOOST, "desc", "Increases maximum health.");
        builder.add(MobEffects.ABSORPTION, "desc", "Temporarily adds additional hearts that can't be regenerated.");
        builder.add(MobEffects.SATURATION, "desc", "Restores hunger and saturation.");
        builder.add(MobEffects.GLOWING, "desc", "Outlines the affected entity.");
        builder.add(MobEffects.LEVITATION, "desc", "Floats the affected entity upward.");
        builder.add(MobEffects.LUCK, "desc", "Can increase chances of high-quality and more loot.");
        builder.add(MobEffects.UNLUCK, "desc", "Can reduce chances of high-quality and more loot.");
        builder.add(MobEffects.SLOW_FALLING, "desc", "Decreases falling speed and negates fall damage.");
        builder.add(MobEffects.CONDUIT_POWER, "desc", "Increases underwater visibility and mining speed, prevents drowning.");
        builder.add(MobEffects.DOLPHINS_GRACE, "desc", "Increases swimming speed.");
        builder.add(MobEffects.BAD_OMEN, "desc", "Causes an illager raid to start upon entering a village.");
        builder.add(MobEffects.HERO_OF_THE_VILLAGE, "desc", "Gives discounts on trades with villagers, and makes villagers throw gifts.");
        builder.add(MobEffects.DARKNESS, "desc", "Darkens the screen.");
        builder.add("effect.deeperdarker.sculk_affinity.desc", "Prevents the player from emitting vibrations and being heard.");
        builder.add("effect.alexscaves.deepsight.desc", "Increases visibility underwater. May conflict with shaders.");
        builder.add("effect.alexscaves.magnetizing.desc", "Renders the inflicted with the ability to be pushed or pulled by magnetic forces without wearing metal armor.");
    }
}
