package com.v1ctim.mod.registry;

import com.v1ctim.mod.V1ctimMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, V1ctimMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GLITCH_STUTTER       = registerSound("glitch_stutter");
    public static final DeferredHolder<SoundEvent, SoundEvent> STATIC_BURST          = registerSound("static_burst");
    public static final DeferredHolder<SoundEvent, SoundEvent> CORRUPTION_EXPAND     = registerSound("corruption_expand");
    public static final DeferredHolder<SoundEvent, SoundEvent> ARTIFACT_STATIC_VOICE = registerSound("artifact_static_voice");
    public static final DeferredHolder<SoundEvent, SoundEvent> WORLD_HUM_WRONG       = registerSound("world_hum_wrong");
    public static final DeferredHolder<SoundEvent, SoundEvent> SATURATED_HIT         = registerSound("saturated_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> VICTIM_APPEAR_GLITCH  = registerSound("victim_appear_glitch");
    public static final DeferredHolder<SoundEvent, SoundEvent> VICTIM2_SCREAM        = registerSound("victim2_scream");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(V1ctimMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
}
