package de.heinzenburger;

public interface TextInput {
    char readChar(char... allowedChars);

    int readInt(int min, int max);
}
