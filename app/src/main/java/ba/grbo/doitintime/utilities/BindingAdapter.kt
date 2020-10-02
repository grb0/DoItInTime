package ba.grbo.doitintime.utilities

import android.content.Context
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ba.grbo.doitintime.data.ToDo
import ba.grbo.doitintime.ui.adapters.ToDoAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("toDo")
fun RecyclerView.bindToDo(toDo: ToDo?) {
    toDo?.let { (adapter as ToDoAdapter).wrapDataAndSubmitList(it) }

}

@BindingAdapter("adapter")
fun RecyclerView.bindAdapter(adapter: ToDoAdapter?) {
    adapter?.let { this.adapter = it }
}

@BindingAdapter("warningMessage", "context", requireAll = true)
fun TextInputLayout.bindErrorMessage(@StringRes warningMessage: Int?, context: Context) {
    error = if (warningMessage != null) context.getString(warningMessage) else null
}

//fun AutoCompleteTextView.bindText()