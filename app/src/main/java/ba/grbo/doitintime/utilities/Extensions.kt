package ba.grbo.doitintime.utilities

import ba.grbo.doitintime.R

val Int.isExpanded: Boolean
    get() = this == R.drawable.ic_collapse

val Boolean.drawable: Int
    get() = if (this) R.drawable.ic_collapse else R.drawable.ic_expand