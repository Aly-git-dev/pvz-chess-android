package com.upiiz.examen_mare_02.data.game.pieces;

import com.upiiz.examen_mare_02.data.game.*;

public class ZombieWalker extends Piece {
    public ZombieWalker(Side side, int row, int col) { super(side, row, col, 14); }

    @Override
    public boolean canMove(int fr, int fc, int tr, int tc, Board board) {
        // 1 casilla en cualquier dirección
        return Math.max(Math.abs(tr - fr), Math.abs(tc - fc)) == 1;
    }

    @Override public int getAttackRange() { return 1; }
    @Override public int getBaseDamagePerSecond() { return 1; }
    @Override public String getTypeId() { return "ZOMBIE_WALKER"; }
}
