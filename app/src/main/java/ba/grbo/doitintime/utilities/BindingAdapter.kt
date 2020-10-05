package ba.grbo.doitintime.utilities

import android.content.Context
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import ba.grbo.doitintime.R
import ba.grbo.doitintime.data.Priority
import ba.grbo.doitintime.data.Status
import ba.grbo.doitintime.data.ToDo
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

@BindingAdapter("warningMessage", "context", requireAll = true)
fun TextInputLayout.bindErrorMessage(@StringRes warningMessage: Int?, context: Context) {
    error = if (warningMessage != null) context.getString(warningMessage) else null
}

@BindingAdapter("priorityImage")
fun CustomImageButton.bindPriorityImage(priority: Priority?) {
    priority?.let {
        if (tag != it.name) {
            setImageResource(
                when (it) {
                    Priority.High -> R.drawable.ic_priority_high
                    Priority.Normal -> R.drawable.ic_priority_normal
                    Priority.Low -> R.drawable.ic_priority_low
                }
            )
            executed = true
            tag = it.name
        } else if (tag == it.name) {
            setImageResource(
                when (it) {
                    Priority.High -> R.drawable.ic_priority_high
                    Priority.Normal -> R.drawable.ic_priority_normal
                    Priority.Low -> R.drawable.ic_priority_low
                }
            )
        }
    }
}

@InverseBindingAdapter(attribute = "priorityImage")
fun CustomImageButton.getPriority() = Priority.valueOf(tag.toString())

@BindingAdapter("priorityImageAttrChanged")
fun CustomImageButton.setPriorityTagListener(attrChange: InverseBindingListener) {
    setOnTagChangedListener { attrChange.onChange() }
}

@BindingAdapter("statusImage")
fun CustomImageButton.bindStatusImage(status: Status?) {
    status?.let {
        if (tag != it.identifier) {
            setImageResource(
                when (it) {
                    Status.Active -> R.drawable.ic_status_active
                    Status.Completed -> R.drawable.ic_status_completed
                    Status.OnHold -> R.drawable.ic_status_on_hold
                }
            )
            executed = true
            tag = it.identifier
        } else if (tag == it.identifier) {
            setImageResource(
                when (it) {
                    Status.Active -> R.drawable.ic_status_active
                    Status.Completed -> R.drawable.ic_status_completed
                    Status.OnHold -> R.drawable.ic_status_on_hold
                }
            )
        }
    }
}

@InverseBindingAdapter(attribute = "statusImage")
fun CustomImageButton.getStatus() = Status.valueOf(identifier = tag.toString())

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