package com.pro0inter.heydoc.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.pro0inter.heydoc.R;
import com.pro0inter.heydoc.api.DTOs.PatientDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by redayoub on 5/18/19.
 */

public class PatientAdapter extends ArrayAdapter<PatientDTO> {
    private static final String BIRTH_DATE_FORMAT = "dd/MM/yyyy";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BIRTH_DATE_FORMAT);
    private LayoutInflater mLayoutInflater;
    private Set<PatientDTO> notFiltredSet;


    public PatientAdapter(@NonNull Context context, int resource) {
        super(context, resource);

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                initNotFilterdSet();
                FilterResults results = new FilterResults();
                ArrayList<PatientDTO> filtredResult = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {

                    results.values = notFiltredSet;
                    results.count = notFiltredSet.size();
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < getCount(); i++) {
                        PatientDTO dto = getItem(i);
                        if (dto.getFirstName() != null && dto.getFirstName().toLowerCase().startsWith(constraint.toString()))
                            filtredResult.add(dto);
                        if (dto.getLastName() != null && dto.getLastName().toLowerCase().startsWith(constraint.toString()))
                            filtredResult.add(dto);

                    }

                    results.count = filtredResult.size();
                    results.values = filtredResult;

                }


                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((Collection<? extends PatientDTO>) results.values);

                notifyDataSetChanged();
            }
        };
    }

    private void initNotFilterdSet() {
        if (notFiltredSet == null)
            notFiltredSet = new HashSet<>();
        for (int i = 0; i < getCount(); i++) {
            PatientDTO dto = getItem(i);
            notFiltredSet.add(dto);
        }
    }

    public View getView(int pos, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        DataHandler handler;
        if (convertView == null) {

            row = mLayoutInflater.inflate(R.layout.patient_row_layout, parent, false);
            handler = new DataHandler();
            handler.firstName = row.findViewById(R.id.patient_firstName);
            handler.lastName = row.findViewById(R.id.patient_lastName);
            handler.birthDate = row.findViewById(R.id.patient_birthDate);
            handler.gender = row.findViewById(R.id.patient_gender);
            handler.bloodType = row.findViewById(R.id.patient_boodType);


            row.setTag(handler);
        } else {
            handler = (DataHandler) row.getTag();
        }

        PatientDTO dataProvider = getItem(pos);
        // empty handler
        handler.clear();

        handler.firstName.setText(dataProvider.getFirstName());
        handler.lastName.setText(dataProvider.getLastName());
        handler.birthDate.setText(simpleDateFormat.format(dataProvider.getDateOfBirth()));
        setGender(handler.gender, dataProvider.getGender());
        handler.bloodType.setText(dataProvider.getBlood_type());


        return row;
    }

    private void setGender(TextView textView, Character gender) {
        switch (gender) {
            case 'M': {
                textView.setText(getContext().getString(R.string.Male));
                textView.setTextColor(Color.BLUE);
                break;
            }
            case 'F': {
                textView.setText(getContext().getString(R.string.Female));
                textView.setTextColor(Color.parseColor("#FF00F1"));
                break;
            }
        }
    }

    static class DataHandler {
        TextView firstName;
        TextView lastName;
        TextView birthDate;
        TextView gender;
        TextView bloodType;

        public void clear() {
            firstName.setText("");
            lastName.setText("");
            birthDate.setText("");
            gender.setText("");
            bloodType.setText("");
        }
    }

}
