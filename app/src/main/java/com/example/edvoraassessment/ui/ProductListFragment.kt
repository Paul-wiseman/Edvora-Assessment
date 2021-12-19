package com.example.edvoraassessment.ui

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edvoraassessment.R
import com.example.edvoraassessment.databinding.FragmentProductListBinding
import com.example.edvoraassessment.models.ParentItem
import com.example.edvoraassessment.models.ProductItem
import com.example.edvoraassessment.ui.adapter.ParentItemAdapter
import com.example.edvoraassessment.util.NetworkResult
import com.example.edvoraassessment.util.collectLatestLifecycleFlow
import com.example.edvoraassessment.util.snackBack
import com.example.edvoraassessment.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.M)
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var products: List<ProductItem>
    private lateinit var firstProductsList: List<ParentItem>
    private val parentAdapter by lazy {
        ParentItemAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        viewModel.getProducts()
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.btnFilter.setOnClickListener {
            dialog()
        }

        /** Resetting the recycviewItem back to previous state*/
        binding.btnClear.setOnClickListener {
            parentAdapter.setData(firstProductsList)
        }

        /*** handling the Api response*/
        collectLatestLifecycleFlow(viewModel.getProductResponse) { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    binding.parentRecyclerview.showShimmer()
                }
                is NetworkResult.Failure -> {
                    binding.parentRecyclerview.hideShimmer()
                    binding.root.snackBack(response.message.toString())
                }
                is NetworkResult.Success -> {
                    binding.parentRecyclerview.hideShimmer()
                    products = response.data ?: emptyList()

                    val parentItem = response.data?.groupBy {
                        it.product_name
                    }?.map {
                        ParentItem(it.key, it.value)
                    }
                    if (parentItem != null) {
                        binding.parentRecyclerview.apply {
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL, false
                            )
                            firstProductsList = parentItem
                            parentAdapter.setData(parentItem)
                            adapter = parentAdapter
                        }

                    }
                }

            }
        }
    }

    /** Displays the filter dialog*/
    private fun dialog() {
        val productsList = mutableSetOf<String>()
        val stateList = mutableSetOf<String>()
        val city = mutableSetOf<String>()
        products.forEach {
            productsList.add(it.product_name)
            stateList.add(it.address.state)
            city.add(it.address.city)
        }
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.filter_drop_down)
        dialog.window?.setBackgroundDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.dialog_bg
            )
        )
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.findViewById<AutoCompleteTextView>(R.id.tie_product).apply {
            this.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.filter_list_item,
                    productsList.toList()
                )
            )
            handleSelection(
                R.id.tie_product,
                dialog.findViewById(R.id.tie_product),
                dialog.findViewById(R.id.et_filter)
            )

        }
        dialog.findViewById<AutoCompleteTextView>(R.id.tie_state).apply {
            this.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.filter_list_item,
                    stateList.toList()
                )
            )
            handleSelection(
                R.id.tie_state,
                dialog.findViewById(R.id.tie_state),
                dialog.findViewById(R.id.et_filter)
            )
        }
        dialog.findViewById<AutoCompleteTextView>(R.id.tie_city).apply {
            this.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.filter_list_item,
                    city.toList()
                )
            )
            handleSelection(
                R.id.tie_city,
                dialog.findViewById(R.id.tie_city),
                dialog.findViewById(R.id.et_filter)
            )
        }
        dialog.show()
    }

    /**handling the filter selection*/
    private fun handleSelection(idInput: Int, dropDown: AutoCompleteTextView, et: EditText) {
        dropDown.setOnItemClickListener { parent, view, position, id ->
            val text = view as TextView
            et.setText(text.text)
            when (idInput) {
                R.id.tie_product -> {
                    val result = products.filter { it.product_name == text.text }
                    val adapterItem = mutableListOf<ParentItem>()
                    result.toSet().forEach {
                        adapterItem.add(ParentItem(it.product_name, listOf(result[0])))
                    }
                    parentAdapter.setData(adapterItem)
                }
                R.id.tie_state -> {
                    val result = products.filter { it.address.state == text.text }
                    val adapterItem = mutableListOf<ParentItem>()
                    result.toSet().forEach {
                        adapterItem.add(ParentItem(it.address.state, listOf(result[0])))
                    }
                    parentAdapter.setData(adapterItem)
                }
                R.id.tie_city -> {
                    val result = products.filter { it.address.city == text.text }
                    val adapterItem = mutableListOf<ParentItem>()
                    result.toSet().forEach {
                        adapterItem.add(ParentItem(it.address.city, listOf(result[0])))
                    }
                    parentAdapter.setData(adapterItem)
                }

            }


        }
    }

    /**
     * Fragments outlive their views.
     * Binding class has to be set to null to clean memory in [onDestroyView]
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}