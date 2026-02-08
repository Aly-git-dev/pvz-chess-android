package com.upiiz.examen_mare_02.data.game;

public interface Attackable {
    int getAttackRange();
    int getBaseDamagePerSecond();
    void receiveDamage(int dmg);
    boolean isAlive();
    int getHp();
    int getMaxHp();
}
