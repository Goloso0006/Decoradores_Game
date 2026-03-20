package com.mygamepattern.rpg.domain;

/**
 * Contrato para Hero y todos sus decoradores.
 * Define los métodos que debe implementar cualquier héroe,
 * ya sea base o decorado.
 *
 * Esto permite que los decoradores se compongan sin límite.
 */
public interface IHero {

    /**
     * Retorna las estadísticas actuales del héroe
     * (incluyendo modificadores de decoradores)
     */
    com.mygamepattern.rpg.domain.HeroStats getStats();

    /**
     * Retorna el nombre del héroe
     */
    String getName();

    /**
     * Retorna la raza del héroe
     */
    String getRace();

    /**
     * Retorna el nivel del héroe
     */
    int getLevel();

    /**
     * Retorna información formateada del héroe para mostrar en UI
     */
    String getDisplayInfo();

    /**
     * Aplica daño al héroe (modifica HP)
     * @param damage Cantidad de daño
     * @return Daño real aplicado (considerando defensa)
     */
    int takeDamage(int damage);

    /**
     * Aplicar un efecto/buff al héroe (abstracto, lo implementan subclases)
     * @param effectName Nombre del efecto
     * @param duration Duración en turnos (0 = permanente)
     */
    void applyEffect(String effectName, int duration);

    /**
     * Retorna descripción de decoraciones aplicadas (ej: "Armadura +5 DEF, Espada +10 ATK")
     */
    String getDecorationInfo();

    /**
     * HP actual del heroe durante combate.
     */
    int getCurrentHp();

    /**
     * Indica si el heroe sigue con vida.
     */
    boolean isAlive();
}
