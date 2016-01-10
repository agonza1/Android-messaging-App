package upc.edu.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import upc.edu.chat.R;
import upc.edu.chat.objetos.MensajesChat;


public class Mensaje extends BaseAdapter {

    private ArrayList<MensajesChat> mensajesChats;
    private LayoutInflater inflater = null;

    public Mensaje(Context context, ArrayList<MensajesChat> mensajesChats) {
        this.mensajesChats = mensajesChats;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mensajesChats.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;


        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.adapter_mensaje, null);
            holder = new ViewHolder();
            holder.nombre = (TextView) convertView.findViewById(R.id.adapter_mensaje_cliente);
            holder.mensaje = (TextView) convertView.findViewById(R.id.adapter_mensaje_texto);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        // Para porde hacer click en el checkbox
        MensajesChat d = (MensajesChat) getItem(position);

        // Setting all values in listview
        holder.nombre.setText(d.nombre);
        holder.mensaje.setText(d.mensaje);

        return convertView;
    }

    public Object getItem(int arg0) {
        return mensajesChats.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @Override
    public int getViewTypeCount() {
        return 3; // Count of different layouts
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    static class ViewHolder
    {
        TextView nombre;
        TextView mensaje;
    }
}
