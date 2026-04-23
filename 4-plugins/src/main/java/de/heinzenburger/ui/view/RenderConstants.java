package de.heinzenburger.ui.view;

/**
 * Constants for rendering UI elements.
 * Centralizes box drawing characters and formatting values.
 */
public final class RenderConstants {

    private RenderConstants() {
    }

    // Box dimensions
    public static final int BOX_WIDTH = 62;

    // Box drawing characters
    public static final String HORIZONTAL = "═";
    public static final String VERTICAL = "║";
    public static final String TOP_LEFT = "╔";
    public static final String TOP_RIGHT = "╗";
    public static final String BOTTOM_LEFT = "╚";
    public static final String BOTTOM_RIGHT = "╝";
    public static final String CROSS_LEFT = "╠";
    public static final String CROSS_RIGHT = "╣";

    // Table characters
    public static final String TABLE_CROSS = "╪";
    public static final String TABLE_VERTICAL = "│";
    public static final String TABLE_TOP_CROSS = "╤";
    public static final String TABLE_BOTTOM_CROSS = "╧";

    // Map border characters
    public static final String MAP_HORIZONTAL = "──";
    public static final String MAP_TOP_LEFT = "┌";
    public static final String MAP_TOP_RIGHT = "┐";
    public static final String MAP_BOTTOM_LEFT = "└";
    public static final String MAP_BOTTOM_RIGHT = "┘";
    public static final String MAP_VERTICAL = "│";

    // Pre-built borders
    public static final String HORIZONTAL_BORDER = HORIZONTAL.repeat(BOX_WIDTH);
    public static final String TOP_BORDER = TOP_LEFT + HORIZONTAL_BORDER + TOP_RIGHT + "\n";
    public static final String BOTTOM_BORDER = BOTTOM_LEFT + HORIZONTAL_BORDER + BOTTOM_RIGHT + "\n";
    public static final String SEPARATOR = CROSS_LEFT + HORIZONTAL_BORDER + CROSS_RIGHT + "\n";

    // Player marker
    public static final char PLAYER_MARKER = '@';
}
