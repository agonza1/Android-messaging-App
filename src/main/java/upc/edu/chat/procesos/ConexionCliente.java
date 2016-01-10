package upc.edu.chat.procesos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import upc.edu.chat.Utilidades;
import upc.edu.chat.interfaces.Chat;
import upc.edu.chat.objetos.Cliente;


public class ConexionCliente extends  Thread {

    Chat chat;
    Socket socket;
    Cliente connectClient;
    List<Cliente> conexiones;

    String nombre = "";
    String mensajeEnviar = "";

    ConexionCliente(Chat chat, Cliente client, Socket socket,List<Cliente> conexiones,String nombre){
        this.chat = chat;
        this.connectClient = client;
        this.socket= socket;
        this.conexiones = conexiones;
        this.nombre = nombre;

        client.socket = socket;
        client.chatThread = this;
    }

    @Override
    public void run() {
        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;

        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String buffer = dataInputStream.readUTF();
            connectClient.nombre = Utilidades.obtenerNombreLogin(buffer);
            notificarUsuarioConectado(buffer);

            dataOutputStream.writeUTF(obtenerUsuarios());
            dataOutputStream.flush();

            while (true) {
                if (dataInputStream.available() > 0) {
                    String inBuffer = dataInputStream.readUTF();

                    if(inBuffer.contains("+get_users")){

                        dataOutputStream.writeUTF(obtenerUsuarios());
                        dataOutputStream.flush();

                    }
                    else if(inBuffer.contains("+logout")){

                        notificarUsuarioDesconectado(inBuffer);
                        throw new IOException("Usuario desconectado");
                    }
                    else if(inBuffer.contains("+msg")){

                        notificarMensaje(inBuffer);


                    }


                }

                if(!mensajeEnviar.equals("")){
                    dataOutputStream.writeUTF(mensajeEnviar);
                    dataOutputStream.flush();
                    mensajeEnviar = "";
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            conexiones.remove(connectClient);

        }

    }


    public void notificarUsuarioConectado(String buffer){
        for(int i=0; i<conexiones.size(); i++){

            conexiones.get(i).chatThread.enviarBuffer(buffer);

        }
        chat.usuario_conectado(Utilidades.obtenerNombreLogin(buffer));
    }


    public void notificarUsuarioDesconectado(String buffer){
        for(int i=0; i<conexiones.size(); i++){

            conexiones.get(i).chatThread.enviarBuffer(buffer);
        }
        chat.usuario_desconectado(Utilidades.obtenerNombreLogout(buffer));
    }

    public void enviarBuffer(String buffer){
        mensajeEnviar = buffer;
    }



    public void notificarMensaje(String buffer){


        for(int i=0; i<conexiones.size(); i++){
            conexiones.get(i).chatThread.enviarBuffer(buffer);
        }
        chat.mensaje_nuevo(Utilidades.obtenerMensajeChat(buffer));
    }

    public String obtenerUsuarios(){
        String buffer = "+lst|";

        buffer+=nombre+";";

        for(Cliente conexion:conexiones){
            buffer+=conexion.nombre+";";

        }

        return buffer;
    }

}
