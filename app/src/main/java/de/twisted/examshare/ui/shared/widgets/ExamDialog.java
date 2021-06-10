package de.twisted.examshare.ui.shared.widgets;

import android.content.DialogInterface.OnShowListener;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;

public class ExamDialog {

    private final AlertDialog dialog;

    public ExamDialog(AlertDialog.Builder builder) {
        this.dialog = builder.create();
    }

    public void setOnShowListener(OnShowListener listener) {
        this.dialog.setOnShowListener(listener);
    }

    public Button getButton(int whichButton) {
        return this.dialog.getButton(whichButton);
    }

    public void switchButtons(boolean enabled) {
        Button buttonPos = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button buttonNeg = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        buttonPos.setEnabled(enabled);
        buttonNeg.setEnabled(enabled);
        dialog.setCancelable(enabled);
    }

    public String getCheckedItem() {
        ListView listView = dialog.getListView();
        return listView.getAdapter().getItem(listView.getCheckedItemPosition()).toString();
    }

    public void dismiss() {
        this.dialog.dismiss();
    }

    public void show() {
        this.dialog.show();
    }

    public boolean isShowing() {
        return this.dialog.isShowing();
    }

}
