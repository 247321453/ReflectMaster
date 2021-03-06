package com.jamiexu.app.reflectmaster.j.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import com.jamiexu.app.reflectmaster.j.MasterUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FieldAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private Field[] fields;

    private Field[] rawFields;

    private Object obj;

    public void setFields(Field[] fields) {
        this.fields = fields;
        rawFields = fields;
    }

    public FieldAdapter(Context context, Field[] fields, Object obj) {
        this.context = context;
        this.fields = fields;
        this.obj = obj;
        rawFields = fields;
    }

    public FieldAdapter(Context context, Field[] fields) {
        this.context = context;
        this.fields = fields;
    }

    @Override
    public int getCount() {

        return fields.length;
    }

    @Override
    public Object getItem(int p1) {

        return fields[p1];
    }

    @Override
    public long getItemId(int p1) {

        return fields[p1].hashCode();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int p1, View p2, ViewGroup p3) {
        LinearLayout layout = new LinearLayout(context);

        layout.setOrientation(LinearLayout.VERTICAL);
        TextView name = new TextView(context);
        TextView type = new TextView(context);
        TextView value = new TextView(context);

        name.setText("Name：" + fields[p1].getName());

        type.setText("Type：" + fields[p1].getType().getCanonicalName());
        if (!fields[p1].isAccessible()) fields[p1].setAccessible(true);
        try {
            value.setText("Value：" + MasterUtils.getObjectString(fields[p1].get(obj)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        name.setTextColor(Color.GREEN);
        type.setTextColor(Color.WHITE);
        value.setTextColor(Color.WHITE);
        layout.addView(name);
        layout.addView(type);
        layout.addView(value);
        return layout;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults results = new FilterResults();
                List<Field> temp = new ArrayList<>();
                for (Field field : rawFields) {
                    if (field.getName().toUpperCase().contains(charSequence.toString().toUpperCase()))
                        temp.add(field);


                }
                results.values = temp.toArray(new Field[0]);
                results.count = temp.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                fields = (Field[]) filterResults.values;
                if (filterResults.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
