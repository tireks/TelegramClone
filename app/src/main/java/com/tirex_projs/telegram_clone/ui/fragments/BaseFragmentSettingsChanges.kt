package com.tirex_projs.telegram_clone.ui.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.viewbinding.ViewBinding
import com.tirex_projs.telegram_clone.MainActivity
import com.tirex_projs.telegram_clone.R
import com.tirex_projs.telegram_clone.utilits.APP_ACTIVITY
import com.tirex_projs.telegram_clone.utilits.hideKeyboard

abstract class BaseFragmentSettingsChanges<VB : ViewBinding> : BaseFragmentPrior<VB>(){
    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
        setHasOptionsMenu(true)
        hideKeyboard()
    }

    override fun onStop() {
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        APP_ACTIVITY.menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_changeName_menu_confirm_change -> change()
        }
        return true
    }

    open fun change() {

    }
}
/*abstract class BaseFragment<VB: ViewBinding> : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflateViewBinding(inflater, container)
        (activity as MainActivity).mAppDrawer.disableDrawer()
        return binding.root
    }
    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onStop() {
        (activity as MainActivity).mAppDrawer.enableDrawer()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}*/