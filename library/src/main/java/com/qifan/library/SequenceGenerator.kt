package com.qifan.library

import android.util.Log
import androidx.annotation.IntRange

/**
 * Created by Qifan on 2019-05-11.
 */
class SequenceGenerator {
    private var currentIndex = -1

    @IntRange(from = 0)
    fun nextIndex(numberOfElements: Int): Int {
        if (numberOfElements < 1) {
            Log.w("SequenceGenerator", "You need have at least 1 element to work properly")
            return 0
        }
        currentIndex = nextIndex(currentIndex, numberOfElements)
        return currentIndex
    }

    private fun nextIndex(currentIndex: Int, numberOfElements: Int): Int {
        var index = currentIndex + 1
        index = if (index < numberOfElements) index else 0
        return index
    }
}