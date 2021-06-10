package de.twisted.examshare.util.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.core.util.Consumer;

public class TextChangedListener {

    public static TextWatcher get(Consumer<CharSequence> consumer) {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                consumer.accept(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }
}
