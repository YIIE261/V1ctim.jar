package com.v1ctim.mod.client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.util.List;
import java.util.Random;

/**
 * Muestra una ventana de error falsa con apariencia nativa del sistema
 * operativo, simulando un error de Windows.
 *
 * IMPORTANTE - esto NO es un virus ni hace nada real al equipo: solo abre
 * un cuadro de dialogo estandar de Java (JOptionPane) configurado para
 * usar el "look and feel" nativo del sistema operativo del jugador. En
 * Windows, esto hace que la ventana se vea identica a un error real de
 * Windows (mismos botones, icono y estilo), pero es puramente visual: se
 * cierra con el boton "Aceptar/OK" como cualquier ventana normal y no
 * ejecuta ningun comando ni modifica nada del sistema.
 *
 * En Linux o macOS se va a mostrar igual como una ventana de error nativa
 * de ESE sistema operativo (no va a poder imitar el estilo visual de
 * Windows especificamente, ya que no se puede falsificar el look and feel
 * de un sistema operativo distinto al que esta corriendo).
 *
 * Se ejecuta en un hilo separado (no en el hilo de render de Minecraft)
 * para no congelar el juego mientras la ventana esta abierta, ya que
 * JOptionPane.showMessageDialog es una llamada bloqueante.
 */
public class FakeSystemErrorHandler {

    private static final Random RANDOM = new Random();

    private static final List<String[]> FAKE_ERRORS = List.of(
            new String[]{"Explorer.exe", "Explorer.exe ha dejado de funcionar.\nWindows esta buscando una solucion al problema."},
            new String[]{"Error del sistema", "Error 0xC000021A: un proceso critico del sistema ha terminado inesperadamente."},
            new String[]{"Windows - Error fatal", "La instruccion en 0x00000000 hizo referencia a memoria en 0x00000000.\nLa memoria no pudo ser \"read\"."},
            new String[]{"Windows", "Un dispositivo conectado a este sistema no esta funcionando correctamente."},
            new String[]{"Advertencia del sistema", "Se ha detectado actividad no autorizada en este equipo."}
    );

    public static void showRandomFakeError() {
        String[] chosen = FAKE_ERRORS.get(RANDOM.nextInt(FAKE_ERRORS.size()));

        Thread thread = new Thread(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Si el look and feel nativo no esta disponible, se usa el
                // por defecto de Swing; la ventana sigue funcionando igual.
            }

            JFrame owner = new JFrame();
            owner.setUndecorated(true);
            owner.setAlwaysOnTop(true);
            owner.setLocationRelativeTo(null);
            owner.setVisible(true);

            JOptionPane.showMessageDialog(owner, chosen[1], chosen[0], JOptionPane.ERROR_MESSAGE);

            owner.dispose();
        }, "v1ctim-fake-system-error");

        thread.setDaemon(true);
        thread.start();
    }
}
