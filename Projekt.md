# Programmentwurf: Catch That Cow!

**Name:** [Name, Vorname]
**Matrikelnummer:** [MNR]
**Abgabedatum:** [DATUM]

---

## Kapitel 1: Einführung

### Übersicht über die Applikation

**Catch That Cow!** ist ein textbasiertes Spiel, in dem der Spieler Tiere in verschiedenen Biomen fängt und in rundenbasierten Kämpfen gegen wilde Tiere antritt.

#### Spielmechanik

Die Tiere leben in verschiedenen Biomen und haben Stats in verschiedenen Kategorien (z.B. Geschwindigkeit, Länge, Gewicht). Diese Stats hängen von der Tierart ab und variieren zufällig leicht.

**Kampfsystem:**
- Begegnung mit wilden Tieren ("Eine wilde Kobra erscheint!")
- Raubtiere greifen zuerst an, bei Fluchttieren kann der Spieler wählen (angreifen oder fliehen)
- Kampfinventar: 3 zufällig ausgewählte Tiere aus dem Inventar
- Kampf bis 3 Punkte (Best-of-5)
- Jede Runde: Angriff in einer bestimmten Kategorie
  - Gegnertier wählt zufällig eine Kategorie
  - Spieler wählt ein Tier und eine Kategorie
  - Bessere Stats gewinnen die Runde
  - Verwendetes Tier wird für weitere Runden deaktiviert
- Bei Niederlage: Zufälliges Tier aus Kampfinventar verloren
- Bei Sieg: Gegnertier wird zum Inventar hinzugefügt

**Weltkarte:**
- Karte mit (2n+1)² Biomen
- Start im zentralen Startbiom
- Navigation in Himmelsrichtungen (Norden, Osten, Süden, Westen)
- Tiere sind in Level eingeteilt
- Biome mit Distanz X zum Startbiom enthalten Tiere mit Level X

### Wie startet man die Applikation?

**Voraussetzungen:**
- Java JDK 11 oder höher
- Maven 3.6 oder höher

**Schritt-für-Schritt-Anleitung:**
1. Repository klonen: `git clone [repository-url]`
2. In das Projektverzeichnis wechseln: `cd CatchThatCow`
3. Projekt bauen: `mvn clean install`
4. Anwendung starten: `mvn -pl 0-CatchThatCow-main exec:java`

Alternativ:
```bash
cd 0-CatchThatCow-main
java -cp target/classes de.heinzenburger.Main
```

### Wie testet man die Applikation?

**Voraussetzungen:**
- Java JDK 11 oder höher
- Maven 3.6 oder höher
- JUnit 5 (wird automatisch von Maven heruntergeladen)

**Schritt-für-Schritt-Anleitung:**
1. Alle Tests ausführen: `mvn test`
2. Tests für ein bestimmtes Modul: `mvn -pl [modul-name] test`
3. Test-Coverage generieren: `mvn jacoco:report`
4. Coverage-Report ansehen: `target/site/jacoco/index.html` im Browser öffnen

---

## Kapitel 2: Clean Architecture

### Was ist Clean Architecture?

Clean Architecture ist ein Architekturmodell von Robert C. Martin (Uncle Bob), das darauf abzielt, Software wartbar, testbar und technologieunabhängig zu gestalten. Das zentrale Konzept ist die **Dependency Rule**: Abhängigkeiten dürfen nur nach innen zeigen, niemals nach außen.

Die Architektur ist in konzentrische Schichten organisiert:
- **Innerster Kern (Entities/Domain):** Geschäftslogik und Domänenobjekte
- **Use Cases (Application):** Anwendungsspezifische Geschäftsregeln
- **Interface Adapters (Adapters):** Konvertierung zwischen Use Cases und externen Systemen
- **Frameworks & Drivers (Plugins):** Externe Frameworks, Datenbanken, UI

**Vorteile:**
- Unabhängigkeit von Frameworks und UI-Technologien
- Testbarkeit ohne externe Abhängigkeiten
- Unabhängigkeit von Datenbanken
- Verzögerte Entscheidungen über Technologien möglich

### Analyse der Dependency Rule

#### Positiv-Beispiel: Dependency Rule

**Klasse:** `MeetAnimalUseCase` (Application Layer)

