package com.familylooped.photos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.familylooped.R;
import com.familylooped.common.AppController;
import com.familylooped.common.fragments.DialogClickListener;
import com.familylooped.common.logger.Log;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Noman on 4/26/2015.
 */
public class AdapterMyPhoto extends ArrayAdapter<String> {
    Activity activity;
    ArrayList<String> list;
    ImageLoader mImageLoader;
    MyPhotos myPhotos;
    private int items;

    public AdapterMyPhoto(Activity activity, ArrayList<String> list, MyPhotos myPhotos) {
        super(activity, R.layout.item_photo, list);
        this.activity = activity;
        this.list = list;
        this.myPhotos = myPhotos;
        mImageLoader = AppController.getInstance().getImageLoader();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_photo, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            // viewHolder.btn_delete = (ImageButton) convertView.findViewById(R.id.btn_delete);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        Log.e("Path", "is " + list.get(position));
        File file = new File(list.get(position));
        Log.e("URI", Uri.fromFile(file).toString());
        viewHolder.imageView.setImageURI(Uri.fromFile(file));

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(position);
            }
        });




        /*viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPhotos.showDialog("Are you sure you want to delete this photo ?", "Yes", "No", new DialogClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        File file = new File(list.get(position));
                        boolean deleted = file.delete();
                        list.remove(position);
                        notifyDataSetChanged();

                    }
                })

            }
        });*/
        return convertView;

    }

    private void showPopUp(final int position) {
        new AlertDialog.Builder(activity)

                .setSingleChoiceItems(R.array.delete_choice, 0, null)
                .setTitle("What do you want ?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                if (selectedPosition == 1) {
                                    myPhotos.showDialog("Are you sure you want to delete this photo ?", "Yes", "No", new DialogClickListener() {
                                        @Override
                                        public void onPositiveButtonClick() {
                                            File file = new File(list.get(position));
                                            boolean deleted = file.delete();
                                            list.remove(position);
                                            notifyDataSetChanged();

                                        }
                                    });

                                }
                            }
                        }

                )
                .

                        show();
    }

    class ViewHolder {

        ImageView imageView;
        ImageButton btn_delete;
    }
}
