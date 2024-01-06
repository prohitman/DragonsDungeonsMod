package com.prohitman.dragonsdungeons.common.blocks;

import com.prohitman.dragonsdungeons.common.blocks.enums.ConnectedState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ModBlockStateProperties {
    public static final EnumProperty<ConnectedState> CONNECTED_STATE = EnumProperty.create("connected_state", ConnectedState.class);
}
