# 📋 Esquema: Clase Hero

## Descripción General

La clase **Hero** es la entidad principal del sistema RPG. Representa un héroe con estadísticas base que pueden ser modificadas dinámicamente mediante decoradores (armaduras, armas, poderes, buffs).

## Estructura de Clases

```
IHero (interfaz)
  ├── Hero (clase base)
  ├── HeroDecorator (clase abstracta, próxima fase)
  │   ├── EquipmentDecorator
  │   ├── BuffDecorator
  │   └── PowerDecorator
```

---

## 1. HeroStats (Objeto de Valor)

**Responsabilidad**: Encapsular las 6 estadísticas base del héroe de forma inmutable.

### Atributos
```java
- hp: int                  // Puntos de vida (mínimo 1)
- mana: int                // Energía mágica (mínimo 0)
- attack: int              // Daño físico base (mínimo 0)
- defense: int             // Resistencia a daño (mínimo 0)
- speed: int               // Orden de turno (mínimo 1)
- intelligence: int        // Modificador mágico (mínimo 0)
```

### Métodos Principales
```java
// Constructor
HeroStats(int hp, int mana, int attack, int defense, int speed, int intelligence)
HeroStats(HeroStats other)  // Constructor copia

// Getters
int getHp(), getMana(), getAttack(), getDefense(), getSpeed(), getIntelligence()

// Builder pattern
HeroStats withHp(int newHp)
HeroStats withMana(int newMana)
HeroStats withAttack(int newAttack)
HeroStats withDefense(int newDefense)
HeroStats withSpeed(int newSpeed)
HeroStats withIntelligence(int newIntelligence)

// Operaciones
HeroStats add(HeroStats other)  // Suma ambos conjuntos (para decoradores aditivos)

// Serialización
String toJson()                  // Gson
String toString()
```

### Características
- ✅ Inmutable (no tiene setters)
- ✅ Constructor copia defensivo
- ✅ Validación de mínimos
- ✅ Método `add()` para decoradores aditivos
- ✅ Serializable a JSON

---

## 2. IHero (Interfaz)

**Responsabilidad**: Definir contrato para Hero y todos sus decoradores.

### Métodos Requeridos
```java
// Estadísticas
HeroStats getStats()                    // Retorna estadísticas actuales

// Identidad
String getName()                         // Nombre del héroe
String getRace()                         // Raza (Humano, Elfo, etc.)
int getLevel()                          // Nivel (1+)

// Información
String getDisplayInfo()                 // UI: Formato completo del héroe
String getDecorationInfo()              // UI: Lista de decoraciones aplicadas

// Acciones
int takeDamage(int damage)              // Recibe daño, modifica HP actual
void applyEffect(String effectName, int duration)  // Aplica buff/debuff
```

### Propósito
Permite que decoradores se compongan sin límite usando composición en lugar de herencia.

---

## 3. Hero (Clase Base)

**Responsabilidad**: Implementar IHero como héroe base sin decoraciones.

### Atributos
```java
- final String name          // Nombre del héroe (ej: "Aragorn")
- final String race          // Raza (ej: "Humano")
- final int level            // Nivel (ej: 5)
- final HeroStats baseStats  // Estadísticas base
- int currentHp              // HP actual durante combate (mutable)
```

### Métodos Principales

#### Constructor
```java
Hero(String name, String race, int level, HeroStats baseStats)
```

#### Implementación de IHero
```java
@Override HeroStats getStats()          // Retorna copia defensiva
@Override String getName()              // Retorna name
@Override String getRace()              // Retorna race
@Override int getLevel()                // Retorna level

@Override int takeDamage(int damage)
  - Calcula: actualDamage = max(1, damage - defense/2)
  - Modifica: currentHp -= actualDamage
  - Retorna: actualDamage aplicado

@Override void applyEffect(...)         // Sin implementación (override en decoradores)
@Override String getDecorationInfo()    // Retorna "(sin decoraciones)"

@Override String getDisplayInfo()
  - Formato visual con bordes:
    ╔════════════════════════════╗
    ║ Aragorn - Humano (Lvl 5)  ║
    ║ HP: 95/100 | Mana: 50     ║
    ║ ATK: 20 | DEF: 10         ║
    ║ SPD: 15 | INT: 8          ║
    ║ Decoraciones: (sin)        ║
    ╚════════════════════════════╝
```

#### Métodos Adicionales
```java
int getCurrentHp()          // Retorna HP actual
void restoreHp()            // Restaura HP al máximo
boolean isAlive()           // true si HP > 0
String getSummary()         // Resumen: "Aragorn (Humano Lvl5) - HP: 95/100"
```

---

## Ejemplo de Uso

```java
// 1. Crear estadísticas base
HeroStats stats = new HeroStats(100, 50, 20, 10, 15, 8);

// 2. Crear héroe
Hero hero = new Hero("Aragorn", "Humano", 5, stats);

// 3. Mostrar información
System.out.println(hero.getDisplayInfo());

// 4. Simular combate
int damage = hero.takeDamage(25);
System.out.println("Daño recibido: " + damage);  // 25 - 5 (defensa) = 20

// 5. Verificar estado
System.out.println(hero.getSummary());  // "Aragorn (Humano Lvl5) - HP: 80/100"

// 6. Restaurar
hero.restoreHp();
System.out.println(hero.isAlive());  // true
```

---

## Flujo de Decorator Pattern

La arquitectura permite composición así:

```
Hero base
  ↓
[Decorator: Armadura +5 DEF]
  ↓
[Decorator: Espada +10 ATK]
  ↓
[Decorator: Buff de Velocidad +3 SPD]
  ↓
Estadísticas finales = suma de todas las modificaciones
```

Cada decorador envuelve el anterior, permitiendo:
- ✅ Aplicar múltiples equipos/buffs
- ✅ Remover decoradores dinámicamente
- ✅ Composición sin límite
- ✅ Cambiar comportamiento en tiempo real

---

## Próximas Fases

1. **HeroDecorator** (clase abstracta) - Base para todos los decoradores
2. **EquipmentDecorator** - Armas, armaduras (permanentes)
3. **BuffDecorator** - Efectos temporales (3-5 turnos)
4. **PowerDecorator** - Habilidades especiales

---

**Status**: ✅ Implementado y compilable con Maven  
**Ubicación**: `src/main/java/com/mygamepattern/rpg/domain/`  
**Prueba**: Ejecutar con `.\build.ps1 exec:java`

