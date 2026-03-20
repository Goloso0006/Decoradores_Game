# MyGamePattern - RPG Combat Web

Sistema RPG con patrón **Decorator** embebido en web. Permite equipar héroes con armaduras, armas, poderes y buffs dinámicamente, simular combates en tiempo real desde interfaz web.

## ✅ Setup completado

### Entorno verificado
- **Java**: OpenJDK 25.0.2 ✓
- **Maven**: 3.9.6 (embebido en `tools/`) ✓
- **Dependencias**:
  - Gson 2.11.0 (JSON)
  - JUnit 5.10.2 (tests)
  - exec-maven-plugin 3.5.0 (ejecución)

### Estructura del proyecto
```
MyGamePattern/
├── pom.xml                          # Configuración Maven
├── src/
│   ├── main/java/com/mygamepattern/
│   │   └── Main.java               # Entry point
│   └── test/java/
│       └── MainSmokeTest.java      # Test base
├── tools/apache-maven-3.9.6/       # Maven embebido (NO trackear en git)
├── mvn.bat                         # Script batch para ejecutar Maven
├── build.ps1                       # Script PowerShell para ejecutar Maven
└── README.md                       # Este archivo
```

## 🚀 Comandos rápidos

### Opción 1: PowerShell (recomendado)
```powershell
cd C:\Users\marlo\OneDrive\Escritorio\MyGamePattern

# Compilar
.\build.ps1 clean compile

# Ejecutar tests
.\build.ps1 clean test

# Ejecutar aplicación
.\build.ps1 exec:java

# Build completo
.\build.ps1 clean install
```

### Opción 2: Batch script
```batch
cd C:\Users\marlo\OneDrive\Escritorio\MyGamePattern
mvn.bat clean test
mvn.bat exec:java
```

### Opción 3: Maven directo
```powershell
.\tools\apache-maven-3.9.6\bin\mvn.cmd clean test
.\tools\apache-maven-3.9.6\bin\mvn.cmd exec:java
```

### Opción 4: Sin Maven (javac/java)
```powershell
javac -d out src/main/java/com/mygamepattern/Main.java
java -cp out com.mygamepattern.Main
```

## 📋 Status actual
- ✅ Compilación: OK
- ✅ Tests: 14/14 PASS (Hero tests)
- ✅ Ejecución: Main.java funcional
- ✅ Clase Hero: Completada y validada
- ⏳ Siguiente: Implementar HeroDecorator

## 🎯 Clases Implementadas

### ✅ Dominio RPG (src/main/java/com/mygamepattern/rpg/domain/)
- **HeroStats.java** - Objeto de valor con 6 estadísticas
- **IHero.java** - Interfaz para patrón Decorator
- **Hero.java** - Clase base del héroe con combate

### ✅ Tests (src/test/java/com/mygamepattern/rpg/domain/)
- **HeroTest.java** - 14 tests unitarios (100% PASS)

## 📚 Próximos pasos
1. ✅ Diseñar esquema de clases Hero
2. ⏳ Implementar HeroDecorator (clase abstracta)
3. ⏳ Crear EquipmentDecorator (armaduras, armas)
4. ⏳ Crear BuffDecorator (pociones, efectos)
5. ⏳ Crear PowerDecorator (habilidades)
6. ⏳ Desarrollar motor de combate (CombatSimulator)
7. ⏳ Crear servidor HTTP con interfaz web

## 📖 Documentación
- `CLASE_HERO_COMPLETADA.md` - Descripción completa de Hero
- `REFERENCIA_HERO.md` - Quick reference y ejemplos
- `ESQUEMA_HERO.md` - Esquema arquitectónico

