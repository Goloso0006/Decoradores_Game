package com.mygamepattern.rpg.domain.decorator;

import com.mygamepattern.rpg.domain.HeroDecorator;
import com.mygamepattern.rpg.domain.HeroStats;
import com.mygamepattern.rpg.domain.IHero;

/**
 * Decorador para buffs: pociones, hechizos, efectos temporales.
 */
public class BuffDecorator extends HeroDecorator {
    private int hpBonus;
    private int attackBonus;
    private int speedBonus;

    public BuffDecorator(IHero hero, String name, int hpBonus, int attackBonus, int speedBonus) {
        super(hero, name);
        this.hpBonus = hpBonus;
        this.attackBonus = attackBonus;
        this.speedBonus = speedBonus;
    }

    @Override
    protected HeroStats modifyStats(HeroStats stats) {
        return stats.withHp(stats.getHp() + hpBonus)
                    .withAttack(stats.getAttack() + attackBonus)
                    .withSpeed(stats.getSpeed() + speedBonus);
    }
}

