package com.lubaspc.traveltolucos.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewStructure
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lubaspc.traveltolucos.MTViewModel
import com.lubaspc.traveltolucos.databinding.FragmentHistoryBinding
import com.lubaspc.traveltolucos.model.WeekModel
import com.lubaspc.traveltolucos.ui.Purple500
import com.lubaspc.traveltolucos.ui.Purple700
import com.lubaspc.traveltolucos.ui.Teal200
import com.lubaspc.traveltolucos.ui.WeekView
import com.lubaspc.traveltolucos.utils.parseDate

class HistoryFragment : Fragment() {
    private val vModel by activityViewModels<MTViewModel>()
    private val weekState = mutableStateListOf<WeekModel>()

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                viewLifecycleOwner
            )
        )
        setContent {
            MaterialTheme(
                colors = lightColors(
                    primary = Purple500,
                    primaryVariant = Purple700,
                    secondary = Teal200
                ),
                content = { WeekView(weeks = weekState) }
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vModel.weeks.observe(this) {
            weekState.addAll(it)
        }
    }
}