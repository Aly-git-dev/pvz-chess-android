package com.upiiz.examen_mare_02.ui.skins;

import androidx.annotation.DrawableRes;

import com.upiiz.examen_mare_02.R;
import com.upiiz.examen_mare_02.data.game.Piece;

import java.util.HashMap;
import java.util.Map;

/** Mapea getTypeId() de cada pieza a su drawable. */
public final class PieceSkins {

    private static final Map<String, Integer> MAP = new HashMap<>();
    static {
        // PLANTS
        MAP.put("PLANT_SNIPER",   R.drawable.plant_sniper);
        MAP.put("PLANT_RUNNER",   R.drawable.plant_runner);
        MAP.put("PLANT_ARCHER",   R.drawable.plant_archer);
        MAP.put("PLANT_GUARDIAN", R.drawable.plant_guardian);

        // ZOMBIES
        MAP.put("ZOMBIE_TANK",    R.drawable.zombie_tank);
        MAP.put("ZOMBIE_WALKER",  R.drawable.zombie_walker);
        MAP.put("ZOMBIE_BRUTE",   R.drawable.zombie_brute);
        MAP.put("ZOMBIE_MAGE",    R.drawable.zombie_mage);
    }

    @DrawableRes
    public static int drawableFor(Piece p) {
        Integer res = MAP.get(p.getTypeId());
        return res != null ? res : android.R.color.transparent;
    }

    private PieceSkins() {}
}
