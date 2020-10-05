package ba.grbo.doitintime.utilities

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton

class CustomImageButton : AppCompatImageButton {
    private var onTagChangedListener: (() -> Unit)? = null
    var executed = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        deffStyleAttr: Int
    ) : super(context, attributeSet, deffStyleAttr)

    override fun setTag(tag: Any?) {
        super.setTag(tag)
        if (!executed) onTagChangedListener?.invoke()
        executed = false
    }

    fun setOnTagChangedListener(onTagChangedListener: () -> Unit) {
        this.onTagChangedListener = onTagChangedListener
    }
}