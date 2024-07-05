package com.prohitman.dragonsdungeons.common.entities.variant;

import com.prohitman.dragonsdungeons.client.entities.renderers.WargEyeColor;

import java.util.Arrays;
import java.util.Comparator;

public enum WargVariant {
    PALE(0, WargEyeColor.YELLOW),
    SNOWY(1, WargEyeColor.BLUE),
    BLACK(2, WargEyeColor.YELLOW),
    CHESTNUT(3, WargEyeColor.YELLOW),
    STRIPED(4, WargEyeColor.YELLOW),
    ASHEN(5, WargEyeColor.BLUE),
    RUSTY(6, WargEyeColor.YELLOW),
    WOOD(7, WargEyeColor.YELLOW),
    SPOTTED(8, WargEyeColor.YELLOW);

    private static final WargVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(WargVariant::getId)).toArray(WargVariant[]::new);
    private final int id;
    private final WargEyeColor eyeColor;

    WargVariant(int id, WargEyeColor eyeColor) {
        this.id = id;
        this.eyeColor = eyeColor;
    }

    public int getId() {
        return this.id;
    }
    public WargEyeColor getEyeColor() {
        return this.eyeColor;
    }

    public static WargVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
