package ba.grbo.doitintime.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ba.grbo.doitintime.data.Info
import ba.grbo.doitintime.data.Task
import ba.grbo.doitintime.data.ToDo
import ba.grbo.doitintime.databinding.InfoItemBinding
import ba.grbo.doitintime.databinding.TaskItemBinding
import kotlinx.coroutines.*

class ToDoAdapter(
    private val viewLifecyleOwner: LifecycleOwner
) : ListAdapter<Item, RecyclerView.ViewHolder>(ItemDiffCallback()) {
    companion object {
        private const val INFO = 0
        private const val TASK = 1
    }

    private val job = Job()
    private val adapterScope = CoroutineScope(Dispatchers.Default + job)

    fun cancelAdapterJob() {
        job.cancel()
    }

    fun wrapDataAndSubmitList(toDo: ToDo) {
        adapterScope.launch {
            val data = if (toDo.task.isEmpty()) listOf(Item.InfoItem(toDo.info))
            else listOf(Item.InfoItem(toDo.info)) + toDo.task.map { Item.TaskItem(it) }

            withContext(Dispatchers.Main) { submitList(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TASK -> TaskViewHolder.from(parent, viewLifecyleOwner)
        INFO -> InfoViewHolder.from(parent, viewLifecyleOwner)
        else -> throw IllegalArgumentException("Unknown viewType: $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> {
                val task = (getItem(position) as Item.TaskItem).task
                holder.bind(task)
            }
            is InfoViewHolder -> {
                val info = (getItem(position) as Item.InfoItem).info
                holder.bind(info)
            }
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is Item.TaskItem -> TASK
        is Item.InfoItem -> INFO
    }

    class InfoViewHolder private constructor(private val binding: InfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup, viewLifecyleOwner: LifecycleOwner): InfoViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = InfoItemBinding.inflate(inflater, parent, false).apply {
                    lifecycleOwner = viewLifecyleOwner
                }
                return InfoViewHolder(binding)
            }
        }

        fun bind(info: Info) {
            binding.info = info
            binding.executePendingBindings()
        }
    }

    class TaskViewHolder private constructor(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup, viewLifecyleOwner: LifecycleOwner): TaskViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = TaskItemBinding.inflate(inflater, parent, false).apply {
                    lifecycleOwner = viewLifecyleOwner
                }
                return TaskViewHolder(binding)
            }
        }

        fun bind(task: Task) {
            binding.task = task
            binding.executePendingBindings()
        }
    }
}


class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
}

sealed class Item {
    abstract val id: Int

    data class TaskItem(val task: Task) : Item() {
        override val id = task.id
    }

    data class InfoItem(val info: Info) : Item() {
        override val id = info.id
    }
}