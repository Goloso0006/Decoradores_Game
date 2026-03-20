package com.mygamepattern;

import com.mygamepattern.app.GameConfig;
import com.mygamepattern.app.factory.EquipmentFactory;
import com.mygamepattern.app.factory.HeroBuilder;
import com.mygamepattern.rpg.combat.CombatSimulator;
import com.mygamepattern.rpg.domain.IHero;
import com.mygamepattern.rpg.domain.decorator.BuffDecorator;
import com.mygamepattern.rpg.domain.decorator.PowerDecorator;
import com.mygamepattern.web.GameHttpServer;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length > 0 && "console".equalsIgnoreCase(args[0])) {
            runConsoleDemo();
            return;
        }

        GameConfig config = GameConfig.getInstance();
        GameHttpServer server = new GameHttpServer(config.getHttpPort());
        server.start();
        System.out.println("Abre tu navegador en http://localhost:" + config.getHttpPort());
    }

    private static void runConsoleDemo() {
        IHero hero1 = new HeroBuilder()
                .withName("Aragorn")
                .withRace("Humano")
                .withLevel(5)
                .withHp(100)
                .withAttack(20)
                .withDefense(10)
                .build();

        hero1 = EquipmentFactory.createEquipment(hero1, "espada_efica");
        hero1 = EquipmentFactory.createEquipment(hero1, "armadura_mithril");
        hero1 = new BuffDecorator(hero1, "Pocion de Fuerza", 20, 10, 2);
        hero1 = new PowerDecorator(hero1, "Golpe Critico", 1.3, 0);

        IHero hero2 = new HeroBuilder()
                .withName("Legolas")
                .withRace("Elfo")
                .withLevel(4)
                .withHp(80)
                .withAttack(18)
                .withDefense(8)
                .build();

        hero2 = EquipmentFactory.createEquipment(hero2, "arco_fuego");
        hero2 = EquipmentFactory.createEquipment(hero2, "armadura_cuero");
        hero2 = new BuffDecorator(hero2, "Velocidad", 0, 5, 5);

        CombatSimulator combat = new CombatSimulator(hero1, hero2);
        combat.simulate();
        combat.printLog();
    }
}
