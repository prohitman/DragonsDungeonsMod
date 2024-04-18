package com.prohitman.dragonsdungeons.common.entities.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum WargVariant {
    PALE(0),
    SNOWY(1),
    BLACK(2),
    CHESTNUT(3),
    STRIPED(4),
    ASHEN(5),
    RUSTY(6),
    WOOD(7),
    SPOTTED(8),
    ZARG(9);

    private static final WargVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(WargVariant::getId)).toArray(WargVariant[]::new);
    private final int id;

    WargVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static WargVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
