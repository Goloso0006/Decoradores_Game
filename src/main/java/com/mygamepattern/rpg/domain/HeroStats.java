package com.mygamepattern.rpg.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Encapsula las estadísticas base de un héroe.
 * Es un objeto inmutable que puede ser copiado para crear variaciones.
 *
 * Estadísticas:
 * - HP (Hit Points): Puntos de vida
 * - Mana: Energía mágica
 * - Ataque: Daño físico base
 * - Defensa: Resistencia a daño
 * - Velocidad: Orden de turno en combate
 * - Inteligencia: Modificador mágico
 */
public class HeroStats {
    private int hp;
    private int mana;
    private int attack;
    private int defense;
    private int speed;
    private int intelligence;

    /**
     * Constructor con todas las estadísticas
     */
    public HeroStats(int hp, int mana, int attack, int defense, int speed, int intelligence) {
        this.hp = Math.max(1, hp);                    // Mínimo 1 HP
        this.mana = Math.max(0, mana);                // Mínimo 0 mana
        this.attack = Math.max(0, attack);            // Mínimo 0 ataque
        this.defense = Math.max(0, defense);          // Mínimo 0 defensa
        this.speed = Math.max(1, speed);              // Mínimo 1 velocidad
        this.intelligence = Math.max(0, intelligence); // Mínimo 0 inteligencia
    }

    /**
     * Constructor copia: permite crear variaciones de estadísticas
     */
    public HeroStats(HeroStats other) {
        this(other.hp, other.mana, other.attack, other.defense, other.speed, other.intelligence);
    }

    // ============= Getters =============
    public int getHp() {
        return hp;
    }

    public int getMana() {
        return mana;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getIntelligence() {
        return intelligence;
    }

    // ============= Métodos de modificación (retorna nueva instancia) =============
    public HeroStats withHp(int newHp) {
        return new HeroStats(newHp, this.mana, this.attack, this.defense, this.speed, this.intelligence);
    }

    public HeroStats withMana(int newMana) {
        return new HeroStats(this.hp, newMana, this.attack, this.defense, this.speed, this.intelligence);
    }

    public HeroStats withAttack(int newAttack) {
        return new HeroStats(this.hp, this.mana, newAttack, this.defense, this.speed, this.intelligence);
    }

    public HeroStats withDefense(int newDefense) {
        return new HeroStats(this.hp, this.mana, this.attack, newDefense, this.speed, this.intelligence);
    }

    public HeroStats withSpeed(int newSpeed) {
        return new HeroStats(this.hp, this.mana, this.attack, this.defense, newSpeed, this.intelligence);
    }

    public HeroStats withIntelligence(int newIntelligence) {
        return new HeroStats(this.hp, this.mana, this.attack, this.defense, this.speed, newIntelligence);
    }

    /**
     * Suma las estadísticas de otro HeroStats (usado para decoradores aditivos)
     */
    public HeroStats add(HeroStats other) {
        return new HeroStats(
            this.hp + other.hp,
            this.mana + other.mana,
            this.attack + other.attack,
            this.defense + other.defense,
            this.speed + other.speed,
            this.intelligence + other.intelligence
        );
    }

    @Override
    public String toString() {
        return String.format(
            "HeroStats{HP:%d, Mana:%d, ATK:%d, DEF:%d, SPD:%d, INT:%d}",
            hp, mana, attack, defense, speed, intelligence
        );
    }

    /**
     * Serializa a JSON usando Gson
     */
    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}

