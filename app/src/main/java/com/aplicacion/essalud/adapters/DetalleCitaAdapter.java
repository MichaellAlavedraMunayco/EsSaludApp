package com.aplicacion.essalud.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aplicacion.essalud.R;
import com.aplicacion.essalud.models.ItemCita;

import java.util.ArrayList;
import java.util.List;

public class DetalleCitaAdapter extends BaseAdapter {

    private Context context;
    private List<ItemCita> listItems = new ArrayList<ItemCita>();;
    private final LayoutInflater layoutInflater;

    public DetalleCitaAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(ItemCita item) {
        this.listItems.add(item);
        notifyDataSetChanged();
    }

    public List<ItemCita> getList() {
        return this.listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = layoutInflater.inflate(R.layout.item_detalle_cita, null);
        ImageView imvIconItemDC = view.findViewById(R.id.imvIconItemDC);
        TextView txvNombreItemDC = (TextView) view.findViewById(R.id.txvNombreItemDC);
        TextView txvDescripcionItemDC = (TextView) view.findViewById(R.id.txvDescripcionItemDC);
        ItemCita itemCita = listItems.get(position);
        imvIconItemDC.setImageResource(itemCita.getIcon());
        txvNombreItemDC.setText(itemCita.getNombre());
        txvDescripcionItemDC.setText(itemCita.getDescripcion());
        return view;
    }
}
