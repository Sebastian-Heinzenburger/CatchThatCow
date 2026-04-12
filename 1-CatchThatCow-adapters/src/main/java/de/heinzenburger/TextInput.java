package de.heinzenburger;

public interface TextInput {
    char readChar(Character... allowedChars);

    int readInt(int min, int max);
}
