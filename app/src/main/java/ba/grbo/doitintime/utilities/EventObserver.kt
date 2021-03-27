package ba.grbo.doitintime.utilities

import androidx.lifecycle.Observer

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(
    private val onEventUnhandledContent: (T) -> Unit,
    private val onEventIsNull: () -> Unit = {}
) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        if (event != null) event.getContentIfNotHandled()?.let { onEventUnhandledContent(it) }
        else onEventIsNull()
    }
}