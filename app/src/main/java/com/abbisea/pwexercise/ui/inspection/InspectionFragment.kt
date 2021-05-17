package com.abbisea.pwexercise.ui.inspection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import com.abbisea.pwexercise.databinding.InspectionFragmentBinding
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InspectionFragment : Fragment() {

    private val viewModel: InspectionViewModel by viewModels()
    private val adapter = InspectionAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = InspectionFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.recyclerviewInspection.adapter = adapter
        binding.recyclerviewInspection.layoutManager = LinearLayoutManager(requireContext())
        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {
        with(viewModel) {
            listEntities.observe(viewLifecycleOwner) {
                adapter.updateData(it)
            }
            showSavedToast.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    "Saved successfully! Returning home",
                    Toast.LENGTH_SHORT
                ).show()
                navigateUp(findNavController(), null)
            }
            showInvalidInspectionDialog.observe(viewLifecycleOwner) {
                MaterialDialog.Builder(requireContext())
                    .content("One or more of your answers are invalid! Check you have answered NA or a number between 0 and 100")
                    .positiveText("OK")
                    .show()
            }
            showSubmissionErrorToast.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    "There was an issue with submission. Your inspection has been saved and you may try again",
                    Toast.LENGTH_SHORT
                ).show()
                navigateUp(findNavController(), null)
            }
            showSuccessToast.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    "Submission successful! Returning home",
                    Toast.LENGTH_SHORT
                ).show()
                navigateUp(findNavController(), null)
            }
        }
    }

}