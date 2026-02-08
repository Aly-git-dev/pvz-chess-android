package com.upiiz.examen_mare_02.data.game;

public abstract class Piece implements Movable, Attackable {
    protected final Side side;
    protected int row, col;
    protected final int maxHp;
    protected int hp;

    public Piece(Side side, int row, int col, int maxHp) {
        this.side = side;
        this.row = row;
        this.col = col;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public Side getSide() { return side; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public void setPosition(int r, int c) { this.row = r; this.col = c; }

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }

    @Override public void receiveDamage(int dmg) { hp = Math.max(0, hp - Math.max(0, dmg)); }
    @Override public boolean isAlive() { return hp > 0; }

    /** Identificador para serializar/deserializar (ej. "PLANT_SNIPER") */
    public abstract String getTypeId();
}
