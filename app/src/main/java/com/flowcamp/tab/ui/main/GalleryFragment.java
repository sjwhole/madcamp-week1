package com.flowcamp.tab.ui.main;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);


        // 스크린 사이즈 구하기
        int SCREEN_WIDTH = getContext().getResources().getDisplayMetrics().widthPixels;

        // load all images from gallery
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


        TableLayout tableLayout = rootView.findViewById(R.id.tableLayout);

        int COLUMN_SIZE = 3;
        int i;
        TableRow row = null;
//        for (i = 0; i < listOfAllImages.size(); i++) {
        for (i = 0; i < 20; i++) {
            if (i % COLUMN_SIZE == 0) {
                row = new TableRow(getActivity());
//                row.setBackgroundColor(Color.parseColor("#ffff0000")); // for test
            }

            ImageView iv = makeImageView(imageList.get(i), COLUMN_SIZE);
            row.addView(iv);
            if (i % COLUMN_SIZE == COLUMN_SIZE-1) {
                // TableRow를 TableRayout에 추가
                tableLayout.addView(row);
            }
        }

//        if (i % COLUMN_SIZE != 3) {
//            tableLayout.addView(row);
//        }

        return rootView;
    }

    private ImageView makeImageView(String imagePath, int columnSize) {
        int SCREEN_WIDTH = getContext().getResources().getDisplayMetrics().widthPixels;
        int MARGIN = SCREEN_WIDTH / 100;
        int IMAGE_WIDTH = (SCREEN_WIDTH - 6*MARGIN) / columnSize;
        int IMAGE_HEIGHT = IMAGE_WIDTH;

        ImageView iv = new ImageView(getActivity());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP); // 크기에 맞게 자르는 type


        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        Log.i(null, bitmap.getWidth() + " " + bitmap.getHeight());
        // bitmap resize
        // TODO: Maintain picture's ratio
        // ERROR: if IMAGE_WIDTH > bitmap.getWidth() -> ERROR!!!
        /*if (IMAGE_WIDTH > bitmap.getWidth()) {
            Log.i(null, "width!");
        }
        else if (IMAGE_HEIGHT > bitmap.getHeight()) {
            Log.i(null, "height!");
        }
        else*/
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
//        iv.setBackgroundColor(Color.parseColor("#ff00B700")); // for test
        iv.setLayoutParams(new TableRow.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT));

        TableRow.LayoutParams lp = (TableRow.LayoutParams) iv.getLayoutParams();
        lp.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);

        // image 기능
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return iv;
    }
}