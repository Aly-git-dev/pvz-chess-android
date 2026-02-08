package com.upiiz.examen_mare_02.data.game.pieces;

import com.upiiz.examen_mare_02.data.game.*;

public class ZombieTank extends Piece {
    public ZombieTank(Side side, int row, int col) { super(side, row, col, 20); } // mucha vida

    @Override
    public boolean canMove(int fr, int fc, int tr, int tc, Board board) {
        // 1 casilla ortogonal
        return Math.abs(tr - fr) + Math.abs(tc - fc) == 1;
    }

    @Override public int getAttackRange() { return 1; }     // corto
    @Override public int getBaseDamagePerSecond() { return 2; }
    @Override public String getTypeId() { return "ZOMBIE_TANK"; }
}
