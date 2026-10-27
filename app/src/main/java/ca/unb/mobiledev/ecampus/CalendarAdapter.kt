package ca.unb.mobiledev.ecampus
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter

class CalendarAdapter(val daysOfMonth: ArrayList<String>, val onItemListener: OnItemListener) : Adapter<CalendarViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarViewHolder {
        var inflator: LayoutInflater = LayoutInflater.from(parent.context)
        var view: View = inflator.inflate(R.layout.calendar_cell, parent, false)
        var layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = (parent.height*0.16666666).toInt()
        return CalendarViewHolder(view,onItemListener)
    }

    override fun onBindViewHolder(
        holder: CalendarViewHolder,
        position: Int
    ) {
        holder.dayOfMonth.text = daysOfMonth[position]
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    public interface OnItemListener
    {
        fun onItemClick(position: Int, dayText:String)
    }
}