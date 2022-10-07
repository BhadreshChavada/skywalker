package com.skywalker.ui.main.adapter

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.skywalker.R
import java.util.*

class WalkthroughAdapter(
    context: Context,
    var images: IntArray,
    var text: Array<String>
) : PagerAdapter() {

    var mLayoutInflater: LayoutInflater
    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.adapter_walkthrough, container, false)

        val imageView = itemView.findViewById<View>(R.id.ivWtImage) as ImageView
        val description = itemView.findViewById<View>(R.id.tvDescription) as TextView

        imageView.setImageResource(images[position])
        description.setText(text[position])


        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }


    init {
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}