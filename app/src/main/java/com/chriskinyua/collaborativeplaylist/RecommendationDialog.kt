package com.chriskinyua.collaborativeplaylist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.recommendation_dialog.view.*

class RecommendationDialog : DialogFragment() {

    interface RecommendationHandler{
        fun getRecommendations(seed: String)
    }

    private lateinit var recommendationHandler: RecommendationHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is RecommendationHandler){
            recommendationHandler = context
        } else {
            throw RuntimeException(
                "The Activity is not implementing the RecommendationHandler interface.")
        }
    }

    private lateinit var etRecommendation: EditText


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Recommendations")
        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.recommendation_dialog, null
        )

        etRecommendation = dialogView.etRecommendation

        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton("Generate Playlist") {
                dialog, which ->

        }
        dialogBuilder.setNegativeButton("Cancel") {
                dialog, which ->
        }


        return dialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etRecommendation.text.isEmpty()) {
                etRecommendation.error = "This field can not be empty"
            } else {
                handleSubmit()
                dialog!!.dismiss()
            }
        }
    }

    private fun handleSubmit() {
        recommendationHandler.getRecommendations(etRecommendation.text.toString())
    }


}