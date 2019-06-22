package com.aplicacion.essalud.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aplicacion.essalud.R;
import com.aplicacion.essalud.models.Motivo;

import java.util.ArrayList;
import java.util.List;

public class MotivosAdapter extends BaseAdapter {

    private Context context;
    private List<Motivo> listMotivos = new ArrayList<Motivo>();;
    private final LayoutInflater layoutInflater;

    public MotivosAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(Motivo motivo) {
        this.listMotivos.add(motivo);
        notifyDataSetChanged();
    }

    public List<Motivo> getList() {
        return this.listMotivos;
    }

    @Override
    public int getCount() {
        return listMotivos.size();
    }

    @Override
    public Object getItem(int position) {
        return listMotivos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = layoutInflater.inflate(R.layout.item_motivo, null);
        TextView txvMotivo = (TextView) view.findViewById(R.id.txvMotivo);
        ImageButton btnRemoverMotivo = (ImageButton) view.findViewById(R.id.btnRemoverMotivo);
        Motivo motivo = listMotivos.get(position);
        txvMotivo.setText(motivo.getMotivo());
        btnRemoverMotivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listMotivos.remove(position);
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
