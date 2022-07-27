package com.lubaspc.traveltolucos.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lubaspc.traveltolucos.MTViewModel
import com.lubaspc.traveltolucos.model.PersonMD
import com.lubaspc.traveltolucos.utils.formatPrice

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
class SaveFormDetailsFragment: Fragment() {
    private val vModel by activityViewModels<MTViewModel>()
    private val priceTotal = mutableStateOf(0.0)
    private val persons = mutableStateListOf<PersonMD>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(inflater.context).apply {
        setContent {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )
            setContent {
                MaterialTheme(
                    colorScheme = if (isSystemInDarkTheme()) dynamicDarkColorScheme(inflater.context)
                    else dynamicLightColorScheme(inflater.context),
                    content = {ViewContent()}
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vModel.personSave.observe(this){
            persons.clear()
            persons.addAll(it.filter { it.checked })
        }
        vModel.charges.observe(this){
            priceTotal.value = it.filter { c -> c.checked }.sumOf { c -> c.total }
            //vBind.tvTotal.text = "T. ${it.filter { c -> c.checked }.sumOf { c -> c.total }.formatPrice}"
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    private fun ViewContent(){
        Scaffold(
            floatingActionButton = {

            }
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(persons.size+1)){
                items(persons){
                    Column {
                        LazyColumn{
                            stickyHeader {
                                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = it.name)
                                }
                            }
                            items(it.listCharges){

                            }
                        }
                    }
                }
            }
        }
    }

}