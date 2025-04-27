package com.example.com_us.ui.record.month

import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.media.Image
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.data.model.question.Answer
import com.example.com_us.data.model.question.Data
import com.example.com_us.databinding.FragmentRecordBinding
import com.example.com_us.ui.compose.TimeLineList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import timber.log.Timber
import java.util.Date

@AndroidEntryPoint
class RecordFragment :BaseFragment<FragmentRecordBinding, RecordViewModel>(
    FragmentRecordBinding::inflate
) {
    override val viewModel : RecordViewModel by viewModels()
    private var currentIndex = 4
    private val navController by lazy { findNavController() }


    private fun setRecordList(record:List<Answer>,answerDate :String) {
        binding.viewRecord.compose.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TimeLineList(record, answerDate = answerDate, navController = navController, directions = RecordFragmentDirections.Companion)
            }
        }
    }



    override fun onBindLayout() {
        super.onBindLayout()
        showCurrentState()

        binding.viewRecord.layout.visibility = View.GONE
        binding.viewFlipper.displayedChild = 0

        currentIndex = 4

        binding.btnPrevious.visibility = View.INVISIBLE
        binding.btnNext.setOnClickListener {
            binding.viewRecord.layout.visibility= View.GONE
            when(currentIndex) {
                4 ->  {
                    currentIndex =5
                    binding.date.text="2025년 5월"
                    binding.viewFlipper.displayedChild=1
                    binding.btnNext.visibility= View.VISIBLE
                    binding.btnPrevious.visibility = View.VISIBLE
                    showCurrentState()

                }
                5 -> {
                    currentIndex =6
                    binding.date.text="2025년 6월"
                    binding.viewFlipper.displayedChild=2
                    binding.btnNext.visibility= View.VISIBLE
                    binding.btnPrevious.visibility = View.VISIBLE
                    showCurrentState()

                }
                6-> {
                    currentIndex = 7
                    binding.date.text="2025년 7월"
                    binding.viewFlipper.displayedChild=3
                    binding.btnNext.visibility= View.INVISIBLE
                    binding.btnPrevious.visibility = View.VISIBLE
                    showCurrentState()

                }

            }

        }
        binding.btnPrevious.setOnClickListener {
            binding.viewRecord.layout.visibility= View.GONE
            when(currentIndex) {
                5 -> {
                    // 5월 일때
                    currentIndex =4
                    binding.btnPrevious.visibility= View.INVISIBLE
                    binding.btnNext.visibility= View.VISIBLE
                    binding.date.text="2025년 4월"
                    binding.viewFlipper.displayedChild=0

                }
                6 -> {
                    // 6월 일때
                    currentIndex =5
                    binding.btnPrevious.visibility= View.VISIBLE
                    binding.btnNext.visibility= View.VISIBLE

                    binding.date.text="2025년 5월"
                    binding.viewFlipper.displayedChild=1

                }
                7->{
                    currentIndex =6
                    binding.btnPrevious.visibility= View.VISIBLE
                    binding.btnNext.visibility= View.VISIBLE
                    binding.date.text="2025년 6월"
                    binding.viewFlipper.displayedChild=2

                }
            }
        }



        lifecycleScope.launch {
            viewModel.record.collect{
                it.forEach {
                    if (it.answerDate.contains("4월")) {
                        set4MonthBlock(it)
                    }
                    if (it.answerDate.contains("5월")) {
                        set5MonthBlock(it)
                    }
                    if (it.answerDate.contains("6월")) {
                        set6MonthBlock(it)
                    }
                    if (it.answerDate.contains("7월")) {
                        set7MonthBlock(it)
                    }

                }
            }
        }

        val firstWeek = binding.month4.firstWeek
        val secondWeek = binding.month4.secondWeek
        val thirdWeek = binding.month4.thirdWeek
        val fourthWeek = binding.month4.fourthWeek
        val fifthWeek = binding.month4.fifthWeek

        //4월 클릭 리스너
        addClickListener(firstWeek)
        addClickListener(secondWeek)
        addClickListener(thirdWeek)
        addClickListener(fourthWeek)
        addClickListener(fifthWeek)

        //5월 클릭 리스터
        val mayFirstWeek = binding.month5.firstWeek
        val  maySecondWeek = binding.month5.secondWeek
        val  mayThirdWeek = binding.month5.thirdWeek
        val  mayFourthWeek = binding.month5.fourthWeek
        val  mayFifthWeek = binding.month5.fifthWeek

        //5월 클릭 리스너
        addClickListener(mayFirstWeek)
        addClickListener(maySecondWeek)
        addClickListener(mayThirdWeek)
        addClickListener(mayFourthWeek)
        addClickListener(mayFifthWeek)

        //6월 클릭 리스터

        val juneFirstWeek = binding.month6.firstWeek
        val  juneSecondWeek = binding.month6.secondWeek
        val  juneThirdWeek = binding.month6.thirdWeek
        val  juneFourthWeek = binding.month6.fourthWeek
        val  juneFifthWeek = binding.month6.fifthWeek
        val  juneSixthWeek = binding.month6.sixthWeek

        //6월 클릭 리스너
        addClickListener(juneFirstWeek)
        addClickListener(juneSecondWeek)
        addClickListener(juneThirdWeek)
        addClickListener(juneFourthWeek)
        addClickListener(juneFifthWeek)
        addClickListener(juneSixthWeek)


        //7월 클릭 리스너

        val julyFirstWeek = binding.month7.firstWeek
        val  julySecondWeek = binding.month7.secondWeek
        val  julyThirdWeek = binding.month7.thirdWeek
        val  julyFourthWeek = binding.month7.fourthWeek
        val  julyFifthWeek = binding.month7.fifthWeek

        addClickListener(julyFirstWeek)
        addClickListener(julySecondWeek)
        addClickListener(julyThirdWeek)
        addClickListener(julyFourthWeek)
        addClickListener(julyFifthWeek)



    }


    private fun addClickListener(table: TableRow) {
        for (i in 0 until table.childCount) {
            val view = table.getChildAt(i)
            if (view is ImageView && view.visibility == View.VISIBLE) {
                view.setOnClickListener {
                    val date =  it.tag.toString()
                    Timber.d("clicked date :${date.substringBeforeLast("일 ") +"일"}")
                    onClickDate(date)
                }
            }
        }
    }



    private fun onClickDate(selectedDate : String) {

        binding.viewRecord.txtDate.text= selectedDate
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.record.collectLatest {
            val record = it.filter {
                    it.answerDate == ( selectedDate.substringBeforeLast("일 ") + "일")
                }
                Timber.d("추출 데이터 :$record")
                if (record.isNotEmpty()) {
                    binding.viewRecord.layout.visibility = View.VISIBLE
                    setRecordList(record.first().answers, answerDate =record.first().answerDate)
                }
                else {
                    binding.viewRecord.layout.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun showCurrentState(){
        //todo : 오늘 날짜를 기준으로 오늘까지의 날짜만 검은색으로 표시
        // todo : 이후 날짜는 검은색으로 표시
        // todo : 오늘 날짜에 표시하기

        //1. 현재 시간 가져오기
        val now  = System.currentTimeMillis()
        // 날짜 생성하기
        val date: Date = Date(now)
        val sdf = SimpleDateFormat("MM-dd")
        val getTime = sdf.format(date);
        Timber.d("오늘 :$getTime")

        // 오늘 날짜 위에 검은색 동그라미 뷰 추가하기
        val month = getTime.substring(0,2)
        val day = getTime.substring(3,5)
        showToday(day, month)
    }

    private fun showMonthDate(month: String) {
        val koreanDays = listOf("월", "화", "수", "목", "금", "토", "일")

        val tableLayout = when(month){
            "04" -> binding.month4.tableLayout
            "05" -> binding.month5.tableLayout
            "06" -> binding.month6.tableLayout
            "07" -> binding.month7.tableLayout
            else -> binding.month4.tableLayout
        }

        for (i in 0 until tableLayout.childCount) {
            val tableRow = tableLayout.getChildAt(i) as TableRow
            for (j in 0 until tableRow.childCount) {
                val tv = tableRow.getChildAt(j)
                if (tv is TextView) {
                    val text =tv.text.toString()
                    //todo : 요일이 아닐 때
                    if(text in koreanDays || text== "") {
                        continue
                    }
                    // todo 요일도 아니고 오늘 날짜보다 이후인 경우

                        Timber.d("text :$text")
                        val resId = requireView().context.resources.getIdentifier("text_day_$text","id", requireView().context.packageName)
                        val textView = resId.let { requireView().findViewById<TextView>(it) }
                        textView.setTextColor(resources.getColor(R.color.gray_500))
                        continue
                }
            }
        }
    }
    private fun showToday(day :  String, month : String){
        val tableLayout = when(month){
            "04" -> binding.month4.tableLayout
            "05" -> binding.month5.tableLayout
            "06" -> binding.month6.tableLayout
            "07" -> binding.month7.tableLayout
            else -> binding.month4.tableLayout
        }

        val koreanDays = listOf("월", "화", "수", "목", "금", "토", "일")


        for (i in 0 until tableLayout.childCount) {
            val tableRow = tableLayout.getChildAt(i) as TableRow
            for (j in 0 until tableRow.childCount) {
                val tv = tableRow.getChildAt(j)
                if (tv is TextView) {
                    val text =tv.text.toString()
                    if (text == day){
                        Timber.d("찾았따 :$text, $day")
                       //  그 뷰 위에  아이콘 추가
                       val resId = requireView().context.resources.getIdentifier("iv_$day","id", requireView().context.packageName)
                        val iv = resId.let { requireView().findViewById<ImageView>(it) }
                        iv?.visibility = View.VISIBLE
                        continue

                    }

                    //todo : 요일이 아닐 때
                    else if(text in koreanDays || text== "") {
                        continue
                    }
                    // todo 요일도 아니고 오늘 날짜보다 이후인 경우

                    else if (text.toInt()> day.toInt()) {
                        Timber.d("text :$text")
                        val resId = requireView().context.resources.getIdentifier("text_day_$text","id", requireView().context.packageName)
                        val textView = resId.let { requireView().findViewById<TextView>(it) }
                        textView.setTextColor(resources.getColor(R.color.gray_500))
                        continue
                    }
                }
            }
        }
    }
    private fun set4MonthBlock(data : Data) {
        val date = data.answerDate
        if (data.answers.isEmpty()) return
        val category = setWeekBlockImage(data.answers.first().category)
        val dayRegex = "2025년 04월 (\\d{2})일".toRegex()
        val matchResult = dayRegex.find(date)

        matchResult?.groupValues?.get(1)?.toIntOrNull()?.let { day ->
            val imageView = when (day) {
                1 -> binding.month4.firstWeekTuesday
                2 -> binding.month4.firstWeekWednesday
                3 -> binding.month4.firstWeekThursday
                4 -> binding.month4.firstWeekFriday
                5 -> binding.month4.firstWeekSaturday
                6 -> binding.month4.firstWeekSunday
                7 -> binding.month4.secondWeekMonday
                8 -> binding.month4.secondWeekTuesday
                9 -> binding.month4.secondWeekWednesday
                10 -> binding.month4.secondWeekThursday
                11 -> binding.month4.secondWeekFriday
                12 -> binding.month4.secondWeekSaturday
                13 -> binding.month4.secondWeekSunday
                14 -> binding.month4.thirdWeekMonday
                15 -> binding.month4.thirdWeekTuesday
                16 -> binding.month4.thirdWeekWednesday
                17 -> binding.month4.thirdWeekThursday
                18 -> binding.month4.thirdWeekFriday
                19 -> binding.month4.thirdWeekSaturday
                20 -> binding.month4.thirdWeekSunday
                21 -> binding.month4.fourthWeekMonday
                22 -> binding.month4.fourthWeekTuesday
                23 -> binding.month4.fourthWeekWednesday
                24 -> binding.month4.fourthWeekThursday
                25 -> binding.month4.fourthWeekFriday
                26 -> binding.month4.fourthWeekSaturday
                27 -> binding.month4.fourthWeekSunday
                28 -> binding.month4.fifthWeekMonday
                29 -> binding.month4.fifthWeekTuesday
                30 -> binding.month4.fifthWeekWednesday
                else -> binding.month4.firstWeekTuesday
            }
            imageView.setImageDrawable(category)
        }
    }

    private fun set5MonthBlock(data: Data) {
        val date = data.answerDate
        if (data.answers.isEmpty()) return
        val category = setWeekBlockImage(data.answers.first().category)
        val dayRegex = "2025년 05월 (\\d{2})일".toRegex()
        val matchResult = dayRegex.find(date)

        matchResult?.groupValues?.get(1)?.toIntOrNull()?.let { day ->
            val imageView = when (day) {
                1 -> binding.month5.firstWeekThursday
                2 -> binding.month5.firstWeekFriday
                3 -> binding.month5.firstWeekSaturday
                4 -> binding.month5.firstWeekSunday
                5 -> binding.month5.secondWeekMonday
                6 -> binding.month5.secondWeekTuesday
                7 -> binding.month5.secondWeekWednesday
                8 -> binding.month5.secondWeekThursday
                9 -> binding.month5.secondWeekFriday
                10 -> binding.month5.secondWeekSaturday
                11 -> binding.month5.secondWeekSunday
                12 -> binding.month5.thirdWeekMonday
                13 -> binding.month5.thirdWeekTuesday
                14 -> binding.month5.thirdWeekWednesday
                15 -> binding.month5.thirdWeekThursday
                16 -> binding.month5.thirdWeekFriday
                17 -> binding.month5.thirdWeekSaturday
                18 -> binding.month5.thirdWeekSunday
                19 -> binding.month5.fourthWeekMonday
                20 -> binding.month5.fourthWeekTuesday
                21 -> binding.month5.fourthWeekWednesday
                22 -> binding.month5.fourthWeekThursday
                23 -> binding.month5.fourthWeekFriday
                24 -> binding.month5.fourthWeekSaturday
                25 -> binding.month5.fourthWeekSunday
                26 -> binding.month5.fifthWeekMonday
                27 -> binding.month5.fifthWeekTuesday
                28 -> binding.month5.fifthWeekWednesday
                29 -> binding.month5.fifthWeekThursday
                30 -> binding.month5.fifthWeekFriday
                31 -> binding.month5.fifthWeekSaturday
                else -> binding.month5.firstWeekThursday
            }
            imageView.setImageDrawable(category)
        }
    }

    private fun set6MonthBlock(data: Data) {
        val date = data.answerDate
        if (data.answers.isEmpty()) return
        val category = setWeekBlockImage(data.answers.first().category)
        val dayRegex = "2025년 06월 (\\d{2})일".toRegex()
        val matchResult = dayRegex.find(date)

        matchResult?.groupValues?.get(1)?.toIntOrNull()?.let { day ->
            val imageView = when (day) {
                1 -> binding.month6.firstWeekSunday
                2 -> binding.month6.secondWeekMonday
                3 -> binding.month6.secondWeekTuesday
                4 -> binding.month6.secondWeekWednesday
                5 -> binding.month6.secondWeekThursday
                6 -> binding.month6.secondWeekFriday
                7 -> binding.month6.secondWeekSaturday
                8 -> binding.month6.secondWeekSunday
                9 -> binding.month6.thirdWeekMonday
                10 -> binding.month6.thirdWeekTuesday
                11 -> binding.month6.thirdWeekWednesday
                12 -> binding.month6.thirdWeekThursday
                13 -> binding.month6.thirdWeekFriday
                14 -> binding.month6.thirdWeekSaturday
                15 -> binding.month6.thirdWeekSunday
                16 -> binding.month6.fourthWeekMonday
                17 -> binding.month6.fourthWeekTuesday
                18 -> binding.month6.fourthWeekWednesday
                19 -> binding.month6.fourthWeekThursday
                20 -> binding.month6.fourthWeekFriday
                21 -> binding.month6.fourthWeekSaturday
                22 -> binding.month6.fourthWeekSunday
                23 -> binding.month6.fifthWeekMonday
                24 -> binding.month6.fifthWeekTuesday
                25 -> binding.month6.fifthWeekWednesday
                26 -> binding.month6.fifthWeekThursday
                27 -> binding.month6.fifthWeekFriday
                28 -> binding.month6.fifthWeekSaturday
                29 -> binding.month6.fifthWeekSunday
                30 -> binding.month6.firstWeekMonday // 6월 마지막날
                else -> binding.month6.firstWeekSunday
            }
            imageView.setImageDrawable(category)
        }
    }

    private fun set7MonthBlock(data: Data) {
        val date = data.answerDate
        if (data.answers.isEmpty()) return
        val category = setWeekBlockImage(data.answers.first().category)
        val dayRegex = "2025년 07월 (\\d{2})일".toRegex()
        val matchResult = dayRegex.find(date)

        matchResult?.groupValues?.get(1)?.toIntOrNull()?.let { day ->
            val imageView = when (day) {
                1 -> binding.month7.firstWeekTuesday
                2 -> binding.month7.firstWeekWednesday
                3 -> binding.month7.firstWeekThursday
                4 -> binding.month7.firstWeekFriday
                5 -> binding.month7.firstWeekSaturday
                6 -> binding.month7.firstWeekSunday
                7 -> binding.month7.secondWeekMonday
                8 -> binding.month7.secondWeekTuesday
                9 -> binding.month7.secondWeekWednesday
                10 -> binding.month7.secondWeekThursday
                11 -> binding.month7.secondWeekFriday
                12 -> binding.month7.secondWeekSaturday
                13 -> binding.month7.secondWeekSunday
                14 -> binding.month7.thirdWeekMonday
                15 -> binding.month7.thirdWeekTuesday
                16 -> binding.month7.thirdWeekWednesday
                17 -> binding.month7.thirdWeekThursday
                18 -> binding.month7.thirdWeekFriday
                19 -> binding.month7.thirdWeekSaturday
                20 -> binding.month7.thirdWeekSunday
                21 -> binding.month7.fourthWeekMonday
                22 -> binding.month7.fourthWeekTuesday
                23 -> binding.month7.fourthWeekWednesday
                24 -> binding.month7.fourthWeekThursday
                25 -> binding.month7.fourthWeekFriday
                26 -> binding.month7.fourthWeekSaturday
                27 -> binding.month7.fourthWeekSunday
                28 -> binding.month7.fifthWeekMonday
                29 -> binding.month7.fifthWeekTuesday
                30 -> binding.month7.fifthWeekWednesday
                31 -> binding.month7.fifthWeekThursday
                else -> binding.month7.firstWeekTuesday
            }
            imageView.setImageDrawable(category)
            imageView.tag = date // 날짜 문자열을 태그로 설정
            imageView.setOnClickListener {
                onClickDate(it.tag as String)
            }
        }
    }

    private fun setWeekBlockImage(category : String): Drawable {
        val drawable = when(category) {
            "DAILY" ->  resources.getDrawable(R.drawable.ic_face_orange)
            "SCHOOL" ->  resources.getDrawable(R.drawable.ic_face_blue_v2)
            "HOBBY" ->   resources.getDrawable(R.drawable.ic_face_pink)
            "FAMILY" -> resources.getDrawable(R.drawable.ic_face_purple)
            "FRIEND" -> resources.getDrawable(R.drawable.ic_face_green)
            else -> resources.getDrawable(R.drawable.ic_blank_week)
        }
        return drawable
    }
}