package upc.edu.chat.procesos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import upc.edu.chat.Utilidades;
import upc.edu.chat.interfaces.Chat;

public class Cliente extends Thread {
    Chat chat;
    String nombre;
    String dstAddress;
    int dstPort;

    String mensajeEnviar = "";
    boolean goOut = false;

    public Cliente(Chat chat,String nombre, String address, int port) {
        this.chat = chat;
        this.nombre = nombre;
        dstAddress = address;
        dstPort = port;

    }

    @Override
    public void run() {
        Socket socket = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        chat.estado(Chat.ESTADO_CLIENTE_CONECTANDO);
        try {
            socket = new Socket(dstAddress, dstPort);
            dataOutputStream = new DataOutputStream( socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());

            dataOutputStream.writeUTF(crearBufferLogin(nombre));
            dataOutputStream.flush();

            while (true) {
                if (dataInputStream.available() > 0) {

                    String inBuffer = dataInputStream.readUTF();

                    if(inBuffer.contains("+lst")){

                        obtenerUsuariosBuffer(inBuffer);
                    }
                    else if(inBuffer.contains("+login")){

                        chat.usuario_conectado(Utilidades.obtenerNombreLogin(inBuffer));


                    }
                    else if(inBuffer.contains("+logout")){
                        chat.usuario_desconectado(Utilidades.obtenerNombreLogout(inBuffer));

                    }
                    else if(inBuffer.contains("+msg")){

                        chat.mensaje_nuevo(Utilidades.obtenerMensajeChat(inBuffer));
                    }
                    else if(inBuffer.contains("+bye")){
                        throw new IOException("Exit");
                    }


                }

                if(!mensajeEnviar.equals("")){
                    dataOutputStream.writeUTF(mensajeEnviar);
                    dataOutputStream.flush();
                    mensajeEnviar = "";
                }

                if(goOut){
                    dataOutputStream.writeUTF(crearBufferLogout(nombre));
                    dataOutputStream.flush();
                    break;
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            final String eString = e.toString();
            chat.estado(Chat.ESTADO_DESCONECTADO);

        } catch (IOException e) {
            e.printStackTrace();
            final String eString = e.toString();
            chat.estado(Chat.ESTADO_DESCONECTADO);
        }


        if (socket != null) {

            try {
                socket.close();
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

        if (dataInputStream != null) {
            try {
                dataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        chat.estado(Chat.ESTADO_DESCONECTADO);

    }

    public String crearBufferLogin(String nombre){

        return "+login|" + nombre;
    }

    public String crearBufferLogout(String nombre){

        return "+logout|" + nombre;
    }


    public void obtenerUsuariosBuffer(String buffer){

        String data[] = buffer.replace("+lst|","").split(";");
        chat.estado(Chat.ESTADO_CLIENTE_CONECTADO);
        chat.listado_usuarios(data);

    }


    public void obtenerUsuarios(){

        String buffer = "+get_users";
        enviarBuffer(buffer);

    }

    public void enviarMensaje(String mensaje){

        String buffer = "+msg|"+nombre+"|" + mensaje;
        enviarBuffer(buffer);

    }

    public void enviarBuffer(String buffer){
        mensajeEnviar = buffer;
    }

    public void close(){
        goOut = true;
    }

}
