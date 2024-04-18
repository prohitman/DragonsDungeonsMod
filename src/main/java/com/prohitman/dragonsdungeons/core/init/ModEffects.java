package com.prohitman.dragonsdungeons.core.init;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.effects.IntoxicatedEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, DragonsDungeons.MODID);

    public static final RegistryObject<MobEffect> INTOXICATEDEFFECT = MOB_EFFECTS.register("intoxicated",
            () -> new IntoxicatedEffect(MobEffectCategory.HARMFUL, 0x499f41).addAttributeModifier(Attributes.MOVEMENT_SPEED,
                    "7107DE5E-7C38-4030-940E-514C1F160890", -0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));

}
