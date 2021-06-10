package de.twisted.examshare.ui.shared.widgets

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import de.twisted.examshare.R

class OverlayProgressBar(context: Context) {

    private val dialogOverlay: AlertDialog =
        AlertDialog.Builder(context, R.style.ThemeOverlay_ExamShare_Dialog_Progress)
            .setView(LayoutInflater.from(context).inflate(R.layout.dialog_progress_bar, null))
            .setCancelable(false)
            .create()

    private fun show() {
        dialogOverlay.show()
    }

    private fun hide() {
        dialogOverlay.cancel()
    }

    fun setVisible(visible: Boolean) {
        if (visible) show() else hide()
    }
}