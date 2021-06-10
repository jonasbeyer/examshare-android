package de.twisted.examshare.util.helper;

import android.widget.EditText;
import de.twisted.examshare.R;

public class ValidationUtil {

    private final static int MIN_USERNAME_LENGTH = 4;
    private final static int MIN_PASSWORD_LENGTH = 6;
    private final static String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static int isUsernameValid(CharSequence userSequence) {
        String username = userSequence.toString().trim();
        String userSyntax = "^[a-zA-Z0-9_]+$";

        if (username.length() < MIN_USERNAME_LENGTH)
            return 2;
        if (username.matches(userSyntax) && username.matches(".*?[a-zA-Z].*?.*?[a-zA-Z].*?"))
            return 3;
        if (!username.matches(userSyntax))
            return 0;

        return 1;
    }

    public static int isUsernameValid(EditText editText) {
       return isUsernameValid(editText.getText());
    }

    public static boolean isEmailValid(CharSequence email) {
        return email.toString().matches(EMAIL_PATTERN);
    }

    public static boolean isEmailValid(EditText email) {
        if (!isEmailValid(email.getText())) {
            email.setText(email.getText().toString().trim());
            // email.setBackgroundResource(R.drawable.form_edittext_error);
            return false;
        }
        return true;
    }

    public static boolean isPasswordValid(CharSequence password) {
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    public static boolean isPasswordValid(EditText password) {
        if (!isPasswordValid(password.getText())) {
            // password.setBackgroundResource(R.drawable.form_edittext_error);
            return false;
        }
        return true;
    }

    public static boolean arePasswordsValid(CharSequence... passwords) {
        for (CharSequence password : passwords) {
            if (!isPasswordValid(password))
                return false;
        }

        return true;
    }

    public static boolean areEqual(CharSequence password, CharSequence repeatedPassword) {
        return password.toString().equals(repeatedPassword.toString());
    }

}
