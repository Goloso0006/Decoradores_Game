package com.mygamepattern.app;

/**
 * Singleton para configuración global del juego.
 */
public class GameConfig {
    private static GameConfig instance;
    
    private int httpPort = 8080;
    private int maxCombatRounds = 10;
    private String gameName = "MyGamePattern RPG";

    private GameConfig() {
    }

    public static GameConfig getInstance() {
        if (instance == null) {
            synchronized (GameConfig.class) {
                if (instance == null) {
                    instance = new GameConfig();
                }
            }
        }
        return instance;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int port) {
        this.httpPort = port;
    }

    public int getMaxCombatRounds() {
        return maxCombatRounds;
    }

    public void setMaxCombatRounds(int rounds) {
        this.maxCombatRounds = rounds;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String name) {
        this.gameName = name;
    }
}

