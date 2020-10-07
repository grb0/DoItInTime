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
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.IdRes
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
import ba.grbo.doitintime.utilities.*
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
                        priorityDropdownMenuLayout,
                        statusDropdownMenuLayout,
                        priorityDropdownMenu,
                        statusDropdownMenu,
                        sortLinearLayout,
                        sortHint,
                        sortTypeRadioGroup,
                        sortOrderRadioGroup,
                        descriptionRadioButton,
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
                titleEditText: CustomEditText,
                priorityButton: CustomImageButton,
                statusButton: CustomImageButton,
                expandButton: CustomImageButton,
                infoCard: MaterialCardView,
                setOnReleaseFocusListener: (((ev: MotionEvent) -> Unit)?) -> Unit,
                showKeyboard: (View) -> Unit,
                hideKeyboard: (View) -> Unit,
                context: Context,
                priorityDropdownMenuLayout: TextInputLayout,
                statusDropdownMenuLayout: TextInputLayout,
                priorityDropdownMenu: AutoCompleteTextView,
                statusDropdownMenu: AutoCompleteTextView,
                sortLinearLayout: LinearLayout,
                sortHint: TextView,
                sortTypeRadioGroup: CustomRadioGroup,
                sortOrderRadioGroup: CustomRadioGroup,
                descriptionRadioButton: RadioButton,
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
                    titleEditText,
                    context
                )
                setupDropdownMenuLayouts(
                    priorityDropdownMenuLayout,
                    statusDropdownMenuLayout
                )
                setupDropdownMenus(
                    priorityDropdownMenu,
                    statusDropdownMenu,
                    priorityDropdownMenuLayout,
                    statusDropdownMenuLayout,
                    infoCard,
                    setOnReleaseFocusListener,
                    context
                )
                setupSortLinearLayout(
                    sortLinearLayout,
                    sortHint,
                    infoCard,
                    setOnReleaseFocusListener,
                    context
                )
                setupSortRadioGroups(
                    sortTypeRadioGroup,
                    sortOrderRadioGroup,
                    sortLinearLayout
                )
                setupSortRadioButtons(
                    descriptionRadioButton,
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
                titleEditText: CustomEditText,
                infoCard: MaterialCardView,
                showKeyboard: (View) -> Unit,
                hideKeyboard: (View) -> Unit,
                setOnReleaseFocusListener: (((ev: MotionEvent) -> Unit)?) -> Unit
            ) {
                titleEditText.run {
                    setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            showKeyboard(this)
                            setOnReleaseFocusListener {
                                getOnReleaseFocusListener(
                                    infoCard,
                                    it,
                                    ::clearFocus
                                )
                            }
                        } else if (!hasFocus) {
                            hideKeyboard(this)
                            if (text.toString().isEmpty() && originalText.isNotEmpty()) {
                                setText(originalText)
                            } else if (text.toString().isNotEmpty()) {
                                setText(removeExcessiveSpace(text.toString()))
                            }
                            setOnReleaseFocusListener(null) // Releasing listener when leaving view
                        }
                    }
                }
            }

            private fun setupCustomButtons(
                priorityButton: CustomImageButton,
                statusButton: CustomImageButton,
                expandButton: CustomImageButton,
                titleEditText: CustomEditText,
                context: Context
            ) {
                setupPriorityCustomButton(priorityButton, context)
                setupStatusCustomButton(statusButton, context)
                setupExpandButton(expandButton, titleEditText)
            }

            private fun setupDropdownMenus(
                priorityDropdownMenu: AutoCompleteTextView,
                statusDropdownMenu: AutoCompleteTextView,
                priorityDropdownMenuLayout: TextInputLayout,
                statusDropdownMenuLayout: TextInputLayout,
                infoCard: MaterialCardView,
                setOnReleaseFocusListener: (((ev: MotionEvent) -> Unit)?) -> Unit,
                context: Context
            ) {
                setupPriorityDropdownMenu(
                    priorityDropdownMenu,
                    priorityDropdownMenuLayout,
                    infoCard,
                    setOnReleaseFocusListener,
                    context
                )
                setupStatusDropdownMenu(
                    statusDropdownMenu,
                    statusDropdownMenuLayout,
                    infoCard,
                    setOnReleaseFocusListener,
                    context
                )
            }

            private fun setupDropdownMenuLayouts(
                priorityDropdownMenuLayout: TextInputLayout,
                statusDropdownMenuLayout: TextInputLayout,
            ) {
                setupDropdownMenuLayout(priorityDropdownMenuLayout)
                setupDropdownMenuLayout(statusDropdownMenuLayout)
            }

            private fun setupDropdownMenuLayout(
                dropdownMenuLayout: TextInputLayout,
            ) {
                dropdownMenuLayout.setStartIconTintMode(PorterDuff.Mode.DST)
            }

            private fun setupPriorityDropdownMenu(
                priorityDropdownMenu: AutoCompleteTextView,
                priorityDropdownMenuLayout: TextInputLayout,
                infoCard: MaterialCardView,
                setOnReleaseFocusListener: (((ev: MotionEvent) -> Unit)?) -> Unit,
                context: Context
            ) {
                setupDropdownMenu(
                    Priority.priorities,
                    Priority::getDrawables,
                    priorityDropdownMenu,
                    priorityDropdownMenuLayout,
                    infoCard,
                    setOnReleaseFocusListener,
                    context
                )
            }

            private fun setupStatusDropdownMenu(
                statusDropdownMenu: AutoCompleteTextView,
                statusDropdownMenuLayout: TextInputLayout,
                infoCard: MaterialCardView,
                setOnReleaseFocusListener: (((ev: MotionEvent) -> Unit)?) -> Unit,
                context: Context
            ) {
                setupDropdownMenu(
                    Status.statuses,
                    Status::getDrawables,
                    statusDropdownMenu,
                    statusDropdownMenuLayout,
                    infoCard,
                    setOnReleaseFocusListener,
                    context
                )
            }

            private fun setupDropdownMenu(
                contents: List<String>,
                drawables: (String) -> Int,
                dropdownMenu: AutoCompleteTextView,
                dropdownMenuLayout: TextInputLayout,
                infoCard: MaterialCardView,
                setOnReleaseFocusListener: (((ev: MotionEvent) -> Unit)?) -> Unit,
                context: Context
            ) {
                val contentAdapter = MaterialSpinnerAdapter(
                    context,
                    R.layout.dropdown_list_item,
                    contents
                )

                dropdownMenu.run {
                    setAdapter(contentAdapter)
                    setOnFocusChangeListener { _, hasFocus ->
                        setOnReleaseFocusListener(if (hasFocus) { event ->
                            getOnReleaseFocusListener(
                                infoCard,
                                event,
                                ::clearFocus
                            )
                        } else null)
                        dropdownMenuLayout.setEndIconActivated(hasFocus)
                    }
                    doOnTextChanged { content, _, _, _ ->
                        // If device is rotated before any selection is made
                        if (content.toString() == "") return@doOnTextChanged

                        dropdownMenuLayout.setStartIconDrawable(drawables(content.toString()))
                    }
                }
            }

            private fun setupSortLinearLayout(
                sortLinearLayout: LinearLayout,
                sortHint: TextView,
                infoCard: MaterialCardView,
                setOnReleaseFocusListener: (((ev: MotionEvent) -> Unit)?) -> Unit,
                context: Context
            ) {
                sortLinearLayout.setOnClickListener { it.requestFocusFromTouch() }

                sortLinearLayout.setOnFocusChangeListener { _, hasFocus ->
                    setOnReleaseFocusListener(if (hasFocus) { event ->
                        getOnReleaseFocusListener(
                            infoCard,
                            event,
                            sortLinearLayout::clearFocus
                        )
                    } else null)
                    sortHint.setTextColor(
                        ContextCompat.getColor(
                            context,
                            if (hasFocus) R.color.colorPrimary else R.color.radio_group_frame_text
                        )
                    )
                }
            }

            private fun setupSortRadioGroups(
                sortTypeRadioGroup: CustomRadioGroup,
                sortOrderRadioGroup: CustomRadioGroup,
                sortLinearLayout: LinearLayout
            ) {
                setupSortTypeRadioGroup(sortTypeRadioGroup, sortLinearLayout)
                setupSortOrderRadioGroup(sortOrderRadioGroup, sortLinearLayout)
            }

            private fun setupSortTypeRadioGroup(
                sortTypeRadioGroup: CustomRadioGroup,
                linearLayout: LinearLayout
            ) {
                setupRadioGroup(sortTypeRadioGroup, linearLayout)
            }

            private fun setupSortOrderRadioGroup(
                sortOrderRadioGroup: CustomRadioGroup,
                linearLayout: LinearLayout
            ) {
                setupRadioGroup(sortOrderRadioGroup, linearLayout)
            }

            private fun setupRadioGroup(
                radioGroup: CustomRadioGroup,
                linearLayout: LinearLayout
            ) {
                radioGroup.setOnClickListener {
                    linearLayout.requestFocusFromTouch()
                }

                radioGroup.setOnCheckedChangeListener { _, checkedId ->
                    radioGroup.checkedId = checkedId
                }
            }

            private fun setupPriorityCustomButton(
                button: CustomImageButton,
                context: Context
            ) {
                setupCustomButton(
                    button,
                    R.menu.priorities_popup_menu,
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
                    R.menu.statuses_popup_menu,
                    context,
                    ::getStatusAction
                )
            }

            private fun setupExpandButton(
                expandButton: CustomImageButton,
                titleEditText: CustomEditText
            ) {
                expandButton.setOnClickListener {
                    expandButton.setImageResource(
                        if (expandButton.imgResource.isExpanded) R.drawable.ic_expand
                        else R.drawable.ic_collapse
                    )

                    // Fixing wierd bug
                    val params = titleEditText.layoutParams
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    titleEditText.layoutParams = params
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
                descriptionRadioButton: RadioButton,
                priorityRadioButton: RadioButton,
                statusRadioButton: RadioButton,
                customRadioButton: RadioButton,
                ascendingRadioButton: RadioButton,
                descendingRadioButton: RadioButton,
                sortLinearLayout: LinearLayout

            ) {
                setupSortRadioButton(descriptionRadioButton, sortLinearLayout)
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

            private fun getOnReleaseFocusListener(
                view: View,
                event: MotionEvent,
                action: () -> Unit
            ) {
                val touchPoint = Point(event.rawX.roundToInt(), event.rawY.roundToInt())
                val viewTouched = isPointInsideViewBounds(
                    view,
                    touchPoint
                )
                if (!viewTouched) action()
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

            private fun getPriorityAction(
                @IdRes itemId: Int,
                priorityButton: CustomImageButton
            ) = try {
                priorityButton.setImageResource(
                    when (itemId) {
                        R.id.popup_priority_high -> R.drawable.ic_priority_high
                        R.id.popup_priority_normal -> R.drawable.ic_priority_normal
                        R.id.popup_priority_low -> R.drawable.ic_priority_low
                        else -> throw IllegalArgumentException("Unknown itemId: $itemId")
                    }
                )
                true
            } catch (e: IllegalArgumentException) {
                // TODO Send exception to the server
                false
            }

            private fun getStatusAction(
                @IdRes itemId: Int,
                statusButton: CustomImageButton
            ) = try {
                statusButton.setImageResource(
                    when (itemId) {
                        R.id.popup_status_active -> R.drawable.ic_status_active
                        R.id.popup_status_on_hold -> R.drawable.ic_status_on_hold
                        R.id.popup_status_completed -> R.drawable.ic_status_completed
                        else -> throw IllegalArgumentException("Unknown itemId: $itemId")
                    }
                )
                true
            } catch (e: IllegalArgumentException) {
                // TODO Send exception to the server
                false
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