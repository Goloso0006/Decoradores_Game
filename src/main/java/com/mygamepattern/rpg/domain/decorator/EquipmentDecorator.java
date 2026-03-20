package com.mygamepattern.rpg.domain.decorator;

import com.mygamepattern.rpg.domain.HeroDecorator;
import com.mygamepattern.rpg.domain.HeroStats;
import com.mygamepattern.rpg.domain.IHero;

/**
 * Decorador para equipamiento: armas, armaduras, etc.
 */
public class EquipmentDecorator extends HeroDecorator {
    private int attackBonus;
    private int defenseBonus;

    public EquipmentDecorator(IHero hero, String name, int attackBonus, int defenseBonus) {
        super(hero, name);
        this.attackBonus = attackBonus;
        this.defenseBonus = defenseBonus;
    }

    @Override
    protected HeroStats modifyStats(HeroStats stats) {
        return stats.withAttack(stats.getAttack() + attackBonus)
                    .withDefense(stats.getDefense() + defenseBonus);
    }
}

