package com.example.timerddlt.presentation

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.timerddlt.databinding.ActivityStatisticBinding
import com.example.timerddlt.domain.model.Event
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StatisticActivity : AppCompatActivity() {
    private var binding: ActivityStatisticBinding? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarStatistic)
        binding?.toolbarStatistic!!.setNavigationOnClickListener {
            onBackPressed()
        }
        val pieChart = binding?.pieChart

        val calendar = Calendar.getInstance()
        var content: String = ""

        var yearNow = calendar.get(Calendar.YEAR)
        var monthNow = calendar.get(Calendar.MONTH)
        var dayNow = calendar.get(Calendar.DAY_OF_MONTH)

        val id = Locale("en", "US")
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", id)
        content = simpleDateFormat.format(calendar.time)
        binding?.tvDay!!.text = "Date: $content"
        loadPieChart(pieChart!!, content)

        binding?.iconSelect!!.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(
                    this,
                    { view, year, month, dayOfMonth ->
                        calendar.set(year, month, dayOfMonth)
                        content = simpleDateFormat.format(calendar.time)
                        binding?.tvDay!!.text = "Date: $content"
                        loadPieChart(pieChart, content)
                        yearNow = year
                        monthNow = month
                        dayNow = dayOfMonth
                    }, yearNow, monthNow, dayNow
                )
            datePickerDialog.setTitle("Select date")
            datePickerDialog.show()
        }


    }

    private fun loadPieChart(pieChart: PieChart, content: String) {
        initPieChart(pieChart)
        val dataList1: List<Event> = listOf(
            Event("Study", "abc", 123, 234, 456, true),
            Event("Study", "abc", 123, 234, 456, true),
            Event("Study", "abc", 123, 234, 456, true),
            Event("Sport", "abc", 123, 234, 456, true),
            Event("Sport", "abc", 123, 234, 456, true),
            Event("Relax", "abc", 123, 234, 456, true),
            Event("Relax", "abc", 123, 234, 456, true),
            Event("Relax", "abc", 123, 234, 456, true)
        )
        val dataList = ArrayList(dataList1)
        val hashMap: HashMap<String, Float> = HashMap<String, Float>()
        for (i in dataList) {
            if (i.title in hashMap.keys) {
                hashMap[i.title] = hashMap[i.title]!! + 1
            } else {
                hashMap[i.title] = 1F
            }
        }
        for (i in hashMap.keys) {
            hashMap[i] = (hashMap[i]!! / dataList.size * 10000).toInt().toFloat() / 100
        }

        setDataToPieChart(pieChart, hashMap, content)
    }

    private fun initPieChart(pieChart: PieChart) {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        //adding padding
        pieChart.setExtraOffsets(20f, 0f, 20f, 20f)
        pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.isWordWrapEnabled = true

    }

    private fun setDataToPieChart(
        pieChart: PieChart,
        hashMap: HashMap<String, Float>,
        content: String
    ) {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        // Add data here
        for (i in hashMap.keys) {
            dataEntries.add(PieEntry(hashMap[i]!!, i))
        }
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#4DD0E1"))
        colors.add(Color.parseColor("#FFF176"))
        colors.add(Color.parseColor("#FF8A65"))
        colors.add(Color.parseColor("#AA8A65"))
        colors.add(Color.parseColor("#BB8A65"))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(15f)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        //create hole in center
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)


        //add text in center
        pieChart.setDrawCenterText(true);
        pieChart.centerText = "Statistic in $content"

        pieChart.invalidate()

    }
}