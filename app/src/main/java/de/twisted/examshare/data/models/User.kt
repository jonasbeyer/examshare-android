package de.twisted.examshare.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(
    val id: String,
    val username: String,
    val email: String,
    val role: UserRole,
    val notifications: Set<String>,
    val properties: Map<String, String?>,
    val tasks: Int,
    val ratingAverage: Float,
    val createdAt: Date
) : Parcelable {

    // public void loadData(boolean init, Consumer<ExResponse> consumer) {
//     if (init && sessionId.isEmpty()) {
//         consumer.accept(new ExResponse(ResponseType.UNAUTHORIZED).putData("local", true));
//         return;
//     }
//
//     Request request = httpClient.buildRequest(String.format(API_URL, "info"), Collections.singletonMap("userId", getId()));
//     httpClient.getResponse(request, init ? ErrorType.DIALOG : ErrorType.TOAST, response -> {
//         if (response.getType() != ResponseType.SUCCESS) {
//             consumer.accept(response);
//             return;
//         }
//
//         this.username = response.getString("Username");
//         this.role = response.getString("Role");
//         this.registered = response.getLong("Registered");
//         this.ratingAverage = response.getDouble("Rating");
//         this.tasks = response.getInt("Tasks");
//         this.properties = response.getMap("Properties");
//         this.setPersonalData(response, myProfile);
//         consumer.accept(response);
//     });
// }
//
// private void setPersonalData(ExResponse response, boolean myProfile) {
//     if (myProfile) {
//         this.userId = response.getInt("_id");
//         this.email = response.getString("Email");
//         this.notifications = response.getList("Notifications");
//         this.moderationAllowed = response.getBoolean("ModerationAllowed");
//     }
// }
//
//
// public void findTasks(String title, int filter, int lastId, BiConsumer<List<ExamModel>, Integer> consumer) {
//     Request request = httpClient.buildRequest(String.format(API_URL, "findTasks"), new HashMap<String, Object>() {{
//         put("title", title);
//         put("filter", filter);
//         put("lastId", lastId);
//         put("userId", myProfile ? -1 : userId);
//     }});
//
//     httpClient.getResponseSingle(request, response -> {
//         int totalCount = response.getInt("Total");
//         boolean success = response.getType() == ResponseType.SUCCESS;
//         consumer.accept(success ? response.getItems(httpClient.getGson(), Task.class) : null, totalCount);
//     });
// }
//

//
// public void updateProperties(Map<String, String> properties, Consumer<Boolean> consumer) {
//     Map<String, Object> data = new HashMap<>();
//     for (String property : properties.keySet())
//         data.put(TextUtil.lowercaseFirstLetter(property), properties.get(property));
//
//     Request request = httpClient.buildRequest(String.format(API_URL, "updateProperties"), data);
//     httpClient.getResponse(request, response -> {
//         if (response.getType() == ResponseType.SUCCESS)
//             this.properties = properties;
//
//         consumer.accept(response.getType() == ResponseType.SUCCESS);
//     });
// }
//
// public void uploadProfileImage(File file, Consumer<ExResponse> consumer) {
//     Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//     String contentType = ImageLibrary.getMimeType(file);
//     if (contentType != null)
//         builder.addFormDataPart("profileImage", file.getName(), RequestBody.create(MediaType.parse(contentType), file));
//
//     Request request = httpClient.buildRequest(String.format(API_URL, "setProfileImage"), builder.build(), true);
//     httpClient.getResponse(request, consumer);
// }
//
// public void loadProfileImage(Context context, ImageView imageView) {
//     this.loadProfileImage(context, userId, imageView);
// }
//
// public void loadProfileImage(Context context, int userId, ImageView imageView) {
//     String imageUrl = httpClient.buildURL(String.format(API_URL, "getProfileImage"), Collections.singletonList(String.valueOf(userId)));
//     Glide.with(context)
//             .asBitmap()
//             .load(imageUrl)
//             .apply(new RequestOptions()
//                     .optionalCircleCrop()
//                     .diskCacheStrategy(DiskCacheStrategy.NONE)
//                     .placeholder(R.drawable.ic_baseline_account_circle_grey_24dp))
//             .transition(BitmapTransitionOptions.withCrossFade())
//             .into(imageView);
// }
//
// public void disable(String password, Consumer<ExResponse> consumer) {
//     Request request = httpClient.buildRequest(String.format(API_URL, "disable"), Collections.singletonMap("password", password));
//     httpClient.getResponse(request, consumer);
// }
//    private val registered: Long = 0
//        get() {
//            val date = Date(field)
//            return SimpleDateFormat("dd.MM.yyyy HH:mm 'Uhr'", Locale.getDefault()).format(date)
//        }

    fun getStringProperty(property: String): String? = properties[property]

    fun getBooleanProperty(property: String): Boolean = properties[property]?.toBoolean() ?: true

    fun getIntProperty(property: String): Int = properties[property]?.toInt() ?: -1

//    fun setNotificationEnabled(subject: String, enabled: Boolean) {
//        ActivityHolder.changeNotification(subject, enabled)
//        if (enabled) {
//            notifications!!.add(subject)
//            return
//        }
//        notifications!!.remove(subject)
//    }

//    fun isNotificationEnabled(subject: String): Boolean {
//        return notifications != null && notifications.contains(subject)
//    }
//
//    private int getId() {
//     return this.myProfile ? -1 : this.userId;
// }
}