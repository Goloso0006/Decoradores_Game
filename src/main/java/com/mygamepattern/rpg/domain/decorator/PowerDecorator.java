package com.mygamepattern.rpg.domain.decorator;

import com.mygamepattern.rpg.domain.HeroDecorator;
import com.mygamepattern.rpg.domain.HeroStats;
import com.mygamepattern.rpg.domain.IHero;

/**
 * Decorador para poderes especiales: habilidades, golpes críticos, etc.
 */
public class PowerDecorator extends HeroDecorator {
    private double attackMultiplier;
    private int intelligenceBonus;

    public PowerDecorator(IHero hero, String name, double attackMultiplier, int intelligenceBonus) {
        super(hero, name);
        this.attackMultiplier = attackMultiplier;
        this.intelligenceBonus = intelligenceBonus;
    }

    @Override
    protected HeroStats modifyStats(HeroStats stats) {
        int newAttack = (int) (stats.getAttack() * attackMultiplier);
        return stats.withAttack(newAttack)
                    .withIntelligence(stats.getIntelligence() + intelligenceBonus);
    }
}

