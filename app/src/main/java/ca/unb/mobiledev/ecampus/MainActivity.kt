package ca.unb.mobiledev.ecampus

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import kotlin.math.max

var maxId:Int = 0

data class Course(
    val name: String,
    val description: String,
    val id: Int
)
public class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_layout)
        loadSavedCourses()

        val root = findViewById<View>(R.id.mainview)
        ViewCompat.setOnApplyWindowInsetsListener(root.parent as View) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout()
            )

            view.updatePadding(
                top = systemBars.top
            )

            insets
        }

    }

    fun toCalendar(view: View) {
        val intent = Intent(this@MainActivity, CalendarActivity::class.java)
        startActivity(intent)
    }
    fun deleteCourse(view: View) {
        val parentView = view.parent as View
        val parentId = parentView.id
        (parentView.parent as? ViewGroup)?.removeView(parentView)
        removeCourse(parentId)

    }
    fun newCourse(view: View) {
        val popupView = layoutInflater.inflate(R.layout.new_course_popup, null)

        val popup = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupView.findViewById<Button>(R.id.buttonCancel)
            .setOnClickListener {
                popup.dismiss()
            }

        popupView.findViewById<Button>(R.id.buttonAccept)
            .setOnClickListener {
                val name = popupView.findViewById<TextInputEditText>(R.id.popupCourseName).text.toString()
                if (name.trim() !=""){
                    popup.dismiss()
                    maxId+=1
                    addCourse(Course(name.trim(),"",maxId ))
                }

            }

        popup.showAtLocation(
            findViewById(R.id.mainview),
            Gravity.CENTER,
            0,
            0
        )

        popup.isOutsideTouchable = false
        popup.elevation = 8f
    }
    fun addCourse(newCourse: Course){
        val file = File(filesDir, "courses.json")
        val jsonArray = if (file.exists()) {
            JSONArray(file.readText())
        } else {
            JSONArray()
        }

        val jsonObject = JSONObject().apply {
            put("name", newCourse.name)
            put("description", newCourse.description)
            put("id", maxId)
        }
        jsonArray.put(jsonObject)
        file.writeText(jsonArray.toString())

        val container = findViewById<LinearLayout>(R.id.mainview)

        val newView = layoutInflater.inflate(
            R.layout.course_list_entry,
            container,
            false
        )
        container.addView(newView,1)

        newView.findViewById<TextView>(R.id.courseName).text = newCourse.name
        newView.setId(jsonObject.getString("id").toInt())
    }
    fun removeCourse(courseId: Int){
        val file = File(filesDir, "courses.json")
        val jsonArray = if (file.exists()) {
            JSONArray(file.readText())
        } else {
            return
        }

        for (i in jsonArray.length() - 1 downTo 0) {
            val item = jsonArray.getJSONObject(i)

            if (item.optString("id") == courseId.toString()) {
                jsonArray.remove(i)
            }
        }
        file.writeText(jsonArray.toString())

    }
    fun loadSavedCourses(){
        val file = File(filesDir, "courses.json")
        val jsonArray = if (file.exists()) {
            JSONArray(file.readText())
        } else {
            return
        }

        val container = findViewById<LinearLayout>(R.id.mainview)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("name")
            maxId = max(maxId,jsonObject.getString("id").toInt())
            val newView = layoutInflater.inflate(
                R.layout.course_list_entry,
                container,
                false
            )
            container.addView(newView, 1)
            newView.findViewById<TextView>(R.id.courseName).text = name
            newView.setId(jsonObject.getString("id").toInt())
        }
    }
}
