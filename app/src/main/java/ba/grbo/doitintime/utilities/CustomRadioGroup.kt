package ba.grbo.doitintime.utilities

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioGroup
import androidx.annotation.IdRes

class CustomRadioGroup : RadioGroup {
    private var onCheckedIdChangedListener: (() -> Unit)? = null

    @IdRes
    var checkedId: Int = -1
        set(value) {
            field = value
            onCheckedIdChangedListener?.invoke()
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    fun setOnCheckedIdChangedListener(onCheckedIdChangedListener: () -> Unit) {
        this.onCheckedIdChangedListener = onCheckedIdChangedListener
    }
}