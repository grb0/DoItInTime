package ba.grbo.doitintime.utilities

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import com.google.android.material.textfield.TextInputLayout

class CustomTextInputLayout : TextInputLayout {
    private var customEndIconOnClickListener: OnClickListener? = null
    private var actionUponClick: (() -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        deffStyleAttr: Int
    ) : super(context, attributeSet, deffStyleAttr)

    override fun setEndIconOnClickListener(endIconOnClickListener: OnClickListener?) {
        super.setEndIconOnClickListener(endIconOnClickListener)
        customEndIconOnClickListener = OnClickListener {
            endIconOnClickListener?.onClick(it)
            actionUponClick?.invoke()
        }
    }

    fun setCustomEndIconOnClickListener(actionUponClick: (() -> Unit)?) {
        this.actionUponClick = actionUponClick
        super.setEndIconOnClickListener(customEndIconOnClickListener)
    }
}