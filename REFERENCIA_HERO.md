# 📖 REFERENCIA RÁPIDA - CLASE HERO

## Quick Reference Card

### Crear un Héroe

```java
// Paso 1: Crear estadísticas
HeroStats stats = new HeroStats(
    100,  // HP
    50,   // Mana
    20,   // Ataque
    10,   // Defensa
    15,   // Velocidad
    8     // Inteligencia
);

// Paso 2: Crear héroe
Hero hero = new Hero("Aragorn", "Humano", 5, stats);
```

---

## API de Hero

### Getters

```java
String getName()                    // "Aragorn"
String getRace()                    // "Humano"
int getLevel()                      // 5
HeroStats getStats()                // Copia defensiva
int getCurrentHp()                  // HP actual (0-100)
String getDisplayInfo()             // Info formateada
String getDecorationInfo()          // Decoraciones aplicadas
String getSummary()                 // Resumen corto
```

### Acciones de Combate

```java
int takeDamage(int damage)          // Retorna daño real aplicado
void restoreHp()                    // Restaura HP al máximo
boolean isAlive()                   // true si HP > 0
```

### Efectos

```java
void applyEffect(String name, int duration)
// En Hero base: sin implementación
// En decoradores: aplicar buff/debuff
```

---

## Lógica de Daño

```
Daño Recibido = 25
Defensa = 10

Daño Real = max(1, 25 - 10/2)
          = max(1, 25 - 5)
          = max(1, 20)
          = 20

HP Nuevo = 100 - 20 = 80
```

---

## Validaciones Automáticas

| Parámetro | Mínimo | Acción |
|-----------|--------|--------|
| HP | 1 | Si < 1 → ajustar a 1 |
| Mana | 0 | Si < 0 → ajustar a 0 |
| Ataque | 0 | Si < 0 → ajustar a 0 |
| Defensa | 0 | Si < 0 → ajustar a 0 |
| Velocidad | 1 | Si < 1 → ajustar a 1 |
| Inteligencia | 0 | Si < 0 → ajustar a 0 |
| Nivel | 1 | Si < 1 → ajustar a 1 |

---

## Ejemplos de Uso

### Ejemplo 1: Crear y Mostrar

```java
Hero hero = new Hero("Legolas", "Elfo", 8, 
    new HeroStats(120, 80, 25, 8, 18, 12));

System.out.println(hero.getDisplayInfo());
// Muestra información visual formateada
```

### Ejemplo 2: Combate

```java
Hero warrior = new Hero("Gimli", "Enano", 7,
    new HeroStats(150, 30, 30, 15, 10, 5));

// Recibir daño
int damage = warrior.takeDamage(40);
System.out.println("Daño: " + damage);  // 40 - 7 = 33

// Verificar estado
if (warrior.isAlive()) {
    System.out.println("Sigue en pie: " + warrior.getSummary());
}
```

### Ejemplo 3: Múltiples Ataques

```java
Hero hero = new Hero("Frodo", "Hobbit", 3,
    new HeroStats(80, 40, 10, 8, 12, 6));

hero.takeDamage(20);  // 100 - 10 = 80
hero.takeDamage(15);  // 80 - 10 = 70
hero.takeDamage(25);  // 70 - 10 = 60

System.out.println(hero.getCurrentHp());  // 60
```

### Ejemplo 4: Restauración

```java
hero.takeDamage(50);
System.out.println(hero.getCurrentHp());  // 50

hero.restoreHp();
System.out.println(hero.getCurrentHp());  // 100
```

---

## Métodos de HeroStats

```java
// Constructor
HeroStats stats = new HeroStats(100, 50, 20, 10, 15, 8);

// Getters
int hp = stats.getHp();              // 100
int mana = stats.getMana();          // 50

// With methods (crea nuevas instancias)
HeroStats enhanced = stats.withAttack(30);  // ATK: 20 → 30
HeroStats buffed = enhanced.withDefense(15); // DEF: 10 → 15

// Sumar estadísticas (para decoradores)
HeroStats bonus = new HeroStats(0, 0, 10, 5, 0, 0);  // +10 ATK, +5 DEF
HeroStats total = stats.add(bonus);

// Serializar
String json = stats.toJson();        // Gson JSON
String text = stats.toString();      // HeroStats{...}
```

---

## Interfaz IHero

```java
public interface IHero {
    HeroStats getStats();
    String getName();
    String getRace();
    int getLevel();
    String getDisplayInfo();
    int takeDamage(int damage);
    void applyEffect(String effectName, int duration);
    String getDecorationInfo();
}
```

**Propósito**: Permite que Hero y Decoradores implementen el mismo contrato, permitiendo composición sin límite.

---

## Flujo Típico

```
1. Crear HeroStats
   ↓
2. Crear Hero
   ↓
3. Mostrar información (getDisplayInfo)
   ↓
4. Combate (takeDamage)
   ↓
5. Verificar estado (isAlive)
   ↓
6. [Futura] Aplicar decoradores (EquipmentDecorator, BuffDecorator)
   ↓
7. [Futura] Mostrar decoraciones (getDecorationInfo)
```

---

## Tests Disponibles

14 tests unitarios en `HeroTest.java`:

| Test | Propósito |
|------|----------|
| testHeroCreation | Validar nombre, raza, nivel |
| testHeroStatsInitialized | Validar estadísticas iniciales |
| testHeroCurrentHpInitialized | Validar HP inicial = máximo |
| testTakeDamage | Daño simple con defensa |
| testTakeDamageMinimum | Daño mínimo de 1 |
| testTakeDamageMultiple | Múltiples ataques acumulativos |
| testTakeDamageKill | Muerte del héroe |
| testRestoreHp | Restauración a máximo |
| testGetDisplayInfo | Display formateado |
| testGetSummary | Resumen corto |
| testGetDecorationInfo | Decoraciones (sin decoraciones) |
| testGetStatsCopy | Copia defensiva |
| testInvalidHeroStats | Validación de mínimos |

---

## Próximas Clases (Decoradores)

```
HeroDecorator (abstracto)
  ├── EquipmentDecorator
  │   ├── ArmorDecorator
  │   └── WeaponDecorator
  ├── BuffDecorator
  │   ├── PotionDecorator
  │   └── SpellDecorator
  └── PowerDecorator
      ├── CriticalStrikeDecorator
      └── ArcaneDecorator
```

Cada decorador:
- Implementará IHero
- Envolverá otro IHero
- Modificará getStats() sumando sus bonificaciones
- Se mostrará en getDecorationInfo()

---

## Estructura de Carpetas

```
src/main/java/com/mygamepattern/
├── Main.java
└── rpg/
    ├── domain/                    ← Clases de dominio
    │   ├── HeroStats.java         ✅ Implementado
    │   ├── IHero.java             ✅ Implementado
    │   ├── Hero.java              ✅ Implementado
    │   ├── HeroDecorator.java     ⏳ Próximo
    │   ├── decorator/
    │   │   ├── EquipmentDecorator.java
    │   │   ├── BuffDecorator.java
    │   │   └── PowerDecorator.java
    │   └── combat/
    │       └── CombatSimulator.java
    └── web/                       ← Servidor HTTP (futuro)
        └── HttpServer.java
```

---

## Compilación y Tests

```bash
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Ejecutar aplicación
mvn exec:java

# Build completo
mvn clean install
```

---

**Status**: ✅ COMPLETADO  
**Última actualización**: 2026-03-20  
**Próximo**: HeroDecorator

