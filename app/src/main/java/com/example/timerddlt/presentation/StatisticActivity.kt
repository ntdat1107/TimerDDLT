package com.example.timerddlt.presentation

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.timerddlt.data.repository.TimerRepositoryImpl
import com.example.timerddlt.databinding.ActivityStatisticBinding
import com.example.timerddlt.domain.model.Event
import com.example.timerddlt.domain.repository.TimerRepository
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

    private lateinit var timerRepositoryImpl: TimerRepository
    private lateinit var vm: MainViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        timerRepositoryImpl = TimerRepositoryImpl.provideTimerRepositoryImpl(applicationContext)
        vm = MainViewModel(timerRepositoryImpl)

        setSupportActionBar(binding?.toolbarStatistic)
        binding?.toolbarStatistic!!.setNavigationOnClickListener {
            onBackPressed()
        }
        val pieChart = binding?.pieChart

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        var content: String = ""

        var yearNow = calendar.get(Calendar.YEAR)
        var monthNow = calendar.get(Calendar.MONTH)
        var dayNow = calendar.get(Calendar.DAY_OF_MONTH)

        val id = Locale("en", "US")
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", id)
        content = simpleDateFormat.format(calendar.time)
        binding?.tvDay!!.text = "Date: $content"

        vm.getEventsByDateHelper(calendar.timeInMillis)
        Handler().postDelayed({
            val eventLists = vm.getEventsByDateResult()
            if (eventLists.isEmpty()) {
                binding?.tvNoItem!!.visibility = View.VISIBLE
                binding?.pieChart!!.visibility = View.GONE
            } else {
                binding?.pieChart!!.visibility = View.VISIBLE
                binding?.tvNoItem!!.visibility = View.GONE
                loadPieChart(pieChart!!, content, eventLists)
            }
        }, 500)



        binding?.iconSelect!!.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(
                    this,
                    { _, year, month, dayOfMonth ->
                        calendar.set(year, month, dayOfMonth)
                        content = simpleDateFormat.format(calendar.time)
                        binding?.tvDay!!.text = "Date: $content"


                        vm.getEventsByDateHelper(calendar.timeInMillis)
                        Handler().postDelayed({
                            val eventLists = vm.getEventsByDateResult()
                            if (eventLists.isEmpty()) {
                                binding?.tvNoItem!!.visibility = View.VISIBLE
                                binding?.pieChart!!.visibility = View.GONE
                            } else {
                                binding?.pieChart!!.visibility = View.VISIBLE
                                binding?.tvNoItem!!.visibility = View.GONE
                                loadPieChart(pieChart!!, content, eventLists)
                            }
                        }, 500)
                        yearNow = year
                        monthNow = month
                        dayNow = dayOfMonth
                    }, yearNow, monthNow, dayNow
                )
            datePickerDialog.setTitle("Select date")
            datePickerDialog.show()
        }


    }

    private fun loadPieChart(pieChart: PieChart, content: String, eventLists: List<Event>) {
        initPieChart(pieChart)
        val dataList = ArrayList(eventLists)
        val hashMap: HashMap<String, Double> = HashMap<String, Double>()
        var sum: Double = 0.0
        for (i in dataList) {
            if (i.title in hashMap.keys) {
                hashMap[i.title] = hashMap[i.title]!! + i.lasting
            } else {
                hashMap[i.title] = i.lasting.toDouble()
            }
            sum += i.lasting
        }
        Log.i("test", sum.toString())
        for (i in hashMap.keys) {
            Log.i("test", hashMap[i]!!.toString())
            hashMap[i] = ((hashMap[i]!! / sum) * 10000).toInt().toDouble() / 100
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
        hashMap: HashMap<String, Double>,
        content: String
    ) {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        // Add data here
        for (i in hashMap.keys) {
            Log.i("test", hashMap[i]!!.toFloat().toString())
            dataEntries.add(PieEntry(hashMap[i]!!.toFloat(), i))
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