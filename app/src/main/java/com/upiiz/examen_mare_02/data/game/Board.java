package com.upiiz.examen_mare_02.data.game;

import com.upiiz.examen_mare_02.data.game.pieces.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int SIZE = 8;
    private final Piece[][] grid = new Piece[SIZE][SIZE];

    public Piece get(int r, int c) { return in(r,c) ? grid[r][c] : null; }
    public void set(Piece p) { grid[p.getRow()][p.getCol()] = p; }
    public void clear(int r, int c) { if (in(r,c)) grid[r][c] = null; }
    public static boolean in(int r, int c) { return r>=0 && r<SIZE && c>=0 && c<SIZE; }
    public boolean isEmpty(int r, int c) { return in(r,c) && grid[r][c] == null; }

    public boolean tryMove(int fr, int fc, int tr, int tc) {
        if (!in(fr,fc) || !in(tr,tc)) return false;
        Piece p = grid[fr][fc];
        if (p == null || grid[tr][tc] != null) return false;
        if (!p.canMove(fr, fc, tr, tc, this)) return false;
        grid[fr][fc] = null;
        p.setPosition(tr, tc);
        grid[tr][tc] = p;
        return true;
    }

    public boolean isPathClear(int fr, int fc, int tr, int tc) {
        int dr = Integer.compare(tr, fr);
        int dc = Integer.compare(tc, fc);
        if (dr != 0 && dc != 0) return false;
        int r = fr + dr, c = fc + dc;
        while (r != tr || c != tc) {
            if (grid[r][c] != null) return false;
            r += dr; c += dc;
        }
        return true;
    }

    /** Lista de destinos válidos (fila,col) para la pieza en (fr,fc). */
    public List<int[]> validMoves(int fr, int fc) {
        List<int[]> list = new ArrayList<>();
        if (!in(fr,fc)) return list;
        Piece p = grid[fr][fc];
        if (p == null) return list;
        for (int r=0; r<SIZE; r++) for (int c=0; c<SIZE; c++) {
            if (grid[r][c] != null) continue;           // destino debe estar vacío
            if (p.canMove(fr, fc, r, c, this)) list.add(new int[]{r,c});
        }
        return list;
    }

    /** Daño por tiempo a quien se tardó (tardySide). */
    public void applyTimeDamage(long elapsedSeconds, Side tardySide) {
        if (elapsedSeconds <= 0) return;
        for (int r=0; r<SIZE; r++) for (int c=0; c<SIZE; c++) {
            Piece victim = grid[r][c];
            if (victim == null || victim.getSide() != tardySide) continue;

            int total = 0;
            for (int rr=0; rr<SIZE; rr++) for (int cc=0; cc<SIZE; cc++) {
                Piece enemy = grid[rr][cc];
                if (enemy == null || enemy.getSide() == tardySide) continue;
                int dist = Math.max(Math.abs(rr - r), Math.abs(cc - c)); // Chebyshev
                if (dist <= enemy.getAttackRange()) total += enemy.getBaseDamagePerSecond() * (int)elapsedSeconds;
            }
            if (total > 0) victim.receiveDamage(total);
        }
        // limpiar muertas
        for (int r=0; r<SIZE; r++) for (int c=0; c<SIZE; c++) {
            Piece p = grid[r][c];
            if (p != null && !p.isAlive()) grid[r][c] = null;
        }
    }

    public int countPieces(Side side) {
        int n = 0;
        for (int r=0;r<SIZE;r++) for (int c=0;c<SIZE;c++) {
            Piece p = grid[r][c];
            if (p != null && p.getSide()==side && p.isAlive()) n++;
        }
        return n;
    }
    public boolean hasPieces(Side side) { return countPieces(side) > 0; }

    public Side getWinnerOrNull() {
        boolean plants = hasPieces(Side.PLANT);
        boolean zombies = hasPieces(Side.ZOMBIE);
        if (plants && zombies) return null;
        if (plants && !zombies) return Side.PLANT;
        if (!plants && zombies) return Side.ZOMBIE;
        return null;
    }

    public static Board initial() {
        Board b = new Board();

        // FILA ZOMBIES (arriba, r=0): 8 tipos
        b.set(new ZombieBrute (Side.ZOMBIE, 0, 0));
        b.set(new ZombieWalker(Side.ZOMBIE, 0, 1));
        b.set(new ZombieMage  (Side.ZOMBIE, 0, 2));
        b.set(new ZombieTank  (Side.ZOMBIE, 0, 3));
        b.set(new ZombieTank  (Side.ZOMBIE, 0, 4));
        b.set(new ZombieMage  (Side.ZOMBIE, 0, 5));
        b.set(new ZombieWalker(Side.ZOMBIE, 0, 6));
        b.set(new ZombieBrute (Side.ZOMBIE, 0, 7));

        // PEONES ZOMBIES (r=1): Walkers
        for (int c=0; c<8; c++) b.set(new ZombieWalker(Side.ZOMBIE, 1, c));

        // PEONES PLANTAS (r=6): Runners
        for (int c=0; c<8; c++) b.set(new PlantRunner(Side.PLANT, 6, c));

        // FILA PLANTAS (abajo, r=7): 8 tipos
        b.set(new PlantGuardian(Side.PLANT, 7, 0));
        b.set(new PlantArcher  (Side.PLANT, 7, 1));
        b.set(new PlantSniper  (Side.PLANT, 7, 2));
        b.set(new PlantRunner  (Side.PLANT, 7, 3));
        b.set(new PlantRunner  (Side.PLANT, 7, 4));
        b.set(new PlantSniper  (Side.PLANT, 7, 5));
        b.set(new PlantArcher  (Side.PLANT, 7, 6));
        b.set(new PlantGuardian(Side.PLANT, 7, 7));

        return b;
    }

}
