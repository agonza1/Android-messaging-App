package upc.edu.chat;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import upc.edu.chat.objetos.MensajesChat;


public class Utilidades {

    private static final String PATTERN =
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static boolean validarIp(final String ip){

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public static boolean validarPuerto(final String port){

        try {
            int puerto = Integer.parseInt(port);

            if(puerto>0 && puerto<9999)
                return true;

        }
        catch (NumberFormatException e){
        }

       return false;
    }

    public static String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip +=  inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            e.printStackTrace();
            ip += "- NO IP - \n";
        }

        return ip;
    }


    public static MensajesChat obtenerMensajeChat(String buffer){

        String data[] = buffer.split("\\|");
        return new MensajesChat(data[1],data[2]);

    }

    public static String obtenerNombreLogin(String buffer){

        String nombre = buffer.replace("+login|","");
        return nombre;

    }


    public static String obtenerNombreLogout(String buffer){

        String nombre = buffer.replace("+logout|","");
        return nombre;

    }
}
