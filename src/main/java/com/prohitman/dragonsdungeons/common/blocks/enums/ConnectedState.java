package com.prohitman.dragonsdungeons.common.blocks.enums;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ConnectedState  implements StringRepresentable {
    NONE,
    BOTH,
    BOTTOM,
    TOP;

    public String toString() {
        return this.getSerializedName();
    }

    public @NotNull String getSerializedName() {
        return this == NONE ? "none" : this == BOTH ? "both" : this == BOTTOM ? "bottom" : "top";
    }
}
