package com.flowcamp.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class PhoneListViewAdapter extends BaseAdapter {

    final ArrayList<Phone> listPhone;
    private Context context;

    public PhoneListViewAdapter(Context context, ArrayList<Phone> listPhone) {
        this.context = context;
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
        ShapeableImageView siv = (ShapeableImageView) viewPhone.findViewById(R.id.pn_profile);
        if (phone.profile != null) {
            siv.setImageBitmap(phone.profile);
        } else {
            siv.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }
        ((TextView) viewPhone.findViewById(R.id.namepn)).setText(String.format("%s", phone.name));
        ((TextView) viewPhone.findViewById(R.id.numpn)).setText(String.format("%s", phone.num));
        ((ImageButton) viewPhone.findViewById(R.id.call)).setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone.getNum()));
            context.startActivity(callIntent);
        });

        return viewPhone;
    }
}
