import androidx.recyclerview.widget.DiffUtil
import ba.grbo.doitintime.ui.adapters.Item

class ItemDiffCallbacks : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Item, newItem: Item) = when (oldItem) {
        is Item.TaskItem -> {
            newItem as Item.TaskItem
            oldItem.task == newItem.task
        }
        is Item.InfoItem -> {
            newItem as Item.InfoItem
            oldItem.info == newItem.info
        }
    }
}