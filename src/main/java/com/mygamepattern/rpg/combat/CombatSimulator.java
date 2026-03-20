package com.mygamepattern.rpg.combat;

import com.mygamepattern.rpg.domain.IHero;

/**
 * Simulador de combate básico entre dos héroes.
 */
public class CombatSimulator {
    private IHero hero1;
    private IHero hero2;
    private StringBuilder log;

    public CombatSimulator(IHero hero1, IHero hero2) {
        this.hero1 = hero1;
        this.hero2 = hero2;
        this.log = new StringBuilder();
    }

    /**
     * Simula combate por turnos (máximo 10 rondas)
     */
    public void simulate() {
        log.append("=== INICIO DEL COMBATE ===\n");
        log.append(hero1.getName()).append(" vs ").append(hero2.getName()).append("\n\n");

        for (int round = 1; round <= 10; round++) {
            log.append("--- Ronda ").append(round).append(" ---\n");

            // Turno del héroe 1
            int damage1 = getAttackDamage(hero1);
            int realDamage1 = hero2.takeDamage(damage1);
            log.append(hero1.getName()).append(" ataca con ").append(damage1)
                    .append(" de daño (").append(realDamage1).append(" real)\n");

            if (!hero2.isAlive()) {
                log.append(hero2.getName()).append(" ha sido derrotado!\n");
                log.append("¡GANADOR: ").append(hero1.getName()).append("!\n");
                break;
            }

            // Turno del héroe 2
            int damage2 = getAttackDamage(hero2);
            int realDamage2 = hero1.takeDamage(damage2);
            log.append(hero2.getName()).append(" ataca con ").append(damage2)
                    .append(" de daño (").append(realDamage2).append(" real)\n");

            if (!hero1.isAlive()) {
                log.append(hero1.getName()).append(" ha sido derrotado!\n");
                log.append("¡GANADOR: ").append(hero2.getName()).append("!\n");
                break;
            }

            log.append("HP: ").append(hero1.getName()).append(" = ")
                    .append(getCurrentHp(hero1)).append(" | ")
                    .append(hero2.getName()).append(" = ")
                    .append(getCurrentHp(hero2)).append("\n\n");
        }

        log.append("=== FIN DEL COMBATE ===\n");
    }

    private int getAttackDamage(IHero hero) {
        return hero.getStats().getAttack() + (int) (Math.random() * 10);
    }

    private int getCurrentHp(IHero hero) {
        return hero.getCurrentHp();
    }

    public String getLog() {
        return log.toString();
    }

    public void printLog() {
        System.out.println(log.toString());
    }

    public IHero getHero1() {
        return hero1;
    }

    public IHero getHero2() {
        return hero2;
    }

    public boolean hasWinner() {
        return !hero1.isAlive() || !hero2.isAlive();
    }
}