```java
public class MeetAnimalUseCase {
    UiAnzeiger uiAnzeiger;

    public MeetAnimalUseCase(UiAnzeiger uiAnzeiger) {
        this.uiAnzeiger = uiAnzeiger;
    }

    public void sayHi() {
        Animal animal = new Animal();
        uiAnzeiger.printsomething(animal.getNoise());
    }
}
```

**UML-Diagramm:**
```
┌─────────────────────┐
│  MeetAnimalUseCase  │
├─────────────────────┤
│ - uiAnzeiger        │
├─────────────────────┤
│ + sayHi(): void     │
└─────────────────────┘
         │
         │ depends on
         ↓
┌─────────────────────┐         ┌─────────────────────┐
│   <<interface>>     │         │      Animal         │
│     UiAnzeiger      │         ├─────────────────────┤
├─────────────────────┤         │ - noise: String     │
│ + printsomething()  │         ├─────────────────────┤
└─────────────────────┘         │ + getNoise(): String│
         ↑                       └─────────────────────┘
         │ implements                      ↑
         │                                 │ uses
┌─────────────────────┐                   │
│  TerminalPresenter  │───────────────────┘
├─────────────────────┤
│ - textPresenter     │
├─────────────────────┤
│ + printsomething()  │
└─────────────────────┘
```

**Analyse:**
- `MeetAnimalUseCase` (Application) hängt von `UiAnzeiger` (Interface im Application Layer) ab
- `MeetAnimalUseCase` verwendet `Animal` aus der Domain-Schicht (innerste Schicht)
- Die konkrete Implementierung `TerminalPresenter` (Adapter) implementiert das Interface
- **Abhängigkeitsrichtung:** Adapter → Application → Domain ✓
- **Dependency Rule erfüllt:** Alle Abhängigkeiten zeigen nach innen

**Wer hängt von der Klasse ab:**
- `Main` (Plugin-Schicht) erzeugt und verwendet `MeetAnimalUseCase`
- Keine Abhängigkeiten von äußeren Schichten innerhalb der Klasse

#### Negativ-Beispiel: Dependency Rule

**Hinweis:** Im aktuellen Code-Stand gibt es noch keine explizite Verletzung der Dependency Rule. Ein typisches Negativ-Beispiel wäre:

**Hypothetisches Beispiel:**
```java
public class MeetAnimalUseCase {
    SystemOutPrintlnPresenter presenter;  // FALSCH: Direkte Abhängigkeit von Plugin-Schicht!

    public void sayHi() {
        Animal animal = new Animal();
        presenter.print(animal.getNoise());  // Application hängt direkt von Plugin ab
    }
}
```

**Problem:**
- Use Case (Application Layer) hängt direkt von konkreter Implementierung aus Plugin-Layer ab
- Dependency Rule verletzt: Application → Plugin (nach außen!)
- Testbarkeit erschwert (Mock nicht einfach austauschbar)
- Technologie-Abhängigkeit in Business-Logik

**Lösung:**
- Interface `UiAnzeiger` im Application Layer definieren
- Use Case hängt nur vom Interface ab
- Plugin-Schicht implementiert das Interface
- Abhängigkeit wird umgekehrt (Dependency Inversion Principle)

### Analyse der Schichten

#### Schicht 1: Domain (3-CatchThatCow-domain)

**Klasse:** `Animal`

```java
public class Animal {
    String noise = "Oink";

    public String getNoise() {
        return noise;
    }
}
```

**UML:**
```
┌─────────────────────┐
│      Animal         │
├─────────────────────┤
│ - noise: String     │
├─────────────────────┤
│ + getNoise(): String│
└─────────────────────┘
```

**Aufgabe:**
- Repräsentation eines Tieres im Spiel
- Enthält grundlegende Eigenschaften (hier: Geräusch)
- Reine Geschäftslogik ohne externe Abhängigkeiten

**Einordnung in Clean Architecture:**
- **Schicht:** Entities / Domain (innerster Kreis)
- **Begründung:**
  - Enthält Kern-Geschäftsobjekte
  - Keine Abhängigkeiten zu anderen Schichten
  - Repräsentiert fundamentale Geschäftsregeln
  - Wird von allen anderen Schichten verwendet
  - Änderungen nur bei Änderung der Geschäftslogik

#### Schicht 2: Application (2-CatchThatCow-application)

**Klassen:** `MeetAnimalUseCase` und `UiAnzeiger`

