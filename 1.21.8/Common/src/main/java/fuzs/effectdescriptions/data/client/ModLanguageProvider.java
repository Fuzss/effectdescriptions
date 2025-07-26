package fuzs.effectdescriptions.data.client;

import fuzs.effectdescriptions.config.ItemEffectDescription;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.effect.MobEffects;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ItemEffectDescription.SHIFT_COMPONENT, "\u21E7 Shift");
        translationBuilder.add(ItemEffectDescription.VIEW_DESCRIPTIONS_COMPONENT, "Hold %s to view descriptions.");
        translationBuilder.add(MobEffects.SPEED.value(), "desc", "Increases walking speed.");
        translationBuilder.add(MobEffects.SLOWNESS.value(), "desc", "Decreases walking speed.");
        translationBuilder.add(MobEffects.HASTE.value(), "desc", "Increases mining and attack speed.");
        translationBuilder.add(MobEffects.MINING_FATIGUE.value(), "desc", "Decreases mining and attack speed.");
        translationBuilder.add(MobEffects.STRENGTH.value(), "desc", "Increases melee damage dealt.");
        translationBuilder.add(MobEffects.INSTANT_HEALTH.value(), "desc", "Heals living entities, and damages undead.");
        translationBuilder.add(MobEffects.INSTANT_DAMAGE.value(), "desc", "Damages living entities, and heals undead.");
        translationBuilder.add(MobEffects.JUMP_BOOST.value(), "desc", "Increases jump height and reduces fall damage.");
        translationBuilder.add(MobEffects.NAUSEA.value(), "desc", "Wobbles and warps the screen.");
        translationBuilder.add(MobEffects.REGENERATION.value(), "desc", "Regenerates health over time.");
        translationBuilder.add(MobEffects.RESISTANCE.value(), "desc", "Reduces incoming damage.");
        translationBuilder.add(MobEffects.FIRE_RESISTANCE.value(),
                "desc",
                "Prevents taking damage from fire sources, like fire and lava.");
        translationBuilder.add(MobEffects.WATER_BREATHING.value(),
                "desc",
                "Prevents drowning and enables breathing underwater.");
        translationBuilder.add(MobEffects.INVISIBILITY.value(),
                "desc",
                "Grants invisibility, and reduces the detection range of other mobs.");
        translationBuilder.add(MobEffects.BLINDNESS.value(),
                "desc",
                "Impairs vision by adding close black fog and disables the ability to sprint and critical hit.");
        translationBuilder.add(MobEffects.NIGHT_VISION.value(), "desc", "Improves sight in dark areas and underwater.");
        translationBuilder.add(MobEffects.HUNGER.value(), "desc", "Depletes hunger and saturation.");
        translationBuilder.add(MobEffects.WEAKNESS.value(), "desc", "Decreases melee damage dealt.");
        translationBuilder.add(MobEffects.POISON.value(), "desc", "Inflicts non-lethal damage over time.");
        translationBuilder.add(MobEffects.WITHER.value(), "desc", "Inflicts potentially lethal damage over time.");
        translationBuilder.add(MobEffects.HEALTH_BOOST.value(), "desc", "Increases maximum health.");
        translationBuilder.add(MobEffects.ABSORPTION.value(),
                "desc",
                "Temporarily adds additional hearts that can't be regenerated.");
        translationBuilder.add(MobEffects.SATURATION.value(), "desc", "Restores hunger and saturation.");
        translationBuilder.add(MobEffects.GLOWING.value(), "desc", "Outlines the affected entity.");
        translationBuilder.add(MobEffects.LEVITATION.value(), "desc", "Floats the affected entity upward.");
        translationBuilder.add(MobEffects.LUCK.value(), "desc", "Can increase chances of high-quality and more loot.");
        translationBuilder.add(MobEffects.UNLUCK.value(), "desc", "Can reduce chances of high-quality and more loot.");
        translationBuilder.add(MobEffects.SLOW_FALLING.value(),
                "desc",
                "Decreases falling speed and negates fall damage.");
        translationBuilder.add(MobEffects.CONDUIT_POWER.value(),
                "desc",
                "Prevents drowning, increases underwater visibility and mining speed.");
        translationBuilder.add(MobEffects.DOLPHINS_GRACE.value(), "desc", "Increases swimming speed.");
        translationBuilder.add(MobEffects.BAD_OMEN.value(),
                "desc",
                "Causes an ominous event upon entering a village or the trial chambers.");
        translationBuilder.add(MobEffects.HERO_OF_THE_VILLAGE.value(),
                "desc",
                "Gives discounts on trades with villagers, and makes villagers throw gifts.");
        translationBuilder.add(MobEffects.DARKNESS.value(), "desc", "Adds pulsating black fog and darkens the screen.");
        translationBuilder.add(MobEffects.TRIAL_OMEN.value(),
                "desc",
                "Transforms nearby trial spawners into ominous trial spawners.");
        translationBuilder.add(MobEffects.RAID_OMEN.value(),
                "desc",
                "Causes an illager raid to start upon entering a village, once the effect expires.");
        translationBuilder.add(MobEffects.WIND_CHARGED.value(),
                "desc",
                "Affected entities emit a burst of wind upon death.");
        translationBuilder.add(MobEffects.WEAVING.value(),
                "desc",
                "Affected entities spread cobweb blocks upon death.");
        translationBuilder.add(MobEffects.OOZING.value(), "desc", "Makes the entity spawn two slimes upon death.");
        translationBuilder.add(MobEffects.INFESTED.value(),
                "desc",
                "Gives the entity a 10% chance to spawn between 1 and 3 silverfish when hurt.");
        translationBuilder.add("effect.deeperdarker.sculk_affinity.desc",
                "Prevents the player from emitting vibrations and being heard.");
        translationBuilder.add("effect.alexscaves.deepsight.desc",
                "Increases visibility underwater. May conflict with shaders.");
        translationBuilder.add("effect.alexscaves.magnetizing.desc",
                "Renders the inflicted with the ability to be pushed or pulled by magnetic forces without wearing metal armor.");
        translationBuilder.add("effect.alexsmobs.bug_pheromones.desc", "Make hostile arthropods neutral to the user.");
        translationBuilder.add("effect.alexsmobs.clinging.desc", "Allows walking upside-down on ceilings.");
        translationBuilder.add("effect.alexsmobs.debilitating_sting.desc",
                "An Effect which poisons the victim but paralyzes arthropods. It will stop if the victim is at half health and is not an arthropod. Inflicted by a Tarantula Hawk.");
        translationBuilder.add("effect.alexsmobs.ender_flu.desc",
                "After the effect runs out naturally); the user takes massive damage and an Enderiophage will spawn. Inflicted by an Enderiophage.");
        translationBuilder.add("effect.alexsmobs.exsanguination.desc", "Gradually drains health.");
        translationBuilder.add("effect.alexsmobs.fear.desc",
                "Makes the target unable to move. Effect applied when a Tiger reveals itself to the user.");
        translationBuilder.add("effect.alexsmobs.knockback_resistance.desc", "Provides 50% knockback resistance.");
        translationBuilder.add("effect.alexsmobs.lava_vision.desc", "Allows seeing through lava.");
        translationBuilder.add("effect.alexsmobs.oiled.desc",
                "Oil floats on water. Cover yourself in oil to float on the surface of water and outside in the rain.");
        translationBuilder.add("effect.alexsmobs.orcas_might.desc",
                "Grants increased attack speed. Effect may be applied when swimming with an Orca.");
        translationBuilder.add("effect.alexsmobs.poison_resistance.desc", "Grants immunity to poison.");
        translationBuilder.add("effect.alexsmobs.soulsteal.desc", "Grants the user attacks some life-steal.");
        translationBuilder.add("effect.alexsmobs.sunbird_blessing.desc",
                "Decreases fall speed); prevents fall damage and makes it easier to fly with an Elytra or Tarantula Hawk Elytra. Effect is applied when near a Sunbird.");
        translationBuilder.add("effect.alexsmobs.sunbird_curse.desc",
                "Increases fall speed); causing the user to hit the ground quicker. This effectively impedes the use of an Elytra. Effect is applied when attacking a Sunbird.");
        translationBuilder.add("effect.alexsmobs.tigers_blessing.desc",
                "Tigers near the user will be neutral and will protect them from hostile foes. The effect will go away early when attacking a Tiger. Applied by feeding a Tiger chicken or pork chops.");
        translationBuilder.add("effect.alexsmobs.earthquake.desc", "Shakes the user violently.");
        translationBuilder.add("effect.alexsmobs.fleet_footed.desc",
                "Increases player speed whilst jumping and sprinting.");
        translationBuilder.add("effect.alexsmobs.power_down.desc",
                "Hello, hello? Uhh, I wanted to record a message for you...");
        translationBuilder.add("effect.alexsmobs.mosquito_repellent.desc",
                "Makes Crimson Mosquitoes avoid the player.");
        translationBuilder.add("effect.farmersdelight.nourishment.desc",
                "Prevents hunger and saturation loss unless damaged.");
        translationBuilder.add("effect.farmersdelight.comfort.desc", "Allows health regeneration with low hunger.");
        translationBuilder.add("effect.farmersdelight.nourishment.description",
                "Prevents hunger and saturation loss unless damaged.");
        translationBuilder.add("effect.farmersdelight.comfort.description",
                "Allows health regeneration with low hunger.");
        translationBuilder.add("effect.farmersrespite.caffeinated.desc", "Increases movement and attack speed");
        translationBuilder.add("effect.farmersrespite.caffeinated.description", "Increases movement and attack speed");
        translationBuilder.add("effect.mynethersdelight.g_pungent.desc",
                "Alleviates burning, regenerate health near heat sources with fire protection armor or fire resistance effect.");
        translationBuilder.add("effect.mynethersdelight.b_pungent.desc",
                "Alleviates burning, regenerate health near heat sources with fire protection armor or fire resistance effect.");
        translationBuilder.add("effect.mynethersdelight.g_pungent.description",
                "Alleviates burning, regenerate health near heat sources with fire protection armor or fire resistance effect.");
        translationBuilder.add("effect.mynethersdelight.b_pungent.description",
                "Alleviates burning, regenerate health near heat sources with fire protection armor or fire resistance effect.");
        translationBuilder.add("effect.supplementaries.overencumbered.desc",
                "Prevents sprinting. Further levels reduce jump height hinder walking speed respectively");
        translationBuilder.add("effect.via_romana.travellers_fatigue.desc", "Prevents fast traveling.");
        translationBuilder.add("effect.via_romana.travellers_fatigue.description", "Prevents fast traveling.");
        translationBuilder.add("effect.enderzoology.displacement.desc",
                "Randomly teleports opponents upon receiving damage.");
        translationBuilder.add("effect.longshot.longshot.description",
                "Increases projectile velocity and damage dealt.");
    }
}
