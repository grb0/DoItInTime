package ba.grbo.doitintime.utilities

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import ba.grbo.doitintime.R
import ba.grbo.doitintime.data.*
import ba.grbo.doitintime.ui.adapters.ToDoAdapter

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
    priority?.let {
        if (tag != it) {
            setImageResource(Priority.getDrawables(it))
            executed = true
            tag = it
        } else if (tag == it) setImageResource(Priority.getDrawables(it))
    }
}

@InverseBindingAdapter(attribute = "priorityImage")
fun CustomImageButton.getPriority(): Priority = tag as Priority

@BindingAdapter("priorityImageAttrChanged")
fun CustomImageButton.setPriorityTagListener(attrChange: InverseBindingListener) {
    setOnTagChangedListener { attrChange.onChange() }
}

@BindingAdapter("statusImage")
fun CustomImageButton.bindStatusImage(status: Status?) {
    status?.let {
        if (tag != it) {
            setImageResource(Status.getDrawables(it))
            executed = true
            tag = it
        } else if (tag == it) setImageResource(Status.getDrawables(it))
    }
}

@InverseBindingAdapter(attribute = "statusImage")
fun CustomImageButton.getStatus(): Status = tag as Status

@BindingAdapter("statusImageAttrChanged")
fun CustomImageButton.setStatusTagListener(attrChange: InverseBindingListener) {
    setOnTagChangedListener { attrChange.onChange() }
}

@BindingAdapter("expandImage")
fun CustomImageButton.bindExpandImage(state: Boolean?) {
    state?.let {
        if (tag != it) {
            setImageResource(if (it) R.drawable.ic_collapse else R.drawable.ic_expand)
            executed = true
            tag = it
        } else if (tag == it) {
            setImageResource(if (it) R.drawable.ic_collapse else R.drawable.ic_expand)
        }
    }
}

@InverseBindingAdapter(attribute = "expandImage")
fun CustomImageButton.getState(): Boolean = tag as Boolean

@BindingAdapter("expandImageAttrChanged")
fun CustomImageButton.setStateTagListener(attrChange: InverseBindingListener) {
    setOnTagChangedListener { attrChange.onChange() }
}

@BindingAdapter("checkedTasksSortingTypeRadioButton")
fun CustomRadioGroup.bindCheckedTasksSortingTypeRadioButton(tasksSortingType: TasksSortingType?) {
    tasksSortingType?.let {
        if (tag != it) {
            check(TasksSortingType.getCheckedRadioButtonId(it))
            executed = true
            tag = it
        } else if (tag == it) check(TasksSortingType.getCheckedRadioButtonId(it))
    }
}

@InverseBindingAdapter(attribute = "checkedTasksSortingTypeRadioButton")
fun CustomRadioGroup.getTasksSortingType(): TasksSortingType = tag as TasksSortingType

@BindingAdapter("checkedTasksSortingTypeRadioButtonAttrChanged")
fun CustomRadioGroup.setTasksSortingTypeTagListener(attrChange: InverseBindingListener) {
    setOnTagChangedListener { attrChange.onChange() }
}

@BindingAdapter("checkedTasksSortingOrderRadioButton")
fun CustomRadioGroup.bindCheckedTasksSortingOrderRadioButton(
    tasksSortingOrder: TasksSortingOrder?
) {
    tasksSortingOrder?.let {
        if (tag != it) {
            check(TasksSortingOrder.getCheckedRadioButtonId(it))
            executed = true
            tag = it
        } else if (tag == it) check(TasksSortingOrder.getCheckedRadioButtonId(it))
    }
}

@InverseBindingAdapter(attribute = "checkedTasksSortingOrderRadioButton")
fun CustomRadioGroup.getTasksSortingOrder(): TasksSortingOrder = tag as TasksSortingOrder

@BindingAdapter("checkedTasksSortingOrderRadioButtonAttrChanged")
fun CustomRadioGroup.setTasksSortingOrderTagListener(attrChange: InverseBindingListener) {
    setOnTagChangedListener { attrChange.onChange() }
}