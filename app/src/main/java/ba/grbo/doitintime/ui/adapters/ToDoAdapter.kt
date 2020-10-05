package ba.grbo.doitintime.ui.adapters

import ItemDiffCallbacks
import android.content.Context
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ba.grbo.doitintime.R
import ba.grbo.doitintime.data.*
import ba.grbo.doitintime.databinding.InfoItemBinding
import ba.grbo.doitintime.databinding.TaskItemBinding
import ba.grbo.doitintime.utilities.CustomEditText
import ba.grbo.doitintime.utilities.CustomImageButton
import ba.grbo.doitintime.utilities.MaterialSpinnerAdapter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.roundToInt


@FragmentScoped
class ToDoAdapter @Inject constructor(
    private val viewsEnabled: LiveData<Boolean>,
    private val titleWarningMessage: LiveData<Int>,
    private val viewLifecyleOwner: LifecycleOwner,
    private val context: Context,
    private val showKeyboard: (View) -> Unit,
    private val hideKeyboard: (View) -> Unit,
    private val setOnReleaseFocusListener: (onReleaseFocusListener: ((MotionEvent) -> Unit)?) -> Unit
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
                setOnReleaseFocusListener: (((ev: MotionEvent) -> Unit)?) -> Unit,
                showKeyboard: (View) -> Unit,
                hideKeyboard: (View) -> Unit
            ): InfoViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = InfoItemBinding.inflate(inflater, parent, false).apply {
                    lifecycleOwner = viewLifecyleOwner
                    setupViews(
                        infoConstraintLayout,
                        titleEditText,
                        priorityButton,
                        statusButton,
                        expandButton,
                        infoCard,
                        setOnReleaseFocusListener,
                        showKeyboard,
                        hideKeyboard,
                        context,
                        priorityDropdownMenu,
                        statusDropdownMenu,
                        priorityLayout,
                        statusLayout,
                        sortLinearLayout,
                        sortHint,
                        sortRadioGroup,
                        sortOrderRadioGroup,
                        titleRadioButton,
                        priorityRadioButton,
                        statusRadioButton,
                        customRadioButton,
                        ascendingRadioButton,
                        descendingRadioButton
                    )
                }
                return InfoViewHolder(binding)
            }

            private fun setupViews(
                infoConstraintLayout: ConstraintLayout,
                titleEditText: EditText,
                priorityButton: CustomImageButton,
                statusButton: CustomImageButton,
                expandButton: CustomImageButton,
                infoCard: MaterialCardView,
                setOnReleaseFocusListener: (((ev: MotionEvent) -> Unit)?) -> Unit,
                showKeyboard: (View) -> Unit,
                hideKeyboard: (View) -> Unit,
                context: Context,
                priorityDropdownMenu: AutoCompleteTextView,
                statusDropdownMenu: AutoCompleteTextView,
                priorityLayout: TextInputLayout,
                statusLayout: TextInputLayout,
                sortLinearLayout: LinearLayout,
                sortHint: TextView,
                sortRadioGroup: RadioGroup,
                sortOrderRadioGroup: RadioGroup,
                titleRadioButton: RadioButton,
                priorityRadioButton: RadioButton,
                statusRadioButton: RadioButton,
                customRadioButton: RadioButton,
                ascendingRadioButton: RadioButton,
                descendingRadioButton: RadioButton
            ) {
                setupInfoConstraintLayout(infoConstraintLayout)
                setupTitleEditText(
                    titleEditText,
                    infoCard,
                    showKeyboard,
                    hideKeyboard,
                    setOnReleaseFocusListener
                )
                setupCustomButtons(
                    priorityButton,
                    statusButton,
                    expandButton,
                    context
                )
                setupDropdownMenus(
                    priorityDropdownMenu,
                    statusDropdownMenu,
                    priorityLayout,
                    statusLayout,
                    context
                )
                setupSortLinearLayout(
                    sortLinearLayout,
                    sortHint,
                    context
                )
                setupSortRadioGroups(
                    sortRadioGroup,
                    sortOrderRadioGroup,
                    sortLinearLayout
                )
                setupSortRadioButtons(
                    titleRadioButton,
                    priorityRadioButton,
                    statusRadioButton,
                    customRadioButton,
                    ascendingRadioButton,
                    descendingRadioButton,
                    sortLinearLayout
                )
            }

            private fun setupInfoConstraintLayout(layout: ConstraintLayout) {
                layout.setOnClickListener { it.requestFocusFromTouch() }
            }

            private fun setupTitleEditText(
                editText: EditText,
                infoCard: MaterialCardView,
                showKeyboard: (View) -> Unit,
                hideKeyboard: (View) -> Unit,
                setOnReleaseFocusListener: (((ev: MotionEvent) -> Unit)?) -> Unit
            ) {
                editText.setOnFocusChangeListener { view, hasFocus ->
                    view as CustomEditText
                    if (hasFocus) {
                        showKeyboard(view)
                        setOnReleaseFocusListener {
                            val touchPoint = Point(it.rawX.roundToInt(), it.rawY.roundToInt())
                            val infoCardTouched = isPointInsideViewBounds(
                                infoCard,
                                touchPoint
                            )
                            if (!infoCardTouched) view.clearFocus()
                        }
                    }
                    if (!hasFocus) {
                        hideKeyboard(view)
                        view.run {
                            if (text.toString().isEmpty()) setText(originalText)
                            else setText(removeExcessiveSpace(text.toString()))
                            setOnReleaseFocusListener(null) // Releasing listener when leaving view
                        }
                    }
                }
            }

            private fun setupCustomButtons(
                priorityButton: CustomImageButton,
                statusButton: CustomImageButton,
                expandButton: CustomImageButton,
                context: Context
            ) {
                setupPriorityCustomButton(priorityButton, context)
                setupStatusCustomButton(statusButton, context)
                setupExpandButton(expandButton)
            }

            private fun setupDropdownMenus(
                priorityDropdownMenu: AutoCompleteTextView,
                statusDropdownMenu: AutoCompleteTextView,
                priorityLayout: TextInputLayout,
                statusLayout: TextInputLayout,
                context: Context
            ) {
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
                        Status.OnHold.identifier,
                        Status.Completed.identifier
                    ),
                    listOf(
                        R.drawable.ic_status_active,
                        R.drawable.ic_status_on_hold,
                        R.drawable.ic_status_completed
                    ),
                    "status",
                    statusDropdownMenu,
                    statusLayout,
                    context
                )
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

            private fun setupSortLinearLayout(
                sortLinearLayout: LinearLayout,
                sortHint: TextView,
                context: Context
            ) {
                sortLinearLayout.setOnClickListener { it.requestFocusFromTouch() }

                sortLinearLayout.setOnFocusChangeListener { _, hasFocus ->
                    sortHint.setTextColor(
                        ContextCompat.getColor(
                            context,
                            if (hasFocus) R.color.colorPrimary else R.color.radio_group_frame_text
                        )
                    )
                }
            }

            private fun setupSortRadioGroups(
                sortRadioGroup: RadioGroup,
                sortOrderRadioGroup: RadioGroup,
                sortLinearLayout: LinearLayout
            ) {
                setupSortRadioGroup(sortRadioGroup, sortLinearLayout)
                setupSortOrderRadioGroup(sortOrderRadioGroup, sortLinearLayout)
            }

            private fun setupSortRadioGroup(
                sortRadioGroup: RadioGroup,
                sortLinearLayout: LinearLayout
            ) {
                setupRadioGroup(sortRadioGroup, sortLinearLayout)
            }

            private fun setupSortOrderRadioGroup(
                sortOrderRadioGroup: RadioGroup,
                sortLinearLayout: LinearLayout
            ) {
                setupRadioGroup(sortOrderRadioGroup, sortLinearLayout)
            }

            private fun setupRadioGroup(
                radioGroup: RadioGroup,
                linearLayout: LinearLayout
            ) {
                radioGroup.setOnClickListener {
                    linearLayout.requestFocusFromTouch()
                }

                radioGroup.setOnCheckedChangeListener { _, _ ->
                    linearLayout.requestFocusFromTouch()
                }
            }


            private fun setupPriorityCustomButton(
                button: CustomImageButton,
                context: Context
            ) {
                setupCustomButton(
                    button,
                    R.menu.priorities_menu,
                    context,
                    ::getPriorityAction
                )
            }

            private fun setupStatusCustomButton(
                button: CustomImageButton,
                context: Context
            ) {
                setupCustomButton(
                    button,
                    R.menu.statuses_menu,
                    context,
                    ::getStatusAction
                )
            }

            private fun setupExpandButton(expandButton: CustomImageButton) {
                expandButton.setOnClickListener {
                    it.tag = when (it.tag) {
                        true -> false
                        false -> true
                        else -> throw IllegalArgumentException("Unknown tag: ${it.tag}")
                    }
                }
            }

            private fun setupCustomButton(
                button: CustomImageButton,
                @MenuRes menu: Int,
                context: Context,
                action: (Int, CustomImageButton) -> Boolean,
            ) {
                button.setOnClickListener {
                    it.requestFocusFromTouch()
                    showPopup(
                        menu,
                        button,
                        context,
                        action
                    )
                }
            }

            private fun setupSortRadioButtons(
                titleRadioButton: RadioButton,
                priorityRadioButton: RadioButton,
                statusRadioButton: RadioButton,
                customRadioButton: RadioButton,
                ascendingRadioButton: RadioButton,
                descendingRadioButton: RadioButton,
                sortLinearLayout: LinearLayout

            ) {
                setupSortRadioButton(titleRadioButton, sortLinearLayout)
                setupSortRadioButton(priorityRadioButton, sortLinearLayout)
                setupSortRadioButton(statusRadioButton, sortLinearLayout)
                setupSortRadioButton(customRadioButton, sortLinearLayout)
                setupSortRadioButton(ascendingRadioButton, sortLinearLayout)
                setupSortRadioButton(descendingRadioButton, sortLinearLayout)
            }

            private fun setupSortRadioButton(
                sortRadioButton: RadioButton,
                sortLinearLayout: LinearLayout
            ) {
                sortRadioButton.setOnClickListener {
                    sortLinearLayout.requestFocusFromTouch()
                }
            }

            private fun isPointInsideViewBounds(view: View, point: Point): Boolean = Rect().run {
                // get view rectangle
                view.getDrawingRect(this)

                // apply offset
                IntArray(2).also { locationOnScreen ->
                    view.getLocationOnScreen(locationOnScreen)
                    offset(locationOnScreen[0], locationOnScreen[1])
                }

                // check is rectangle contains point
                contains(point.x, point.y)
            }

            private fun removeExcessiveSpace(text: String) = text.trim()
                .fold(StringBuilder()) { result, char ->
                    if ((char != ' ' && char != '\n') ||
                        (char != '\n' && result[result.length - 1] != ' ')
                    ) {
                        result.append(char)
                    } else if ((char == '\n') && result[result.length - 1] != ' ') result.append(' ')
                    result
                }
                .toString()

            private fun getStatusAction(id: Int, statusButton: CustomImageButton) = when (id) {
                R.id.status_active -> {
                    statusButton.tag = Status.Active.identifier
                    true
                }
                R.id.status_on_hold -> {
                    statusButton.tag = Status.OnHold.identifier
                    true
                }
                R.id.status_completed -> {
                    statusButton.tag = Status.Completed.identifier
                    true
                }
                else -> throw IllegalArgumentException("Unknown id: $id")
            }

            private fun getPriorityAction(id: Int, priorityButton: CustomImageButton) = when (id) {
                R.id.high_priority -> {
                    priorityButton.tag = Priority.High.name
                    true
                }
                R.id.normal_priority -> {
                    priorityButton.tag = Priority.Normal.name
                    true
                }
                R.id.low_priority -> {
                    priorityButton.tag = Priority.Low.name
                    true
                }
                else -> throw IllegalArgumentException("Unknown id: $id")
            }

            private fun showPopup(
                @MenuRes menu: Int,
                button: CustomImageButton,
                context: Context,
                action: (Int, CustomImageButton) -> Boolean
            ) {
                PopupMenu(context, button).apply {
                    setOnMenuItemClickListener {
                        action(it.itemId, button)
                    }
                    inflate(menu)
                    show()
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
            setOnReleaseFocusListener,
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