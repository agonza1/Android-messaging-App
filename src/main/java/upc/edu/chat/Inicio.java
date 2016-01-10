package upc.edu.chat;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import upc.edu.chat.adapter.Mensaje;
import upc.edu.chat.interfaces.Chat;
import upc.edu.chat.objetos.MensajesChat;
import upc.edu.chat.procesos.Servidor;


public class Inicio extends android.support.v7.app.AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener,Chat, AdapterView.OnItemLongClickListener {


    String miUsuario = "";

    Mensaje adaptadorMensajes = null;

    ArrayList<String> usuariosChat = new ArrayList<>();
    ArrayList<MensajesChat> usuariosMensajes = new ArrayList<>();

    Button enviar;
    EditText mensaje;

    EditText ipServidor;
    EditText puertoServidor;
    Button conectarServidor;
    Button crearServidor;
    Button usuarios;

    TextView estado;

    ListView mensajes;


    Servidor servidorChat = null;
    upc.edu.chat.procesos.Cliente clienteChat = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        pedirNombre();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(servidorChat != null && servidorChat.isAlive()) {
            servidorChat.close();
            servidorChat = null;
        }

        if(clienteChat != null && clienteChat.isAlive()) {
            clienteChat.close();
            clienteChat = null;
        }
    }

    private void pedirNombre(){
        ((ImageView) findViewById(R.id.fondo)).setVisibility(View.VISIBLE);


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Chat");
        alert.setMessage("Ingrese su nombre de usuario ");

        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                if(input.getText().toString().isEmpty()){
                    Toast.makeText(Inicio.this,"El nombre no puede estar vacio",Toast.LENGTH_LONG).show();
                    pedirNombre();
                }
                else {
                    ((ImageView) findViewById(R.id.fondo)).setVisibility(View.GONE);
                    miUsuario = input.getText().toString();
                    Inicio.this.setTitle("Chat [" + miUsuario + "]");
                    configurar();
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                finish();
            }
        });

        alert.show();


    }



    private void configurar(){

        enviar = (Button) findViewById(R.id.enviar);
        mensaje = (EditText) findViewById(R.id.mensaje);
        mensajes = (ListView) findViewById(R.id.mensajes);

        usuarios = (Button) findViewById(R.id.usuarios);

        ipServidor = (EditText) findViewById(R.id.ip);
        puertoServidor = (EditText) findViewById(R.id.puerto);
        conectarServidor = (Button) findViewById(R.id.conectar);
        crearServidor = (Button) findViewById(R.id.crear);

        estado = (TextView) findViewById(R.id.estado);

        enviar.setOnClickListener(this);
        conectarServidor.setOnClickListener(this);
        crearServidor.setOnClickListener(this);
        usuarios.setOnClickListener(this);

    }

    private void cambiarEstado(String texto) {
        estado.setText(texto);
    }




    private void conectar(String ip, int puerto){

        clienteChat = new upc.edu.chat.procesos.Cliente(Inicio.this,miUsuario,ip,puerto);
        clienteChat.start();

    }

    private void crear(int puerto){
        servidorChat =new Servidor(miUsuario,this,puerto);
        servidorChat.start();


    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.conectar:

                if(conectarServidor.getText().toString().equals("DESCONECTAR")) {

                    usuariosChat.clear();
                    usuariosMensajes.clear();

                    if(clienteChat != null && clienteChat.isAlive()) {
                        clienteChat.close();
                        clienteChat = null;
                    }

                }
                else if(ipServidor.getText().toString().isEmpty() || puertoServidor.getText().toString().isEmpty()) {

                    Toast.makeText(this,"La ip del servidor o el puerto no deben estar vacios",Toast.LENGTH_LONG).show();
                }
                else if(Utilidades.validarIp(ipServidor.getText().toString()) && Utilidades.validarPuerto(puertoServidor.getText().toString())) {
                    ipServidor.setEnabled(false);
                    puertoServidor.setEnabled(false);

                    crearServidor.setEnabled(false);
                    conectarServidor.setEnabled(false);
                    conectarServidor.setText("CONECTANDO");
                    cambiarEstado("Conectando a " + ipServidor.getText().toString() + ":" + puertoServidor.getText().toString());
                    conectar(ipServidor.getText().toString(), Integer.parseInt(puertoServidor.getText().toString()));

                }

                break;


            case R.id.crear:
                if(crearServidor.getText().toString().equals("DETENER")) {

                    usuariosChat.clear();
                    usuariosMensajes.clear();

                    if(servidorChat != null && servidorChat.isAlive()) {
                        servidorChat.close();
                        servidorChat = null;
                    }

                }
                else if(puertoServidor.getText().toString().isEmpty()) {

                    Toast.makeText(this," el puerto no debe estar vacio",Toast.LENGTH_LONG).show();
                }

                else if(Utilidades.validarPuerto(puertoServidor.getText().toString())) {
                    ipServidor.setEnabled(false);
                    ipServidor.setText("");
                    puertoServidor.setEnabled(false);

                    crearServidor.setEnabled(false);
                    conectarServidor.setEnabled(false);
                    crearServidor.setText("INICIANDO");
                    cambiarEstado("Iniciando servidor");
                    crear(Integer.parseInt(puertoServidor.getText().toString()));




                }

                break;

            case R.id.usuarios:

                if(servidorChat != null && servidorChat.isAlive()) {
                    Intent intent = new Intent(Inicio.this, Usuarios.class);
                    intent.putStringArrayListExtra("usuarios", usuariosChat);
                    startActivity(intent);
                }
                else if(clienteChat != null && clienteChat.isAlive()) {
                    Intent intent = new Intent(Inicio.this, Usuarios.class);
                    intent.putStringArrayListExtra("usuarios", usuariosChat);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this,"Sin conexion",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.enviar:


                if(mensaje.getText().toString().isEmpty()) {

                    Toast.makeText(this,"El mensaje no debe estar vacio",Toast.LENGTH_LONG).show();
                }
                else if(servidorChat != null && servidorChat.isAlive()){
                    servidorChat.enviarMensaje(mensaje.getText().toString());
                    mensaje.setText("");
                    InputMethodManager ipmm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    ipmm.hideSoftInputFromWindow(mensaje.getWindowToken(), 0);
                }
                else if(clienteChat != null && clienteChat.isAlive()){
                    clienteChat.enviarMensaje(mensaje.getText().toString());
                    mensaje.setText("");
                    InputMethodManager ipmm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    ipmm.hideSoftInputFromWindow(mensaje.getWindowToken(), 0);
                }

                break;


        }
    }



    private void actualizarMensajes() {

        if (adaptadorMensajes == null){
            adaptadorMensajes = new Mensaje(this,usuariosMensajes);
            mensajes.setAdapter(adaptadorMensajes);
        }
        else

        {
            adaptadorMensajes.notifyDataSetChanged();
        }

        mensajes.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mensajes.setSelection(adaptadorMensajes.getCount() - 1);
            }
        });

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void listado_usuarios(final String[] nombres) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(usuariosChat != null) {
                    usuariosChat.clear();
                    Collections.addAll(usuariosChat, nombres);

                }
                else {
                    usuariosChat = new ArrayList<String>();
                    Collections.addAll(usuariosChat, nombres);
                }


            }

        });
    }


    @Override
    public void usuario_conectado(final String nombre) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(usuariosChat != null && usuariosChat.indexOf(nombre) ==-1){
                    usuariosMensajes.add(new MensajesChat(nombre,"Se ha conectado."));
                    usuariosChat.add(nombre);
                    actualizarMensajes();
                }

            }
        });
    }

    @Override
    public void usuario_desconectado(final String nombre) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(usuariosChat != null){
                    usuariosMensajes.add(new MensajesChat(nombre,"Se ha desconectado."));
                    usuariosChat.remove(nombre);
                    actualizarMensajes();

                }

            }
        });
    }

    @Override
    public void mensaje_nuevo(final MensajesChat mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                usuariosMensajes.add(mensaje);
                actualizarMensajes();
            }
        });
    }


    @Override
    public void estado(final int estado) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (estado == ESTADO_SERVIDOR_ESPERANDO) {
                    crearServidor.setText("DETENER");
                    crearServidor.setEnabled(true);
                    cambiarEstado("Esperando clientes en : " + Utilidades.getIpAddress() + ":" + puertoServidor.getText().toString());
                } else if (estado == ESTADO_CLIENTE_CONECTADO) {
                    conectarServidor.setText("DESCONECTAR");
                    conectarServidor.setEnabled(true);
                    cambiarEstado("Conectar a : " + ipServidor.getText().toString() + ":" + puertoServidor.getText().toString());
                } else if (estado == ESTADO_DESCONECTADO) {

                    cambiarEstado("Desconectado");

                    if(usuariosChat != null)
                        usuariosChat.clear();

                    ipServidor.setEnabled(true);
                    puertoServidor.setEnabled(true);

                    crearServidor.setText("CREAR");
                    crearServidor.setEnabled(true);

                    conectarServidor.setText("CONECTAR");
                    conectarServidor.setEnabled(true);
                }


            }
        });


    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(clienteChat != null && clienteChat.isAlive()){

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Chat");
            alert.setMessage("Desea descargar lista de usuarios? ");
            alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    clienteChat.obtenerUsuarios();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();


        }

        return false;
    }
}
