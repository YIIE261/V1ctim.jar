package com.v1ctim.mod.client;

import net.minecraft.CrashReport;
import net.minecraft.ReportedException;

/**
 * Genera un "crash" del juego cuando Victim2 mata al jugador.
 *
 * IMPORTANTE - esto NO danha el equipo del jugador ni es un virus/malware:
 * simplemente usa el propio sistema de reportes de errores de Minecraft
 * (CrashReport/ReportedException) para forzar que el juego se cierre
 * mostrando la pantalla de crash clasica de Minecraft, con un mensaje
 * tematico. Es el mismo mecanismo que usa el juego cuando ocurre un error
 * real; aca simplemente lo disparamos nosotros a proposito como efecto de
 * terror. El jugador puede volver a abrir Minecraft normalmente despues.
 */
public class FakeCrashHandler {

    public static void triggerCrash() {
        CrashReport crashReport = new CrashReport(
                "V1CTIM te encontro.",
                new RuntimeException("Victim2 te asesino. El juego no puede continuar.")
        );
        crashReport.addCategory("V1ctim");

        // Basta con lanzar el ReportedException: el bucle principal de
        // Minecraft ya lo captura y el mismo llama a Minecraft.crash(report)
        // para mostrar la pantalla de crash antes de cerrar. Llamar a
        // Minecraft.crash() aca ademas de lanzar la excepcion hacia que el
        // crash se procesara/escribiera dos veces.
        throw new ReportedException(crashReport);
    }
}
