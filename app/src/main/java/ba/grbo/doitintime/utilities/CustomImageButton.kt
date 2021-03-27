package ba.grbo.doitintime.utilities

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton

class CustomImageButton : AppCompatImageButton {
    private var onImageResourceChangedListener: (() -> Unit)? = null

    @DrawableRes
    var imgResource: Int = -1
        set(value) {
            field = value
            onImageResourceChangedListener?.invoke()
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        deffStyleAttr: Int
    ) : super(context, attributeSet, deffStyleAttr)

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        imgResource = resId
    }

    fun setOnImageResourceChangedListener(onImageResourceChangedListener: () -> Unit) {
        this.onImageResourceChangedListener = onImageResourceChangedListener
    }
}