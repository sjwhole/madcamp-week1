package com.flowcamp.tab.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
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

public class PhoneFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentPhoneBinding binding;
    private Context context;

    public PhoneFragment(Context context) {
        this.context = context;
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

        Uri uri = CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                CommonDataKinds.Phone.DISPLAY_NAME,
                CommonDataKinds.Phone.NUMBER,
        };

        String sortOrder = CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        try {
            @SuppressLint("Recycle") Cursor cursor = context.getContentResolver().query(uri, projection, null, null, sortOrder);
            if (cursor.moveToFirst()) {
                do {
                    Phone phone = new Phone((int) cursor.getLong(0), cursor.getString(1), cursor.getString(2));
                    phoneArrayList.add(phone);

                } while (cursor.moveToNext());
            }
        } catch (SecurityException ignored) {
            ignored.printStackTrace();
            phoneArrayList.add(new Phone(1, "연락처 접근 권한을 허용해주세요", ""));
        }

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