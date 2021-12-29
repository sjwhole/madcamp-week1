package com.flowcamp.tab.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.flowcamp.tab.Phone;
import com.flowcamp.tab.PhoneListViewAdapter;
import com.flowcamp.tab.R;
import com.flowcamp.tab.databinding.FragmentPhoneBinding;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhoneFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentPhoneBinding binding;

    public static PhoneFragment newInstance(int index) {
        PhoneFragment fragment = new PhoneFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_phone, container, false);

        ArrayList<Phone> phoneArrayList = new ArrayList<>();
        phoneArrayList.add(new Phone(1, "성준", "01012344312"));
        phoneArrayList.add(new Phone(2, "고퍼", "01098519417"));
        phoneArrayList.add(new Phone(3, "듀크", "01019712412"));
        phoneArrayList.add(new Phone(4, "모비", "01084329742"));
        phoneArrayList.add(new Phone(5, "뎃", "01012302934"));

        PhoneListViewAdapter adapter = new PhoneListViewAdapter(phoneArrayList);


        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}