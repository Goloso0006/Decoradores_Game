package com.mygamepattern.rpg.domain;

/**
 * Clase abstracta para envolver un Hero y modificar sus estadísticas.
 * Implementa patrón Decorator.
 */
public abstract class HeroDecorator implements IHero {
    protected IHero wrappedHero;
    protected String decoratorName;

    public HeroDecorator(IHero hero, String name) {
        this.wrappedHero = hero;
        this.decoratorName = name;
    }

    @Override
    public HeroStats getStats() {
        return modifyStats(wrappedHero.getStats());
    }

    @Override
    public String getName() {
        return wrappedHero.getName();
    }

    @Override
    public String getRace() {
        return wrappedHero.getRace();
    }

    @Override
    public int getLevel() {
        return wrappedHero.getLevel();
    }

    @Override
    public int takeDamage(int damage) {
        return wrappedHero.takeDamage(damage);
    }

    @Override
    public void applyEffect(String effectName, int duration) {
        wrappedHero.applyEffect(effectName, duration);
    }

    @Override
    public String getDisplayInfo() {
        HeroStats s = getStats();
        return String.format(
            "Heroe: %s (%s Lvl %d) | HP: %d | Mana: %d | ATK: %d | DEF: %d | SPD: %d | INT: %d | Deco: %s",
            getName(), getRace(), getLevel(),
            getCurrentHp(), s.getMana(), s.getAttack(), s.getDefense(), s.getSpeed(), s.getIntelligence(),
            getDecorationInfo()
        );
    }

    @Override
    public String getDecorationInfo() {
        String baseDecorations = wrappedHero.getDecorationInfo();
        if (baseDecorations.equals("(sin decoraciones)")) {
            return decoratorName;
        }
        return baseDecorations + ", " + decoratorName;
    }

    @Override
    public int getCurrentHp() {
        return wrappedHero.getCurrentHp();
    }

    @Override
    public boolean isAlive() {
        return wrappedHero.isAlive();
    }

    /**
     * Método para que subclases modifiquen las estadísticas
     */
    protected abstract HeroStats modifyStats(HeroStats stats);
}
