package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * A player that chooses randomly from the currently available cells. 
 */
public class CpuPlayer extends Player {
    private static final AtomicInteger CPU_COUNT = new AtomicInteger();
    private final Random random;

    /**
     * Creates a CPU player with a randomly seeded move generator.
     *
     * @param marker this player's marker
     * @throws IllegalArgumentException if {@code marker} is not {@code X} or {@code O}
     */

    /**
     * Creates a CPU player using the specified marker
     * @param marker this player's marker
     */
    public CpuPlayer(char marker) {
        this(marker, new Random());
    }

    /**
     * Creates a CPU player using the supplied random-number generator.
     *
     * @param marker this player's marker
     * @param random generator used to select a move
     * @throws NullPointerException if {@code random} is null
     * @throws IllegalArgumentException if {@code marker} is not {@code X} or {@code O}
     */     
    public CpuPlayer(char marker, Random random) {
        super(marker);
        this.random = Objects.requireNonNull(random, "random");
        setName("CPU #" + CPU_COUNT.incrementAndGet());
    }
    

    /** Places this player's marker in a randomly selected empty cell. */
    @Override
    public void makeMove(Board board) {
        List<int[]> availableMoves = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (board.isValid(row, column)) {
                    availableMoves.add(new int[]{row, column});
                }
            }
        }
        if (availableMoves.isEmpty()) {
            throw new IllegalStateException("The CPU cannot move because the board is full");
        }

        int[] move = availableMoves.get(random.nextInt(availableMoves.size()));
        board.place(move[0], move[1], marker);
        System.out.printf("CPU (%c) chose row %d, column %d.%n", marker, move[0] + 1, move[1] + 1);
    }
}