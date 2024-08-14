package com.example.com_us.ui.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.com_us.R
import com.example.com_us.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentQuestionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: QuestionViewModel

    private var lastSelectedView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val questionViewModel =
            ViewModelProvider(this).get(QuestionViewModel::class.java)

        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textDashboard
        questionViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        viewModel = ViewModelProvider(this)[QuestionViewModel::class.java]

        viewModel.selectedThemeId.observe(viewLifecycleOwner, Observer {
            val selectedView = binding.root.findViewById<TextView>(it)
            val themeKor = selectedView.text
            setThemeSelected(selectedView, themeKor.toString())
        })

        binding.includeThemeAll.textviewTheme.setOnClickListener(this)
        binding.includeThemeDaily.textviewTheme.setOnClickListener(this)
        binding.includeThemeFamily.textviewTheme.setOnClickListener(this)
        binding.includeThemeFriend.textviewTheme.setOnClickListener(this)
        binding.includeThemeRandom.textviewTheme.setOnClickListener(this)
        binding.includeThemeInterest.textviewTheme.setOnClickListener(this)
        binding.includeThemeSchool.textviewTheme.setOnClickListener(this)

        return root
    }

    override fun onResume() {
        super.onResume()

        val themeAllView = binding.includeThemeAll
        viewModel.updateSelectedThemeId(themeAllView.textviewTheme.id)
    }

    private fun setThemeSelected(selectedView: TextView, themeKor: String) {
        binding.textviewQuestionTitle.text = String.format(resources.getString(R.string.question_title), themeKor)

        lastSelectedView?.let { view ->
            view.setBackgroundResource(R.drawable.shape_stroke_rect5_gray200)  // 기본 배경 리소스로 변경
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))  // 기본 텍스트 색상으로 변경
        }

        selectedView?.let { view ->
            view.setBackgroundResource(R.drawable.shape_fill_rect5_gray700)
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))  // 안전한 색상 적용
        }

        lastSelectedView = selectedView
    }

    private fun setThemeUnSelected() {

    }

    override fun onClick(view: View) {
        viewModel.updateSelectedThemeId(view.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}