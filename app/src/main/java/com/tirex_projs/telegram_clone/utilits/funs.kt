package com.tirex_projs.telegram_clone.utilits

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.tirex_projs.telegram_clone.R
import de.hdodenhof.circleimageview.CircleImageView

fun Fragment.showToastFrag(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    dataContainerViewId: Int,
    addStack: Boolean = true
) {
    if (addStack) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(dataContainerViewId, fragment)
            .commit()
    }
    else {
        supportFragmentManager.beginTransaction()
            .replace(dataContainerViewId, fragment)
            .commit()
    }
}

fun Fragment.replaceFragment(fragment: Fragment, dataContainerViewId: Int) {
    this.parentFragmentManager.beginTransaction()
        .addToBackStack(null)
        .replace(dataContainerViewId, fragment)
        .commit()
}

fun hideKeyboard(){
    val imm : InputMethodManager = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun ImageView.downloadAndSetImage(url: String){
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.default_photo)
        .into(this)
}