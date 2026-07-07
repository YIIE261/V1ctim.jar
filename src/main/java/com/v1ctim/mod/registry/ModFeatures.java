package com.v1ctim.mod.registry;

import com.v1ctim.mod.V1ctimMod;
import com.v1ctim.mod.worldgen.MineTunnelFeature;
import com.v1ctim.mod.worldgen.StoneSteveStatueFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, V1ctimMod.MOD_ID);

    public static final DeferredHolder<Feature<?>, MineTunnelFeature> MINE_TUNNEL =
            FEATURES.register("mine_tunnel",
                    () -> new MineTunnelFeature(NoneFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, StoneSteveStatueFeature> STEVE_STATUE =
            FEATURES.register("steve_statue",
                    () -> new StoneSteveStatueFeature(NoneFeatureConfiguration.CODEC));
}
