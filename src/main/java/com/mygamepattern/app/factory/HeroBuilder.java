package com.mygamepattern.app.factory;

import com.mygamepattern.rpg.domain.Hero;
import com.mygamepattern.rpg.domain.HeroStats;
import com.mygamepattern.rpg.domain.IHero;

/**
 * Builder para construir héroes de forma fluida.
 */
public class HeroBuilder {
    private String name = "Héroe";
    private String race = "Humano";
    private int level = 1;
    private int hp = 100;
    private int mana = 50;
    private int attack = 20;
    private int defense = 10;
    private int speed = 15;
    private int intelligence = 8;

    public HeroBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public HeroBuilder withRace(String race) {
        this.race = race;
        return this;
    }

    public HeroBuilder withLevel(int level) {
        this.level = level;
        return this;
    }

    public HeroBuilder withHp(int hp) {
        this.hp = hp;
        return this;
    }

    public HeroBuilder withAttack(int attack) {
        this.attack = attack;
        return this;
    }

    public HeroBuilder withDefense(int defense) {
        this.defense = defense;
        return this;
    }

    public HeroBuilder withSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public HeroBuilder withIntelligence(int intelligence) {
        this.intelligence = intelligence;
        return this;
    }

    public IHero build() {
        HeroStats stats = new HeroStats(hp, mana, attack, defense, speed, intelligence);
        return new Hero(name, race, level, stats);
    }
}

