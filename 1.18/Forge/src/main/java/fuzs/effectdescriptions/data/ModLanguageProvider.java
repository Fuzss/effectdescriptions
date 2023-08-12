package fuzs.effectdescriptions.data;

import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTranslations() {
        this.add(MobEffects.MOVEMENT_SPEED, "desc", "Increases walking speed.");
        this.add(MobEffects.MOVEMENT_SLOWDOWN, "desc", "Decreases walking speed.");
        this.add(MobEffects.DIG_SPEED, "desc", "Increases mining and attack speed.");
        this.add(MobEffects.DIG_SLOWDOWN, "desc", "Decreases mining and attack speed.");
        this.add(MobEffects.DAMAGE_BOOST, "desc", "Increases dealt melee damage.");
        this.add(MobEffects.HEAL, "desc", "Heals living entities, damages undead.");
        this.add(MobEffects.HARM, "desc", "Damages living entities, heals undead.");
        this.add(MobEffects.JUMP, "desc", "Increases jump height and reduces fall damage.");
        this.add(MobEffects.CONFUSION, "desc", "Wobbles and warps the screen.");
        this.add(MobEffects.REGENERATION, "desc", "Regenerates health over time.");
        this.add(MobEffects.DAMAGE_RESISTANCE, "desc", "Reduces incoming damage.");
        this.add(MobEffects.FIRE_RESISTANCE, "desc", "Prevents taking damage from fire sources, like fire and lava.");
        this.add(MobEffects.WATER_BREATHING, "desc", "Prevents loosing air underwater and drowning.");
        this.add(MobEffects.INVISIBILITY, "desc", "Grants invisibility, and reduces other mobs' detection range.");
        this.add(MobEffects.BLINDNESS, "desc", "Impairs vision and disables the ability to sprint and critical hit.");
        this.add(MobEffects.NIGHT_VISION, "desc", "Lets the player see well in darkness and underwater.");
        this.add(MobEffects.HUNGER, "desc", "Increases food exhaustion.");
        this.add(MobEffects.WEAKNESS, "desc", "Decreases dealt melee damage.");
        this.add(MobEffects.POISON, "desc", "Inflicts damage over time, but doesn't kill.");
        this.add(MobEffects.WITHER, "desc", "Inflicts potentially deadly damage over time.");
        this.add(MobEffects.HEALTH_BOOST, "desc", "Increases maximum health.");
        this.add(MobEffects.ABSORPTION, "desc", "Temporarily adds additional hearts that can't be regenerated.");
        this.add(MobEffects.SATURATION, "desc", "Restores hunger and saturation.");
        this.add(MobEffects.GLOWING, "desc", "Outlines the affected entity.");
        this.add(MobEffects.LEVITATION, "desc", "Floats the affected entity upward.");
        this.add(MobEffects.LUCK, "desc", "Can increase chances of high-quality and more loot.");
        this.add(MobEffects.UNLUCK, "desc", "Can reduce chances of high-quality and more loot.");
        this.add(MobEffects.SLOW_FALLING, "desc", "Decreases falling speed and negates fall damage.");
        this.add(MobEffects.CONDUIT_POWER, "desc", "Increases underwater visibility and mining speed, prevents drowning.");
        this.add(MobEffects.DOLPHINS_GRACE, "desc", "Increases swimming speed.");
        this.add(MobEffects.BAD_OMEN, "desc", "Causes an illager raid to start upon entering a village.");
        this.add(MobEffects.HERO_OF_THE_VILLAGE, "desc", "Gives discounts on trades with villagers, and makes villagers throw gifts.");
//        this.add(MobEffects.DARKNESS, "desc", "Darkens the screen.");
    }
}
