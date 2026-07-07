package com.v1ctim.mod.registry;

import com.v1ctim.mod.V1ctimMod;
import com.v1ctim.mod.entity.StalkerEntity;
import com.v1ctim.mod.entity.TorturedEntity;
import com.v1ctim.mod.entity.Victim2Entity;
import com.v1ctim.mod.entity.VictimEntity;
import com.v1ctim.mod.entity.WalkerEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, V1ctimMod.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<StalkerEntity>> STALKER =
            ENTITY_TYPES.register("stalker",
                    () -> EntityType.Builder.of(StalkerEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(10)
                            .build("stalker"));

    public static final DeferredHolder<EntityType<?>, EntityType<WalkerEntity>> WALKER =
            ENTITY_TYPES.register("walker",
                    () -> EntityType.Builder.of(WalkerEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(10)
                            .build("walker"));

    public static final DeferredHolder<EntityType<?>, EntityType<VictimEntity>> VICTIM =
            ENTITY_TYPES.register("victim",
                    () -> EntityType.Builder.of(VictimEntity::new, MobCategory.MISC)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(16)
                            .fireImmune()
                            .build("victim"));

    public static final DeferredHolder<EntityType<?>, EntityType<Victim2Entity>> VICTIM2 =
            ENTITY_TYPES.register("victim2",
                    () -> EntityType.Builder.of(Victim2Entity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(12)
                            .build("victim2"));

    public static final DeferredHolder<EntityType<?>, EntityType<TorturedEntity>> TORTURED =
            ENTITY_TYPES.register("tortured",
                    () -> EntityType.Builder.of(TorturedEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(10)
                            .build("tortured"));

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(STALKER.get(), StalkerEntity.createAttributes().build());
        event.put(WALKER.get(), WalkerEntity.createAttributes().build());
        event.put(VICTIM.get(), VictimEntity.createAttributes().build());
        event.put(VICTIM2.get(), Victim2Entity.createAttributes().build());
        event.put(TORTURED.get(), TorturedEntity.createAttributes().build());
    }
}
