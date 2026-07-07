package com.v1ctim.mod.registry;

import com.v1ctim.mod.V1ctimMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, V1ctimMod.MOD_ID);

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, V1ctimMod.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> BLOOD_WATER_TYPE =
            FLUID_TYPES.register("blood_water",
                    () -> new FluidType(FluidType.Properties.create()
                            .descriptionId("fluid.v1ctim.blood_water")
                            .canConvertToSource(false)
                            .canSwim(true)
                            .canDrown(true)
                            .pathType(PathType.WATER)
                            .adjacentPathType(PathType.WATER_BORDER)
                            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                            .density(1200)
                            .viscosity(1200)
                            .lightLevel(0)
                            .motionScale(0.007)));

    public static final DeferredHolder<Fluid, BaseFlowingFluid> BLOOD_WATER_SOURCE =
            FLUIDS.register("blood_water",
                    () -> new BaseFlowingFluid.Source(fluidProperties()));

    public static final DeferredHolder<Fluid, BaseFlowingFluid> BLOOD_WATER_FLOWING =
            FLUIDS.register("blood_water_flowing",
                    () -> new BaseFlowingFluid.Flowing(fluidProperties()));

    private static BaseFlowingFluid.Properties fluidProperties() {
        return new BaseFlowingFluid.Properties(BLOOD_WATER_TYPE, BLOOD_WATER_SOURCE, BLOOD_WATER_FLOWING)
                .slopeFindDistance(4)
                .levelDecreasePerBlock(1)
                .block(() -> ModBlocks.BLOOD_WATER_BLOCK.get())
                .bucket(() -> ModItems.BLOOD_WATER_BUCKET.get());
    }
}
