package de.twisted.examshare.data.task

import de.twisted.examshare.data.models.Task
import de.twisted.examshare.util.ImageLibrary
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val taskService: RestTaskService
) {

    fun createTask(task: Task): Single<Task> {
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("title", task.title)
            .addFormDataPart("subjectId", task.subjectId)
            .addFormDataPart("grade", task.grade.toString())
            .addFormDataPart("creator", task.creator ?: "")
            .addFormDataPart("schoolForm", task.schoolForm)
            .addFormDataPart("federalState", task.federalState)

        task.keywords.forEach { builder.addFormDataPart("keywords", it) }

        task.taskFiles?.forEach {
            val contentType = ImageLibrary.getMimeType(it) ?: return@forEach
            builder.addFormDataPart("taskImages", it.name, it.asRequestBody(contentType.toMediaTypeOrNull()))
        }

        task.solutionFiles?.forEach {
            val contentType = ImageLibrary.getMimeType(it) ?: return@forEach
            builder.addFormDataPart("solutionImages", it.name, it.asRequestBody(contentType.toMediaTypeOrNull()))
        }

        return this.taskService
            .createTask(builder.build())
            .flatMap { taskDao.insertOne(it).andThen(Single.just(it)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteTask(taskId: String): Completable {
        return taskService.deleteTask(taskId)
            .andThen(Completable.fromAction { taskDao.deleteOne(taskId) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getObservableTasks(subjectId: String): Observable<List<Task>> {
        return this.refreshTasks(subjectId)
            .andThen(taskDao.observeAll(subjectId))
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun refreshTasks(subjectId: String): Completable {
        return this.taskService.getTasks(subjectId)
            .flatMapCompletable { taskList -> taskDao.insertAll(taskList) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateTask(task: Task): Completable {
        return taskService.updateTask(task.id, task)
            .flatMapCompletable { taskDao.updateOne(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun rateTask(taskId: String, rating: Int): Completable = taskService
        .rateTask(taskId, rating)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun favoriteTask(task: Task): Completable = taskService
        .favoriteTask(task.id, task.isFavorite)
        .andThen(taskDao.updateOne(task))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    //
    // public void findTasks(String subject, QueryData queryData, int lastId, BiConsumer<List<ExamModel>, Integer> consumer) {
    //     if (queryData.hasKeyword()) {
    //         this.searchTasks(subject, queryData, consumer);
    //         return;
    //     }
    //     this.loadTasks(subject, lastId, consumer);
    // }
    //
    // private void loadTasks(String subject, int lastId, BiConsumer<List<ExamModel>, Integer> consumer) {
    //     Request request = httpClient.buildRequest(String.format(API_URL, "list"), new HashMap<String, Object>() {{
    //         put("subject", subject);
    //         put("lastId", lastId);
    //     }});
    //
    //     httpClient.getResponseSingle(request, response -> {
    //         int totalCount = response.getInt("Total");
    //         boolean success = response.getType() == ResponseType.SUCCESS;
    //         consumer.accept(success ? response.getItems(httpClient.getGson(), Task.class) : null, totalCount);
    //     });
    // }
    //
    // private void searchTasks(String subject, QueryData queryData, BiConsumer<List<ExamModel>, Integer> consumer) {
    //     Request request = httpClient.buildRequest(String.format(API_URL, "search"), new HashMap<String, Object>() {{
    //         put("subject", subject);
    //         putAll(queryData.getMap());
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
}