package com.aplicacion.essalud.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.aplicacion.essalud.MotivosActivity;
import com.aplicacion.essalud.R;
import com.aplicacion.essalud.models.Horario;
import com.aplicacion.essalud.models.Medico;
import com.aplicacion.essalud.models.Servicio;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HorariosAdapter extends BaseAdapter {

    Context context;
    List<Horario> listHorarios;
    private final LayoutInflater layoutInflater;

    public HorariosAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listHorarios = new ArrayList<>();
        this.listHorarios.add(new Horario(R.drawable.ic_date_range_white_24dp, new Medico("Nombre del medico"), new Servicio("Nombre del servicio"), "04 abril 2019", "10:05AM"));
        this.listHorarios.add(new Horario(R.drawable.ic_date_range_white_24dp, new Medico("Nombre del medico"), new Servicio("Nombre del servicio"), "05 abril 2019", "10:05AM"));
        this.listHorarios.add(new Horario(R.drawable.ic_date_range_white_24dp, new Medico("Nombre del medico"), new Servicio("Nombre del servicio"), "06 abril 2019", "10:05AM"));
        this.listHorarios.add(new Horario(R.drawable.ic_date_range_white_24dp, new Medico("Nombre del medico"), new Servicio("Nombre del servicio"), "07 abril 2019", "10:05AM"));
        this.listHorarios.add(new Horario(R.drawable.ic_date_range_white_24dp, new Medico("Nombre del medico"), new Servicio("Nombre del servicio"), "08 abril 2019", "10:05AM"));
        this.listHorarios.add(new Horario(R.drawable.ic_date_range_white_24dp, new Medico("Nombre del medico"), new Servicio("Nombre del servicio"), "09 abril 2019", "10:05AM"));
        this.listHorarios.add(new Horario(R.drawable.ic_date_range_white_24dp, new Medico("Nombre del medico"), new Servicio("Nombre del servicio"), "10 abril 2019", "10:05AM"));
        this.listHorarios.add(new Horario(R.drawable.ic_date_range_white_24dp, new Medico("Nombre del medico"), new Servicio("Nombre del servicio"), "11 abril 2019", "10:05AM"));
        this.listHorarios.add(new Horario(R.drawable.ic_date_range_white_24dp, new Medico("Nombre del medico"), new Servicio("Nombre del servicio"), "12 abril 2019", "10:05AM"));
    }

    @Override
    public int getCount() {
        return listHorarios.size();
    }

    @Override
    public Object getItem(int position) {
        return listHorarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(R.layout.item_horario, null);
        MaterialCardView mcvHorario = (MaterialCardView) view.findViewById(R.id.mcvHorario);
        ImageView imvHorarioIcon = (ImageView) view.findViewById(R.id.imvHorarioIcon);
        TextView txvMedico = (TextView) view.findViewById(R.id.txvMedico);
        TextView txvServicio = (TextView) view.findViewById(R.id.txvServicio);
        TextView txvFecha = (TextView) view.findViewById(R.id.txvFecha);
        TextView txvHora = (TextView) view.findViewById(R.id.txvHora);
        Horario horario = listHorarios.get(position);
        imvHorarioIcon.setImageResource(horario.getSrcIcon());
        txvMedico.setText(horario.getMedico().getNombre());
        txvServicio.setText(horario.getServicio().getNombre());
        txvFecha.setText(horario.getFecha());
        txvHora.setText(horario.getHora());
        mcvHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MotivosActivity.class));
            }
        });
        return view;
    }
}
