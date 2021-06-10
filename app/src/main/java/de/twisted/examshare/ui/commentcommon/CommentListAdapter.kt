package de.twisted.examshare.ui.commentcommon

import android.view.*
import android.widget.PopupMenu
import androidx.core.view.forEachIndexed
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Comment
import de.twisted.examshare.databinding.ItemCommentBinding
import de.twisted.examshare.ui.account.setProfileAvatar

class CommentListAdapter(
    private val parentComment: Comment?,
    private val actions: CommentActions
) : ListAdapter<Comment, CommentViewHolder>(CommentDiffCallback) {

// private Task task;
// private Comment parent;
// private View errorView;
// private ExamActivity activity;
//
// private boolean isLoaded;
//
// public CommentListAdapter(ExamActivity activity, Task task, Comment parent) {
//     super(Collections.emptyList());
//     this.task = task;
//     this.parent = parent;
//     this.activity = activity;
//     this.itemList = new ArrayList<>();
// }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding, actions, true)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        return holder.bind(getItem(position), parentComment != null && position == 0)
    }

// @Override
// public void setItemList(List<ExamModel> commentList) {
//     if (commentList == null) {
//         updateErrorView(true);
//         return;
//     }
//     this.itemList = commentList;
//     this.isLoaded = true;
//     this.notifyDataSetChanged();
//     this.updateErrorView(false);
// }
//
// public void setItemList(Comment parent, List<ExamModel> commentList) {
//     boolean success = commentList != null;
//     if (!success) {
//         commentList = this.itemList;
//         if (commentList.isEmpty())
//             commentList.add(parent);
//     } else {
//         commentList.add(0, parent);
//     }
//     setItemList(commentList);
//     updateParentComment(success ? getChildCount() : parent.getCommentCount());
// }
//
//
//
// public void removeComment(int position, boolean message) {
//     if (parent == null || parent != null && position != 0) {
//         itemList.remove(position);
//         updateErrorView(false);
//         updateParentComment(false);
//         notifyDataSetChanged();
//         if (message) {
//             TextUtil.snackbar(activity, R.string.deleted_comment);
//         }
//         return;
//     }
//     ((CommentDetailsActivity) activity).delete(message);
// }
//
// private void updateErrorView(boolean error) {
//     if (errorView == null) return;
//     if (itemList.size() > 0) {
//         errorView.setVisibility(View.GONE);
//         return;
//     }
//     int title = error ? R.string.no_internet_connection : R.string.no_comments;
//     int message = error ? R.string.connection_error_retry : R.string.no_comments_hint;
//     int drawable = error ? R.drawable.ic_error_outline : R.drawable.ic_search_grey;
//     ((TextView) errorView.findViewById(R.id.title)).setText(title);
//     ((TextView) errorView.findViewById(R.id.message)).setText(message);
//     ((ImageView) errorView.findViewById(R.id.imageView)).setImageResource(drawable);
//     ((LinearLayout) errorView).setVisibility(View.VISIBLE);
// }
//
//
// private void updateParentComment(boolean increase) {
//     if (parent != null)
//         updateParentComment(parent.getCommentCount() + (increase ? 1 : -1));
// }
//
// private void updateParentComment(int count) {
//     parent.setCommentCount(count);
//     setComment(parent, 0);
// }
//
// private int getChildCount() {
//     int count = itemList.size() - 1;
//     return count < 0 ? 0 : count;
// }
//
// public void changeComment(Comment comment, Bundle bundle) {
//     boolean delete = bundle.getBoolean("deleted");
//     if (delete) {
//         boolean message = bundle.getBoolean("message");
//         removeComment(comment, message);
//     } else {
//         setComment(comment);
//     }
// }
//
// @Override
// public View getView(int position, View view, ViewGroup parentView) {
//     Comment comment = (Comment) getItem(position);
//     boolean isParent = parent != null && position == 0;
//
//     TextView title = (TextView) view.findViewById(R.id.commentTitle);
//     title.setTypeface(null, comment.isTaskAuthorsComment(task) ? Typeface.BOLD : Typeface.NORMAL);
//
//     ImageButton commentButton = view.findViewById(R.id.commentButton);
//     commentButton.setOnClickListener(v -> {
//         if (isParent) {
//             showCommentEditor(null);
//             return;
//         }
//         Intent intent = new Intent(activity, CommentDetailsActivity.class);
//         intent.putExtra("task", task);
//         intent.putExtra("comment", comment);
//         intent.putExtra("answer", true);
//         activity.startActivityForResult(intent, 2);
//     });
//
//     return view;
// }
//
//
// @Override
// public OnMenuItemClickListener getMenuListener(ExamModel model, int postion) {
//     return item -> {
//         Comment comment = (Comment) model;
//         switch (item.getItemId()) {
//             case R.string.delete:
//                 Dialogs.confirmDelete(activity, R.string.confirm_delete_comment, () -> {
//                     activity.showProgressDialog();
//                     commentManager.deleteComment(comment.getId(), response -> {
//                         activity.getProgressDialog().dismiss();
//                         removeIfNotAvaiable(response, postion);
//                         if (response.getType() == ResponseType.SUCCESS)
//                             removeComment(postion, true);
//                     });
//                 });
//                 break;
//             case R.string.report:
//                 Dialogs.showReportDialog(activity, comment, response -> {
//                     this.removeIfNotAvaiable(response, postion);
//                     if (response.getType() == ResponseType.SUCCESS)
//                         TextUtil.snackbar(activity, R.string.comment_reported);
//                 });
//         }
//         return true;
//     };
// }
//
// public void showCommentEditor(Comment comment) {
//     commentEditor.showView(comment);
//     commentEditor.setSendAction(() -> {
//         activity.showProgressDialog();
//         if (comment == null) {
//             executeAddAction();
//         } else {
//             executeUpdateAction(comment);
//         }
//     });
// }
//
// @Override
// public OnItemClickListener getClickListener() {
//     return (parent, view, position, id) -> {
//         if (this.parent == null || position != 0) {
//             Intent intent = new Intent(activity, CommentDetailsActivity.class);
//             intent.putExtra("task", task);
//             intent.putExtra("comment", (Comment) itemList.get(position));
//             activity.startActivityForResult(intent, 2);
//         }
//     };
// }
//
// private void executeAddAction() {
//     int commentThread = parent != null ? parent.getId() : -1;
//     commentManager.addComment(task.getId(), commentThread, commentEditor.getCommentText(), response -> {
//         activity.getProgressDialog().dismiss();
//         removeIfNotAvaiable(response, 0);
//         if (response.getType() == ResponseType.SUCCESS) {
//             addComment(response.getInt("commentId"), commentEditor.getCommentText(), parent == null ? 0 : 1);
//             commentEditor.hideView();
//             TextUtil.snackbar(activity, R.string.added_comment);
//         }
//     });
// }
//
// private void executeUpdateAction(Comment comment) {
//     commentManager.updateComment(comment, commentEditor.getCommentText(), response -> {
//         activity.getProgressDialog().dismiss();
//         removeIfNotAvaiable(response, getCommentPosition(comment));
//         if (response.getType() == ResponseType.SUCCESS) {
//             comment.setMessage(commentEditor.getCommentText());
//             comment.setUpdated(true);
//             commentEditor.hideView();
//             setComment(comment);
//             TextUtil.snackbar(activity, R.string.edited_comment);
//         }
//     });
// }
//
// private void removeIfNotAvaiable(ExResponse response, int position) {
//     if (response.getType() != ResponseType.NOT_FOUND) return;
//     if (response.isMissing("Comment")) {
//         activity.notifyTaskChange(task, UpdateStatus.REMOVED);
//         activity.finishStack();
//         TextUtil.toast(activity, R.string.task_not_exist);
//     } else {
//         TextUtil.toast(activity, R.string.comment_not_exist);
//         commentEditor.hideView();
//         removeComment(position, false);
//     }
// }
//
// private void updateComment(Comment comment, ImageView likeButton, ImageView dislikeButton, TextView likeCount, TextView dislikeCount) {
//     likeButton.getDrawable().setColorFilter(getColor(comment.isLiked()), Mode.SRC_IN);
//     dislikeButton.getDrawable().setColorFilter(getColor(comment.isDisliked()), Mode.SRC_IN);
//     likeCount.setText(String.valueOf(comment.getLikeCount()));
//     dislikeCount.setText(String.valueOf(comment.getDislikeCount()));
// }
//
}

