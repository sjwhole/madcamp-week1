package com.flowcamp.tab.ui.main;

import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.flowcamp.tab.R;
import com.flowcamp.tab.databinding.FragmentPhoneBinding;

import java.util.LinkedList;
import java.util.Queue;

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

        // 스크린 사이즈 구하기
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;

        // 사진 데이터를 queue에 넣기
        Queue<Image> queue = new LinkedList<>();


        // column 일단 4로 고정
        // row 몇 개 필요한지 계산
        int rows = 3;

        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        TableLayout tableLayout = rootView.findViewById(R.id.tableLayout);

        int index_image = 0;
        for (int i = 0; i < rows; i++) {
            TableRow row = new TableRow(getActivity());
            row.setBackgroundColor(Color.parseColor("#ffff0000")); // for test

            //
            ImageView iv1 = makeImageView(R.drawable.image1, screenWidth/4, screenWidth/4);
            row.addView(iv1);

            ImageView iv2 = makeImageView(R.drawable.image2, screenWidth/4, screenWidth/4);
            row.addView(iv2);

            ImageView iv3 = makeImageView(R.drawable.image3, screenWidth/4, screenWidth/4);
            row.addView(iv3);

            ImageView iv4 = makeImageView(R.drawable.image4, screenWidth/4, screenWidth/4);
            row.addView(iv4);


            // TableRow를 TableRayout에 추가
            tableLayout.addView(row);
        }

        return rootView;
    }

    private ImageView makeImageView(int resId, int width, int height) {
        ImageView iv = new ImageView(getActivity());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP); // 크기에 맞게 자르는 타입
        iv.setImageResource(resId);

        // image 디자인
        iv.setBackgroundColor(Color.parseColor("#ff00B700")); // for test
        iv.setLayoutParams(new TableRow.LayoutParams(width-20, height-20));

        TableRow.LayoutParams lp = (TableRow.LayoutParams) iv.getLayoutParams();
        lp.setMargins(20, 20, 20, 20);

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