package com.familylooped.auth;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.familylooped.R;
import com.familylooped.common.logger.Log;

import java.util.ArrayList;

/**
 * Created by Noman on 4/30/2015.
 */
public class AdapterInvitePeople extends ArrayAdapter<ModelInvitePeople> {
    Activity activity;
    ArrayList<ModelInvitePeople> list;


    public AdapterInvitePeople(Activity activity, ArrayList<ModelInvitePeople> list) {
        super(activity, R.layout.item_invite, list);
        this.activity = activity;
        this.list = list;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_invite, null);
            viewHolder = new ViewHolder();


            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        final ViewHolder finalViewHolder = viewHolder;
        finalViewHolder.checkBox.setText(list.get(position).getName()+"\n"+list.get(position).getEmail());

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(position).setCheck(isChecked);
            }
        });
        if (list.get(position).check) {
            viewHolder.checkBox.setChecked(true);
        } else {
            viewHolder.checkBox.setChecked(false);
        }


        return convertView;

    }

    class ViewHolder {


        CheckBox checkBox;
    }
}
