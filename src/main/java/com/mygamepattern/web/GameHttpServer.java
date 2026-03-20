package com.mygamepattern.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mygamepattern.app.factory.EquipmentFactory;
import com.mygamepattern.app.factory.HeroBuilder;
import com.mygamepattern.rpg.combat.CombatSimulator;
import com.mygamepattern.rpg.domain.IHero;
import com.mygamepattern.rpg.domain.decorator.BuffDecorator;
import com.mygamepattern.rpg.domain.decorator.PowerDecorator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Servidor HTTP simple embebido con endpoints REST para el juego RPG.
 */
public class GameHttpServer {
    private HttpServer server;
    private final Gson gson;
    private final int port;

    public GameHttpServer(int port) {
        this.port = port;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new HomeHandler());
        server.createContext("/api/status", new StatusHandler());
        server.createContext("/api/combat/simulate", new SimulateCombatHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Web UI lista en http://localhost:" + port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private class HomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendText(exchange, 405, "Method Not Allowed", "text/plain");
                return;
            }
            sendText(exchange, 200, buildIndexHtml(), "text/html; charset=UTF-8");
        }
    }

    private class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("server", "MyGamePattern RPG");
            response.put("version", "1.0");
            sendJson(exchange, 200, response);
        }
    }

    private class SimulateCombatHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendText(exchange, 405, "Method Not Allowed", "text/plain");
                return;
            }

            try {
                String body = readBody(exchange);
                CombatRequest request = gson.fromJson(body, CombatRequest.class);
                if (request == null || request.hero1 == null || request.hero2 == null) {
                    sendJson(exchange, 400, Map.of("error", "Payload invalido"));
                    return;
                }

                IHero h1 = buildHeroFromRequest(request.hero1);
                IHero h2 = buildHeroFromRequest(request.hero2);

                CombatSimulator simulator = new CombatSimulator(h1, h2);
                simulator.simulate();

                Map<String, Object> response = new HashMap<>();
                response.put("hero1", h1.getDisplayInfo());
                response.put("hero2", h2.getDisplayInfo());
                response.put("combatLog", simulator.getLog());
                sendJson(exchange, 200, response);
            } catch (Exception ex) {
                sendJson(exchange, 500, Map.of("error", ex.getMessage()));
            }
        }
    }

    private IHero buildHeroFromRequest(HeroRequest req) {
        IHero hero = new HeroBuilder()
                .withName(valueOr(req.name, "Hero"))
                .withRace(valueOr(req.race, "Humano"))
                .withLevel(defaultInt(req.level, 1))
                .withHp(defaultInt(req.hp, 100))
                .withAttack(defaultInt(req.attack, 20))
                .withDefense(defaultInt(req.defense, 10))
                .withSpeed(defaultInt(req.speed, 15))
                .withIntelligence(defaultInt(req.intelligence, 8))
                .build();

        if (req.equipment != null) {
            for (String eq : req.equipment) {
                if (eq != null && !eq.isBlank()) {
                    hero = EquipmentFactory.createEquipment(hero, eq);
                }
            }
        }

        if (req.buffEnabled) {
            hero = new BuffDecorator(
                    hero,
                    valueOr(req.buffName, "Buff"),
                    defaultInt(req.buffHpBonus, 0),
                    defaultInt(req.buffAttackBonus, 0),
                    defaultInt(req.buffSpeedBonus, 0)
            );
        }

        if (req.powerEnabled) {
            hero = new PowerDecorator(
                    hero,
                    valueOr(req.powerName, "Power"),
                    req.powerMultiplier == 0.0 ? 1.2 : req.powerMultiplier,
                    defaultInt(req.powerIntelligenceBonus, 0)
            );
        }

        return hero;
    }

    private int defaultInt(Integer value, int fallback) {
        return value == null ? fallback : value;
    }

    private String valueOr(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void sendJson(HttpExchange exchange, int status, Object body) throws IOException {
        sendText(exchange, status, gson.toJson(body), "application/json; charset=UTF-8");
    }

    private void sendText(HttpExchange exchange, int status, String body, String contentType) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static class CombatRequest {
        HeroRequest hero1;
        HeroRequest hero2;
    }

    private static class HeroRequest {
        String name;
        String race;
        Integer level;
        Integer hp;
        Integer attack;
        Integer defense;
        Integer speed;
        Integer intelligence;
        String[] equipment;

        boolean buffEnabled;
        String buffName;
        Integer buffHpBonus;
        Integer buffAttackBonus;
        Integer buffSpeedBonus;

        boolean powerEnabled;
        String powerName;
        double powerMultiplier;
        Integer powerIntelligenceBonus;
    }

    private static String buildIndexHtml() {
        String template = """
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>MyGamePattern RPG</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .card { border: 1px solid #ccc; border-radius: 8px; padding: 12px; }
    label { display: block; margin-top: 8px; font-size: 14px; }
    input, select { width: 100%; padding: 6px; margin-top: 2px; }
    button { margin-top: 16px; padding: 10px 14px; }
    pre { background: #111; color: #0f0; padding: 10px; border-radius: 8px; white-space: pre-wrap; }
  </style>
</head>
<body>
  <h1>RPG Combat - Decorator</h1>
  <p>Arma dos heroes y simula combate.</p>

  <div class="grid">
    <div class="card" id="hero1">{{FORM1}}</div>
    <div class="card" id="hero2">{{FORM2}}</div>
  </div>

  <button id="fightBtn">Simular combate</button>

  <h3>Resultado</h3>
  <pre id="result">Listo para simular...</pre>

  <script>
    function hero(prefix) {
      return {
        name: document.getElementById(prefix + '_name').value,
        race: document.getElementById(prefix + '_race').value,
        level: Number(document.getElementById(prefix + '_level').value),
        hp: Number(document.getElementById(prefix + '_hp').value),
        attack: Number(document.getElementById(prefix + '_attack').value),
        defense: Number(document.getElementById(prefix + '_defense').value),
        speed: Number(document.getElementById(prefix + '_speed').value),
        intelligence: Number(document.getElementById(prefix + '_int').value),
        equipment: [
          document.getElementById(prefix + '_eq1').value,
          document.getElementById(prefix + '_eq2').value
        ],
        buffEnabled: document.getElementById(prefix + '_buffOn').checked,
        buffName: document.getElementById(prefix + '_buffName').value,
        buffHpBonus: Number(document.getElementById(prefix + '_buffHp').value),
        buffAttackBonus: Number(document.getElementById(prefix + '_buffAtk').value),
        buffSpeedBonus: Number(document.getElementById(prefix + '_buffSpd').value),
        powerEnabled: document.getElementById(prefix + '_powOn').checked,
        powerName: document.getElementById(prefix + '_powName').value,
        powerMultiplier: Number(document.getElementById(prefix + '_powMul').value),
        powerIntelligenceBonus: Number(document.getElementById(prefix + '_powInt').value)
      }
    }

    document.getElementById('fightBtn').addEventListener('click', async () => {
      const payload = { hero1: hero('h1'), hero2: hero('h2') };
      const res = await fetch('/api/combat/simulate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      const data = await res.json();
      const out = data.error
        ? ('ERROR: ' + data.error)
        : ('HEROE 1\\n' + data.hero1 + '\\n\\nHEROE 2\\n' + data.hero2 + '\\n\\nCOMBATE\\n' + data.combatLog);
      document.getElementById('result').textContent = out;
    });
  </script>
</body>
</html>
""";

        return template
                .replace("{{FORM1}}", heroForm("h1", "Heroe 1", "Aragorn", "Humano"))
                .replace("{{FORM2}}", heroForm("h2", "Heroe 2", "Legolas", "Elfo"));
    }

    private static String heroForm(String p, String title, String defaultName, String defaultRace) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>").append(title).append("</h2>");
        sb.append("<label>Nombre <input id=\"").append(p).append("_name\" value=\"").append(defaultName).append("\" /></label>");
        sb.append("<label>Raza <input id=\"").append(p).append("_race\" value=\"").append(defaultRace).append("\" /></label>");
        sb.append("<label>Nivel <input id=\"").append(p).append("_level\" type=\"number\" value=\"1\" /></label>");
        sb.append("<label>HP <input id=\"").append(p).append("_hp\" type=\"number\" value=\"100\" /></label>");
        sb.append("<label>Ataque <input id=\"").append(p).append("_attack\" type=\"number\" value=\"20\" /></label>");
        sb.append("<label>Defensa <input id=\"").append(p).append("_defense\" type=\"number\" value=\"10\" /></label>");
        sb.append("<label>Velocidad <input id=\"").append(p).append("_speed\" type=\"number\" value=\"15\" /></label>");
        sb.append("<label>Inteligencia <input id=\"").append(p).append("_int\" type=\"number\" value=\"8\" /></label>");

        sb.append("<label>Equipo 1<select id=\"").append(p).append("_eq1\">")
          .append("<option value=\"\">(ninguno)</option>")
          .append("<option value=\"espada_basica\">Espada Basica</option>")
          .append("<option value=\"espada_efica\">Espada Efica</option>")
          .append("<option value=\"arco_madera\">Arco de Madera</option>")
          .append("<option value=\"arco_fuego\">Arco de Fuego</option>")
          .append("</select></label>");

        sb.append("<label>Equipo 2<select id=\"").append(p).append("_eq2\">")
          .append("<option value=\"\">(ninguno)</option>")
          .append("<option value=\"armadura_cuero\">Armadura de Cuero</option>")
          .append("<option value=\"armadura_acero\">Armadura de Acero</option>")
          .append("<option value=\"armadura_mithril\">Armadura de Mithril</option>")
          .append("</select></label>");

        sb.append("<label><input id=\"").append(p).append("_buffOn\" type=\"checkbox\" /> Buff</label>");
        sb.append("<label>Nombre Buff <input id=\"").append(p).append("_buffName\" value=\"Buff\" /></label>");
        sb.append("<label>Buff HP <input id=\"").append(p).append("_buffHp\" type=\"number\" value=\"0\" /></label>");
        sb.append("<label>Buff ATK <input id=\"").append(p).append("_buffAtk\" type=\"number\" value=\"0\" /></label>");
        sb.append("<label>Buff SPD <input id=\"").append(p).append("_buffSpd\" type=\"number\" value=\"0\" /></label>");

        sb.append("<label><input id=\"").append(p).append("_powOn\" type=\"checkbox\" /> Power</label>");
        sb.append("<label>Nombre Power <input id=\"").append(p).append("_powName\" value=\"Power\" /></label>");
        sb.append("<label>Multiplicador <input id=\"").append(p).append("_powMul\" type=\"number\" step=\"0.1\" value=\"1.2\" /></label>");
        sb.append("<label>Power INT <input id=\"").append(p).append("_powInt\" type=\"number\" value=\"0\" /></label>");
        return sb.toString();
    }
}
