package com.example.timerddlt.presentation

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.timerddlt.databinding.ActivityStatisticBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.text.SimpleDateFormat
import java.util.*

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

        binding?.day!!.setOnClickListener {
            val yearNow = calendar.get(Calendar.YEAR)
            val monthNow = calendar.get(Calendar.MONTH)
            val dayNow = calendar.get(Calendar.DAY_OF_MONTH)

            val id = Locale("en", "US")
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", id)
            val datePickerDialog =
                DatePickerDialog(
                    this,
                    { view, year, month, dayOfMonth ->
                        calendar.set(year, month, dayOfMonth)
                        content = simpleDateFormat.format(calendar.time)
                        loadPieChart(pieChart!!, content)
                    }, yearNow, monthNow, dayNow
                )
            datePickerDialog.setTitle("Select date")
            datePickerDialog.show()
        }


    }

    private fun loadPieChart(pieChart: PieChart, content: String) {
        initPieChart(pieChart)
        setDataToPieChart(pieChart, content)
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

    private fun setDataToPieChart(pieChart: PieChart, content: String) {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        // Add data here
        dataEntries.add(PieEntry(72f, "Study"))
        dataEntries.add(PieEntry(26f, "Game"))
        dataEntries.add(PieEntry(2f, "Relax"))

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