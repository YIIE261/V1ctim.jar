package com.v1ctim.mod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Primera pantalla de aviso (splash) del mod, pensada para mostrarse antes
 * de que el jugador entre a un mundo (ver client/ClientSetupEvents.java
 * para cuando exactamente se abre).
 *
 * Flujo pedido por el usuario:
 *  1. "Este mod no es un virus o malware, esta hecho con fines de
 *     entretenimiento" + botones Aceptar / Rechazar.
 *  2. Si Aceptar -> segunda pantalla de confirmacion: "Al aceptar estaras
 *     consciente de que nada de esto puede danar tu dispositivo y que no
 *     eres sensible a lo siguiente" + Confirmar / Rechazar.
 *  3. Si confirma en la segunda pantalla -> el juego sigue normalmente
 *     (se cierra la pantalla y aparece el menu principal / mundo).
 *  4. Si rechaza en cualquiera de las dos -> se cierra el juego por
 *     completo (Minecraft.getInstance().stop()), sacando al jugador.
 */
public class DisclaimerScreen extends Screen {

    private final boolean secondStep;

    public DisclaimerScreen() {
        this(false);
    }

    public DisclaimerScreen(boolean secondStep) {
        super(Component.literal("V1ctim - Aviso"));
        this.secondStep = secondStep;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int buttonY = this.height / 2 + 20;

        String acceptLabel = secondStep ? "Confirmar" : "Aceptar";

        this.addRenderableWidget(Button.builder(
                Component.literal(acceptLabel),
                button -> onAccept()
        ).bounds(centerX - 105, buttonY, 100, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Rechazar"),
                button -> onReject()
        ).bounds(centerX + 5, buttonY, 100, 20).build());
    }

    private void onAccept() {
        if (!secondStep) {
            // Pasa a la segunda pantalla de confirmacion.
            Minecraft.getInstance().setScreen(new DisclaimerScreen(true));
        } else {
            // Confirmado: cierra la pantalla y deja seguir al juego con
            // normalidad (vuelve al menu principal detras de esta pantalla).
            Minecraft.getInstance().setScreen(null);
        }
    }

    private void onReject() {
        // Rechazar en cualquiera de las dos pantallas cierra el juego.
        Minecraft.getInstance().stop();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        String message = secondStep
                ? "Al aceptar estaras consciente de que nada de esto puede"
                        + " danar tu dispositivo y que no eres sensible a lo siguiente."
                : "Este mod no es un virus o malware, esta hecho con fines"
                        + " de entretenimiento.";

        int centerX = this.width / 2;
        int textY = this.height / 2 - 30;

        for (String line : wrapText(message, 50)) {
            graphics.drawCenteredString(this.font, line, centerX, textY, 0xFFFFFF);
            textY += 12;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        // No se puede cerrar con ESC: el jugador debe elegir un boton.
        return false;
    }

    /** Envuelve un texto largo en varias lineas de hasta maxChars caracteres, cortando por palabra. */
    private static java.util.List<String> wrapText(String text, int maxChars) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (String word : text.split(" ")) {
            if (current.length() + word.length() + 1 > maxChars) {
                lines.add(current.toString());
                current = new StringBuilder();
            }
            if (current.length() > 0) current.append(" ");
            current.append(word);
        }
        if (current.length() > 0) lines.add(current.toString());

        return lines;
    }
}
