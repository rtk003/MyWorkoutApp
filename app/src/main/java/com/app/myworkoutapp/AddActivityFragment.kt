package com.app.myworkoutapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.myworkoutapp.data.ActivityMC
import com.app.myworkoutapp.databinding.FragmentAddActivityBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddActivityFragment : Fragment() {


    private val viewModel: ActivitiesViewModel by activityViewModels {
        ActivityViewModelFactory(
            (activity?.application as ActivitiesApplication).database.itemDao()
        )
    }

    private val navigationArgs: ActivityDetailsFragmentArgs by navArgs()

    lateinit var item: ActivityMC

    private var _binding: FragmentAddActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddActivityBinding.inflate(inflater, container, false)
        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                item = selectedItem
                bind(item)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
        return binding.root

    }

    /**
     * Returns true if the EditTexts are not empty
     */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.titleTV.text.toString(),
            binding.placeTV.text.toString(),
            binding.dateTV.text.toString(),
        )
    }

    /**
     * Binds views with the passed in [item] information.
     */
    private fun bind(item: ActivityMC) {
        binding.apply {
            titleTV.setText(item.title, TextView.BufferType.SPANNABLE)
            placeTV.setText(item.place, TextView.BufferType.SPANNABLE)
            dateTV.setText(item.date, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }


    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.titleTV.text.toString(),
                binding.placeTV.text.toString(),
                binding.dateTV.text.toString(),
            )
            val action = AddActivityFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Fill All Fields", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.titleTV.text.toString(),
                this.binding.placeTV.text.toString(),
                this.binding.dateTV.text.toString(),
            )
            val action = AddActivityFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }else {
            Toast.makeText(requireContext(), "Fill All Fields", Toast.LENGTH_LONG).show()
        }
    }
}