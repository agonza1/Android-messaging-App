package upc.edu.chat.objetos;


public class MensajesChat {

    public String nombre;
    public String mensaje;
    public long tiempo;

    public MensajesChat(String nombre, String mensaje){
        this.nombre = nombre;
        this.mensaje = mensaje;
        this.tiempo = System.currentTimeMillis();
    }
}
