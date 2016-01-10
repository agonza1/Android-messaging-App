package upc.edu.chat.interfaces;

import upc.edu.chat.objetos.MensajesChat;


public interface Chat {

    public static final int ESTADO_DESCONECTADO = 0;
    public static final int ESTADO_CLIENTE_CONECTANDO = 1;
    public static final int ESTADO_CLIENTE_CONECTADO = 2;
    public static final int ESTADO_SERVIDOR_ESPERANDO = 3;

    void listado_usuarios(String[] nombres);
    void usuario_conectado(String nombre);
    void usuario_desconectado(String nombre);
    void mensaje_nuevo(MensajesChat mensaje);
    void estado(int estado);

}
