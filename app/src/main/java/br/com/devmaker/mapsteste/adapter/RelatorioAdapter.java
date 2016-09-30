package br.com.devmaker.mapsteste.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.devmaker.mapsteste.R;
import br.com.devmaker.mapsteste.model.Street;


/**
 * Created by Dev_Maker on 27/04/2016.
 */
public class RelatorioAdapter extends BaseAdapter{

    private ArrayList<Street> objects;
    private Context context;

    public RelatorioAdapter(Context context, ArrayList<Street> objects) {
        this.objects = objects;
        this.context = context;
    }




    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        ViewHolder vh;
        if (v == null) {
            vh = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.adapter_relatorio, null);

            vh.nome = (TextView) v.findViewById(R.id.textNome);

            v.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        Street street = new Street();
        street = objects.get(position);
        vh.nome.setText(street.getStreet());




        // the view must be returned to our activity
        return v;

    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Street getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        public TextView nome;
    }

}
