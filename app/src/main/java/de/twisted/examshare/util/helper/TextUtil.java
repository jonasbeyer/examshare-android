package de.twisted.examshare.util.helper;

import android.app.Activity;
import android.content.Context;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import de.twisted.examshare.R;
import de.twisted.examshare.util.listeners.TextChangedListener;

public class TextUtil {

    private static Toast toast;

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.toString().trim().isEmpty();
    }

    public static boolean isEmpty(EditText editText) {
        return isEmpty(editText, true);
    }

    public static boolean isEmpty(EditText editText, boolean trim) {
        if (trim)
            trimEditText(editText);
        if (editText.length() == 0) {
            editText.requestFocus();
            // editText.setBackgroundResource(R.drawable.form_edittext_error);
            return true;
        }
        return false;
    }

    // public static TextWatcher getNotEmptyListener(EditText editText) {
    //     return TextChangedListener.get(text -> {
    //         if (!TextUtil.isEmpty(text))
    //             editText.setBackgroundResource(R.drawable.form_edittext);
    //     });
    // }

    public static String getGenitive(String name) {
        if (name.toLowerCase().endsWith("s") || name.toLowerCase().endsWith("x") || name.toLowerCase().endsWith("z"))
            return name + "`";

        return name + "s";
    }

    public static String lowercaseFirstLetter(String input) {
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    public static int getTextColor(Context context, int count) {
        if (count < 3) return context.getResources().getColor(R.color.colorError);
        if (count < 5) return context.getResources().getColor(R.color.orange);

        return context.getResources().getColor(R.color.colorSuccess);
    }

    public static String replaceChars(String input) {
        return input
                .replaceAll("ü", "ue")
                .replaceAll("ö", "oe")
                .replaceAll("ä", "ae")
                .replaceAll("ß", "ss")
                .replaceAll("Ü", "Ue")
                .replaceAll("Ö", "Oe")
                .replaceAll("Ä", "Ae")
                .replaceAll("/", "%slash");
    }

    public static void trimEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        if (editText instanceof AutoCompleteTextView) {
            ((AutoCompleteTextView) editText).setText(text, false);
            return;
        }
        editText.setText(text);
    }

    public static void snackbar(Activity activity, int resId) {
        snackbar(activity.findViewById(android.R.id.content), activity.getString(resId));
    }

    public static void snackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void toast(Context context, int resId) {
        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_SHORT);
        toast.show();
    }
}