```java
public class MeetAnimalUseCase {
    UiAnzeiger uiAnzeiger;

    public MeetAnimalUseCase(UiAnzeiger uiAnzeiger) {
        this.uiAnzeiger = uiAnzeiger;
    }

    public void sayHi() {
        Animal animal = new Animal();
        uiAnzeiger.printsomething(animal.getNoise());
    }
}

public interface UiAnzeiger {
    void printsomething(String something);
}
```

**UML:**
```
┌─────────────────────┐
│  MeetAnimalUseCase  │
├─────────────────────┤
│ - uiAnzeiger        │
├─────────────────────┤
│ + sayHi(): void     │
└─────────────────────┘
         │
         │ uses
         ↓
┌─────────────────────┐
│   <<interface>>     │
│     UiAnzeiger      │
├─────────────────────┤
│ + printsomething()  │
└─────────────────────┘
```

**Aufgabe:**
- Orchestrierung eines konkreten Anwendungsfalls (Use Case)
- Koordination zwischen Domain-Objekten und Präsentationsschicht
- Definition von Interfaces für externe Abhängigkeiten (Dependency Inversion)

**Einordnung in Clean Architecture:**
- **Schicht:** Use Cases / Application Layer
- **Begründung:**
  - Implementiert anwendungsspezifische Geschäftsregeln
  - Orchestriert den Flow zwischen Domain und Adaptern
  - Definiert Interfaces für Abhängigkeiten (UiAnzeiger)
  - Unabhängig von UI-Technologie und Frameworks
  - Enthält keine Details über Präsentation oder Persistierung

---

## Clean Architecture Übersicht: Code-Zuordnung nach Schichten

### Schichtenmodell des Projekts

```
┌─────────────────────────────────────────────────────────────────┐
│                    0-CatchThatCow-plugin                        │
│                  (Frameworks & Drivers Layer)                   │
│  - SystemOutPrintlnPresenter                                    │
│  - Zukunft: Datenbankanbindung, File I/O, externe APIs         │
└─────────────────────────────────────────────────────────────────┘
                              ↓ implements
┌─────────────────────────────────────────────────────────────────┐
│                  1-CatchThatCow-adapters                        │
│                  (Interface Adapters Layer)                     │
│  - TerminalPresenter (implements UiAnzeiger)                    │
│  - TextPresenter (Interface)                                    │
│  - Zukunft: Controller, Gateways, Presenter für verschiedene   │
│    Ausgabeformate                                               │
└─────────────────────────────────────────────────────────────────┘
                              ↓ implements/uses
┌─────────────────────────────────────────────────────────────────┐
│               2-CatchThatCow-application                        │
│                    (Use Cases Layer)                            │
│  - MeetAnimalUseCase                                            │
│  - UiAnzeiger (Interface)                                       │
│  - Zukunft: FightUseCase, MovePlayerUseCase,                   │
│    CatchAnimalUseCase, InventoryUseCase                        │
└─────────────────────────────────────────────────────────────────┘
                              ↓ uses
┌─────────────────────────────────────────────────────────────────┐
│                 3-CatchThatCow-domain                           │
│                    (Entities Layer)                             │
│  - Animal                                                       │
│  - Zukunft: Player, Biome, FightStats, Inventory, Category     │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│             4-CatchThatCow-abstraction                          │
│        (Gemeinsame Abstraktionen/Utilities - Optional)          │
│  - Aktuell leer                                                 │
│  - Zukunft: Shared DTOs, Common Exceptions, Base Interfaces    │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                   0-CatchThatCow-main                           │
│                    (Composition Root)                           │
│  - Main (Dependency Injection, Startup)                         │
└─────────────────────────────────────────────────────────────────┘
```

---

## Detaillierte Code-Zuordnung für "Catch That Cow!"

### 🎯 3-CatchThatCow-domain (Entities / Domain Layer)

**Zweck:** Kern-Geschäftslogik und Entitäten ohne jegliche Abhängigkeit

**Aktueller Code:**
- ✅ `Animal` - Basis-Tierklasse

**Benötigter Code für das vollständige Spiel:**

