package ru.ruslan2570.schoolnotificationapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class ErrorDialog extends AppCompatDialogFragment {

	private String message;
	public ErrorDialog(String message){
		this.message = message;
	}


	@Override
	public Dialog onCreateDialog(Bundle savedBundle){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Ошибка!")
				.setMessage(message)
				.setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		return builder.create();
	}
}