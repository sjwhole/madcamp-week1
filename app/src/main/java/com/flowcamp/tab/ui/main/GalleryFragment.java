package com.flowcamp.tab.ui.main;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.flowcamp.tab.R;
import com.flowcamp.tab.databinding.FragmentPhoneBinding;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentPhoneBinding binding;

    private FragmentManager fragmentManager;
    private GalleryFragment_CenterZoom centerZoom;
    private FragmentTransaction transaction;

    private boolean isZooming = false;

    public GalleryFragment() {
        // Required empty public constructor
    }

    public static GalleryFragment newInstance(int index) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 2;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

        fragmentManager = getParentFragmentManager();
        centerZoom = new GalleryFragment_CenterZoom();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.gallery_childFrame, centerZoom).commit();
        transaction.hide(centerZoom);
        // hide
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);



        // 스크린 사이즈 구하기
        int SCREEN_WIDTH = getContext().getResources().getDisplayMetrics().widthPixels;

        ArrayList<String> imageList = loadImages(rootView);

        showImages(rootView, imageList);


        return rootView;
    }

    // load all images from gallery
    private ArrayList<String> loadImages(View rootView) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> imageList = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        cursor = rootView.getContext().getContentResolver().query(uri, projection, null,
                null, sortOrder);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            imageList.add(absolutePathOfImage);
        }

        return imageList;
    }

    private void showImages(View rootView, ArrayList<String> imageList) {
        TableLayout tableLayout = rootView.findViewById(R.id.tableLayout);

        int COLUMN_SIZE = 3;
        int i;
        TableRow row = null;
        for (i = 0; i < imageList.size(); i++) {
            if (i % COLUMN_SIZE == 0) {
                row = new TableRow(getActivity());
            }

            ImageView iv = makeImageView(imageList.get(i), COLUMN_SIZE);
            row.addView(iv);
            if (i % COLUMN_SIZE == COLUMN_SIZE-1) {
                // TableRow를 TableRayout에 추가
                tableLayout.addView(row);
            }
        }
    }

    private ImageView makeImageView(String imagePath, int columnSize) {
        int SCREEN_WIDTH = getContext().getResources().getDisplayMetrics().widthPixels;
        int MARGIN = SCREEN_WIDTH / 100;
        int IMAGE_WIDTH = (SCREEN_WIDTH - 2*columnSize*MARGIN) / columnSize;
        int IMAGE_HEIGHT = IMAGE_WIDTH;

        ImageView iv = new ImageView(getActivity());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP); // 크기에 맞게 자르는 type

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        // bitmap resize
        int resizeWidth, resizeHeight;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            resizeWidth = (int)(bitmap.getWidth() * ((double)IMAGE_HEIGHT/(double)bitmap.getHeight()));
            resizeHeight = IMAGE_HEIGHT;
            bitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, resizeHeight,false);
        }
        else {
            resizeWidth = IMAGE_WIDTH;
            resizeHeight = (int)(bitmap.getHeight() * ((double)IMAGE_WIDTH/bitmap.getWidth()));
            bitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, resizeHeight, false);
        }

        iv.setImageBitmap(bitmap);

        // image 디자인
        iv.setLayoutParams(new TableRow.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT));

        TableRow.LayoutParams lp = (TableRow.LayoutParams) iv.getLayoutParams();
        lp.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);

        // image 기능
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleZoom(centerZoom, imagePath);
            }
        });

        return iv;
    }

    private void toggleZoom(Fragment fragment, String imagePath) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (isZooming) {
            // hide
            ft.hide(fragment);
            isZooming = false;
            Log.i(null, "hide");
        }
        else {
            // show up
            ImageView img = getView().findViewById(R.id.zoom_image);
            img.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            ft.show(fragment);
            isZooming = true;
            Log.i(null, "show");
        }
        ft.commit();
    }
}