package com.upiiz.examen_mare_02.data.game.pieces;

import com.upiiz.examen_mare_02.data.game.*;

public class PlantGuardian extends Piece {
    public PlantGuardian(Side side, int row, int col) { super(side, row, col, 12); }

    @Override
    public boolean canMove(int fr, int fc, int tr, int tc, Board board) {
        // Movimiento en "L" (caballo) y puede saltar
        int dr = Math.abs(tr - fr), dc = Math.abs(tc - fc);
        return (dr==2 && dc==1) || (dr==1 && dc==2);
    }
    @Override public int getAttackRange() { return 2; }
    @Override public int getBaseDamagePerSecond() { return 1; }
    @Override public String getTypeId() { return "PLANT_GUARDIAN"; }
}
