# Catch That Cow! 🐄

Ein textbasiertes Spiel, in dem du wilde Tiere in rundenbasierten Stat-Battles fängst!

## Spielprinzip

- Bewege dich durch verschiedene Biome
- Begegne wilden Tieren mit unterschiedlichen Stats
- Kämpfe in rundenbasierten Battles
- Fange Tiere und baue dein Inventar auf

## Technologie

Das Projekt verwendet **Clean Architecture** mit folgender Schichtenstruktur:

```
┌─────────────────────────────────────┐
│  0-CatchThatCow-plugin (Plugins)    │  Console I/O
├─────────────────────────────────────┤
│  1-CatchThatCow-adapters (Adapters) │  Presenter, Input
├─────────────────────────────────────┤
│  2-CatchThatCow-application (Use    │  Spiellogik
│  Cases)                             │
├─────────────────────────────────────┤
│  3-CatchThatCow-domain (Entities)   │  Geschäftslogik
├─────────────────────────────────────┤
│  4-CatchThatCow-abstraction         │  Shared
└─────────────────────────────────────┘
```

## Voraussetzungen

- Java 11 oder höher
- Maven 3.6 oder höher

## Projekt bauen

```bash
mvn clean compile
```

## Spiel starten

### Einfachste Methode:
```bash
./play.sh
```

### Mit Nix (reproduzierbare Umgebung):
```bash
nix develop
./play.sh
```

### Oder manuell:
```bash
# 1. Kompilieren
mvn clean compile

# 2. Starten
java -cp \
  4-CatchThatCow-abstraction/target/classes:\
  3-CatchThatCow-domain/target/classes:\
  2-CatchThatCow-application/target/classes:\
  1-CatchThatCow-adapters/target/classes:\
  0-CatchThatCow-plugin/target/classes:\
  0-CatchThatCow-main/target/classes \
  de.heinzenburger.Main
```

## Spielanleitung

### Hauptmenü
1. **Erkunden** - Begegne einem wilden Tier im aktuellen Biom
2. **Bewegen** - Gehe zu einem anderen Biom
3. **Inventar anzeigen** - Zeige deine gefangenen Tiere
4. **Beenden** - Spiel verlassen

### Kampfsystem

Wenn du einem wilden Tier begegnest:
- Du erhältst ein zufälliges Kampfinventar von 3 Tieren
- Der Kampf geht bis 3 Punkte (Best-of-5)
- Jede Runde:
  - Bei **Raubtieren** greift das Tier in der ersten Runde zuerst an
  - Bei **Fluchttieren** darfst du zuerst angreifen
  - Wähle ein Tier und eine Stat-Kategorie
  - Das Tier mit dem höheren Stat-Wert gewinnt die Runde

### Stats
- **Geschwindigkeit** - Wie schnell das Tier ist
- **Länge** - Wie lang/groß das Tier ist
- **Gewicht** - Wie schwer das Tier ist
- **Stärke** - Wie stark das Tier ist

### Kampfausgang
- **Sieg**: Du fängst das wilde Tier und es wird deinem Inventar hinzugefügt
- **Niederlage**: Du verlierst ein zufälliges Tier aus deinem Kampfinventar

## Biome & Level

Die Welt besteht aus (2n+1)² Biomen (Standard: 11×11).

**Biome:**
- Startgebiet (Level 0)
- Wüste, Dschungel, Tundra, Wald, etc.

**Tiere nach Level:**
- Level 1: Biene, Hase
- Level 2: Kobra, Wolf
- Level 3: Eisbär, Elefant

Je weiter du vom Startgebiet entfernt bist, desto stärker werden die Tiere!

## Architektur

### Domain Layer (3-CatchThatCow-domain)
Kern-Geschäftslogik:
- `Animal` - Tierentität mit Stats
- `Player` - Spieler mit Inventar und Position
- `Biome` - Spielwelt-Gebiete
- `Fight` - Kampflogik
- `WorldMap` - Spielwelt

### Application Layer (2-CatchThatCow-application)
Use Cases und Interfaces:
- `PlayGameUseCase` - Hauptspiel-Loop
- `FightUseCase` - Kampfsystem
- `EncounterAnimalUseCase` - Tier-Begegnungen
- `MovePlayerUseCase` - Navigation
- `ShowInventoryUseCase` - Inventar-Verwaltung

### Adapter Layer (1-CatchThatCow-adapters)
Implementierungen der Interfaces:
- `TerminalFightPresenter` - Kampf-Ausgabe
- `TerminalNavigationPresenter` - Navigation-Ausgabe
- `TerminalInventoryPresenter` - Inventar-Ausgabe
- `TerminalGamePresenter` - Spiel-Ausgabe
- `ScannerUserInput` - Benutzereingabe

### Plugin Layer (0-CatchThatCow-plugin)
Externe Frameworks:
- `SystemOutPrintlnPresenter` - Console-Ausgabe

### Main (0-CatchThatCow-main)
Composition Root:
- `Main` - Dependency Injection und Spielstart

## Tests ausführen

```bash
mvn test
```

## Nächste Schritte für Erweiterungen

- Flucht-Option bei Fluchttieren
- Speichern/Laden des Spielstands
- Mehr Tierarten und Biome
- Spezielle Fähigkeiten für Tiere
- Achievements und Statistiken
- Farben im Terminal (ANSI-Codes)

## Clean Architecture Prinzipien

✅ **Dependency Rule eingehalten**
- Abhängigkeiten zeigen nur nach innen
- Domain kennt keine äußeren Schichten
- Interfaces in inneren Schichten definiert

✅ **Testbarkeit**
- Use Cases testbar ohne UI
- Domain testbar ohne Abhängigkeiten
- Mocks durch Interfaces möglich

✅ **Technologie-Unabhängigkeit**
- Geschäftslogik unabhängig von Framework
- Console-Output leicht austauschbar
- Persistierung später einfach hinzufügbar

---

Viel Spaß beim Spielen und Erweitern! 🎮
