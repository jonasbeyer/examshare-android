// package de.twisted.examshare.ui.shared.widgets;
//
// import android.app.Dialog;
// import android.content.Context;
// import android.text.TextWatcher;
// import android.view.Gravity;
// import android.view.LayoutInflater;
// import android.view.View;
// import android.view.Window;
// import android.view.WindowManager;
// import android.view.inputmethod.InputMethodManager;
// import android.widget.EditText;
// import android.widget.ImageView;
// import android.widget.LinearLayout.LayoutParams;
// import de.twisted.examshare.R;
// import de.twisted.examshare.util.helper.TextUtil;
// import de.twisted.examshare.ui.shared.base.ExamActivity;
// import de.twisted.examshare.util.listeners.TextChangedListener;
// import de.twisted.examshare.data.models.Comment;
//
// public class CommentEditor {
//
//     private Runnable sendAction;
//
//     private Dialog dialog;
//     private ExamActivity activity;
//     private ImageView profileImage;
//     private ImageView sendButton;
//     private EditText editText;
//
//     public CommentEditor(ExamActivity activity) {
//         View view = LayoutInflater.from(activity).inflate(R.layout.fragment_add_edit_comment, null);
//         this.activity = activity;
//         this.dialog = new Dialog(activity, R.style.CommentInput);
//         this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//         this.dialog.setContentView(view);
//         this.dialog.setOnDismissListener(dialog -> hideKeyboard());
//
//         Window window = this.dialog.getWindow();
//         window.setGravity(Gravity.BOTTOM);
//         window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//         window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//
//         this.profileImage = (ImageView) view.findViewById(R.id.profileImage);
//         this.sendButton = (ImageView) view.findViewById(R.id.sendButton);
//         this.sendButton.setOnClickListener(v -> submit());
//         this.editText = (EditText) view.findViewById(R.id.commentText);
//         this.editText.addTextChangedListener(getIconWatcher());
//     }
//
//     public void showView(Comment comment) {
//         boolean edit = comment != null;
//         // int userId = edit ? comment.getAuthorId() : activity.getUserProfile().getUserId();
//         editText.setText(edit ? comment.getMessage() : "");
//         // activity.getUserProfile().loadProfileImage(activity, userId, profileImage);
//         dialog.show();
//     }
//
//     public void hideView() {
//         this.dialog.dismiss();
//     }
//
//     public String getCommentText() {
//         return editText.getText().toString().trim();
//     }
//
//     public void setSendAction(Runnable sendAction) {
//         this.sendAction = sendAction;
//     }
//
//     public void hideKeyboard() {
//         InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//         imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
//     }
//
//     private void submit() {
//         this.hideKeyboard();
//         this.sendAction.run();
//     }
//
//     private TextWatcher getIconWatcher() {
//         return TextChangedListener.get(text -> sendButton.setVisibility(TextUtil.isEmpty(text) ? View.GONE : View.VISIBLE));
//     }
// }
