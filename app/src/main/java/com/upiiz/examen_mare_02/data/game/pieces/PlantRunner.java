package com.upiiz.examen_mare_02.data.game.pieces;

import com.upiiz.examen_mare_02.data.game.*;

public class PlantRunner extends Piece {
    public PlantRunner(Side side, int row, int col) { super(side, row, col, 8); }

    @Override
    public boolean canMove(int fr, int fc, int tr, int tc, Board board) {
        // Diagonales 1 casilla (como "rey" diagonal)
        return Math.abs(tr - fr) == 1 && Math.abs(tc - fc) == 1;
    }

    @Override public int getAttackRange() { return 2; }
    @Override public int getBaseDamagePerSecond() { return 1; }
    @Override public String getTypeId() { return "PLANT_RUNNER"; }
}
