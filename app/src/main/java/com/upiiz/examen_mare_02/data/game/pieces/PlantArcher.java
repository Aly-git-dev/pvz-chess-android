package com.upiiz.examen_mare_02.data.game.pieces;

import com.upiiz.examen_mare_02.data.game.*;

public class PlantArcher extends Piece {
    public PlantArcher(Side side, int row, int col) { super(side, row, col, 9); }

    @Override
    public boolean canMove(int fr, int fc, int tr, int tc, Board board) {
        // Diagonales cual "alfil", sin saltar
        if (Math.abs(tr - fr) != Math.abs(tc - fc)) return false;
        int dr = Integer.compare(tr, fr), dc = Integer.compare(tc, fc);
        int r = fr + dr, c = fc + dc;
        while (r != tr || c != tc) {
            if (board.get(r,c) != null) return false;
            r += dr; c += dc;
        }
        return true;
    }
    @Override public int getAttackRange() { return 3; }
    @Override public int getBaseDamagePerSecond() { return 1; }
    @Override public String getTypeId() { return "PLANT_ARCHER"; }
}