```java
// Tier mit vollständigen Stats
public class Animal {
    private String species;      // z.B. "Kobra", "Biene", "Eisbär"
    private String noise;
    private int level;
    private AnimalType type;     // PREDATOR oder PREY
    private Map<StatCategory, Integer> stats;  // Speed, Length, Weight, etc.

    // Domain-Logik
    public int getStatValue(StatCategory category);
    public boolean winsAgainst(Animal other, StatCategory category);
}

// Value Objects
public enum AnimalType {
    PREDATOR,  // Raubtier (startet Kampf)
    PREY       // Fluchttier (Spieler kann fliehen)
}

public enum StatCategory {
    SPEED,
    LENGTH,
    WEIGHT,
    STRENGTH
}

// Spieler-Entität
public class Player {
    private Position position;
    private List<Animal> inventory;

    public void addAnimal(Animal animal);
    public void removeAnimal(Animal animal);
    public List<Animal> getRandomBattleInventory(int size);
}

// Biome
public class Biome {
    private BiomeType type;  // DESERT, JUNGLE, TUNDRA, etc.
    private Position position;
    private int level;  // Distanz zum Startbiom
    private List<AnimalSpecies> availableAnimals;

    public Animal spawnRandomAnimal();
}

public enum BiomeType {
    START, DESERT, JUNGLE, TUNDRA, FOREST, OCEAN, MOUNTAINS
}

// Position auf der Weltkarte
public class Position {
    private int x;
    private int y;

    public int distanceFromCenter();
    public Position moveNorth();
    public Position moveSouth();
    public Position moveEast();
    public Position moveWest();
}

// Kampf-Logik
public class Fight {
    private Player player;
    private Animal wildAnimal;
    private int playerScore;
    private int enemyScore;
    private List<Animal> usedAnimals;  // Deaktivierte Tiere

    public FightResult playRound(Animal playerAnimal, StatCategory category);
    public boolean isFinished();
    public FightOutcome getOutcome();
}

public enum FightOutcome {
    PLAYER_WON,
    PLAYER_LOST
}

// Weltkarte
public class WorldMap {
    private int size;  // (2n+1)
    private Map<Position, Biome> biomes;

    public Biome getBiomeAt(Position pos);
    public List<Direction> getAvailableDirections(Position pos);
}

public enum Direction {
    NORTH, SOUTH, EAST, WEST
}
```

---

### 🎯 2-CatchThatCow-application (Use Cases / Application Layer)

**Zweck:** Anwendungsspezifische Geschäftsregeln und Orchestrierung

**Aktueller Code:**
- ✅ `MeetAnimalUseCase` - Grundgerüst für Tier-Begegnung
- ✅ `UiAnzeiger` - Interface für Ausgabe

**Benötigter Code für das vollständige Spiel:**

