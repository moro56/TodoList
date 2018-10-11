package it.emperor.todolist.extension

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.firstVisiblePosition(): Int {
    val lm: RecyclerView.LayoutManager? = this.layoutManager
    return when (lm) {
        is LinearLayoutManager -> lm.findFirstCompletelyVisibleItemPosition()
        is GridLayoutManager -> lm.findFirstCompletelyVisibleItemPosition()
        else -> Int.MAX_VALUE
    }
}