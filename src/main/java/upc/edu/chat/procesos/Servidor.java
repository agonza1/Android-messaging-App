package upc.edu.chat.procesos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import upc.edu.chat.interfaces.Chat;
import upc.edu.chat.objetos.Cliente;
import upc.edu.chat.objetos.MensajesChat;


public class Servidor extends  Thread {
    int puerto = 8080;
    Chat chat;
    public List<Cliente> conexiones;
    ServerSocket serverSocket;
    String nombre;
    boolean goOut = false;



    public Servidor(String nombre,Chat chat, int puerto){
        this.nombre = nombre;
        this.chat = chat;
        this.puerto = puerto;
        conexiones = new ArrayList<Cliente>();

    }

    @Override
    public void run() {
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(puerto);
            chat.estado(Chat.ESTADO_SERVIDOR_ESPERANDO);


            chat.usuario_conectado(nombre);

            while (true) {
                socket = serverSocket.accept();
                Cliente client = new Cliente();
                client.nombre = nombre;
                conexiones.add(client);
                ConexionCliente connectThread = new ConexionCliente(chat, client, socket , conexiones,nombre);
                connectThread.start();

                if(goOut)
                    break;




            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        chat.estado(Chat.ESTADO_DESCONECTADO);

    }



    public void close(){
        enviarSalida();
        goOut = true;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        chat.estado(Chat.ESTADO_DESCONECTADO);

    }

    public void enviarSalida(){



        for(int i=0; i<conexiones.size(); i++){
            String buffer = "+bye";
            conexiones.get(i).chatThread.enviarBuffer(buffer);
        }


    }

    public void enviarUsuarios(){

        String buffer = "+lst|";
        buffer+=nombre+";";
        for(Cliente conexion:conexiones){
            buffer+=conexion.nombre+";";

        }

        for(int i=0; i<conexiones.size(); i++){
            conexiones.get(i).chatThread.enviarBuffer(buffer);
        }

    }


    public String obtenerUsuarios(){
        String buffer = "+lst|";

        buffer+=nombre+";";

        for(Cliente conexion:conexiones){
            buffer+=conexion.nombre+";";

        }

        return buffer;
    }

    public void enviarMensaje(String mensaje){



        for(int i=0; i<conexiones.size(); i++){
            String buffer = "+msg|"+nombre+"|" + mensaje;
            conexiones.get(i).chatThread.enviarBuffer(buffer);
        }


        chat.mensaje_nuevo(new MensajesChat(nombre, mensaje));

    }

}
