package clem.project.buzz.utils

import android.util.Base64

fun String.toUtf8() : String {
    return String(Base64.decode(this, Base64.DEFAULT), Charsets.UTF_8)
}