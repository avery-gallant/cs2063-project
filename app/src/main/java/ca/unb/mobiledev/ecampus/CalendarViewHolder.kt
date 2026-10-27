package ca.unb.mobiledev.ecampus

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.widget.TextView
public class CalendarViewHolder(itemView: View, val onItemListener : CalendarAdapter.OnItemListener) : ViewHolder(itemView), View.OnClickListener{
    public val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
    override fun onClick(v: View?) {
        onItemListener.onItemClick(absoluteAdapterPosition,(dayOfMonth.text).toString())
    }

    init {
    itemView.setOnClickListener(this)
    }

}