```java
// === USE CASES ===

// Hauptspiel-Loop
public class PlayGameUseCase {
    private Player player;
    private WorldMap worldMap;
    private GamePresenter presenter;

    public void start() {
        // Spieler im Startbiom platzieren
        // Hauptmenü anzeigen
        // User-Input verarbeiten
    }
}

// Begegnung mit wildem Tier
public class EncounterAnimalUseCase {
    private FightPresenter presenter;
    private RandomAnimalGenerator generator;

    public void encounter(Biome biome, Player player) {
        Animal wildAnimal = biome.spawnRandomAnimal();
        presenter.showEncounter(wildAnimal);

        // Entscheidung: Kämpfen oder Fliehen (bei Prey)
    }
}

// Kampf durchführen
public class FightUseCase {
    private FightPresenter presenter;

    public FightOutcome executeFight(Player player, Animal wildAnimal) {
        Fight fight = new Fight(player, wildAnimal);
        List<Animal> battleInventory = player.getRandomBattleInventory(3);

        while (!fight.isFinished()) {
            // Runde spielen
            if (wildAnimal.getType() == PREDATOR && fight.isFirstRound()) {
                // Tier greift zuerst an
                StatCategory category = selectRandomCategory();
                Animal playerAnimal = presenter.selectAnimalForDefense(battleInventory);
                fight.playRound(playerAnimal, category);
            } else {
                // Spieler greift an
                Animal playerAnimal = presenter.selectAnimal(battleInventory);
                StatCategory category = presenter.selectCategory();
                fight.playRound(playerAnimal, category);
            }
        }

        if (fight.getOutcome() == PLAYER_WON) {
            player.addAnimal(wildAnimal);
        } else {
            Animal lostAnimal = selectRandomAnimal(battleInventory);
            player.removeAnimal(lostAnimal);
        }

        return fight.getOutcome();
    }
}

// Bewegung zwischen Biomen
public class MovePlayerUseCase {
    private WorldMap worldMap;
    private NavigationPresenter presenter;

    public void move(Player player, Direction direction) {
        Position newPosition = player.getPosition().move(direction);
        Biome newBiome = worldMap.getBiomeAt(newPosition);
        player.setPosition(newPosition);
        presenter.showBiome(newBiome);
    }

    public void showNavigationOptions(Player player) {
        Position pos = player.getPosition();
        List<Direction> directions = worldMap.getAvailableDirections(pos);
        Map<Direction, Biome> surroundingBiomes = getSurroundingBiomes(pos);
        presenter.showNavigationMenu(surroundingBiomes);
    }
}

// Inventar verwalten
public class ShowInventoryUseCase {
    private InventoryPresenter presenter;

    public void showInventory(Player player) {
        List<Animal> animals = player.getInventory();
        presenter.displayInventory(animals);
    }
}

// === INTERFACES (Port Pattern) ===

// Ausgabe-Interfaces (von Application definiert, von Adapters implementiert)
public interface FightPresenter {
    void showEncounter(Animal wildAnimal);
    Animal selectAnimal(List<Animal> availableAnimals);
    StatCategory selectCategory();
    void showRoundResult(FightResult result);
    void showFightOutcome(FightOutcome outcome);
}

public interface NavigationPresenter {
    void showBiome(Biome biome);
    void showNavigationMenu(Map<Direction, Biome> surroundingBiomes);
    Direction getUserDirection();
}

public interface InventoryPresenter {
    void displayInventory(List<Animal> animals);
}

public interface GamePresenter {
    void showWelcome();
    void showMainMenu();
    GameAction getUserAction();
}

public enum GameAction {
    EXPLORE, VIEW_INVENTORY, QUIT
}

// Input-Interfaces
public interface UserInputPort {
    String readLine();
    int readInt();
    Direction readDirection();
}

// Persistierung-Interfaces (falls gewünscht)
public interface PlayerRepository {
    void save(Player player);
    Player load();
}

public interface AnimalRepository {
    List<AnimalSpecies> getAnimalsByLevel(int level);
    Animal createAnimal(AnimalSpecies species);
}
```

---

### 🎯 1-CatchThatCow-adapters (Interface Adapters Layer)

**Zweck:** Konvertierung zwischen Use Cases und externen Systemen

**Aktueller Code:**
- ✅ `TerminalPresenter` - Adapter für Terminal-Ausgabe
- ✅ `TextPresenter` - Interface für Text-Ausgabe

**Benötigter Code für das vollständige Spiel:**

