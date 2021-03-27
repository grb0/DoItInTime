package ba.grbo.doitintime.utilities

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText : AppCompatEditText {
    var originalText = text.toString()
    var actionUponSelectionChanged: (() -> Unit)? = null
    var actionUponFocusChanged: ((Boolean) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        deffStyleAttr: Int
    ) : super(context, attributeSet, deffStyleAttr)

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        if (hasFocus()) actionUponSelectionChanged?.invoke()
        super.onSelectionChanged(selStart, selEnd)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) originalText = text.toString()
        actionUponFocusChanged?.invoke(focused)
    }

    fun restoreSelection(start: Int, end: Int) {
        if (start != 0 || end != 0) {
            if (start != end) setSelection(start, end)
            else setSelection(start)
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
    }
}