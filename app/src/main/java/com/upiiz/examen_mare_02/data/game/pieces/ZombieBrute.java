package com.upiiz.examen_mare_02.data.game.pieces;

import com.upiiz.examen_mare_02.data.game.*;

public class ZombieBrute extends Piece {
    public ZombieBrute(Side side, int row, int col) { super(side, row, col, 22); }

    @Override
    public boolean canMove(int fr, int fc, int tr, int tc, Board board) {
        // Hasta 2 celdas ortogonales, sin saltar
        if (fr != tr && fc != tc) return false;
        int dist = Math.abs(tr - fr) + Math.abs(tc - fc);
        if (dist == 0 || dist > 2) return false;
        return board.isPathClear(fr, fc, tr, tc);
    }
    @Override public int getAttackRange() { return 1; }
    @Override public int getBaseDamagePerSecond() { return 2; }
    @Override public String getTypeId() { return "ZOMBIE_BRUTE"; }
}
