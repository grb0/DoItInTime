package ba.grbo.doitintime.utilities

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText : AppCompatEditText {
    var originalText = text.toString()

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        deffStyleAttr: Int
    ) : super(context, attributeSet, deffStyleAttr)

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) originalText = text.toString()
    }
}