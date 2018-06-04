package pro_bibliotecajuegos.controller;

import java.sql.*;
import javax.swing.table.DefaultTableModel;
import pro_bibliotecajuegos.model.TrivialPlayerModel;

/**
 *
 * @author abrandarizdominguez & ssahuquillo
 */
public class TrivialControllerDataBase {
    
    String url = "BDscore.db";
    Connection connect = null;
    Statement stmt = null;
    Object[] objetos = null;
    
    /**
     * Método que conecta el usuario a la base de datos.
     */
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:" + url);
            /*
             * Si el objeto Connection ha cambiado el valor de null es que el 
             * usuario se ha conectado, por lo que se cambia el texto del TextField.
             */
            if (connect != null) {
                System.out.println("Usuario conectado");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error : " + ex);
        }
    }

    /**
     * Método que desconecta al usuario de la base de datos.
     */
    public void disconnect() {
        try {
            /*
             * Para asegurarse de que el objeto Statement está cerrado, se añade
             * una estructura condicional para que, en caso de estar abierto, se
             * cierre.
             */
            if (stmt != null) {
                stmt.close();
            }
            /*
             * Si el objeto Connection ha cambiado el valor de null es que el 
             * usuario está conectado en ese momento, por lo que se cambia el 
             * texto del TextField y se cierra el objeto Connection.
             */
            if (connect != null) {
                connect.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * Método que crea una tabla con los valores que añade el usuario.
     */
    public void createTable() {
        try {
            stmt = connect.createStatement();
            String tabla = "CREATE TABLE SCORE "
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " PN1 TEXT NOT NULL, "
                    + " PS1 INT NOT NULL, "
                    + " PN2 TEXT NOT NULL, "
                    + " PS2 INT NOT NULL)";
            stmt.executeUpdate(tabla);
            stmt.close();
            System.out.println("Tabla creada con éxito.");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * Método que inserta filas en una tabla con los valores que añade el
     * usuario.
     * @param player1
     * @param player2
     */
    public void insert(TrivialPlayerModel player1, TrivialPlayerModel player2) {
        try {
            stmt = connect.createStatement();
            String insert = "INSERT INTO SCORE (PN1, PS1, PN2, PS2) "
                    + " VALUES ('"
                    + player1.getNombre() + "', " + player1.getPuntuacion()
                    + ", '" + player2.getNombre() + "', " + player2.getPuntuacion()
                    + ");";
            stmt.executeUpdate(insert);
            stmt.close();
            System.out.println("Filas añadidas.");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public void select(DefaultTableModel tabla) {
        try {
            if (tabla.getRowCount() != 0) {
                do {
                    tabla.removeRow(tabla.getRowCount() - 1);
                } while (tabla.getRowCount() > 0);
            }
            stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SCORE;");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String pn1 = rs.getString("PN1");
                int ps1 = rs.getInt("PS1");
                String pn2 = rs.getString("PN2");
                int ps2 = rs.getInt("PS2");
                objetos = new Object[]{id, pn1, ps1, pn2, ps2};
                tabla.addRow(objetos);
            }
            System.out.println("SELECT  * FROM SCORE;");
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
    }
}
