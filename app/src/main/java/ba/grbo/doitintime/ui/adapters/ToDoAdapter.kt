package ba.grbo.doitintime.ui.adapters

import ItemDiffCallbacks
import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ba.grbo.doitintime.R
import ba.grbo.doitintime.data.*
import ba.grbo.doitintime.databinding.InfoItemBinding
import ba.grbo.doitintime.databinding.TaskItemBinding
import ba.grbo.doitintime.utilities.MaterialSpinnerAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.*
import javax.inject.Inject

@FragmentScoped
class ToDoAdapter @Inject constructor(
    private val viewsEnabled: LiveData<Boolean>,
    private val titleWarningMessage: LiveData<Int>,
    private val viewLifecyleOwner: LifecycleOwner,
    private val context: Context,
    private val observeEditTextLength: (Int) -> Unit,
    private val showKeyboard: (View) -> Unit,
    private val hideKeyboard: () -> Unit
) : ListAdapter<Item, RecyclerView.ViewHolder>(ItemDiffCallbacks()) {
    companion object {
        private const val INFO = 0
        private const val TASK = 1
    }

    private val job = Job()
    private val adapterScope = CoroutineScope(Dispatchers.Default + job)

    private class InfoViewHolder private constructor(private val binding: InfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(
                parent: ViewGroup,
                viewLifecyleOwner: LifecycleOwner,
                context: Context,
                observeEditTextLength: (Int) -> Unit,
                showKeyboard: (View) -> Unit,
                hideKeyboard: () -> Unit
            ): InfoViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = InfoItemBinding.inflate(inflater, parent, false).apply {
                    lifecycleOwner = viewLifecyleOwner
                    setupTitleEditText(
                        titleEditText,
                        observeEditTextLength,
                        showKeyboard,
                        hideKeyboard
                    )
                    setupPriorityDropdownMenu(
                        priorityDropdownMenu,
                        priorityLayout,
                        context
                    )
                    setupStatusDropdownMenu(
                        statusDropdownMenu,
                        statusLayout,
                        context
                    )
                }
                return InfoViewHolder(binding)
            }

            private fun setupPriorityDropdownMenu(
                priorityDropdownMenu: AutoCompleteTextView,
                priorityLayout: TextInputLayout,
                context: Context
            ) {
                setupDropdownMenu(
                    listOf(
                        Priority.High.name,
                        Priority.Normal.name,
                        Priority.Low.name
                    ),
                    listOf(
                        R.drawable.ic_priority_high,
                        R.drawable.ic_priority_medium,
                        R.drawable.ic_priority_low
                    ),
                    "priority",
                    priorityDropdownMenu,
                    priorityLayout,
                    context
                )
            }

            private fun setupStatusDropdownMenu(
                statusDropdownMenu: AutoCompleteTextView,
                statusLayout: TextInputLayout,
                context: Context
            ) {
                setupDropdownMenu(
                    listOf(
                        Status.Active.identifier,
                        Status.Completed.identifier,
                        Status.OnHold.identifier
                    ),
                    listOf(
                        R.drawable.ic_status_active,
                        R.drawable.ic_status_completed,
                        R.drawable.ic_status_on_hold
                    ),
                    "status",
                    statusDropdownMenu,
                    statusLayout,
                    context
                )
            }

            private fun setupTitleEditText(
                editText: TextInputEditText,
                observeEditTextLength: (Int) -> Unit,
                showKeyboard: (View) -> Unit,
                hideKeyboard: () -> Unit
            ) {
                editText.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) showKeyboard(v)
                    else hideKeyboard()
                }

                editText.doOnTextChanged { text, _, _, _ ->
                    text?.run { observeEditTextLength(length) }
                }
            }

            private fun setupDropdownMenu(
                contents: List<String>,
                drawables: List<Int>,
                type: String,
                dropdownMenu: AutoCompleteTextView,
                layout: TextInputLayout,
                context: Context
            ) {
                val contentAdapter = MaterialSpinnerAdapter(
                    context,
                    R.layout.dropdown_list_item,
                    contents
                )

                dropdownMenu.setAdapter(contentAdapter)
                dropdownMenu.doOnTextChanged { content, _, _, _ ->
                    // If device is rotated before any selection is made
                    if (content.toString() == "") return@doOnTextChanged

                    layout.setStartIconTintMode(PorterDuff.Mode.DST)
                    layout.setStartIconDrawable(
                        when (content.toString()) {
                            contents[0] -> drawables[0]
                            contents[1] -> drawables[1]
                            contents[2] -> drawables[2]
                            else -> throw IllegalArgumentException("Unknown $type: $content")
                        }
                    )
                }
            }
        }

        fun bind(
            info: Info,
            viewsEnabled: LiveData<Boolean>,
            warningMessage: LiveData<Int>
        ) {
            binding.info = info
            binding.viewsEnabled = viewsEnabled
            binding.warningMessage = warningMessage

            binding.executePendingBindings()
        }
    }

    private class TaskViewHolder private constructor(private val binding: TaskItemBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TASK -> TaskViewHolder.from(parent, viewLifecyleOwner)
        INFO -> InfoViewHolder.from(
            parent,
            viewLifecyleOwner,
            context,
            observeEditTextLength,
            showKeyboard,
            hideKeyboard
        )
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
                holder.bind(
                    info,
                    viewsEnabled,
                    titleWarningMessage
                )
            }
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is Item.TaskItem -> TASK
        is Item.InfoItem -> INFO
    }

    fun cancelJob() {
        job.cancel()
    }

    fun wrapDataAndSubmitList(toDo: ToDo) {
        adapterScope.launch {
            val data = if (toDo.tasks.isEmpty()) listOf(Item.InfoItem(toDo.info))
            else listOf(Item.InfoItem(toDo.info)) + toDo.tasks.map { Item.TaskItem(it) }

            withContext(Dispatchers.Main) { submitList(data) }
        }
    }
}