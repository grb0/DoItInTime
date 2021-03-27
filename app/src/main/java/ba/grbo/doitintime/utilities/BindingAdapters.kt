package ba.grbo.doitintime.utilities

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import ba.grbo.doitintime.data.*
import ba.grbo.doitintime.ui.adapters.ToDoAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("toDo")
fun RecyclerView.bindToDo(toDo: ToDo?) {
    toDo?.let { (adapter as ToDoAdapter).wrapToDoAndSubmitList(it) }

}

@BindingAdapter("adapter")
fun RecyclerView.bindAdapter(adapter: ToDoAdapter?) {
    adapter?.let { this.adapter = it }
}

@BindingAdapter("priorityImage")
fun CustomImageButton.bindPriorityImage(priority: Priority?) {
    priority?.let { if (imgResource != it.drawable) setImageResource(it.drawable) }
}

@InverseBindingAdapter(attribute = "priorityImage")
fun CustomImageButton.getPriority(): Priority = Priority.valueOf(imgResource)

@BindingAdapter("priorityImageAttrChanged")
fun CustomImageButton.setPriorityTagListener(attrChange: InverseBindingListener) {
    setOnImageResourceChangedListener { attrChange.onChange() }
}

@BindingAdapter("statusImage")
fun CustomImageButton.bindStatusImage(status: Status?) {
    status?.let { if (imgResource != it.drawable) setImageResource(it.drawable) }
}

@InverseBindingAdapter(attribute = "statusImage")
fun CustomImageButton.getStatus(): Status = Status.valueOf(imgResource)

@BindingAdapter("statusImageAttrChanged")
fun CustomImageButton.setStatusTagListener(attrChange: InverseBindingListener) {
    setOnImageResourceChangedListener { attrChange.onChange() }
}

@BindingAdapter("expandImage")
fun CustomImageButton.bindExpandImage(state: Boolean?) {
    state?.let { if (imgResource != it.drawable) setImageResource(it.drawable) }
}

@InverseBindingAdapter(attribute = "expandImage")
fun CustomImageButton.getState(): Boolean = imgResource.isExpanded

@BindingAdapter("expandImageAttrChanged")
fun CustomImageButton.setStateTagListener(attrChange: InverseBindingListener) {
    setOnImageResourceChangedListener { attrChange.onChange() }
}

@BindingAdapter("checkedTasksSortingTypeRadioButton")
fun CustomRadioGroup.bindCheckedTasksSortingTypeRadioButton(tasksSortingType: TasksSortingType?) {
    tasksSortingType?.let { if (checkedId != it.id) check(it.id) }
}

@InverseBindingAdapter(attribute = "checkedTasksSortingTypeRadioButton")
fun CustomRadioGroup.getTasksSortingType(): TasksSortingType =
    TasksSortingType.valueOf(checkedId)

@BindingAdapter("checkedTasksSortingTypeRadioButtonAttrChanged")
fun CustomRadioGroup.setTasksSortingTypeTagListener(attrChange: InverseBindingListener) {
    setOnCheckedIdChangedListener { attrChange.onChange() }
}

@BindingAdapter("checkedTasksSortingOrderRadioButton")
fun CustomRadioGroup.bindCheckedTasksSortingOrderRadioButton(
    tasksSortingOrder: TasksSortingOrder?
) {
    tasksSortingOrder?.let { if (checkedId != it.id) check(it.id) }
}

@InverseBindingAdapter(attribute = "checkedTasksSortingOrderRadioButton")
fun CustomRadioGroup.getTasksSortingOrder(): TasksSortingOrder =
    TasksSortingOrder.valueOf(checkedId)

@BindingAdapter("checkedTasksSortingOrderRadioButtonAttrChanged")
fun CustomRadioGroup.setTasksSortingOrderTagListener(attrChange: InverseBindingListener) {
    setOnCheckedIdChangedListener { attrChange.onChange() }
}

@BindingAdapter("restoreFocus")
fun View.bindRestoreFocus(focusedView: Triple<Int, Any?, Int?>?) {
    focusedView?.let {
        if (id == focusedView.first && this is TextInputLayout) {
            (editText as AutoCompleteTextView).run {
                if (!hasFocus()) {
                    requestFocusFromTouch()
                    if (!(focusedView.second as Boolean)) doOnAttach { dismissDropDown() }
                }
            }
        } else if (id == focusedView.first && !hasFocus()) {
            when (this) {
                is ConstraintLayout -> callOnClick()
                is CustomEditText -> {
                    post {
                        restoreSelection(focusedView.second as Int, focusedView.third as Int)
                        setSelectAllOnFocus(false)
                        requestFocusFromTouch()
                        setSelectAllOnFocus(true)
                    }
                }
                is CustomImageButton -> doOnAttach { callOnClick() }
                is AutoCompleteTextView -> {
                    requestFocusFromTouch()
                    if (!(focusedView.second as Boolean)) doOnAttach { this.dismissDropDown() }
                }
                is LinearLayout -> requestFocusFromTouch()
                else -> throw IllegalArgumentException("Unknown view: ${javaClass.name}")
            }
        }
    }
}

@InverseBindingAdapter(attribute = "restoreFocus")
fun View.setRestoreFocusedView(): Triple<Int, Any?, Int?> = when (this) {
    is CustomEditText -> Triple(id, selectionStart, selectionEnd)
    is AutoCompleteTextView -> Triple(id, isPopupShowing, null)
    is TextInputLayout -> Triple(id, (editText as AutoCompleteTextView).isPopupShowing, null)
    is ConstraintLayout,
    is CustomImageButton,
    is LinearLayout -> Triple(id, null, null)
    else -> throw IllegalArgumentException("Unknown view: ${javaClass.name}")
}

@BindingAdapter("restoreFocusAttrChanged")
fun View.setOnRestoreFocusedViewChangeListener(attrChange: InverseBindingListener) {
    when (this) {
        is ConstraintLayout -> setOnClickListener { attrChange.onChange() }
        is CustomEditText -> {
            actionUponSelectionChanged = { attrChange.onChange() }
            setOnFocusChangeListener { _, hasFocus -> if (hasFocus) attrChange.onChange() }
        }
        is CustomImageButton -> setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) attrChange.onChange()
        }
        is AutoCompleteTextView -> {
            setOnClickListener { attrChange.onChange() }
            setOnItemClickListener { _, _, _, _ ->
                dismissDropDown()
                attrChange.onChange()
            }
        }
        is CustomTextInputLayout -> setCustomEndIconOnClickListener { attrChange.onChange() }
        is LinearLayout -> setOnClickListener { attrChange.onChange() }
        else -> throw IllegalArgumentException("Unknown view: ${javaClass.name}")
    }
}