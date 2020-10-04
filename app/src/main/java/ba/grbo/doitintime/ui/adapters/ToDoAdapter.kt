package ba.grbo.doitintime.ui.adapters

import ItemDiffCallbacks
import android.content.Context
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
                    setupViews(
                        infoExpanded.titleEditText,
                        infoExpanded.priorityDropdownMenu,
                        infoExpanded.statusDropdownMenu,
                        infoExpanded.priorityLayout,
                        infoExpanded.statusLayout,
                        observeEditTextLength,
                        showKeyboard,
                        hideKeyboard,
                        context
                    )
                }
                return InfoViewHolder(binding)
            }

            private fun setupViews(
                titleEditText: TextInputEditText,
                priorityDropdownMenu: AutoCompleteTextView,
                statusDropdownMenu: AutoCompleteTextView,
                priorityLayout: TextInputLayout,
                statusLayout: TextInputLayout,
                observeEditTextLength: (Int) -> Unit,
                showKeyboard: (View) -> Unit,
                hideKeyboard: () -> Unit,
                context: Context
            ) {
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
                        R.drawable.ic_priority_normal,
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
//                editText.setOnFocusChangeListener { v, hasFocus ->
//                    if (hasFocus) showKeyboard(v)
//                    else hideKeyboard()
//                }

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
            warningMessage: LiveData<Int>,
            context: Context,
            viewLifecyleOwner: LifecycleOwner
        ) {
            binding.info = info
            binding.viewsEnabled = viewsEnabled
            binding.warningMessage = warningMessage
            binding.cursorPosition = MutableLiveData(0).apply {
                observe(viewLifecyleOwner) {
                    Log.i("ToDoAdapter", "position: $it")
                }
            }

            binding.infoCollapsed.expandButton.setOnClickListener {
                info.expanded.value = true
            }

            binding.infoExpanded.expandButton.setOnClickListener {
                info.expanded.value = false
            }

            binding.infoCollapsed.statusButton.setOnClickListener {
                it.requestFocusFromTouch()
                showPopup(
                    R.menu.statuses_menu,
                    it as ImageButton,
                    context,
                    ::getStatusAction
                )
            }

            binding.infoCollapsed.priorityButton.setOnClickListener {
                it.requestFocusFromTouch()
                showPopup(
                    R.menu.priorities_menu,
                    it as ImageButton,
                    context,
                    ::getPrioriyAction
                )
            }

            binding.executePendingBindings()
        }

        private fun getStatusAction(button: ImageButton, id: Int) = when (id) {
            R.id.status_active -> {
                binding.info?.status?.value = Status.Active
                true
            }
            R.id.status_completed -> {
                binding.info?.status?.value = Status.Completed
                true
            }
            R.id.status_on_hold -> {
                binding.info?.status?.value = Status.OnHold
                true
            }
            else -> false
        }

        private fun getPrioriyAction(button: ImageButton, id: Int) = when (id) {
            R.id.high_priority -> {
                binding.info?.priority?.value = Priority.High
                true
            }
            R.id.normal_priority -> {
                binding.info?.priority?.value = Priority.Normal
                true
            }
            R.id.low_priority -> {
                binding.info?.priority?.value = Priority.Low
                true
            }
            else -> false
        }

        private fun showPopup(
            @MenuRes menu: Int,
            button: ImageButton,
            context: Context,
            action: (ImageButton, Int) -> Boolean
        ) {
            PopupMenu(context, button).apply {
                setOnMenuItemClickListener {
                    action(button, it.itemId)
                }
                inflate(menu)
                show()
            }
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

    companion object {
        private const val INFO = 0
        private const val TASK = 1
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
                    titleWarningMessage,
                    context,
                    viewLifecyleOwner
                )
            }
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is Item.TaskItem -> TASK
        is Item.InfoItem -> INFO
    }

    fun wrapToDoAndSubmitList(toDo: ToDo) {
        adapterScope.launch {
            val data = if (toDo.tasks.isEmpty()) listOf(Item.InfoItem(toDo.info))
            else listOf(Item.InfoItem(toDo.info)) + toDo.tasks.map { Item.TaskItem(it) }

            withContext(Dispatchers.Main) { submitList(data) }
        }
    }

    fun cancelJob() {
        job.cancel()
    }
}