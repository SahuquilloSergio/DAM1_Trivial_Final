package pro_bibliotecajuegos.controller;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import pro_bibliotecajuegos.model.TrivialPlayerModel;
import pro_bibliotecajuegos.model.TrivialQuestionModel;
import pro_bibliotecajuegos.view.TrivialLobbyView;
import pro_bibliotecajuegos.view.TrivialQuestionView;
import pro_bibliotecajuegos.view.TrivialScoreView;

/**
 *
 * @author abrandarizdominguez & ssahuquillo
 */
public class TrivialController {

    boolean acierto;

    AudioClip sonido;
    TrivialControllerDataBase controldb = new TrivialControllerDataBase();
    TrivialQuestionView questionView;
    TrivialScoreView scoreView = new TrivialScoreView();
    PrintWriter pw;
    String listaPro = "FicheroPreguntasPRO.txt";
    String listaSis = "FicheroPreguntasSI.txt";
    String listaBds = "FicheroPreguntasBD.txt";
    String listaLmsxi = "FicheroPreguntasLMSXI.txt";
    String listaFol = "FicheroPreguntasFOL.txt";
    String listaCod = "FicheroPreguntasCOD.txt";
    ArrayList<TrivialQuestionModel> pregunta;
    Scanner sc;
    String delim = ",";
    String linea;
    int bucle;
    String[] lista;
    File fichero;
    String res1;

    public boolean isAcierto() {
        return acierto;
    }

    public void setAcierto(boolean acierto) {
        this.acierto = acierto;
    }

    public String getRes1() {
        return res1;
    }

