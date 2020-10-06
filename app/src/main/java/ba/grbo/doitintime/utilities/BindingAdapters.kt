package ba.grbo.doitintime.utilities

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
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