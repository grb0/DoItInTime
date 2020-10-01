package ba.grbo.doitintime.utilities

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ba.grbo.doitintime.data.ToDo
import ba.grbo.doitintime.ui.adapters.ToDoAdapter

@BindingAdapter("toDo")
fun RecyclerView.bindToDo(toDo: ToDo) {
    (adapter as ToDoAdapter).wrapDataAndSubmitList(toDo)
}