package com.mygamepattern.rpg.domain;

/**
 * Clase base del héroe sin decoraciones.
 *
 * Un Hero es la entidad central que:
 * - Tiene estadísticas base (HP, Mana, ATK, DEF, SPD, INT)
 * - Tiene información de identidad (nombre, raza, nivel)
 * - Puede ser decorado con equipamiento, poderes y buffs
 * - Gestiona su HP actual durante combate
 */
public class Hero implements IHero {
    private final String name;
    private final String race;
    private final int level;
    private final HeroStats baseStats;
    private int currentHp;  // HP actual durante combate

    /**
     * Constructor con parámetros básicos
     * @param name Nombre del héroe
     * @param race Raza (Humano, Elfo, Enano, etc.)
     * @param level Nivel del héroe (afecta poder general)
     * @param baseStats Estadísticas base iniciales
     */
    public Hero(String name, String race, int level, HeroStats baseStats) {
        this.name = name;
        this.race = race;
        this.level = Math.max(1, level);
        this.baseStats = new HeroStats(baseStats); // Copia defensiva
        this.currentHp = baseStats.getHp();
    }

    // ============= Implementación de IHero =============

    @Override
    public HeroStats getStats() {
        return new HeroStats(baseStats);  // Retorna copia
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRace() {
        return race;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int takeDamage(int damage) {
        // Reducir daño según defensa (mínimo 1 daño)
        int actualDamage = Math.max(1, damage - baseStats.getDefense() / 2);
        this.currentHp = Math.max(0, currentHp - actualDamage);
        return actualDamage;
    }

    @Override
    public void applyEffect(String effectName, int duration) {
        // En la clase base, los efectos no hacen nada
        // Los decoradores sobreescriben esto
        System.out.println("[" + name + "] Intento aplicar efecto: " + effectName);
    }

    @Override
    public String getDecorationInfo() {
        return "(sin decoraciones)";
    }

    @Override
    public String getDisplayInfo() {
        return String.format(
            "╔════════════════════════════════════════╗\n" +
            "║ %s - %s (Lvl %d)\n" +
            "╠════════════════════════════════════════╣\n" +
            "║ HP: %d/%d | Mana: %d\n" +
            "║ Ataque: %d | Defensa: %d\n" +
            "║ Velocidad: %d | Inteligencia: %d\n" +
            "║ Decoraciones: %s\n" +
            "╚════════════════════════════════════════╝",
            name, race, level,
            currentHp, baseStats.getHp(), baseStats.getMana(),
            baseStats.getAttack(), baseStats.getDefense(),
            baseStats.getSpeed(), baseStats.getIntelligence(),
            getDecorationInfo()
        );
    }

    // ============= Métodos adicionales =============

    /**
     * Retorna el HP actual del héroe
     */
    public int getCurrentHp() {
        return currentHp;
    }

    /**
     * Restaura HP al máximo
     */
    public void restoreHp() {
        this.currentHp = baseStats.getHp();
    }

    /**
     * Verifica si el héroe está vivo
     */
    public boolean isAlive() {
        return currentHp > 0;
    }

    /**
     * Retorna información resumida del héroe
     */
    public String getSummary() {
        return String.format(
            "%s (%s Lvl%d) - HP: %d/%d",
            name, race, level, currentHp, baseStats.getHp()
        );
    }

    @Override
    public String toString() {
        return String.format(
            "Hero{name='%s', race='%s', level=%d, currentHp=%d, baseStats=%s}",
            name, race, level, currentHp, baseStats
        );
    }
}

