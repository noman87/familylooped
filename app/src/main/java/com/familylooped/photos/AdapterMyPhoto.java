package com.familylooped.photos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.familylooped.R;
import com.familylooped.common.AppController;
import com.familylooped.common.fragments.DialogClickListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Noman on 4/26/2015.
 */
public class AdapterMyPhoto extends ArrayAdapter<ModelMyPhoto> {
    Activity activity;
    ArrayList<ModelMyPhoto> list;
    ImageLoader mImageLoader;
    MyPhotos myPhotos;
    private int items;

    public AdapterMyPhoto(Activity activity, ArrayList<ModelMyPhoto> list, MyPhotos myPhotos) {
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
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        //Log.e("Path", "is " + list.get(position).getImage());
        Picasso.with(activity).load(Uri.parse(list.get(position).getImage())).resizeDimen(R.dimen.photo_width, R.dimen.photo_height).skipMemoryCache().centerInside().into(viewHolder.imageView);


        /*viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //showImage(Uri.fromFile(file));
                //if (list.get(position).isCheck())
                //  list.get(position).setCheck(false);
                //else
                //list.get(position).setCheck(true);
                //notifyDataSetChanged();


                /*if (finalViewHolder.checkBox.getVisibility() == View.GONE) {
                    finalViewHolder.checkBox.setVisibility(View.VISIBLE);
                    finalViewHolder.checkBox.setChecked(true);
                } else {
                    finalViewHolder.checkBox.setVisibility(View.GONE);
                    finalViewHolder.checkBox.setChecked(false);
                }
            }
        });*/

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(position).setCheck(isChecked);
                if (isChecked)
                    finalViewHolder.imageView.setAlpha(0.5f);
                else

                    finalViewHolder.imageView.setAlpha(1f);
            }
        });
        if (list.get(position).check) {
            list.get(position).setShow(true);
            viewHolder.checkBox.setChecked(true);
            //viewHolder.imageView.setAlpha(0.5f);
        } else {
            viewHolder.checkBox.setChecked(false);
            list.get(position).setShow(false);
            // viewHolder.imageView.setAlpha(1f);
        }
        if (list.get(position).isShow()) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
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


    public void showImage(Uri uri) {
        Dialog builder = new Dialog(activity);
        builder.requestWindowFeature(activity.getWindow().FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView view = (ImageView) activity.getLayoutInflater().inflate(R.layout.item_single_image, null);
        view.setImageURI(uri);
        builder.setContentView(view);
        builder.show();
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
                                            File file = new File(list.get(position).getImage());
                                            boolean deleted = file.delete();
                                            list.remove(position);
                                            notifyDataSetChanged();

                                        }
                                    });

                                }
                            }
                        }

                ).show();
    }

    class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
    }
}
