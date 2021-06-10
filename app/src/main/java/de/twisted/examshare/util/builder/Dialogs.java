package de.twisted.examshare.util.builder;

import android.app.Activity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.util.Consumer;

import com.google.android.gms.common.util.BiConsumer;

import de.twisted.examshare.R;
import de.twisted.examshare.ui.LauncherActivity;
import de.twisted.examshare.util.helper.TextUtil;
import de.twisted.examshare.util.helper.ValidationUtil;
import de.twisted.examshare.ui.shared.base.ExamActivity;
import de.twisted.examshare.util.interfaces.ExamModel;
import de.twisted.examshare.data.models.ExResponse;
import de.twisted.examshare.data.models.Task;
import de.twisted.examshare.data.models.User;
import de.twisted.examshare.ui.shared.widgets.ExamDialog;
import okhttp3.Request;

public class Dialogs {

    public static final int MAIL = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
    public static final int PASSWORD = InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;

    public static void confirmDelete(Activity activity, int message, Runnable runnable) {
        // new DialogBuilder(activity, R.string.delete, message)
        //         .setNegativeButton(R.string.no, (Runnable) null)
        //         .setPositiveButton(R.string.yes, runnable)
        //         .show();
    }

    public static void showUpdateDialog(Activity activity, String message) {
        // new DialogBuilder(activity, R.string.update_required, message)
        //         .setPreventOverlapping(true)
        //         .setDefaultButton(true)
        //         .setPositiveButton(R.string.update, dialog -> ActivityHolder.showInAppStore())
        //         .show();
    }

    public static void showEmailDialog(Activity activity, ExResponse response, CharSequence user, CharSequence password) {
        // RestAuthDataSource authManager = ExamShareApplication.getInstance().getAuthManager();
        // DialogBuilder builder = new DialogBuilder(activity, R.string.error, response.getMessage());
        // builder.setNegativeButton(R.string.cancel, null);
        // builder.setPositiveButton(R.string.send_again, () -> showInputDialog(activity, response.getString("email"), Dialogs.MAIL, (input, dialog) -> {
        //     dialog.switchButtons(false);
        //     // authManager.requestVerificationMail(user.toString(), password.toString(), input.trim(), (newResponse) -> {
        //     //     if (newResponse.getType() == ResponseType.SUCCESS) {
        //     //         dialog.dismiss();
        //     //         return;
        //     //     }
        //     //     dialog.switchButtons(true);
        //     //     if (newResponse.getMessage() != null)
        //     //         new DialogBuilder(activity, newResponse.getMessage()).show();
        //     // });
        // }));
        // builder.show();
    }

    public static void showInputDialog(Activity activity, int inputType, BiConsumer<String, ExamDialog> consumer) {
        Dialogs.showInputDialog(activity, null, inputType, consumer);
    }

    public static void showInputDialog(Activity activity, String hint, int inputType, BiConsumer<String, ExamDialog> consumer) {
        // int mailTitle = hint != null ? R.string.send_email : R.string.enter_new_email;
        // int title = inputType == Dialogs.MAIL ? mailTitle : R.string.enter_password;
        //
        // View layout = activity.getLayoutInflater().inflate(R.layout.input_dialog, null);
        // EditText editText = (EditText) layout.findViewById(R.id.editText);
        // editText.setHint(hint != null ? activity.getString(R.string.default_email, hint) : null);
        //
        // DialogBuilder builder = new DialogBuilder(activity, title, layout);
        // builder.setNegativeButton(R.string.cancel, null);
        // builder.setPositiveButton(R.string.send, hint != null, dialog -> consumer.accept(editText.getText().toString(), dialog));
        //
        // ExamDialog dialog = builder.create();
        // editText.setInputType(InputType.TYPE_CLASS_TEXT | inputType);
        // editText.setTypeface(Typeface.DEFAULT);
        // editText.addTextChangedListener(TextChangedListener.get(text -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(isValid(editText, inputType))));
        // dialog.show();
    }

