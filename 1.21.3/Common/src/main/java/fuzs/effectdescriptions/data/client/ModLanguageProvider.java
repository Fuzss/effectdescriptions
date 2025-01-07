package fuzs.effectdescriptions.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        addMobEffect(builder, MobEffects.MOVEMENT_SPEED, "Increases walking speed.");
        addMobEffect(builder, MobEffects.MOVEMENT_SLOWDOWN, "Decreases walking speed.");
        addMobEffect(builder, MobEffects.DIG_SPEED, "Increases mining and attack speed.");
        addMobEffect(builder, MobEffects.DIG_SLOWDOWN, "Decreases mining and attack speed.");
        addMobEffect(builder, MobEffects.DAMAGE_BOOST, "Increases melee damage dealt.");
        addMobEffect(builder, MobEffects.HEAL, "Heals living entities, and damages undead.");
        addMobEffect(builder, MobEffects.HARM, "Damages living entities, and heals undead.");
        addMobEffect(builder, MobEffects.JUMP, "Increases jump height and reduces fall damage.");
        addMobEffect(builder, MobEffects.CONFUSION, "Wobbles and warps the screen.");
        addMobEffect(builder, MobEffects.REGENERATION, "Regenerates health over time.");
        addMobEffect(builder, MobEffects.DAMAGE_RESISTANCE, "Reduces incoming damage.");
        addMobEffect(builder,
                MobEffects.FIRE_RESISTANCE,
                "Prevents taking damage from fire sources, like fire and lava.");
        addMobEffect(builder, MobEffects.WATER_BREATHING, "Prevents losing air underwater and drowning.");
        addMobEffect(builder, MobEffects.INVISIBILITY, "Grants invisibility, and reduces other mobs' detection range.");
        addMobEffect(builder,
                MobEffects.BLINDNESS,
                "Impairs vision and disables the ability to sprint and critical hit.");
        addMobEffect(builder, MobEffects.NIGHT_VISION, "Lets the player see well in darkness and underwater.");
        addMobEffect(builder, MobEffects.HUNGER, "Depletes hunger and saturation.");
        addMobEffect(builder, MobEffects.WEAKNESS, "Decreases melee damage dealt.");
        addMobEffect(builder, MobEffects.POISON, "Inflicts non-lethal damage over time.");
        addMobEffect(builder, MobEffects.WITHER, "Inflicts potentially lethal damage over time.");
        addMobEffect(builder, MobEffects.HEALTH_BOOST, "Increases maximum health.");
        addMobEffect(builder, MobEffects.ABSORPTION, "Temporarily adds additional hearts that can't be regenerated.");
        addMobEffect(builder, MobEffects.SATURATION, "Restores hunger and saturation.");
        addMobEffect(builder, MobEffects.GLOWING, "Outlines the affected entity.");
        addMobEffect(builder, MobEffects.LEVITATION, "Floats the affected entity upward.");
        addMobEffect(builder, MobEffects.LUCK, "Can increase chances of high-quality and more loot.");
        addMobEffect(builder, MobEffects.UNLUCK, "Can reduce chances of high-quality and more loot.");
        addMobEffect(builder, MobEffects.SLOW_FALLING, "Decreases falling speed and negates fall damage.");
        addMobEffect(builder,
                MobEffects.CONDUIT_POWER,
                "Prevents drowning, increases underwater visibility and mining speed.");
        addMobEffect(builder, MobEffects.DOLPHINS_GRACE, "Increases swimming speed.");
        addMobEffect(builder, MobEffects.BAD_OMEN, "Causes an illager raid to start upon entering a village.");
        addMobEffect(builder,
                MobEffects.HERO_OF_THE_VILLAGE,
                "Gives discounts on trades with villagers, and makes villagers throw gifts.");
        addMobEffect(builder, MobEffects.DARKNESS, "Darkens the screen.");
        builder.add("effect.deeperdarker.sculk_affinity.desc",
                "Prevents the player from emitting vibrations and being heard.");
        builder.add("effect.alexscaves.deepsight.desc", "Increases visibility underwater. May conflict with shaders.");
        builder.add("effect.alexscaves.magnetizing.desc",
                "Renders the inflicted with the ability to be pushed or pulled by magnetic forces without wearing metal armor.");
        builder.add("effect.alexsmobs.bug_pheromones.desc", "Make hostile arthropods neutral to the user.");
        builder.add("effect.alexsmobs.clinging.desc", "Allows walking upside-down on ceilings.");
        builder.add("effect.alexsmobs.debilitating_sting.desc",
                "An Effect which poisons the victim but paralyzes arthropods. It will stop if the victim is at half health and is not an arthropod. Inflicted by a Tarantula Hawk.");
        builder.add("effect.alexsmobs.ender_flu.desc",
                "After the effect runs out naturally); the user takes massive damage and an Enderiophage will spawn. Inflicted by an Enderiophage.");
        builder.add("effect.alexsmobs.exsanguination.desc", "Gradually drains health.");
        builder.add("effect.alexsmobs.fear.desc",
                "Makes the target unable to move. Effect applied when a Tiger reveals itself to the user.");
        builder.add("effect.alexsmobs.knockback_resistance.desc", "Provides 50% knockback resistance.");
        builder.add("effect.alexsmobs.lava_vision.desc", "Allows seeing through lava.");
        builder.add("effect.alexsmobs.oiled.desc",
                "Oil floats on water. Cover yourself in oil to float on the surface of water and outside in the rain.");
        builder.add("effect.alexsmobs.orcas_might.desc",
                "Grants increased attack speed. Effect may be applied when swimming with an Orca.");
        builder.add("effect.alexsmobs.poison_resistance.desc", "Grants immunity to poison.");
        builder.add("effect.alexsmobs.soulsteal.desc", "Grants the user attacks some life-steal.");
        builder.add("effect.alexsmobs.sunbird_blessing.desc",
                "Decreases fall speed); prevents fall damage and makes it easier to fly with an Elytra or Tarantula Hawk Elytra. Effect is applied when near a Sunbird.");
        builder.add("effect.alexsmobs.sunbird_curse.desc",
                "Increases fall speed); causing the user to hit the ground quicker. This effectively impedes the use of an Elytra. Effect is applied when attacking a Sunbird.");
        builder.add("effect.alexsmobs.tigers_blessing.desc",
                "Tigers near the user will be neutral and will protect them from hostile foes. The effect will go away early when attacking a Tiger. Applied by feeding a Tiger chicken or pork chops.");
        builder.add("effect.alexsmobs.earthquake.desc", "Shakes the user violently.");
        builder.add("effect.alexsmobs.fleet_footed.desc", "Increases player speed whilst jumping and sprinting.");
        builder.add("effect.alexsmobs.power_down.desc", "Hello, hello? Uhh, I wanted to record a message for you...");
        builder.add("effect.alexsmobs.mosquito_repellent.desc", "Makes Crimson Mosquitoes avoid the player.");
        builder.add("effect.farmersdelight.nourishment.desc", "Prevents hunger and saturation loss unless damaged.");
        builder.add("effect.farmersdelight.comfort.desc", "Allows health regeneration with low hunger.");
        builder.add("effect.farmersdelight.nourishment.description",
                "Prevents hunger and saturation loss unless damaged.");
        builder.add("effect.farmersdelight.comfort.description", "Allows health regeneration with low hunger.");
        builder.add("effect.farmersrespite.caffeinated.desc", "Increases movement and attack speed");
        builder.add("effect.farmersrespite.caffeinated.description", "Increases movement and attack speed");
        builder.add("effect.mynethersdelight.g_pungent.desc",
                "Alleviates burning, regenerate health near heat sources with fire protection armor or fire resistance effect.");
        builder.add("effect.mynethersdelight.b_pungent.desc",
                "Alleviates burning, regenerate health near heat sources with fire protection armor or fire resistance effect.");
        builder.add("effect.mynethersdelight.g_pungent.description",
                "Alleviates burning, regenerate health near heat sources with fire protection armor or fire resistance effect.");
        builder.add("effect.mynethersdelight.b_pungent.description",
                "Alleviates burning, regenerate health near heat sources with fire protection armor or fire resistance effect.");
        builder.add("effect.supplementaries.overencumbered.desc",
                "Prevents sprinting. Further levels reduce jump height hinder walking speed respectively");
        builder.add("effect.via_romana.travellers_fatigue.desc", "Prevents fast traveling.");
        builder.add("effect.via_romana.travellers_fatigue.description", "Prevents fast traveling.");
        builder.add("effect.enderzoology.displacement.desc", "Randomly teleports opponents upon receiving damage.");
        builder.add("effect.longshot.longshot.description", "Increases projectile velocity and damage dealt.");
    }

    static void addMobEffect(TranslationBuilder builder, Holder<MobEffect> mobEffect, String value) {
        builder.add(mobEffect.value(), "desc", value);
    }
}
