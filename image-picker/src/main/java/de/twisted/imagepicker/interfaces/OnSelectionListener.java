package de.twisted.imagepicker.interfaces;

import android.view.View;

import de.twisted.imagepicker.modals.Img;

/**
 * Created by akshay on 07/05/18.
 */


public interface OnSelectionListener {
    void OnClick(Img Img, View view, int position);
}