    private static boolean isValid(EditText editText, int inputType) {
        if (editText.getHint() != null && TextUtil.isEmpty(editText.getText()))
            return true;
        if (inputType == Dialogs.MAIL)
            return ValidationUtil.isEmailValid(editText.getText());

        return !TextUtils.isEmpty(editText.getText());
    }

    public static void showUnauthorizedDialog(Activity activity, Request request, ExResponse response) {
        if (request.tag().equals(LauncherActivity.class.getSimpleName()))
            return;

        // boolean login = !response.isMissing("login");
        // DialogBuilder builder = new DialogBuilder(activity, R.string.error, response.getMessage());
        // builder.setPreventOverlapping(true);
        // builder.setCancelable(!login);
        // builder.setPositiveButton(login ? R.string.login : R.string.ok, login ? () -> ActivityHolder.logout(activity) : null);
        // builder.show();
    }

    public static void showReportDialog(ExamActivity activity, ExamModel model, Consumer<ExResponse> consumer) {
        // ReportManager reportManager = ExamShareApplication.getInstance().getReportManager();
        // DialogBuilder builder = new DialogBuilder(activity, R.string.choose_reason, "");
        // builder.setItems(R.array.report_reasons);
        // builder.setNegativeButton(R.string.cancel, null);
        // builder.setPositiveButton(R.string.send, dialog -> {
        //     dialog.switchButtons(false);
        //     // Report report = new Report(dialog.getCheckedItem(), model);
        //     // reportManager.add(report, (response) -> {
        //     //     if (response.isDenied()) {
        //     //         dialog.switchButtons(true);
        //     //         return;
        //     //     }
        //     //     dialog.dismiss();
        //     //     consumer.accept(response);
        //     // });
        // });
        // builder.show();
    }

    public static void showUserPropertiesDialog(ExamActivity activity, User userProfile) {
        // View content = activity.getLayoutInflater().inflate(R.layout.account_properties, null);
        // Switch accountVisibility = content.findViewById(R.id.accountVisibility);
        // Switch commentNotification = content.findViewById(R.id.commentNotification);
        //
        // MaterialEditText grade = content.findViewById(R.id.grade);
        // MaterialDropDownInput schoolForm = content.findViewById(R.id.schoolForm);
        // MaterialDropDownInput federalState = content.findViewById(R.id.federalState);
        // grade.setFilters(new InputFilter[]{new InputFilterMinMax(1, 13)});
        // schoolForm.setAdapter(activity.getAutoCompleteAdapter(R.array.school_forms));
        // federalState.setAdapter(activity.getAutoCompleteAdapter(R.array.federal_states));
        //
        // accountVisibility.setChecked(userProfile.getBooleanProperty("Public"));
        // commentNotification.setChecked(userProfile.getBooleanProperty("Notifications"));
        // grade.setText(userProfile.getStringProperty("Grade"));
        // schoolForm.setText(userProfile.getStringProperty("SchoolForm"), false);
        // federalState.setText(userProfile.getStringProperty("FederalState"), false);
        //
        // DialogBuilder builder = new DialogBuilder(activity, R.string.account_properties, content);
        // builder.setNegativeButton(R.string.cancel, null);
        // builder.setPositiveButton(R.string.ok, dialog -> {
        //     dialog.switchButtons(false);
        //     Map<String, String> properties = new HashMap<>();
        //     properties.put("Public", String.valueOf(accountVisibility.isChecked()));
        //     properties.put("Notifications", String.valueOf(commentNotification.isChecked()));
        //     properties.put("Grade", grade.getText().toString().trim());
        //     properties.put("SchoolForm", schoolForm.getText().toString().trim());
        //     properties.put("FederalState", federalState.getText().toString().trim());
        //     // userProfile.updateProperties(properties, success -> {
        //     //     if (success) {
        //     //         dialog.dismiss();
        //     //         TextUtil.snackbar(activity, R.string.changed_account_properties);
        //     //     }
        //     //     dialog.switchButtons(true);
        //     // });
        // });
        // builder.show();
    }

}
