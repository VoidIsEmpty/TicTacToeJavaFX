package com.example;

/**
 * 
 * Defines the shared marker and move contract for a Tic-Tac-Toe player.
 */ 
public abstract class Player {
    protected final char marker;
    private String name;

    /**
     * Creates a player with the given marker.
     *
     * @param marker the player's marker, either {@code X} or {@code O}
     * @throws IllegalArgumentException if the marker is not {@code X} or {@code O}
     */
    protected Player(char marker) {
        if (marker != 'X' && marker != 'O') {
            throw new IllegalArgumentException("A player marker must be X or O");
        }
        this.marker = marker;
    }

    /**
     * Returns this player's marker.
     *
     * @return the player's marker
     */
    public char getMarker() {
        return marker;
    }

    /**
     * Returns this player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets this player's name.
     *
     * @param name the player's name
     * @throws IllegalArgumentException if the name is null or blank
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("A player name must not be blank");
        }
        this.name = name;
    }

    /**
     * Makes a move on the supplied board.
     *
     * @param board board on which to make the move
     */
    public abstract void makeMove(Board board);
}