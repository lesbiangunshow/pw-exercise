package com.abbisea.pwexercise.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abbisea.pwexercise.data.models.Inspection
import com.abbisea.pwexercise.databinding.MainFragmentBinding
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val pendingInspectionAdapter = PendingInspectionAdapter { inspectionId ->
        viewModel.onClickPendingInspection(inspectionId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = MainFragmentBinding.inflate(inflater, container, false)
        viewModel.refreshData()

        with(binding) {
            lifecycleOwner = this@MainFragment
            viewModel = this@MainFragment.viewModel
            recyclerviewPendingInspection.adapter = pendingInspectionAdapter
            recyclerviewPendingInspection.layoutManager = LinearLayoutManager(requireContext())
            subscribeUi()
            return root
        }
    }

    private fun subscribeUi() {
        viewModel.pendingInspections.observe(viewLifecycleOwner) {
            pendingInspectionAdapter.updateData(it)
        }
        viewModel.showInspectionsDialog.observe(viewLifecycleOwner) { inspections ->
            MaterialDialog.Builder(requireContext())
                .title("Select inspection")
                .items(inspections.map { it.name })
                .itemsCallback { _, _, _, inspectionName ->
                    inspections.find { it.name == inspectionName }?.let {
                        showLocationsDialog(it)
                    }
                }
                .positiveText(android.R.string.cancel)
                .show()
        }
        viewModel.resumeInspection.observe(viewLifecycleOwner) {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToInspectionFragment(
                    it.id,
                    it.location,
                    isResumed = true
                )
            )
        }
    }

    private fun showLocationsDialog(inspection: Inspection) {
        MaterialDialog.Builder(requireContext())
            .title("Select inspection")
            .items(inspection.possibleLocations)
            .itemsCallback { _, _, _, location ->
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToInspectionFragment(
                        inspection.id,
                        location.toString(),
                        isResumed = false
                    )
                )
            }
            .positiveText(android.R.string.cancel)
            .show()
    }

}