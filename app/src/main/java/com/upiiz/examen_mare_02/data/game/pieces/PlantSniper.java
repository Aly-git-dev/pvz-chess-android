package com.upiiz.examen_mare_02.data.game.pieces;

import com.upiiz.examen_mare_02.data.game.*;

public class PlantSniper extends Piece {
    public PlantSniper(Side side, int row, int col) { super(side, row, col, 10); } // poca vida

    @Override
    public boolean canMove(int fr, int fc, int tr, int tc, Board board) {
        // Recto como "torre" sin saltar
        if (fr != tr && fc != tc) return false;
        return board.isPathClear(fr, fc, tr, tc);
    }

    @Override public int getAttackRange() { return 3; }     // alto rango
    @Override public int getBaseDamagePerSecond() { return 1; }
    @Override public String getTypeId() { return "PLANT_SNIPER"; }
}
