package com.v1ctim.mod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

/**
 * "T#rtured" (nombre interno de registro: "tortured").
 *
 * Reutiliza por completo el comportamiento del Zombie vanilla: camina
 * hacia el jugador mas cercano y ataca cuerpo a cuerpo hasta matarlo o
 * morir. No tiene ninguna habilidad especial (no rompe puertas mas de lo
 * que un zombie normal ya hace en dificultad dificil, no mina bloques).
 *
 * Lo unico distinto es visual: en el resource pack se le asigna la skin
 * de Steve en vez de la textura de zombie
 * (assets/v1ctim/textures/entity/tortured.png), y su renderer de cliente
 * usa el modelo humanoide estandar en vez del modelo de zombie
 * (para que no tenga los brazos extendidos hacia adelante tipicos del
 * zombie, sino la pose normal de un jugador).
 */
public class TorturedEntity extends Zombie {

    public TorturedEntity(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    // Se reutilizan directamente createAttributes(), registerGoals() y
    // toda la logica de ataque/persecucion de la clase Zombie de Minecraft.
    // No hace falta sobrescribir nada mas para que funcione como se pidio:
    // "un Steve caminando hacia el jugador atacandolo hasta la muerte,
    // como un zombie pero con la skin de Steve".
}
