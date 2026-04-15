package de.heinzenburger.shared;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void shouldCreatePosition() {
        Position position = new Position(3, 5);
        assertEquals(3, position.getX());
        assertEquals(5, position.getY());
    }

    @Test
    void shouldCalculateDistance() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(3, 4);
        assertEquals(7, p1.distanceFrom(p2));
    }

    @Test
    void shouldReturnAdjacentPositions() {
        Position center = new Position(5, 5);
        List<Position> adjacent = center.adjacentPositions();

        assertEquals(4, adjacent.size());
        assertTrue(adjacent.contains(new Position(5, 4))); // North
        assertTrue(adjacent.contains(new Position(6, 5))); // East
        assertTrue(adjacent.contains(new Position(5, 6))); // South
        assertTrue(adjacent.contains(new Position(4, 5))); // West
    }

    @Test
    void shouldBeEqualWhenCoordinatesMatch() {
        Position p1 = new Position(3, 5);
        Position p2 = new Position(3, 5);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenCoordinatesDiffer() {
        Position p1 = new Position(3, 5);
        Position p2 = new Position(3, 6);
        assertNotEquals(p1, p2);
    }
}
