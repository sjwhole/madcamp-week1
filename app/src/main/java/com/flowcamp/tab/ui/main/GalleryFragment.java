package com.flowcamp.tab.ui.main;

import android.content.Context;
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
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.flowcamp.tab.R;
import com.flowcamp.tab.databinding.FragmentPhoneBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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

        Queue<String> imageList = loadImages(rootView);

        showImages(rootView, imageList);


        return rootView;
    }

    // load all images from gallery
    private Queue<String> loadImages(View rootView) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        Queue<String> imageList = new LinkedList<>();
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

    private void showImages(View rootView, Queue<String> imageList) {
        if (imageList.size() == 0) {
            return;
        }

        int SCREEN_WIDTH = getContext().getResources().getDisplayMetrics().widthPixels;
        int MARGIN = SCREEN_WIDTH / 100;
        int MAX_WIDTH = SCREEN_WIDTH - 2*MARGIN;

        TableLayout tableLayout = rootView.findViewById(R.id.tableLayout);
        TableRow row = makeRow(getActivity());

        while (imageList.size() > 0) {
            String currentPath = imageList.poll();
            Bitmap current = BitmapFactory.decodeFile(currentPath);
            double currentRatio = (double) current.getHeight() / current.getWidth();

            String nextPath = imageList.poll();
            Bitmap next = BitmapFactory.decodeFile(nextPath);
            if (next == null) {
                // end
                ResizeAndAddBitmap(current,
                        MAX_WIDTH,
                        (int) (current.getHeight() * ((double) MAX_WIDTH/current.getWidth())),
                        row, MARGIN, MARGIN,
                        currentPath);
                tableLayout.addView(row);
                break;
            }
            double nextRatio = (double)next.getHeight() / next.getWidth();

            if (nextRatio > (double) 16/9) {
                // end, end
                ResizeAndAddBitmap(current,
                        MAX_WIDTH,
                        (int) (current.getHeight() * ((double) MAX_WIDTH/current.getWidth())),
                        row, MARGIN, MARGIN,
                        currentPath);
                tableLayout.addView(row);

                row = makeRow(getActivity());
                ResizeAndAddBitmap(next,
                        MAX_WIDTH,
                        (int) (next.getHeight() * ((double) MAX_WIDTH/next.getWidth())),
                        row, MARGIN, MARGIN,
                        nextPath);
                tableLayout.addView(row);

                row = makeRow(getActivity());
            }
            else if (currentRatio + nextRatio > 2) {
                // add, end

                // W1 + W2 = Wm
                // H1 = H2
                // => H = (Wm * R1 * R2) / (R1 + R2)
                // => W1 = H/R1
                double height = ((MAX_WIDTH-MARGIN) * currentRatio * nextRatio) / (currentRatio + nextRatio);

                ResizeAndAddBitmap(current,
                        (int)(height / currentRatio),
                        (int)height,
                        row, MARGIN, MARGIN,
                        currentPath);

                ResizeAndAddBitmap(next,
                        (int)(height / nextRatio),
                        (int)height,
                        row, 0, MARGIN,
                        nextPath);

                tableLayout.addView(row);
                row = makeRow(getActivity());
            }
            // currentRatio + nextRatio <= 2
            else {
                // add, continue
                if (imageList.peek() == null) {
                    // end
                    double height = ((MAX_WIDTH-MARGIN) * currentRatio * nextRatio) / (currentRatio + nextRatio);

                    ResizeAndAddBitmap(current,
                            (int)(height / currentRatio),
                            (int)height,
                            row, MARGIN, MARGIN,
                            currentPath);

                    ResizeAndAddBitmap(next,
                            (int)(height / nextRatio),
                            (int)height,
                            row, 0, MARGIN,
                            nextPath);

                    tableLayout.addView(row);
                }

                String next2Path = imageList.poll();
                Bitmap next2 = BitmapFactory.decodeFile(next2Path);
                double next2Ratio = (double) next2.getHeight() / next2.getWidth();

                // next2가  너무 길면 뒤로 빼기
                if (next2Ratio > 2) {
                    ResizeAndAddBitmap(next2,
                            MAX_WIDTH,
                            (int) (next2.getHeight() * ((double) MAX_WIDTH/next2.getWidth())),
                            row, MARGIN, MARGIN,
                            next2Path);

                    tableLayout.addView(row);
                    row = makeRow(getActivity());
                }
                else {
                    // add, end
                    double height = ((MAX_WIDTH - 2*MARGIN) * currentRatio * nextRatio * next2Ratio) /
                            (currentRatio*next2Ratio + nextRatio*next2Ratio + currentRatio*nextRatio);

                    ResizeAndAddBitmap(current,
                            (int)(height / currentRatio),
                            (int)height,
                            row, MARGIN, MARGIN,
                            currentPath);

                    ResizeAndAddBitmap(next,
                            (int)(height / nextRatio),
                            (int)height,
                            row, MARGIN, MARGIN,
                            nextPath);

                    ResizeAndAddBitmap(next2,
                            (int)(height / next2Ratio),
                            (int)height,
                            row, MARGIN, MARGIN,
                            next2Path);

                    tableLayout.addView(row);
                    row = makeRow(getActivity());
                }
            }
        }
    }

    private TableRow makeRow(Context context) {
        TableRow row = new TableRow(context);
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        return row;
    }

    private void ResizeAndAddBitmap(Bitmap bitmap, int width, int height, TableRow row, int marginLeft, int marginRight, String imagePath) {
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        ImageView iv = makeImageView(bitmap, marginLeft, marginRight, imagePath);
        row.addView(iv);
    }

    private ImageView makeImageView(Bitmap bitmap, int marginLeft, int marginRight, String imagePath) {
        ImageView iv = new ImageView(getActivity());
//        iv.setScaleType(ImageView.ScaleType.CENTER_CROP); // 크기에 맞게 자르는 type

        iv.setImageBitmap(bitmap);

        // image 디자인
        iv.setLayoutParams(new TableRow.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));

        TableRow.LayoutParams lp = (TableRow.LayoutParams) iv.getLayoutParams();
        lp.setMargins(marginLeft, 0, marginRight, marginRight);

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