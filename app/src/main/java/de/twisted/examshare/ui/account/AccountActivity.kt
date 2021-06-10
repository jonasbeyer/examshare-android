package de.twisted.examshare.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import de.twisted.examshare.R
import de.twisted.examshare.databinding.ActivityAccountBinding
import de.twisted.examshare.databinding.ActivityMyAccountBinding
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.ExamActivity
import javax.inject.Inject

class AccountActivity : ExamActivity() { // private ImageView profileImage;
// private RestAuthDataSource authManager;
// private ProgressBar progressBar;
// private RatingBar ratingAverage;
// private User userProfile;
// private TextView username, email, tasksCount, accountRole;
//

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<AccountViewModel>

    private val viewModel: AccountViewModel by viewModels { viewModelFactory }

    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getStringExtra(EXTRA_USER_ID)

        if (userId != null) {
            val binding = ActivityAccountBinding.inflate(layoutInflater)

            setContentView(binding.root)
            supportActionBar?.setTitle(R.string.show_user_profile)
        } else {
            val binding = ActivityMyAccountBinding.inflate(layoutInflater)
            setSupportActionBar(binding.toolbar)
            setContentView(binding.root)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (userId == null) {
            menuInflater.inflate(R.menu.menu_account, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_item_account_properties -> {
            openAccountPropertiesDialog()
            true
        }
        R.id.menu_item_sign_out -> {
            openSignOutDialog()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun openAccountPropertiesDialog() {
        AccountPropertiesDialogFragment().show(supportFragmentManager, DIALOG_ACCOUNT_PROPERTIES)
    }

    private fun openSignOutDialog() {

    }

// @Override
// protected void onLoad(Bundle bundle) {
//     if (bundle != null && bundle.containsKey("userId")) {
//         setTheme(R.style.Theme_ExamShare);
//         setTaskDescription();
//         setContentView(R.layout.activity_account);
//         getSupportActionBar().setTitle(R.string.show_user_profile);
//
//         this.initView();
//         this.setupTaskActionListener();
//         this.showProgressSpinner();
//         this.userProfile = new User(getUserProfile(), bundle.getInt("userId"));
//     } else {
//         setContentView(R.layout.activity_my_account);
//         setSupportActionBar(findViewById(R.id.toolbar));
//
//         this.initView();
//         this.setUpAccountActionListener();
//         this.email = (TextView) findViewById(R.id.email);
//         this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
//         this.progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), Mode.SRC_IN);
//
//         this.userProfile = getUserProfile();
//         this.setValues(userProfile);
//         this.showProgressSpinner(progressBar);
//     }
//
//     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//     this.userProfile.loadProfileImage(this, profileImage);
//     this.userProfile.loadData(false, (response) -> {
//         findViewById(R.id.progressBar).setVisibility(View.GONE);
//         if (response.isDenied()) {
//             if (!userProfile.isMyProfile()) onBackPressed();
//             return;
//         }
//         if (response.getType() == ResponseType.NOT_FOUND) {
//             findViewById(R.id.noAccess).setVisibility(View.VISIBLE);
//             return;
//         }
//
//         setValues(userProfile);
//         findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
//     });
// }
//
// private void initView() {
//     this.username = (TextView) findViewById(R.id.username);
//     this.tasksCount = (TextView) findViewById(R.id.tasks_count);
//     this.accountRole = (TextView) findViewById(R.id.account_role);
//     this.ratingAverage = (RatingBar) findViewById(R.id.rating_average);
//     this.profileImage = (ImageView) findViewById(R.id.profileImage);
//     this.authManager = ExamShareApplication.getInstance().getAuthManager();
// }
//
// private void setValues(User userProfile) {
//     this.username.setText(userProfile.getUsername());
//     this.accountRole.setText(userProfile.getRole());
//     this.ratingAverage.setRating((float) userProfile.getRatingAverage());
//     this.tasksCount.setText(String.valueOf(userProfile.getTasks()));
//     this.tasksCount.setTextColor(TextUtil.getTextColor(this, userProfile.getTasks()));
//     if (userProfile.isMyProfile()) {
//         email.setText(userProfile.getEmail());
//     } else {
//         showOptionalData();
//     }
// }
//
// private void setupTaskActionListener() {
//     findViewById(R.id.showTasks).setOnClickListener(getViewClickListener(ListMode.TASKS));
//     findViewById(R.id.showFavorites).setOnClickListener(getViewClickListener(ListMode.FAVORITES));
// }
//
// private void showOptionalData() {
//     int grade = userProfile.getIntProperty("Grade");
//     String schoolForm = userProfile.getStringProperty("SchoolForm");
//     String federalState = userProfile.getStringProperty("FederalState");
//     if (grade == -1 && schoolForm.isEmpty() && federalState.isEmpty())
//         return;
//
//     ((CardView) findViewById(R.id.extrData)).setVisibility(View.VISIBLE);
//     ((TextView) findViewById(R.id.extraDataLabel)).setVisibility(View.VISIBLE);
//     if (grade != -1) {
//         ((TextView) findViewById(R.id.grade)).setText(getString(R.string.class_string, grade));
//         ((TableRow) findViewById(R.id.gradeRow)).setVisibility(View.VISIBLE);
//     }
//     if (!schoolForm.isEmpty()) {
//         ((TextView) findViewById(R.id.schoolForm)).setText(schoolForm);
//         ((TableRow) findViewById(R.id.schoolFormRow)).setVisibility(View.VISIBLE);
//     }
//     if (!federalState.isEmpty()) {
//         ((TextView) findViewById(R.id.federalState)).setText(federalState);
//         ((TableRow) findViewById(R.id.federalStateRow)).setVisibility(View.VISIBLE);
//     }
// }
//
// private OnClickListener getViewClickListener(ListMode listMode) {
//     return v -> {
//         Intent intent = new Intent(this, TaskListActivity.class);
//         intent.putExtra("userProfile", userProfile);
//         intent.putExtra("listMode", listMode.toString());
//         startActivity(intent);
//     };
// }
//
// private void setUpAccountActionListener() {
//     profileImage.setOnClickListener(v -> ImagePicker.start(this, new ArrayList<>(), 1, 1));
//
//     TableRow changePassword = (TableRow) findViewById(R.id.changePassword);
//     changePassword.setOnClickListener(v -> this.showPasswordDialog((input, dialog) -> {
//         dialog.switchButtons(false);
//         getUserProfile().changePassword(input.get(0), input.get(1), response -> {
//             if (response.getType() == ResponseType.SUCCESS)
//                 dialog.dismiss();
//             if (!response.isDenied()) {
//                 DialogBuilder builder = new DialogBuilder(this, R.string.define_new_password, response.getMessage());
//                 builder.setPositiveButton(R.string.ok, (Runnable) null);
//                 builder.show();
//             }
//             dialog.switchButtons(true);
//         });
//     }));
//
//     TableRow changeMail = (TableRow) findViewById(R.id.changeMail);
//     changeMail.setOnClickListener(v -> Dialogs.showInputDialog(this, Dialogs.MAIL, (input, dialog) -> {
//         dialog.switchButtons(false);
//         getUserProfile().changeEmail(input, response -> {
//             if (response.getType() == ResponseType.SUCCESS)
//                 dialog.dismiss();
//             if (!response.isDenied()) {
//                 DialogBuilder builder = new DialogBuilder(this, R.string.change_email, response.getMessage());
//                 builder.setPositiveButton(R.string.ok, (Runnable) null);
//                 builder.show();
//             }
//             dialog.switchButtons(true);
//         });
//     }));
//
//     TableRow disableAccount = (TableRow) findViewById(R.id.disableAccount);
//     disableAccount.setOnClickListener(v -> Dialogs.showInputDialog(this, Dialogs.PASSWORD, (input, dialog) -> {
//         dialog.switchButtons(false);
//         getUserProfile().disable(input, response -> {
//             dialog.switchButtons(true);
//             if (response.isDenied())
//                 return;
//
//             DialogBuilder builder = new DialogBuilder(this, R.string.disable_account, response.getMessage());
//             builder.setCancelable(response.getType() != ResponseType.SUCCESS);
//             builder.setPositiveButton(R.string.ok, () -> {
//                 if (response.getType() == ResponseType.SUCCESS) {
//                     dialog.dismiss();
//                     ActivityHolder.logout(this);
//                 }
//             });
//             builder.show();
//         });
//     }));
// }
//
// private void showPasswordDialog(BiConsumer<List<String>, ExamDialog> consumer) {
//     View layout = getLayoutInflater().inflate(R.layout.password_input_dialog, null);
//     EditText oldPassword = (EditText) layout.findViewById(R.id.oldPassword);
//     TextInputLayout newPassword = (TextInputLayout) layout.findViewById(R.id.newPassword);
//     EditText repeatPassword = (EditText) layout.findViewById(R.id.newPasswordRepeat);
//
//     DialogBuilder builder = new DialogBuilder(this, R.string.define_new_password, layout);
//     builder.setNegativeButton(R.string.cancel, (Runnable) null);
//     builder.setPositiveButton(R.string.ok, false, dialog -> {
//         List<String> passwords = Arrays.asList(oldPassword.getText().toString(), repeatPassword.getText().toString());
//         consumer.accept(passwords, dialog);
//     });
//
//     ExamDialog dialog = builder.create();
//     oldPassword.addTextChangedListener(getPasswordListener(dialog, oldPassword, newPassword, repeatPassword));
//     newPassword.getEditText().addTextChangedListener(getPasswordListener(dialog, oldPassword, newPassword, repeatPassword));
//     repeatPassword.addTextChangedListener(getPasswordListener(dialog, oldPassword, newPassword, repeatPassword));
//     dialog.show();
// }
//
// private void showSignOutDialog() {
//     DialogBuilder builder = new DialogBuilder(this, R.string.logout, R.string.confirm_logout);
//     builder.setNegativeButton(R.string.cancel, (Runnable) null);
//     builder.setPositiveButton(R.string.logout, () -> {
//         showProgressDialog();
//         authManager.logout((success) -> {
//             getProgressDialog().dismiss();
//             if (success)
//                 ActivityHolder.logout(this);
//         });
//     });
//     builder.show();
// }
//
// private TextWatcher getPasswordListener(ExamDialog dialog, EditText e1, TextInputLayout textInput, EditText e3) {
//     return TextChangedListener.get((text) -> {
//         String password = textInput.getEditText().getText().toString();
//         Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//         button.setEnabled(ValidationUtil.arePasswordsValid(e1.getText(), password) && ValidationUtil.areEqual(password, e3.getText()));
//
//         boolean error = !ValidationUtil.isPasswordValid(password);
//         if (textInput.hasFocus()) {
//             textInput.setErrorEnabled(error);
//             textInput.setError(error ? getString(R.string.password_restriction) : null);
//         }
//     });
// }
//
// @Override
// public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//     if (requestCode != PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
//         return;
//
//     for (int i = 0; i < permissions.length; i++)
//         if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
//             return;
//
//     ImagePicker.start(this, new ArrayList<>(), 1, 1);
// }
//
// @Override
// protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//     if (resultCode != Activity.RESULT_OK || requestCode != 1)
//         return;
//     LinkedHashMap paths = new Gson().fromJson(data.getStringExtra(ImagePicker.IMAGE_RESULTS), LinkedHashMap.class);
//     if (paths.isEmpty())
//         return;
//
//     String path = (String) paths.values().iterator().next();
//     progressBar.setVisibility(View.VISIBLE);
//     userProfile.uploadProfileImage(new File(path), (response) -> {
//         progressBar.setVisibility(View.GONE);
//         if (response.getType() != ResponseType.SUCCESS) {
//             new File(path).delete();
//             return;
//         }
//         Glide.get(this).clearMemory();
//         Glide.with(this)
//                 .load(path)
//                 .apply(RequestOptions
//                         .circleCropTransform()
//                         .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
//                 .listener(new RequestListener<Drawable>() {
//                     @Override
//                     public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                         return false;
//                     }
//
//                     @Override
//                     public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                         new File(path).delete();
//                         return false;
//                     }
//                 })
//                 .transition(DrawableTransitionOptions.withCrossFade())
//                 .into(profileImage);
//     });
// }

    companion object {
        private const val EXTRA_USER_ID = "userId"

        private const val DIALOG_ACCOUNT_PROPERTIES = "dialog_account_properties"

        fun newIntent(context: Context, userId: String? = null): Intent {
            return Intent(context, AccountActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
            }
        }
    }
}