    /**
     * Método "añadirPregunta" en el cual a través de un bucle do while en el
     * que se pide la pregunta y cuatro respuestas; Tras ésto se crea un nuevo
     * objeto de tipo pregunta conformado por estos nuevos parámetros y se
     * escribe en el fichero.
     *
     * @param fichero que pide el método para introducir en el la nueva
     * pregunta.
     */
    public void añadirPregunta(File fichero) {
        try {
            pw = new PrintWriter(new FileWriter(fichero, true));
            sc = new Scanner(fichero);
            // Bucle do while en el que se piden los valores de las variables.
            do {
                pregunta = new ArrayList();
                String preg = JOptionPane.showInputDialog("Pregunta: ");
                String res1 = JOptionPane.showInputDialog("Respuesta 1: ");
                String res2 = JOptionPane.showInputDialog("Respuesta 2: ");
                String res3 = JOptionPane.showInputDialog("Respuesta 3: ");
                String res4 = JOptionPane.showInputDialog("Respuesta 4: ");
                TrivialQuestionModel p = new TrivialQuestionModel(preg, res1, res2, res3, res4);
                pregunta.add(p);
                pw.println(p);
                bucle = JOptionPane.showConfirmDialog(null, "¿Engadir outra?");
            } while (bucle == 0);
        } catch (IOException ex) {
            Logger.getLogger(TrivialQuestionModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
            sc.close();
        }
    }

    /**
     * Método "Visualizar" en el que, se pide un fichero y no devuelve nada, se
     * lee el contenido del fichero y se muestran todas las preguntas que
     * contenga.
     *
     * @param fichero que pide el método para leer el contenido que hay en él.
     */
    public void visualizar(File fichero) {
        pregunta = new ArrayList();
        String ac = "";
        try {
            sc = new Scanner(fichero);
            while (sc.hasNextLine()) {
                linea = sc.nextLine();
                String[] l = linea.split(",");
                for (int i = 0; i < l.length; i += 5) {
                    pregunta.add(new TrivialQuestionModel(l[i], l[i + 1], l[i + 2], l[i + 3], l[i + 4]));
                }
            }
            for (int j = 0; j < pregunta.size(); j++) {
                ac = ac + ("Pregunta " + (j + 1) + "---> "
                        + "Pregunta: " + pregunta.get(j).getPregunta()
                        + "Respuesta 1: " + pregunta.get(j).getRes1()
                        + "Respuesta 2: " + pregunta.get(j).getRes2()
                        + "Respuesta 3: " + pregunta.get(j).getRes3()
                        + "Respuesta 4: " + pregunta.get(j).getRes4() + "\n");
            }
            JOptionPane.showMessageDialog(null, ac);
        } catch (IOException ex) {
            Logger.getLogger(TrivialQuestionModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            sc.close();
        }
    }

    /**
     * Método "leerPregunta" en el que, se pide un fichero y no devuelve nada,
     * se lee el contenido del fichero y se añade a un arrayList - Se escoge una
     * pregunta de forma aleatoria y se desordenan sus respuestas.
     *
     * @param fichero que pide el método para leer el contenido que hay en él.
     * @param player1
     * @param player2
     * @param st
     * @param view
     */
    public void leerPregunta(File fichero, TrivialPlayerModel player1, TrivialPlayerModel player2, String st, TrivialLobbyView view) {
        try {
            questionView = new TrivialQuestionView();
            // Se comprueba que jugador está activo en ese momento:
            if (player1.isActivo() == true) {
                questionView.getJtScore().setText(String.valueOf(player1.getPuntuacion()));
                questionView.getJtNombreJugador().setText(player1.getNombre());
            } else {
                questionView.getJtScore().setText(String.valueOf(player2.getPuntuacion()));
                questionView.getJtNombreJugador().setText(player2.getNombre());
            }
            questionView.setVisible(true);
            sc = new Scanner(fichero);
            pregunta = new ArrayList();
            /*
             * Bucle while que mientras, en el fichero, haya más líneas que leer
             * las incorpora a un Array de tipo String, separando los componentes
             * por un delimitador que se ha asignado previamente. A continuación
             * se añade al ArrayList pregunta.
             */
            while (sc.hasNextLine()) {
                linea = sc.nextLine();
                lista = new String[5];
                lista = linea.split(delim);
                TrivialQuestionModel p = new TrivialQuestionModel(lista[0], lista[1], lista[2], lista[3], lista[4]);
                pregunta.add(p);
            }
            // Se crean y se inicializan las variables a utilizar en la pregunta.
            int finalRes = 4;
            int principioRes = 1;
            int pre = 0;
            Random num = new Random(System.nanoTime());
            int numRespuesta1;
            int numRespuesta2;
            int numRespuesta3;
            int numRespuesta4;
            /*
             * Se crea otro número aleatorio que seleccionará de forma aleatoria 
             * una pregunta de las almacenadas en el fichero.
             */
            int numPregunta = num.nextInt((pregunta.size() - 1) - pre + 1) + pre;
            // Crea un número aleatorio entre 1 y 4 y se almacena en la variable
            numRespuesta1 = num.nextInt(finalRes - principioRes + 1) + principioRes;
            /*
             * Bucle do while que se emplea en cada una de las respuestas
             * siguientes que generará otro número aleatorio para esa respuesta
             * y seguirá generándolo hasta que no coincida.
             */
            do {
                numRespuesta2 = num.nextInt(finalRes - principioRes + 1) + principioRes;
            } while (numRespuesta1 == numRespuesta2);
            do {
                numRespuesta3 = num.nextInt(finalRes - principioRes + 1) + principioRes;
            } while (numRespuesta1 == numRespuesta3 || numRespuesta2 == numRespuesta3);
            do {
                numRespuesta4 = num.nextInt(finalRes - principioRes + 1) + principioRes;
            } while (numRespuesta1 == numRespuesta4 || numRespuesta2 == numRespuesta4 || numRespuesta3 == numRespuesta4);
            // Se crea una variable auxilar para imprimir las respuestas:
            String[] auxiliar = new String[4];
            String numero1 = Integer.toString(numRespuesta1);
            res1 = pregunta.get(numPregunta).getRes1();
            auxiliar[0] = numero1 + ") " + res1;
            String numero2 = Integer.toString(numRespuesta2);
            String res2 = pregunta.get(numPregunta).getRes2();
            auxiliar[1] = numero2 + ") " + res2;
            String numero3 = Integer.toString(numRespuesta3);
            String res3 = pregunta.get(numPregunta).getRes3();
            auxiliar[2] = numero3 + ") " + res3;
            String numero4 = Integer.toString(numRespuesta4);
            String res4 = pregunta.get(numPregunta).getRes4();
            auxiliar[3] = numero4 + ") " + res4;
            // Se ordena el array:
            for (int i = 0; i < (auxiliar.length - 1); i++) {
                for (int j = i + 1; j < auxiliar.length; j++) {
                    if (auxiliar[i].compareToIgnoreCase(auxiliar[j]) > 0) {
                        // Se intercambian los valores:
                        String variableauxiliar = auxiliar[i];
                        auxiliar[i] = auxiliar[j];
                        auxiliar[j] = variableauxiliar;
                    }
                }
            }
            // Se incorporan la pregunta y las 4 respuestas a la interfaz gráfica:
            questionView.getJtTextoPregunta().setText(pregunta.get(numPregunta).getPregunta());
            questionView.getbPregunta1().setText(auxiliar[0]);
            questionView.getbPregunta2().setText(auxiliar[1]);
            questionView.getbPregunta3().setText(auxiliar[2]);
            questionView.getbPregunta4().setText(auxiliar[3]);
            res1 = numero1 + ") " + res1;
            // Se identifica si el boton pulsado coincide con la respuesta correcta:
            questionView.getbPregunta1().addActionListener((ActionEvent ae) -> {
                if (ae.getSource().equals(questionView.getbPregunta1())) {
                    acierto = false;
                    // Si coincide suma 20 puntos a su Score y sigue tirando:
                    if (questionView.getbPregunta1().getText().equalsIgnoreCase(res1)) {
                        acierto = true;
                        sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_coin.wav"));
                        sonido.play();
                        this.darQuesito(st, player1, player2);
                        questionView.getJtTextoPregunta().setText("Acertaches a pregunta");
                        /*
                         * Estructura condicional en el que se identifica 
                         * quien es el jugador activo para darle los puntos.
                         */
                        if (player1.isActivo() == true) {
                            player1.setPuntuacion(player1.getPuntuacion() + 20);
                            questionView.getJtScore().setText(String.valueOf(player1.getPuntuacion()));
                        } else {
                            player2.setPuntuacion(player2.getPuntuacion() + 20);
                            questionView.getJtScore().setText(String.valueOf(player2.getPuntuacion()));
                        }
                    } else {
                        // Si falla se cambian los estados de activo entre los jugadores.
                        sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_lost_a_life.wav"));
                        sonido.play();
                        questionView.getJtTextoPregunta().setText("Fallaches a pregunta!");
                        if (player1.isActivo() == true) {
                            player1.setActivo(false);
                            player2.setActivo(true);
                        } else {
                            player1.setActivo(true);
                            player2.setActivo(false);
                        }
                    }
                }
            });
            // Se identifica si el boton pulsado coincide con la respuesta correcta:
            questionView.getbPregunta2().addActionListener((ActionEvent ae) -> {
                if (ae.getSource().equals(questionView.getbPregunta2())) {
                    acierto = false;
                    // Si coincide suma 20 puntos a su Score y sigue tirando:
                    if (questionView.getbPregunta2().getText().equalsIgnoreCase(res1)) {
                        acierto = true;
                        sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_coin.wav"));
                        sonido.play();
                        this.darQuesito(st, player1, player2);
                        questionView.getJtTextoPregunta().setText("Acertaches a pregunta");
                        /*
                         * Estructura condicional en el que se identifica 
                         * quien es el jugador activo para darle los puntos.
                         */
                        if (player1.isActivo() == true) {
                            player1.setPuntuacion(player1.getPuntuacion() + 20);
                            questionView.getJtScore().setText(String.valueOf(player1.getPuntuacion()));
                        } else {
                            player2.setPuntuacion(player2.getPuntuacion() + 20);
                            questionView.getJtScore().setText(String.valueOf(player2.getPuntuacion()));
                        }
                    } else {
                        // Si falla se cambian los estados de activo entre los jugadores.
                        sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_lost_a_life.wav"));
                        sonido.play();
                        questionView.getJtTextoPregunta().setText("Fallaches a pregunta!");
                        if (player1.isActivo() == true) {
                            player1.setActivo(false);
                            player2.setActivo(true);
                        } else {
                            player1.setActivo(true);
                            player2.setActivo(false);
                        }
                    }
                }
            });
            // Se identifica si el boton pulsado coincide con la respuesta correcta:
            questionView.getbPregunta3().addActionListener((ActionEvent ae) -> {
                if (ae.getSource().equals(questionView.getbPregunta3())) {
                    acierto = false;
                    // Si coincide suma 20 puntos a su Score y sigue tirando:
                    if (questionView.getbPregunta3().getText().equalsIgnoreCase(res1)) {
                        acierto = true;
                        sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_coin.wav"));
                        sonido.play();
                        this.darQuesito(st, player1, player2);
                        questionView.getJtTextoPregunta().setText("Acertaches a pregunta");
                        /*
                         * Estructura condicional en el que se identifica 
                         * quien es el jugador activo para darle los puntos.
                         */
                        if (player1.isActivo() == true) {
                            player1.setPuntuacion(player1.getPuntuacion() + 20);
                            questionView.getJtScore().setText(String.valueOf(player1.getPuntuacion()));
                        } else {
                            player2.setPuntuacion(player2.getPuntuacion() + 20);
                            questionView.getJtScore().setText(String.valueOf(player2.getPuntuacion()));
                        }
                    } else {
                        // Si falla se cambian los estados de activo entre los jugadores.
                        sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_lost_a_life.wav"));
                        sonido.play();
                        questionView.getJtTextoPregunta().setText("Fallaches a pregunta!");
                        if (player1.isActivo() == true) {
                            player1.setActivo(false);
                            player2.setActivo(true);
                        } else {
                            player1.setActivo(true);
                            player2.setActivo(false);
                        }
                    }
                }
            });
            // Se identifica si el boton pulsado coincide con la respuesta correcta:
            questionView.getbPregunta4().addActionListener((ActionEvent ae) -> {
                if (ae.getSource().equals(questionView.getbPregunta4())) {
                    acierto = false;
                    // Si coincide suma 20 puntos a su Score y sigue tirando:
                    if (questionView.getbPregunta4().getText().equalsIgnoreCase(res1)) {
                        acierto = true;
                        sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_coin.wav"));
                        sonido.play();
                        this.darQuesito(st, player1, player2);
                        questionView.getJtTextoPregunta().setText("Acertaches a pregunta");
                        /*
                         * Estructura condicional en el que se identifica 
                         * quien es el jugador activo para darle los puntos.
                         */
                        if (player1.isActivo() == true) {
                            player1.setPuntuacion(player1.getPuntuacion() + 20);
                            questionView.getJtScore().setText(String.valueOf(player1.getPuntuacion()));
                        } else {
                            // Si falla se cambian los estados de activo entre los jugadores.
                            player2.setPuntuacion(player2.getPuntuacion() + 20);
                            questionView.getJtScore().setText(String.valueOf(player2.getPuntuacion()));
                        }
                    } else {
                        sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_lost_a_life.wav"));
                        sonido.play();
                        questionView.getJtTextoPregunta().setText("Fallaches a pregunta!");
                        if (player1.isActivo() == true) {
                            player1.setActivo(false);
                            player2.setActivo(true);
                        } else {
                            player1.setActivo(true);
                            player2.setActivo(false);
                        }
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(TrivialQuestionModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            sc.close();
        }
    }

    public void bloquearBotones(JButton boton1, JButton boton2, JButton boton3, JButton boton4) {
        boton1.setEnabled(false);
        boton2.setEnabled(false);
        boton3.setEnabled(false);
        boton4.setEnabled(false);

    }

    public void comprobarActivo(TrivialPlayerModel player1, TrivialPlayerModel player2) {
        if (player1.isActivo() == true) {
            player1.setActivo(false);
            player2.setActivo(true);
        } else {
            player2.setActivo(false);
            player1.setActivo(true);
        }
    }

    public void analizarPregunta(String textoPregunta, JButton boton, JTextField pantalla) {
        acierto = false;
        // Estructura condicional if/else en la que se indica si se acierta la pregunta.           
        if (boton.getText().equalsIgnoreCase(textoPregunta)) {
            pantalla.setText("Acertaches a pregunta!");
            acierto = true;
        } else {
            pantalla.setText("Fallaches a pregunta!");
            acierto = false;
        }
    }

    public void darQuesito(String aux, TrivialPlayerModel p1, TrivialPlayerModel p2) {
        if (aux.equalsIgnoreCase("c")) {
            if (p1.isActivo() == true) {
                p1.setQuesitoCod(true);
            }
            if (p2.isActivo() == true) {
                p2.setQuesitoCod(true);
            }
        }
        if (aux.equalsIgnoreCase("b")) {
            if (p1.isActivo() == true) {
                p1.setQuesitoBds(true);
            }
            if (p2.isActivo() == true) {
                p2.setQuesitoBds(true);
            }
        }
        if (aux.equalsIgnoreCase("f")) {
            if (p1.isActivo() == true) {
                p1.setQuesitoFol(true);
            }
            if (p2.isActivo() == true) {
                p2.setQuesitoFol(true);
            }
        }
        if (aux.equalsIgnoreCase("l")) {
            if (p1.isActivo() == true) {
                p1.setQuesitoLms(true);
            }
            if (p2.isActivo() == true) {
                p2.setQuesitoLms(true);
            }
        }
        if (aux.equalsIgnoreCase("s")) {
            if (p1.isActivo() == true) {
                p1.setQuesitoSis(true);
            }
            if (p2.isActivo() == true) {
                p2.setQuesitoSis(true);
            }
        }
        if (aux.equalsIgnoreCase("p")) {
            if (p1.isActivo() == true) {
                p1.setQuesitoPro(true);
            }
            if (p2.isActivo() == true) {
                p2.setQuesitoPro(true);
            }
        }
    }

    public void colorQuesito(TrivialLobbyView view) {
        if (view.getPlayer1().isQuesitoCod() == true) {
            view.getJtCod().setBackground(Color.ORANGE);
        }
        if (view.getPlayer2().isQuesitoCod() == true) {
            view.getJtCod2().setBackground(Color.ORANGE);
        }
        if (view.getPlayer1().isQuesitoBds() == true) {
            view.getJtBds().setBackground(Color.GREEN);
        }
        if (view.getPlayer2().isQuesitoBds() == true) {
            view.getJtBds2().setBackground(Color.GREEN);
        }
        if (view.getPlayer1().isQuesitoFol() == true) {
            view.getJtFol().setBackground(Color.CYAN);
        }
        if (view.getPlayer2().isQuesitoFol() == true) {
            view.getJtFol2().setBackground(Color.CYAN);
        }
        if (view.getPlayer1().isQuesitoLms() == true) {
            view.getJtLmsxi().setBackground(Color.MAGENTA);
        }
        if (view.getPlayer2().isQuesitoLms() == true) {
            view.getJtLmsxi2().setBackground(Color.MAGENTA);
        }
        if (view.getPlayer1().isQuesitoSis() == true) {
            view.getJtSis().setBackground(Color.YELLOW);
        }
        if (view.getPlayer2().isQuesitoSis() == true) {
            view.getJtSis2().setBackground(Color.YELLOW);
        }
        if (view.getPlayer1().isQuesitoPro() == true) {
            view.getJtPro().setBackground(Color.PINK);
        }
        if (view.getPlayer2().isQuesitoPro() == true) {
            view.getJtPro2().setBackground(Color.PINK);
        }
    }

    public void toggleButtons(JButton botonOk, JButton boton1, JButton boton2, JButton boton3, JButton boton4, JButton boton5, JButton boton6) {
        if (botonOk.isEnabled() == true) {
            botonOk.setEnabled(false);
            boton1.setEnabled(true);
            boton2.setEnabled(true);
            boton3.setEnabled(true);
            boton4.setEnabled(true);
            boton5.setEnabled(true);
            boton6.setEnabled(true);
        } else {
            botonOk.setEnabled(true);
            boton1.setEnabled(false);
            boton2.setEnabled(false);
            boton3.setEnabled(false);
            boton4.setEnabled(false);
            boton5.setEnabled(false);
            boton6.setEnabled(false);
        }
    }

    public void comprobarGanador(TrivialPlayerModel player1, TrivialPlayerModel player2, JButton botonOk, JButton boton1, JButton boton2, JButton boton3, JButton boton4, JButton boton5, JButton boton6) {
        if (player1.isQuesitoBds() == true && player1.isQuesitoCod() == true
                && player1.isQuesitoFol() == true && player1.isQuesitoLms() == true
                && player1.isQuesitoPro() == true && player1.isQuesitoSis() == true) {
            sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_course_clear.wav"));
            sonido.play();
            JOptionPane.showMessageDialog(null, player1.getNombre() + " gaña.");
            this.toggleButtons(botonOk, boton1, boton2, boton3, boton4, boton5, boton6);
            botonOk.setEnabled(false);
            controldb.connect();
            controldb.insert(player1, player2);
            controldb.disconnect();
        }
        if (player2.isQuesitoBds() == true && player2.isQuesitoCod() == true
                && player2.isQuesitoFol() == true && player2.isQuesitoLms() == true
                && player2.isQuesitoPro() == true && player2.isQuesitoSis() == true) {
            sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_course_clear.wav"));
            sonido.play();
            JOptionPane.showMessageDialog(null, player2.getNombre() + " gaña.");
            this.toggleButtons(botonOk, boton1, boton2, boton3, boton4, boton5, boton6);
            botonOk.setEnabled(false);
            controldb.connect();
            controldb.insert(player1, player2);
            controldb.disconnect();
        }
    }

    public void ventanaScore() {
        if (scoreView.isVisible() == false) {
            sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_message_block.wav"));
            sonido.play();
            scoreView.setVisible(true);
            controldb.connect();
            controldb.createTable();
            controldb.select(scoreView.getModel());
        } else {
            sonido = java.applet.Applet.newAudioClip(getClass().getResource("/mediaSound/smw_princess_help.wav"));
            sonido.play();
            controldb.disconnect();
            scoreView.setVisible(false);
        }
    }
}
