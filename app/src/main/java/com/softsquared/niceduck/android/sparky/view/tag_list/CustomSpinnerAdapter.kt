package com.softsquared.niceduck.android.sparky.view.tag_list

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.softsquared.niceduck.android.sparky.R

class CustomSpinnerAdapter(context: Context, private val list: MutableList<String>) :
    BaseAdapter() {
    private val inflater: LayoutInflater =
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
    private var text: String? = null
    // 스피너에서 선택된 아이템을 액티비티에서 꺼내오는 메서드
    var item: String? = null
        private set

    override fun getCount(): Int {
        return list.size - 1
    }

    override fun getItem(position: Int): String{
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // 화면에 들어왔을 때 보여지는 텍스트뷰 설정
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var mConvertView = convertView
        if (mConvertView == null) mConvertView =
            inflater.inflate(R.layout.spinner_outer_view, parent, false)
        return mConvertView!!
    }

    // 클릭 후 나타나는 텍스트뷰 설정
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var mConvertView = convertView
        if (mConvertView == null) mConvertView =
            inflater.inflate(R.layout.spinner_inner_view, parent, false)

        mConvertView!!.findViewById<TextView>(R.id.spinner_text).text = list[position]

        if (position == 1) {
            mConvertView!!.findViewById<TextView>(R.id.spinner_text).setTextColor(Color.parseColor("#FF7143"))
            mConvertView!!.findViewById<View>(R.id.spinner_view).visibility = GONE
        }

        return mConvertView
    }


}