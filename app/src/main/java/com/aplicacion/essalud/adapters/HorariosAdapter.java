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
import com.aplicacion.essalud.models.database.LocalDB;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Random;

import static com.aplicacion.essalud.methods.Methods.encrypt;

public class HorariosAdapter extends BaseAdapter {

    private Context context;
    private List<Horario> listHorarios;
    private final LayoutInflater layoutInflater;
    private SharedPreferences PREFERENCES;

    public HorariosAdapter(Context context, List<Horario> listHorarios) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listHorarios = listHorarios;
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
        @SuppressLint("ViewHolder")
        View view = layoutInflater.inflate(R.layout.item_horario, null);
        MaterialCardView mcvHorario = (MaterialCardView) view.findViewById(R.id.mcvHorario);
        ImageView imvHorarioIcon = (ImageView) view.findViewById(R.id.imvMotivoIcon);
        TextView txvMedico = (TextView) view.findViewById(R.id.txvMotivo);
        TextView txvServicio = (TextView) view.findViewById(R.id.txvServicio);
        TextView txvFecha = (TextView) view.findViewById(R.id.txvFecha);
        TextView txvHora = (TextView) view.findViewById(R.id.txvHora);
        final Horario horario = listHorarios.get(position);
        imvHorarioIcon.setImageResource(horario.getMedico().getFotoPerfil());
        txvMedico.setText(horario.getMedico().getNombre());
        txvServicio.setText(horario.getServicio().getNombre());
        txvFecha.setText(horario.getFecha());
        txvHora.setText(horario.getHora());
        mcvHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MotivosActivity.class);
                i.putExtra("fecha", horario.getFecha());
                i.putExtra("hora", horario.getHora());
                String SalonLetter = ((new Random().nextInt((2 - 1) + 1) + 1) == 1)? "A" : "B";
                int SalonNumber = new Random().nextInt((12 - 1) + 1) + 1;
                i.putExtra("consultorio", SalonLetter + "-" + SalonNumber);
                i.putExtra("medico_id", horario.getMedico().getId());
                i.putExtra("medico_name", horario.getMedico().getNombre());
                i.putExtra("servicio_id", horario.getServicio().getId());
                i.putExtra("servicio_name", horario.getServicio().getNombre());
                i.putExtra("hospital_address", horario.getHospital().getDireccion());
                i.putExtra("hospital_id", horario.getHospital().getId());
                context.startActivity(i);
            }
        });
        return view;
    }
}
