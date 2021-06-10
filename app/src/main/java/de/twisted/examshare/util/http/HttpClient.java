// package de.twisted.examshare.util.http;
//
// import android.app.Activity;
// import android.os.Handler;
// import android.os.Looper;
// import com.google.gson.Gson;
// import com.google.gson.JsonSyntaxException;
// import de.twisted.examshare.R;
// import de.twisted.examshare.util.ActivityHolder;
// import de.twisted.examshare.util.Preferences;
// import de.twisted.examshare.util.builder.Dialogs;
// import de.twisted.examshare.util.helper.TextUtil;
// import de.twisted.examshare.ui.shared.base.ExamActivity;
// import de.twisted.examshare.data.models.ExResponse;
// import de.twisted.examshare.data.models.ExResponse.ResponseType;
// import okhttp3.Call;
// import okhttp3.Callback;
// import okhttp3.FormBody;
// import okhttp3.HttpUrl;
// import okhttp3.OkHttpClient;
// import okhttp3.OkHttpClient.Builder;
// import okhttp3.Request;
// import okhttp3.RequestBody;
// import okhttp3.Response;
//
// import java.io.IOException;
// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.TimeUnit;
//
// public class HttpClient {
//
//     private Call previousCall;
//
//     private final Gson gson;
//     private final String API;
//     private final OkHttpClient client;
//
//     public HttpClient() {
//         this.client = getClient(null, 4);
//         this.API = Preferences.INFO_URL + "/api/v1/%s";
//         this.gson = new Gson();
//     }
//
//     public Call getResponse(Request request, int timeout, ErrorType errorType, Consumer<ExResponse> consumer) {
//         return this.requestResult(getClient(client, timeout), request, errorType, response -> {
//             if (request.tag() == null) {
//                 consumer.accept(response);
//                 return;
//             }
//
//             Activity activity = ActivityHolder.getCurrentActivity();
//             switch (response.getType()) {
//                 case BAD_VERSION:
//                     Dialogs.showUpdateDialog(activity, response.getMessage());
//                     break;
//                 case UNAUTHORIZED:
//                     Dialogs.showUnauthorizedDialog(activity, request, response);
//             }
//             consumer.accept(response);
//         });
//     }
//
//     public Call getResponse(Request request, ErrorType errorType, Consumer<ExResponse> consumer) {
//         return getResponse(request, -1, errorType, consumer);
//     }
//
//     public Call getResponse(Request request, Consumer<ExResponse> consumer) {
//         return this.getResponse(request, ErrorType.TOAST, consumer);
//     }
//
//     public void getResponseSingle(Request request, Consumer<ExResponse> consumer) {
//         if (previousCall != null && previousCall.request().tag().equals(request.tag()))
//             previousCall.cancel();
//
//         this.previousCall = getResponse(request, consumer);
//     }
//
//     public Request buildRequest(String url, Map<String, Object> parameters) {
//         FormBody.Builder builder = new FormBody.Builder();
//         for (String param : parameters.keySet()) {
//             if (parameters.get(param) instanceof List) {
//                 List<String> args = (List<String>) parameters.get(param);
//                 for (String arg : args) {
//                     builder.add(param, arg);
//                 }
//                 continue;
//             }
//             Object object = parameters.get(param);
//             builder.add(param, object instanceof String ? (String) object : String.valueOf(object));
//         }
//         return buildRequest(url, builder.build(), true);
//     }
//
//     public Request buildRequest(String url, RequestBody requestBody, boolean addTag) {
//         Request.Builder builder = new Request.Builder()
//                 .url(url)
//                 .post(requestBody);
//
//         if (addTag) {
//             Activity activity = ActivityHolder.getCurrentActivity();
//             builder.tag(activity != null ? activity.getClass().getSimpleName() : null);
//         }
//         return builder.build();
//     }
//
//     public void cancelRequests(String className) {
//         for (Call call : client.dispatcher().runningCalls()) {
//             Object tag = call.request().tag();
//             if (tag != null && tag.equals(className))
//                 call.cancel();
//         }
//     }
//
//     public String buildURL(String baseURL, List<String> segments) {
//         HttpUrl.Builder builder = HttpUrl.parse(baseURL).newBuilder();
//         for (String param : segments) {
//             builder.addPathSegment(param);
//         }
//         return builder.build().toString();
//     }
//
//     private Call requestResult(OkHttpClient client, Request request, ErrorType errorType, Consumer<ExResponse> consumer) {
//         Call call = client.newCall(request);
//         call.enqueue(new Callback() {
//             @Override
//             public void onFailure(Call call, IOException e) {
//                 if (!call.isCanceled())
//                     new Handler(Looper.getMainLooper()).post(() -> consumer.accept(parseResponse(call, errorType, "", this)));
//             }
//
//             @Override
//             public void onResponse(Call call, Response result) {
//                 try {
//                     String body = result.body().string();
//                     new Handler(Looper.getMainLooper()).post(() -> consumer.accept(parseResponse(call, errorType, body, this)));
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 }
//             }
//         });
//         return call;
//     }
//
//     private ExResponse parseResponse(Call call, ErrorType errorType, String result, Callback callback) {
//         ExResponse response = this.readExResponse(result);
//         if (response != null)
//             return response;
//
//         ExResponse error = new ExResponse(ResponseType.ERROR);
//         ExamActivity activity = ActivityHolder.getCurrentActivity();
//         if (errorType == ErrorType.NONE || !call.request().tag().equals(activity.getClass().getSimpleName()))
//             return error;
//         if (errorType == ErrorType.TOAST) {
//             TextUtil.toast(activity, R.string.connection_error_retry);
//             return error;
//         }
//         Dialogs.showConnectionError(() -> call.clone().enqueue(callback));
//         return error;
//     }
//
//     private ExResponse readExResponse(String json) {
//         if (json.isEmpty()) return null;
//         try {
//             return gson.fromJson(json, ExResponse.class);
//         } catch (JsonSyntaxException e) {
//             return null;
//         }
//     }
//
//     private OkHttpClient getClient(OkHttpClient client, int timeout) {
//         if (timeout == -1)
//             return client;
//
//         Builder builder = client == null ? new OkHttpClient.Builder() : client.newBuilder();
//         builder.connectTimeout(timeout, TimeUnit.SECONDS)
//                 .readTimeout(timeout, TimeUnit.SECONDS)
//                 .writeTimeout(timeout, TimeUnit.SECONDS);
//
//         if (client == null) {
//             builder.addInterceptor(new AuthInterceptor());
//         }
//         return builder.build();
//     }
//
//     public enum ErrorType {
//         DIALOG,
//         TOAST,
//         NONE
//     }
// }