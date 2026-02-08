package com.upiiz.examen_mare_02.data.game;

import com.upiiz.examen_mare_02.data.game.pieces.*;

public class BoardSerializer {

    // Formato: type,side,row,col,hp|maxHp;
    // Ej: PLANT_SNIPER,P,7,2,10|10;

    public static String serialize(Board board) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Piece p = board.get(r, c);
                if (p == null) continue;
                sb.append(p.getTypeId()).append(",")
                        .append(p.getSide() == Side.PLANT ? "P" : "Z").append(",")
                        .append(p.getRow()).append(",")
                        .append(p.getCol()).append(",")
                        .append(p.getHp()).append("|").append(p.getMaxHp())
                        .append(";");
            }
        }
        return sb.toString();
    }

    public static Board deserialize(String state) {
        // compatibilidad con formato viejito de 8 líneas (P/Z/.)
        if (state != null && state.contains("\n")) return fromLegacyGrid(state);

        Board b = new Board();
        if (state == null || state.isEmpty()) return b;

        String[] tokens = state.split(";");
        for (String t : tokens) {
            if (t.isEmpty()) continue;
            String[] a = t.split(",");
            if (a.length < 5) continue;

            String type = a[0];
            Side side = "P".equals(a[1]) ? Side.PLANT : Side.ZOMBIE;
            int r = Integer.parseInt(a[2]);
            int c = Integer.parseInt(a[3]);

            String[] hpParts = a[4].split("\\|");
            int hp  = Integer.parseInt(hpParts[0]);
            int max = hpParts.length > 1 ? Integer.parseInt(hpParts[1]) : hp;

            Piece p;
            switch (type) {
                case "PLANT_SNIPER":
                    p = new PlantSniper(side, r, c);
                    break;
                case "PLANT_RUNNER":
                    p = new PlantRunner(side, r, c);
                    break;
                case "PLANT_ARCHER":
                    p = new PlantArcher(side, r, c);
                    break;
                case "PLANT_GUARDIAN":
                    p = new PlantGuardian(side, r, c);
                    break;
                case "ZOMBIE_TANK":
                    p = new ZombieTank(side, r, c);
                    break;
                case "ZOMBIE_WALKER":
                    p = new ZombieWalker(side, r, c);
                    break;
                case "ZOMBIE_BRUTE":
                    p = new ZombieBrute(side, r, c);
                    break;
                case "ZOMBIE_MAGE":
                    p = new ZombieMage(side, r, c);
                    break;
                default:
                    p = null;
                    break;
            }

            if (p != null) {
                // ajustar vida (por defecto los constructores ponen hp=max)
                int diff = p.getHp() - hp;
                if (diff > 0) {
                    p.receiveDamage(diff);
                }
                // si hp que viene es > max, lo dejamos en max
                b.set(p);
            }
        }
        return b;
    }

    /** Carga desde el formato viejo 8x8 de caracteres */
    private static Board fromLegacyGrid(String grid) {
        Board b = new Board();
        String[] rows = grid.split("\n");
        for (int r = 0; r < Math.min(rows.length, Board.SIZE); r++) {
            String row = rows[r];
            for (int c = 0; c < Math.min(row.length(), Board.SIZE); c++) {
                char ch = row.charAt(c);
                switch (ch) {
                    case 'Z':
                        b.set(new ZombieTank(Side.ZOMBIE, r, c));
                        break;
                    case 'P':
                        b.set(new PlantSniper(Side.PLANT, r, c));
                        break;
                    // '.' u otros caracteres = casilla vacía
                    default:
                        break;
                }
            }
        }
        return b;
    }
}
