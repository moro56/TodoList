package it.emperor.todolist.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import it.emperor.todolist.R
import kotlinx.android.synthetic.main._navigation.view.*

class NavigationView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var onClick: () -> Unit = {}

    init {
        View.inflate(context, R.layout._navigation, this)

        go_back.setOnClickListener {
            onClick()
        }
    }

    fun setOnClickCallback(onBack: () -> Unit = {}) {
        this.onClick = onBack
    }
}