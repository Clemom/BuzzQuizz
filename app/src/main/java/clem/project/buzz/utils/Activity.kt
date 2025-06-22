package clem.project.buzz.utils

import android.content.Context
import android.content.Intent

fun switchActivity(
    from: Context,
    to: Class<out Context>,
    configure: Intent.() -> Unit = {}
) {
    val intent = Intent(from, to).apply(configure)
    from.startActivity(intent)
}