package com.aplicacion.essalud.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aplicacion.essalud.MotivosActivity;
import com.aplicacion.essalud.R;
import com.aplicacion.essalud.models.Horario;
import com.aplicacion.essalud.models.ItemCita;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.aplicacion.essalud.methods.Methods.showSnackBar;

public class CitasAdapter extends BaseAdapter {

    private Context context;
    private List<Horario> listCitas = new ArrayList<>();
    private final LayoutInflater layoutInflater;

    public CitasAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(Horario horario) {
        this.listCitas.add(horario);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listCitas.size();
    }

    @Override
    public Object getItem(int position) {
        return listCitas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = layoutInflater.inflate(R.layout.item_horario, null);
        if(listCitas.size() == 0){
            showSnackBar(Snackbar.make(view.findViewById(android.R.id.content), "No tienes citas para hoy", Snackbar.LENGTH_LONG));
        } else {
            MaterialCardView mcvHorario = (MaterialCardView) view.findViewById(R.id.mcvHorario);
            ImageView imvHorarioIcon = (ImageView) view.findViewById(R.id.imvMotivoIcon);
            TextView txvMedico = (TextView) view.findViewById(R.id.txvMotivo);
            TextView txvServicio = (TextView) view.findViewById(R.id.txvServicio);
            TextView txvFecha = (TextView) view.findViewById(R.id.txvFecha);
            TextView txvHora = (TextView) view.findViewById(R.id.txvHora);
            final Horario horario = listCitas.get(position);
            imvHorarioIcon.setImageResource(R.drawable.ic_local_pharmacy_white_24dp);
            txvMedico.setText(horario.getMedico().getNombre());
            txvServicio.setText(horario.getServicio().getNombre());
            txvFecha.setText(horario.getFecha());
            txvHora.setText(horario.getHora());
        }
        return view;
    }
}
