package fuzs.effectdescriptions.data;

import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.data.event.GatherDataEvent;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTranslations() {
        this.addAdditional(MobEffects.MOVEMENT_SPEED, "desc", "Increases walking speed.");
        this.addAdditional(MobEffects.MOVEMENT_SLOWDOWN, "desc", "Decreases walking speed.");
        this.addAdditional(MobEffects.DIG_SPEED, "desc", "Increases mining and attack speed.");
        this.addAdditional(MobEffects.DIG_SLOWDOWN, "desc", "Decreases mining and attack speed.");
        this.addAdditional(MobEffects.DAMAGE_BOOST, "desc", "Increases dealt melee damage.");
        this.addAdditional(MobEffects.HEAL, "desc", "Heals living entities, damages undead.");
        this.addAdditional(MobEffects.HARM, "desc", "Damages living entities, heals undead.");
        this.addAdditional(MobEffects.JUMP, "desc", "Increases jump height and reduces fall damage.");
        this.addAdditional(MobEffects.CONFUSION, "desc", "Wobbles and warps the screen.");
        this.addAdditional(MobEffects.REGENERATION, "desc", "Regenerates health over time.");
        this.addAdditional(MobEffects.DAMAGE_RESISTANCE, "desc", "Reduces incoming damage.");
        this.addAdditional(MobEffects.FIRE_RESISTANCE, "desc", "Prevents taking damage from fire sources, like fire and lava.");
        this.addAdditional(MobEffects.WATER_BREATHING, "desc", "Prevents losing air underwater and drowning.");
        this.addAdditional(MobEffects.INVISIBILITY, "desc", "Grants invisibility, and reduces other mobs' detection range.");
        this.addAdditional(MobEffects.BLINDNESS, "desc", "Impairs vision and disables the ability to sprint and critical hit.");
        this.addAdditional(MobEffects.NIGHT_VISION, "desc", "Lets the player see well in darkness and underwater.");
        this.addAdditional(MobEffects.HUNGER, "desc", "Increases food exhaustion.");
        this.addAdditional(MobEffects.WEAKNESS, "desc", "Decreases dealt melee damage.");
        this.addAdditional(MobEffects.POISON, "desc", "Inflicts damage over time, but doesn't kill.");
        this.addAdditional(MobEffects.WITHER, "desc", "Inflicts potentially deadly damage over time.");
        this.addAdditional(MobEffects.HEALTH_BOOST, "desc", "Increases maximum health.");
        this.addAdditional(MobEffects.ABSORPTION, "desc", "Temporarily adds additional hearts that can't be regenerated.");
        this.addAdditional(MobEffects.SATURATION, "desc", "Restores hunger and saturation.");
        this.addAdditional(MobEffects.GLOWING, "desc", "Outlines the affected entity.");
        this.addAdditional(MobEffects.LEVITATION, "desc", "Floats the affected entity upward.");
        this.addAdditional(MobEffects.LUCK, "desc", "Can increase chances of high-quality and more loot.");
        this.addAdditional(MobEffects.UNLUCK, "desc", "Can reduce chances of high-quality and more loot.");
        this.addAdditional(MobEffects.SLOW_FALLING, "desc", "Decreases falling speed and negates fall damage.");
        this.addAdditional(MobEffects.CONDUIT_POWER, "desc", "Increases underwater visibility and mining speed, prevents drowning.");
        this.addAdditional(MobEffects.DOLPHINS_GRACE, "desc", "Increases swimming speed.");
        this.addAdditional(MobEffects.BAD_OMEN, "desc", "Causes an illager raid to start upon entering a village.");
        this.addAdditional(MobEffects.HERO_OF_THE_VILLAGE, "desc", "Gives discounts on trades with villagers, and makes villagers throw gifts.");
        this.addAdditional(MobEffects.DARKNESS, "desc", "Darkens the screen.");
    }
}