```java
// === PRESENTER IMPLEMENTATIONS ===

// Terminal-basierter Fight Presenter
public class TerminalFightPresenter implements FightPresenter {
    private TextPresenter textPresenter;
    private UserInputPort input;

    @Override
    public void showEncounter(Animal wildAnimal) {
        textPresenter.print("Ein wildes " + wildAnimal.getSpecies() + " erscheint!");
    }

    @Override
    public Animal selectAnimal(List<Animal> availableAnimals) {
        textPresenter.print("Wähle ein Tier:");
        for (int i = 0; i < availableAnimals.size(); i++) {
            textPresenter.print((i+1) + ". " + availableAnimals.get(i).getSpecies());
        }
        int choice = input.readInt();
        return availableAnimals.get(choice - 1);
    }

    @Override
    public StatCategory selectCategory() {
        textPresenter.print("Wähle eine Kategorie:");
        textPresenter.print("1. Geschwindigkeit");
        textPresenter.print("2. Länge");
        textPresenter.print("3. Gewicht");
        int choice = input.readInt();
        return StatCategory.values()[choice - 1];
    }

    @Override
    public void showRoundResult(FightResult result) {
        textPresenter.print(result.getWinner() + " gewinnt die Runde!");
    }

    @Override
    public void showFightOutcome(FightOutcome outcome) {
        if (outcome == PLAYER_WON) {
            textPresenter.print("Du hast gewonnen! Tier gefangen.");
        } else {
            textPresenter.print("Du hast verloren! Ein Tier wurde entfernt.");
        }
    }
}

// Terminal-basierter Navigation Presenter
public class TerminalNavigationPresenter implements NavigationPresenter {
    private TextPresenter textPresenter;
    private UserInputPort input;

    @Override
    public void showBiome(Biome biome) {
        textPresenter.print("Du befindest dich in: " + biome.getType());
        textPresenter.print("Level: " + biome.getLevel());
    }

    @Override
    public void showNavigationMenu(Map<Direction, Biome> surroundingBiomes) {
        textPresenter.print("Wohin möchtest du gehen?");
        surroundingBiomes.forEach((dir, biome) -> {
            textPresenter.print(dir + ": " + biome.getType());
        });
        textPresenter.print("S: Bleiben und erkunden");
    }

    @Override
    public Direction getUserDirection() {
        return input.readDirection();
    }
}

// Terminal-basierter Inventory Presenter
public class TerminalInventoryPresenter implements InventoryPresenter {
    private TextPresenter textPresenter;

    @Override
    public void displayInventory(List<Animal> animals) {
        textPresenter.print("=== DEIN INVENTAR ===");
        for (Animal animal : animals) {
            textPresenter.print(animal.getSpecies() + " (Level " + animal.getLevel() + ")");
            animal.getStats().forEach((category, value) -> {
                textPresenter.print("  " + category + ": " + value);
            });
        }
    }
}

// Terminal-basierter Game Presenter
public class TerminalGamePresenter implements GamePresenter {
    private TextPresenter textPresenter;
    private UserInputPort input;

    @Override
    public void showWelcome() {
        textPresenter.print("=================================");
        textPresenter.print("   CATCH THAT COW!              ");
        textPresenter.print("=================================");
    }

    @Override
    public void showMainMenu() {
        textPresenter.print("1. Erkunden");
        textPresenter.print("2. Inventar anzeigen");
        textPresenter.print("3. Beenden");
    }

    @Override
    public GameAction getUserAction() {
        int choice = input.readInt();
        return GameAction.values()[choice - 1];
    }
}

// User Input Adapter
public class TerminalUserInput implements UserInputPort {
    private Scanner scanner;

    public TerminalUserInput() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public int readInt() {
        return scanner.nextInt();
    }

    @Override
    public Direction readDirection() {
        String input = scanner.nextLine().toUpperCase();
        switch (input) {
            case "N": return Direction.NORTH;
            case "S": return Direction.SOUTH;
            case "E": return Direction.EAST;
            case "W": return Direction.WEST;
            default: throw new IllegalArgumentException("Ungültige Richtung");
        }
    }
}

// Repository Implementations (falls Persistierung gewünscht)
public class InMemoryPlayerRepository implements PlayerRepository {
    private Player player;

    @Override
    public void save(Player player) {
        this.player = player;
    }

    @Override
    public Player load() {
        return player;
    }
}

public class InMemoryAnimalRepository implements AnimalRepository {
    private Map<Integer, List<AnimalSpecies>> animalsByLevel;

    @Override
    public List<AnimalSpecies> getAnimalsByLevel(int level) {
        return animalsByLevel.get(level);
    }

    @Override
    public Animal createAnimal(AnimalSpecies species) {
        // Tier mit zufälligen Stats erstellen
        return new Animal(species, generateRandomStats());
    }
}
```

---

### 🎯 0-CatchThatCow-plugin (Frameworks & Drivers Layer)

**Zweck:** Externe Frameworks und konkrete Implementierungen

**Aktueller Code:**
- ✅ `SystemOutPrintlnPresenter` - Console-Ausgabe

**Benötigter Code für das vollständige Spiel:**

```java
// Console Output Implementation
public class SystemOutPrintlnPresenter implements TextPresenter {
    @Override
    public void print(String text) {
        System.out.println(text);
    }
}

// Eventuell: Alternative Implementierungen
public class ColoredConsolePresenter implements TextPresenter {
    @Override
    public void print(String text) {
        // Mit ANSI-Colors für bessere Darstellung
        System.out.println(text);
    }
}

// File-basierte Persistierung (optional)
public class FilePlayerRepository implements PlayerRepository {
    private String filepath;

    @Override
    public void save(Player player) {
        // JSON oder Serialisierung
    }

    @Override
    public Player load() {
        // Von Datei lesen
    }
}

// Random Number Generator (für Tests mockbar)
public class RandomNumberGenerator {
    private Random random = new Random();

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
```

---

### 🎯 0-CatchThatCow-main (Composition Root)

**Zweck:** Dependency Injection und Anwendungsstart

**Aktueller Code:**
- ✅ `Main` - Einstiegspunkt mit DI

**Benötigter Code für das vollständige Spiel:**

