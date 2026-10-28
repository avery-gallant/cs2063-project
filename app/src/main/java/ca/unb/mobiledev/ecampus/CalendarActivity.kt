package ca.unb.mobiledev.ecampus

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


public class CalendarActivity : AppCompatActivity(), CalendarAdapter.OnItemListener
{
    private lateinit var monthYearText : TextView
    private lateinit var calendarRecyclerView : RecyclerView
    private lateinit var selectedDate : LocalDate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.calendar_layout)
        monthYearText = findViewById(R.id.monthYearTV)
        calendarRecyclerView= findViewById(R.id.caledarRecyclerView)
        selectedDate  = LocalDate.now()
        setMonthView()

    }
    private fun setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate))
        val daysInMonth: ArrayList<String?> = daysInMonthArray(selectedDate)

        val calendarAdapter = CalendarAdapter(daysInMonth as ArrayList<String>, this)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(getApplicationContext(), 7)
        calendarRecyclerView.setLayoutManager(layoutManager)
        calendarRecyclerView.setAdapter(calendarAdapter)
    }
    private fun daysInMonthArray(date: LocalDate): ArrayList<String?> {
        val daysInMonthArray = ArrayList<String?>()
        val yearMonth = YearMonth.from(date)

        val daysInMonth = yearMonth.lengthOfMonth()

        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.getDayOfWeek().getValue()

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    fun previousMonthAction(view: View?) {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction(view: View?) {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    public override fun onItemClick(position: Int, dayText: String) {
        if (dayText != "") {
            val message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}
