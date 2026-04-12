package de.heinzenburger;

public interface Random {
    <T> T choose(T[] list);

    <T extends Enum<T>> T choose(Class<T> enumClass);
}

class JavaRandom implements Random {

    @Override
    public <T> T choose(T[] list) {
        int randomIndex = (int) (Math.random() * list.length);
        return list[randomIndex];
    }

    @Override
    public <T extends Enum<T>> T choose(Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();
        return choose(enumConstants);
    }
}