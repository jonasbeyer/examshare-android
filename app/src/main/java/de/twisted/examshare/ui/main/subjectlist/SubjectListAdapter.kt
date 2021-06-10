package de.twisted.examshare.ui.main.subjectlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.twisted.examshare.data.models.Subject
import de.twisted.examshare.databinding.ItemSubjectBinding

class SubjectListAdapter(
    private val actions: SubjectActions
) : ListAdapter<Subject, SubjectViewHolder>(SubjectDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val binding = ItemSubjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubjectViewHolder(binding, actions)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SubjectViewHolder(
    private val binding: ItemSubjectBinding,
    private val actions: SubjectActions
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(subject: Subject) {
        binding.subject = subject
        binding.actions = actions
        binding.executePendingBindings()
    }
}

interface SubjectActions {
    fun openSubjectDetails(subject: Subject)

    fun updateNotificationPreference(subject: Subject)
}

object SubjectDiffCallback : DiffUtil.ItemCallback<Subject>() {
    override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
        return oldItem == newItem
    }
}