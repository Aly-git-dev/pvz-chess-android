package com.upiiz.examen_mare_02.data.game;

public interface Movable {
    boolean canMove(int fromRow, int fromCol, int toRow, int toCol, Board board);
}
