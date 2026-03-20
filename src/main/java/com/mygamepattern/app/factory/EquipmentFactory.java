package com.mygamepattern.app.factory;

import com.mygamepattern.rpg.domain.IHero;
import com.mygamepattern.rpg.domain.decorator.EquipmentDecorator;
import java.util.HashMap;
import java.util.Map;

/**
 * Fábrica de equipamiento usando patrón Prototype.
 * Almacena templates de equipamiento para clonarlos.
 */
public class EquipmentFactory {
    private static final Map<String, EquipmentTemplate> templates = new HashMap<>();

    static {
        // Registrar templates de armas
        templates.put("espada_basica", new EquipmentTemplate("Espada Básica", 10, 0));
        templates.put("espada_efica", new EquipmentTemplate("Espada Élfica", 15, 5));
        templates.put("arco_madera", new EquipmentTemplate("Arco de Madera", 12, 0));
        templates.put("arco_fuego", new EquipmentTemplate("Arco de Fuego", 12, 3));

        // Registrar templates de armaduras
        templates.put("armadura_cuero", new EquipmentTemplate("Armadura de Cuero", 0, 5));
        templates.put("armadura_acero", new EquipmentTemplate("Armadura de Acero", 0, 8));
        templates.put("armadura_mithril", new EquipmentTemplate("Armadura de Mithril", 0, 10));
    }

    /**
     * Crea un decorador de equipamiento basado en un template.
     */
    public static IHero createEquipment(IHero hero, String templateKey) {
        EquipmentTemplate template = templates.get(templateKey);
        if (template == null) {
            throw new IllegalArgumentException("Template no encontrado: " + templateKey);
        }
        return template.createDecorator(hero);
    }

    /**
     * Template interna para equipamiento (patrón Prototype).
     */
    private static class EquipmentTemplate {
        String name;
        int attackBonus;
        int defenseBonus;

        EquipmentTemplate(String name, int attackBonus, int defenseBonus) {
            this.name = name;
            this.attackBonus = attackBonus;
            this.defenseBonus = defenseBonus;
        }

        IHero createDecorator(IHero hero) {
            return new EquipmentDecorator(hero, name, attackBonus, defenseBonus);
        }
    }
}

