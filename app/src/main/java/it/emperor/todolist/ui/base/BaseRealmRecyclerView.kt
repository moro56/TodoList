package it.emperor.todolist.ui.base

import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedCollectionChangeSet
import it.emperor.todolist.extension.firstVisiblePosition


abstract class BaseRealmRecyclerView<V : RecyclerView.ViewHolder> : RecyclerView.Adapter<V>() {

    var recyclerView: RecyclerView? = null

    fun onChange(changeSet: OrderedCollectionChangeSet?) {
        if (changeSet == null) {
            notifyDataSetChanged()
            return
        }

        // For deletions, the adapter has to be notified in reverse order.
        val deletions = changeSet.deletionRanges
        for (i in deletions.indices.reversed()) {
            val range = deletions[i]
            notifyItemRangeRemoved(range.startIndex, range.length)
        }

        val insertions = changeSet.insertionRanges
        for (range in insertions) {
            notifyItemRangeInserted(range.startIndex, range.length)

            if (recyclerView?.firstVisiblePosition() == 0) {
                recyclerView?.scrollToPosition(0)
            }
        }

        val modifications = changeSet.changeRanges
        for (range in modifications) {
            notifyItemRangeChanged(range.startIndex, range.length)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }
}