```java
public class Main {
    public static void main(String[] args) {
        // === PLUGINS (äußerste Schicht) ===
        TextPresenter textPresenter = new SystemOutPrintlnPresenter();

        // === ADAPTERS ===
        UserInputPort userInput = new TerminalUserInput();

        FightPresenter fightPresenter = new TerminalFightPresenter(
            textPresenter,
            userInput
        );

        NavigationPresenter navigationPresenter = new TerminalNavigationPresenter(
            textPresenter,
            userInput
        );

        InventoryPresenter inventoryPresenter = new TerminalInventoryPresenter(
            textPresenter
        );

        GamePresenter gamePresenter = new TerminalGamePresenter(
            textPresenter,
            userInput
        );

        PlayerRepository playerRepository = new InMemoryPlayerRepository();
        AnimalRepository animalRepository = new InMemoryAnimalRepository();

        // === DOMAIN ===
        Player player = new Player(new Position(0, 0));  // Startposition
        WorldMap worldMap = new WorldMap(5);  // (2*5+1)² = 11x11 Biome

        // === USE CASES ===
        EncounterAnimalUseCase encounterUseCase = new EncounterAnimalUseCase(
            fightPresenter,
            animalRepository
        );

        FightUseCase fightUseCase = new FightUseCase(
            fightPresenter
        );

        MovePlayerUseCase moveUseCase = new MovePlayerUseCase(
            worldMap,
            navigationPresenter
        );

        ShowInventoryUseCase inventoryUseCase = new ShowInventoryUseCase(
            inventoryPresenter
        );

        PlayGameUseCase playGameUseCase = new PlayGameUseCase(
            player,
            worldMap,
            gamePresenter,
            encounterUseCase,
            fightUseCase,
            moveUseCase,
            inventoryUseCase
        );

        // === START ===
        playGameUseCase.start();
    }
}
```

---

### 🎯 4-CatchThatCow-abstraction (Optional)

**Zweck:** Gemeinsame Abstraktionen über alle Schichten

**Aktuell:** Leer

**Möglicher Code:**
```java
// Gemeinsame Exceptions
public class GameException extends RuntimeException {
    public GameException(String message) {
        super(message);
    }
}

public class InvalidMoveException extends GameException {
    public InvalidMoveException(String message) {
        super(message);
    }
}

// DTOs für Datenübertragung (falls benötigt)
public class AnimalDTO {
    public String species;
    public int level;
    public Map<String, Integer> stats;
}

// Shared Constants
public class GameConstants {
    public static final int BATTLE_INVENTORY_SIZE = 3;
    public static final int POINTS_TO_WIN = 3;
    public static final int WORLD_SIZE = 5;  // (2n+1)
}
```

---

## Zusammenfassung: Dependency Flow

```
Plugin (System.out)
   ↓ implements
Adapter (TerminalPresenter implements UiAnzeiger)
   ↓ calls
Application (MeetAnimalUseCase uses UiAnzeiger interface)
   ↓ uses
Domain (Animal)
```

**Dependency Rule eingehalten:** ✅
- Plugin kennt Adapter-Interfaces
- Adapter kennt Application-Interfaces
- Application kennt Domain
- Domain kennt niemanden
- Interfaces definiert in inneren Schichten (Dependency Inversion!)

---

## Nächste Schritte für vollständige Implementierung

### Phase 1: Domain Layer erweitern
1. ✅ `Animal` verbessern (Stats, Level, Type)
2. ✅ `Player` implementieren
3. ✅ `Biome` und `Position` implementieren
4. ✅ `Fight` und Kampflogik
5. ✅ `WorldMap` implementieren

### Phase 2: Application Layer
1. ✅ Use Cases implementieren (Fight, Move, Inventory)
2. ✅ Presenter-Interfaces definieren
3. ✅ Repository-Interfaces definieren

### Phase 3: Adapter Layer
1. ✅ Terminal-Presenter für alle Use Cases
2. ✅ Input-Adapter
3. ✅ Repository-Implementierungen

### Phase 4: Plugin Layer
1. ✅ Console-Output optimieren
2. ✅ Optional: File-Persistierung

### Phase 5: Integration
1. ✅ Dependency Injection in Main
2. ✅ Spielloop implementieren
3. ✅ Testing

---

*Diese Übersicht zeigt die vollständige Architektur des Projekts nach Clean Architecture Prinzipien.*
