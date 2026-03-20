package com.mygamepattern.rpg.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Hero
 */
class HeroTest {

    private HeroStats baseStats;
    private Hero hero;

    @BeforeEach
    void setUp() {
        baseStats = new HeroStats(100, 50, 20, 10, 15, 8);
        hero = new Hero("Aragorn", "Humano", 5, baseStats);
    }

    // ============= Tests de Creación =============
    @Test
    void testHeroCreation() {
        assertEquals("Aragorn", hero.getName());
        assertEquals("Humano", hero.getRace());
        assertEquals(5, hero.getLevel());
    }

    @Test
    void testHeroStatsInitialized() {
        HeroStats stats = hero.getStats();
        assertEquals(100, stats.getHp());
        assertEquals(50, stats.getMana());
        assertEquals(20, stats.getAttack());
        assertEquals(10, stats.getDefense());
        assertEquals(15, stats.getSpeed());
        assertEquals(8, stats.getIntelligence());
    }

    @Test
    void testHeroCurrentHpInitialized() {
        assertEquals(100, hero.getCurrentHp());
        assertTrue(hero.isAlive());
    }

    // ============= Tests de Combate =============
    @Test
    void testTakeDamage() {
        // Daño: 25, Defensa: 10 (reduce 5)
        // Daño real: 25 - 5 = 20
        int actualDamage = hero.takeDamage(25);

        assertEquals(20, actualDamage);
        assertEquals(80, hero.getCurrentHp());
    }

    @Test
    void testTakeDamageMinimum() {
        // Daño muy bajo (menor que defensa) debe ser mínimo 1
        hero.takeDamage(3);  // 3 - 5 = -2, pero mínimo 1

        assertEquals(99, hero.getCurrentHp());
    }

    @Test
    void testTakeDamageMultiple() {
        hero.takeDamage(25);  // 100 - 20 = 80
        hero.takeDamage(30);  // 80 - 25 = 55

        assertEquals(55, hero.getCurrentHp());
    }

    @Test
    void testTakeDamageKill() {
        // Aplicar más daño del HP disponible
        hero.takeDamage(200);

        assertEquals(0, hero.getCurrentHp());
        assertFalse(hero.isAlive());
    }

    // ============= Tests de Restauración =============
    @Test
    void testRestoreHp() {
        // Daño: 50, Defensa: 10 (reduce 5) = 45 daño real
        // 100 - 45 = 55 HP
        hero.takeDamage(50);
        assertEquals(55, hero.getCurrentHp());

        hero.restoreHp();
        assertEquals(100, hero.getCurrentHp());
        assertTrue(hero.isAlive());
    }

    // ============= Tests de Display =============
    @Test
    void testGetDisplayInfo() {
        String display = hero.getDisplayInfo();

        assertTrue(display.contains("Aragorn"));
        assertTrue(display.contains("Humano"));
        assertTrue(display.contains("Lvl 5"));
        assertTrue(display.contains("100"));  // HP
        assertTrue(display.contains("50"));   // Mana
        assertTrue(display.contains("20"));   // Ataque
        assertTrue(display.contains("10"));   // Defensa
    }

    @Test
    void testGetSummary() {
        String summary = hero.getSummary();

        assertTrue(summary.contains("Aragorn"));
        assertTrue(summary.contains("Humano"));
        assertTrue(summary.contains("Lvl5"));
        assertTrue(summary.contains("100/100"));
    }

    @Test
    void testGetDecorationInfo() {
        assertEquals("(sin decoraciones)", hero.getDecorationInfo());
    }

    // ============= Tests de Estadísticas =============
    @Test
    void testGetStatsCopy() {
        HeroStats stats1 = hero.getStats();
        HeroStats stats2 = hero.getStats();

        // Son iguales en valores
        assertEquals(stats1.getHp(), stats2.getHp());
        // Pero no son la misma instancia (defensivo)
        assertNotSame(stats1, stats2);
    }

    // ============= Tests de Validación =============
    @Test
    void testInvalidHeroStats() {
        // Estadísticas con valores negativos deben corregirse
        HeroStats invalidStats = new HeroStats(-50, -10, -20, -5, -15, -8);
        Hero invalidHero = new Hero("Test", "Test", -1, invalidStats);

        HeroStats stats = invalidHero.getStats();
        assertTrue(stats.getHp() > 0);
        assertTrue(stats.getMana() >= 0);
        assertTrue(stats.getAttack() >= 0);
        assertEquals(1, invalidHero.getLevel());
    }
}


