package de.twisted.examshare.ui.shared.widgets;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;

public class ExamSpinner extends AppCompatSpinner {

    public ExamSpinner(Context context) {
        super(context);
    }

    public ExamSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExamSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(int array, TextView textView) {
        setAdapter(getResources().getStringArray(array), textView);
    }

    public void setAdapter(String[] itemArray, TextView textView) {
        setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, itemArray) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setPadding(0, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                boolean title = textView == null;
                int color = title ? getResources().getColor(android.R.color.white) : textView.getCurrentTextColor();
                text.setTextColor(color);
                text.setTextSize(title ? 20 : textView.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
                text.setTypeface(title ? Typeface.create("sans-serif-medium", Typeface.NORMAL) : null);
                return view;
            }
        });
    }
}