class CommentViewHolder(
    private val binding: ItemCommentBinding,
    private val actions: CommentActions,
    private val moderation: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: Comment, isParent: Boolean) {
        binding.comment = comment
        binding.isParent = isParent
        binding.actions = actions
        binding.executePendingBindings()

        binding.commentAuthorProfileImage.apply {
            setProfileAvatar(context, this, comment.authorId)
        }

        binding.commentMessage.apply {
            viewTreeObserver.addOnPreDrawListener {
                viewTreeObserver.removeOnPreDrawListener(this)

                binding.commentShowAnswers.isVisible = layout.text.toString() != comment.message
                true
            }
        }

        binding.commentOptionsButton.setOnClickListener {
            showOptionsMenu(it, comment)
        }
    }

    private fun showOptionsMenu(anchor: View, comment: Comment) {
        val popupMenu = PopupMenu(anchor.context, anchor, Gravity.END).apply {
            menuInflater.inflate(R.menu.menu_comment_options, menu)
            menu.forEachIndexed { _, item ->
                when (item.itemId) {
                    R.id.menu_item_edit_comment,
                    R.id.menu_item_delete_comment -> item.isVisible = moderation
                    else -> item.isVisible = comment.author == "Twister21"
                }
            }

            setOnMenuItemClickListener {
                onOptionsMenuItemSelected(it, comment)
                true
            }
        }

        popupMenu.show()
    }

    private fun onOptionsMenuItemSelected(item: MenuItem, comment: Comment) {
        when (item.itemId) {
            R.id.menu_item_edit_comment -> actions.openCommentEditSheet(comment)
            R.id.menu_item_delete_comment -> actions.openCommentConfirmDeleteDialog(comment)
            R.id.menu_item_comment_author_profile -> actions.openAuthorProfile(comment.authorId)
            R.id.menu_item_report_comment -> actions.openReportDialog(comment)
        }
    }
}

object CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}