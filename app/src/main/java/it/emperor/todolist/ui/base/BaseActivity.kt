package it.emperor.todolist.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerAppCompatActivity
import it.emperor.todolist.R
import it.emperor.todolist.view.NavigationView
import kotlinx.android.synthetic.main._toolbar.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding, T : ViewModel> : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected var navigationView: NavigationView? = null
    protected lateinit var binding: B
    protected lateinit var viewModel: T

    protected var useBus: Boolean = false

    @LayoutRes
    protected abstract fun layoutRes(): Int

    protected abstract fun viewModelClass(): Class<T>

    protected abstract fun bind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes())
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(viewModelClass())

        toolbar?.let {
            setSupportActionBar(it)
            it.navigationIcon = null
        }

        findViewById<NavigationView>(R.id.navigation_view)?.setOnClickCallback {
            this.onBackPressed()
        }

        bind()
    }

    override fun onStart() {
        super.onStart()
        if (useBus) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if (useBus) {
            EventBus.getDefault().unregister(this)
        }
    }
}