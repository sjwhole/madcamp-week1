package com.flowcamp.tab;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PhoneListViewAdapter extends BaseAdapter {

    final ArrayList<Phone> listPhone;

    public PhoneListViewAdapter(ArrayList<Phone> listPhone) {
        this.listPhone = listPhone;
    }

    @Override
    public int getCount() {
        return listPhone.size();
    }

    @Override
    public Object getItem(int position) {
        return listPhone.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listPhone.get(position).phoneId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewPhone;

        if (convertView == null) {
            viewPhone = View.inflate(parent.getContext(), R.layout.phone_view, null);
        } else viewPhone = convertView;

        Phone phone = (Phone) getItem(position);
        ((TextView) viewPhone.findViewById(R.id.namepn)).setText(String.format("%s", phone.name));
        ((TextView) viewPhone.findViewById(R.id.numpn)).setText(String.format("%s", phone.num));

        return viewPhone;
    }
}
