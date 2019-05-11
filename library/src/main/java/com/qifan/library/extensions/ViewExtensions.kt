package com.qifan.library.extensions

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StyleableRes

/**
 * Created by Qifan on 2019-05-11.
 */
inline fun View.withStyleableRes(set: AttributeSet?, @StyleableRes attrs: IntArray, init: TypedArray.() -> Unit) {
    val typedArray = context.theme.obtainStyledAttributes(set, attrs, 0, 0)
    if (typedArray != null) {
        try {
            typedArray.init()
        } finally {
            typedArray.recycle()
        }
    }
}