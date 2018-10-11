package it.emperor.todolist.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import it.emperor.todolist.R
import it.emperor.todolist.data.converter.RealmChangeSetResults
import it.emperor.todolist.data.model.Todo
import it.emperor.todolist.extension.gone
import it.emperor.todolist.extension.visible
import it.emperor.todolist.ui.base.BaseRealmRecyclerView
import it.emperor.todolist.ui.main.event.TodoCheckedEvent
import kotlinx.android.synthetic.main.home_list_item.view.*
import org.greenrobot.eventbus.EventBus

class RecyclerViewAdapter(lifecycleOwner: LifecycleOwner, val data: LiveData<RealmChangeSetResults<Todo>>) : BaseRealmRecyclerView<RecyclerViewAdapter.ViewHolder>() {

    private var items: RealmResults<Todo>? = data.value?.realmResults

    init {
        setHasStableIds(true)
        data.observe(lifecycleOwner, Observer {
            items = it.realmResults
            onChange(it?.changeSet)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false))

    override fun getItemCount(): Int = items?.size ?: 0

    override fun getItemId(position: Int): Long {
        val todo = items?.get(position)
        return todo?.id ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items?.get(position))
    }

    class ViewHolder(private val viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        fun bind(todo: Todo?) {
            viewItem.title.text = todo?.title

            viewItem.description.text = todo?.description
            todo?.description?.let {
                viewItem.description.visible()
            } ?: kotlin.run {
                viewItem.description.gone()
            }

            viewItem.animatedCheckBox.setChecked(todo?.done ?: false)
            viewItem.animatedCheckBox.setOnChangeListener {
                EventBus.getDefault().post(TodoCheckedEvent(todo?.id, it))
            }
            viewItem.setOnClickListener {
                viewItem.animatedCheckBox.performClick()
            }
        }
    }
}