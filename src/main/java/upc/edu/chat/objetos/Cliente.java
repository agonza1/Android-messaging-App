package upc.edu.chat.objetos;

import java.net.Socket;

import upc.edu.chat.procesos.ConexionCliente;


public class Cliente {
    public String nombre;
    public Socket socket;
    public ConexionCliente chatThread;

}
