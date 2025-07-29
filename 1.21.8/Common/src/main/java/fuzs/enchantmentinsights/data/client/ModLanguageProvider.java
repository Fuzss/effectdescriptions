package fuzs.enchantmentinsights.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder translationBuilder) {
        this.addVanillaEnchantments(translationBuilder);
    }

    private void addVanillaEnchantments(TranslationBuilder translationBuilder) {
        translationBuilder.add(Enchantments.AQUA_AFFINITY, "desc", "Improves underwater mining speed.");
        translationBuilder.add(Enchantments.BANE_OF_ARTHROPODS, "desc", "Increases damage against arthropods.");
        translationBuilder.add(Enchantments.BLAST_PROTECTION, "desc", "Reduces explosion damage.");
        translationBuilder.add(Enchantments.BINDING_CURSE, "desc", "Prevents armor removal.");
        translationBuilder.add(Enchantments.CHANNELING, "desc", "Summons lightning on struck targets during storms.");
        translationBuilder.add(Enchantments.DEPTH_STRIDER, "desc", "Increases underwater movement speed.");
        translationBuilder.add(Enchantments.EFFICIENCY, "desc", "Increases mining speed.");
        translationBuilder.add(Enchantments.FEATHER_FALLING, "desc", "Reduces fall damage.");
        translationBuilder.add(Enchantments.FIRE_ASPECT, "desc", "Sets targets ablaze.");
        translationBuilder.add(Enchantments.FIRE_PROTECTION, "desc", "Reduces fire damage.");
        translationBuilder.add(Enchantments.FLAME, "desc", "Creates flaming arrows.");
        translationBuilder.add(Enchantments.FORTUNE, "desc", "Increases block drop rates.");
        translationBuilder.add(Enchantments.FROST_WALKER, "desc", "Creates frozen water walkways.");
        translationBuilder.add(Enchantments.IMPALING, "desc", "Increases damage against aquatic mobs.");
        translationBuilder.add(Enchantments.INFINITY, "desc", "Prevents arrow consumption.");
        translationBuilder.add(Enchantments.KNOCKBACK, "desc", "Increases knockback strength.");
        translationBuilder.add(Enchantments.LOOTING, "desc", "Increases mob drop rates.");
        translationBuilder.add(Enchantments.LOYALTY, "desc", "Returns thrown trident.");
        translationBuilder.add(Enchantments.LUCK_OF_THE_SEA, "desc", "Increases fishing treasure chances.");
        translationBuilder.add(Enchantments.LURE, "desc", "Reduces fishing time.");
        translationBuilder.add(Enchantments.MENDING, "desc", "Repairs items with experience.");
        translationBuilder.add(Enchantments.MULTISHOT, "desc", "Shoots multiple arrows.");
        translationBuilder.add(Enchantments.PIERCING, "desc", "Arrows pierce multiple targets.");
        translationBuilder.add(Enchantments.POWER, "desc", "Increases arrow damage.");
        translationBuilder.add(Enchantments.PROJECTILE_PROTECTION, "desc", "Reduces projectile damage.");
        translationBuilder.add(Enchantments.PROTECTION, "desc", "Reduces most damage types.");
        translationBuilder.add(Enchantments.PUNCH, "desc", "Increases arrow knockback.");
        translationBuilder.add(Enchantments.QUICK_CHARGE, "desc", "Reduces crossbow loading time.");
        translationBuilder.add(Enchantments.RESPIRATION, "desc", "Extends underwater breathing.");
        translationBuilder.add(Enchantments.RIPTIDE, "desc", "Launches user in water or rain.");
        translationBuilder.add(Enchantments.SHARPNESS, "desc", "Increases melee damage.");
        translationBuilder.add(Enchantments.SILK_TOUCH, "desc", "Blocks drop themselves.");
        translationBuilder.add(Enchantments.SMITE, "desc", "Increases damage against undead.");
        translationBuilder.add(Enchantments.SOUL_SPEED, "desc", "Increases speed on soul blocks.");
        translationBuilder.add(Enchantments.SWEEPING_EDGE, "desc", "Increases sweep attack damage.");
        translationBuilder.add(Enchantments.SWIFT_SNEAK, "desc", "Increases sneaking speed.");
        translationBuilder.add(Enchantments.THORNS, "desc", "Damages attacking entities.");
        translationBuilder.add(Enchantments.UNBREAKING, "desc", "Increases item durability.");
        translationBuilder.add(Enchantments.VANISHING_CURSE, "desc", "Destroys item on death.");
    }
}
