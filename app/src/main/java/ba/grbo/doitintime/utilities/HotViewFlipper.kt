package ba.grbo.doitintime.utilities

import android.content.Context
import android.util.AttributeSet
import android.widget.ViewFlipper
import ba.grbo.doitintime.R

class HotViewFlipper : ViewFlipper {
    private val initialView: Int

    constructor(context: Context) : super(context) {
        initialView = 0
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val styled = context.theme.obtainStyledAttributes(
            attrs, R.styleable.HotViewFlipperAttributes, 0, 0)

        initialView = try {
            styled.getInteger(
                R.styleable.HotViewFlipperAttributes_initialView, 0)
        } finally {
            styled.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        displayedChild = initialView % childCount
    }
}