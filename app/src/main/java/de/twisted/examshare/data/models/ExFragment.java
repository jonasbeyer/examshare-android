package de.twisted.examshare.data.models;

import android.view.View;

public class ExFragment {

    private View itemView;
    private boolean initialized;

    public ExFragment(View view) {
        this.itemView = view;
    }

    public View getItemView() {
        return itemView;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void setItemView(View itemView) {
        this.itemView = itemView;
    }
}
