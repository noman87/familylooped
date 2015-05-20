package com.familylooped.auth.invitePeople;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import com.familylooped.R;
import com.familylooped.auth.ModelInvitePeople;

import java.util.ArrayList;

/**
 * Created by Noman on 5/20/2015.
 */
public class AdapterContactManually extends ArrayAdapter<ModelManuallyContact> {
    Activity activity;
    ArrayList<ModelManuallyContact> list;


    public AdapterContactManually(Activity activity, ArrayList<ModelManuallyContact> list) {
        super(activity, R.layout.item_manually_add_contact, list);
        this.activity = activity;
        this.list = list;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_manually_add_contact, null);
            viewHolder = new ViewHolder();
            viewHolder.txtName = (EditText)convertView.findViewById(R.id.txt_first_name);
            viewHolder.txtLastName = (EditText)convertView.findViewById(R.id.txt_last_name);
            viewHolder.txtEmail = (EditText)convertView.findViewById(R.id.txt_email);
            viewHolder.btnDelete = (ImageButton)convertView.findViewById(R.id.btn_delete);


            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });


        return convertView;

    }

    private void deleteItem(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    class ViewHolder {
        EditText txtName, txtLastName, txtEmail;
        ImageButton btnDelete;
    }
}
