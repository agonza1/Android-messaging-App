package upc.edu.chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class Usuarios extends Activity {
    ArrayList<String> usuarios = new ArrayList<String>();
    ListView usuariosConectados;

    Button atras;
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_usuarios);
        usuarios = getIntent().getStringArrayListExtra("usuarios");
        configurar();

    }


    private void configurar(){
        usuariosConectados = (ListView) findViewById(R.id.conectados);
        atras = (Button) findViewById(R.id.atras);
        ArrayAdapter<String>  adaptadorUsuario = new ArrayAdapter<String>(this, R.layout.adapter_usuario,usuarios);
        usuariosConectados.setAdapter(adaptadorUsuario);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
