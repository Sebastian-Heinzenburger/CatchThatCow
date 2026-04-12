package de.heinzenburger;

public class Map {
    Biom[][] bioms;

    public Map(int size, Random random) {
        bioms = new Biom[size][size];
        initializeBioms(size, random);
    }

    private void initializeBioms(int size, Random random) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                BiomType biomType = random.choose(BiomType.class);
                int biomLevel = Math.max(x, y);
                bioms[x][y] = new Biom(biomType, biomLevel);
            }
        }
    }

    public Biom getBiomAt(Position position) {
        return bioms[position.getX()][position.getY()];
    }

    public MovementOptions getMovementOptions(Player player) {

    }
}
