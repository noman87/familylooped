package com.familylooped.auth;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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
    ArrayList<ModelInvitePeople> list, checkedList;
    boolean mIsUpdate;


    public AdapterInvitePeople(Activity activity, ArrayList<ModelInvitePeople> list, boolean isUpdate) {
        super(activity, R.layout.item_invite, list);
        this.activity = activity;
        this.list = list;
        mIsUpdate = isUpdate;
        checkedList = new ArrayList<>();

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
            viewHolder.btn_delete = (ImageButton) convertView.findViewById(R.id.btn_delete);
            viewHolder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.txt_last_name = (TextView) convertView.findViewById(R.id.txt_last_name);
            viewHolder.email = (TextView) convertView.findViewById(R.id.txt_email);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        final ViewHolder finalViewHolder = viewHolder;
        String name[] = list.get(position).getName().split(" ");
        if (name.length > 1) {
            viewHolder.txt_name.setText(name[0]);
            viewHolder.txt_last_name.setText(name[1]);
        } else {
            viewHolder.txt_name.setText(list.get(position).getName());
        }
        viewHolder.email.setText(list.get(position).getEmail());
        if (mIsUpdate) {
            finalViewHolder.btn_delete.setVisibility(View.VISIBLE);
        }
        finalViewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
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

    public ArrayList<ModelInvitePeople> getCheckedList() {
        return checkedList;
    }

    class ViewHolder {
        CheckBox checkBox;
        TextView txt_name, email, txt_last_name;
        ImageButton btn_delete;
    }
}
