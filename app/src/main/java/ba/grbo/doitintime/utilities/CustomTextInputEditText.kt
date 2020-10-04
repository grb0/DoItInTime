package ba.grbo.doitintime.utilities

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.textfield.TextInputEditText

class CustomTextInputEditText : TextInputEditText {
    private var onSelectionChangedListener: (() -> Unit)? = null
    var positionStart = 0
    var positionEnd = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        deffStyleAttr: Int
    ) : super(context, attributeSet, deffStyleAttr)

    fun setOnSelectionChangedListener(onSelectionChangedListener: () -> Unit) {
        this.onSelectionChangedListener = onSelectionChangedListener
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        positionStart = selStart
        positionEnd = selEnd

        Log.i("ToDoAdapter", "start: $selStart")
        Log.i("ToDoAdapter", "end: $selEnd")
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
//        if (positionStart != selectionStart && positionEnd != selectionEnd) {
//            setSelection(positionStart, positionEnd)
//        }
    }
}