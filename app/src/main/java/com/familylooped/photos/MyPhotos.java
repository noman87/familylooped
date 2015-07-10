package com.familylooped.photos;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.familylooped.R;
import com.familylooped.common.Utilities;
import com.familylooped.common.activities.BaseActionBarActivity;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.common.fragments.DialogClickListener;
import com.familylooped.common.logger.Log;
import com.familylooped.slideShow.ActivitySlideShow;
import com.google.gson.Gson;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPhotos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPhotos extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String TAG = "My Photos";
    private static String NOTIFICATION = "notification";
    private ProgressDialog mProgressDialog;

    Gson gson = new Gson();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean is_notification;
    private GridView mGridView;
    public ArrayList<ModelMyPhoto> mList, mDownloadList;
    private AdapterMyPhoto mAdapterMyPhoto;
    private int mDownloadIndex = 0;
    private ImageChooserManager imageChooserManager;
    private int mImageCount;
    private ImageButton mBtnDeselect;
    private ImageButton btn_select;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPhotos.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPhotos newInstance(String param1, String param2) {
        MyPhotos fragment = new MyPhotos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static MyPhotos newInstance() {
        MyPhotos fragment = new MyPhotos();
        return fragment;
    }

    public static MyPhotos newInstance(boolean is_notification) {
        MyPhotos fragment = new MyPhotos();
        Bundle args = new Bundle();
        args.putBoolean(NOTIFICATION, is_notification);
        fragment.setArguments(args);
        return fragment;
    }


    public MyPhotos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            is_notification = getArguments().getBoolean(NOTIFICATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_photos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        init(view);
    }

    private void init(View view) {
        ((ImageButton) view.findViewById(R.id.btn_add_photo)).setOnClickListener(this);
        btn_select = (ImageButton) view.findViewById(R.id.btn_select);
        btn_select.setOnClickListener(this);

        ((ImageButton) view.findViewById(R.id.btn_delete)).setOnClickListener(this);

        ((ImageButton) view.findViewById(R.id.btn_back)).setOnClickListener(this);

        mBtnDeselect = (ImageButton) view.findViewById(R.id.btn_diselect);
        mBtnDeselect.setOnClickListener(this);

        // createFolder();
        initializePhotoList();
        mGridView = (GridView) view.findViewById(R.id.grid_view);
        mGridView.setOnItemClickListener(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Downloading");
        mProgressDialog.setMessage("Downloading images...");


       /* mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showDialog("Do you want to remove this photo", "Yes", "No", new DialogClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        removePhoto(position);
                    }
                });

                return false;
            }
        });*/

        showPhotos();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Call", "OnREsume");

    }

    private void initializePhotoList() {
        mList = new ArrayList<>();
        if (Utilities.getUsersPhotoJson(getActivity()) != null) {
            parseData(Utilities.getUsersPhotoJson(getActivity()));
        }
    }

    private void parseData(String json) {
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                mList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), ModelMyPhoto.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPhotos() {
        mAdapterMyPhoto = new AdapterMyPhoto(getActivity(), mList, MyPhotos.this);
        mGridView.setAdapter(mAdapterMyPhoto);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        /*inflater.inflate(R.menu.menu_my_photo, menu);
        ImageButton imageButton = (ImageButton) menu.findItem(R.id.action_play).getActionView();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeFragment(SlideShow.newInstance(), SlideShow.TAG);
                Log.e("URL", "Image " + mList.get(0).getImage());
                //downloadFile(mList.get(0).getImage());

            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_photo:
                addPhoto();
                break;
            case R.id.btn_delete:
                showDialog("Are you sure you want to delete this photo?",
                        "Yes", "No", new DialogClickListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                removePhoto();
                            }

                            @Override
                            public void onDismiss() {

                            }
                        });
                mBtnDeselect.setVisibility(View.GONE);
                break;
            case R.id.btn_select:
                String tag = btn_select.getTag().toString();
                if (TextUtils.equals(tag, "select_all")) {
                    btn_select.setTag("de_select");
                    btn_select.setBackgroundResource(R.drawable.deselect);
                    selectAll();

                } else {
                    btn_select.setTag("select_all");
                    btn_select.setBackgroundResource(R.drawable.select_all);
                    deSelectAll();
                }
                break;

            case R.id.btn_back:
                ((BaseActionBarActivity) getActivity()).popFragmentIfStackExist();
                break;
            case R.id.btn_diselect:
                deSelectAll();
                mBtnDeselect.setVisibility(View.GONE);
                break;
        }
    }

    private void deSelectAll() {
        for (ModelMyPhoto item : mList) {
            item.setCheck(false);
        }
        mAdapterMyPhoto.notifyDataSetChanged();
    }

    private void removePhoto() {
       /* for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isCheck()) {
                File file = null;
                try {
                    file = new File(new URI(mList.get(i).getImage()));
                    boolean status = file.delete();
                    Log.e("delete Status", "is " + status);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

            btn_select.setTag("select_all");
            btn_select.setBackgroundResource(R.drawable.select_all);
            updateJson();
            mAdapterMyPhoto.notifyDataSetChanged();

        }*/
        for (Iterator<ModelMyPhoto> iterator = mList.iterator(); iterator.hasNext(); ) {
            ModelMyPhoto item = iterator.next();
            if (item.isCheck()) {
                /*File file = null;
                try {
                    file = new File(new URI(item.getImage()));
                    //boolean status = file.delete();
                    //Log.e("delete Status", "is " + status);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }*/
                iterator.remove();
            } else {
                item.setShow(true);
            }
        }
        btn_select.setTag("select_all");
        for (ModelMyPhoto photo : mList) {
            Log.e("PATH", photo.getImage());
        }

        btn_select.setBackgroundResource(R.drawable.select_all);
        updateJson();
        mAdapterMyPhoto.notifyDataSetChanged();
    }

    private void updateJson() {
        Utilities.saveUsersPhotoJson(getActivity(), gson.toJson(mList));
    }

    private void addPhoto() {

        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE);
        imageChooserManager.setImageChooserListener(new ImageChooserListener() {
            @Override
            public void onImageChosen(final ChosenImage image) {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (image != null) {

                            copyFile(image.getFileThumbnail(), Utilities.getPhotoPath(getActivity()));

                        }
                    }
                });
            }

            @Override
            public void onError(String s) {

            }
        });
        try {
            imageChooserManager.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean copyFile(String from, String to) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                int end = from.toString().lastIndexOf("/");
                String str1 = from.toString().substring(0, end);
                String str2 = from.toString().substring(end + 1, from.length());
                File destinationFolder = new File(to);
                if (!destinationFolder.exists()) {
                    destinationFolder.mkdirs();
                }
                File source = new File(str1, str2);
                File destination = new File(to, str2);

                if (source.exists()) {
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    addPhotoInList(destination.toURI().toString());
                    showPhotos();
                }

            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void addPhotoInList(String to) {
        String timeAndId = Utilities.getData(System.currentTimeMillis(), Utilities.DATE_FORMAT);
        mList.add(new ModelMyPhoto(timeAndId, to, "Gallery", timeAndId, "null"));
        mAdapterMyPhoto.notifyDataSetChanged();
        Utilities.saveUsersPhotoJson(getActivity(), gson.toJson(mList));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK &&
                (requestCode == ChooserType.REQUEST_PICK_PICTURE ||
                        requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            imageChooserManager.submit(requestCode, data);
        } else if (resultCode == getActivity().RESULT_OK && requestCode == 111) {
            initializePhotoList();
            showPhotos();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        /*MediaScannerConnection.scanFile(getActivity(), new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {

            public void onScanCompleted(String path, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });*/
    }

    public void selectAll() {
        for (ModelMyPhoto item : mList) {
            item.setCheck(true);
        }
        mAdapterMyPhoto.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e(TAG, "" + position);
        Bundle bundle = new Bundle();
        //bundle.putInt("position", position);
        Intent intent = new Intent(getActivity(), ActivitySlideShow.class);
        intent.putExtra("position", position);
        startActivityForResult(intent, 111);
        ///changeActivity(ActivitySlideShow.class, bundle);
    }


}
