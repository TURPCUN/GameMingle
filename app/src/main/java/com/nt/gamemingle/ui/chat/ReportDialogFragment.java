package com.nt.gamemingle.ui.chat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nt.gamemingle.R;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.ChatMessage;

public class ReportDialogFragment extends DialogFragment {

    private ChatViewModel chatViewModel;
    String eventId;
    String messageId;

    String message;
    String messageTime;
    String messageSender;

    public ReportDialogFragment(String eventId, String messageId, String message, String messageTime, String messageSender, AppViewModel appViewModel) {
        chatViewModel = new ChatViewModel(appViewModel);
        this.eventId = eventId;
        this.messageId = messageId;
        this.message = message;
        this.messageTime = messageTime;
        this.messageSender = messageSender;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_report, null);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        RadioButton rdSpam = view.findViewById(R.id.radioButtonOption1);
        RadioButton rdThreatening = view.findViewById(R.id.radioButtonOption2);
        RadioButton rdInappropriate = view.findViewById(R.id.radioButtonOption3);
        RadioButton rdPersonal = view.findViewById(R.id.radioButtonOption4);
        RadioButton rdFake = view.findViewById(R.id.radioButtonOption5);
        RadioButton rdOther = view.findViewById(R.id.radioButtonOption6);

        TextView txtMessage = view.findViewById(R.id.report_chat_message);
        txtMessage.setText(message);

        TextView txtMessageTime = view.findViewById(R.id.report_text_sent_time);
        String shortChatTime = messageTime.substring(0, 5);
        txtMessageTime.setText(shortChatTime);

        TextView txtSenderName = view.findViewById(R.id.report_text_sender_name_chat);
        txtSenderName.setText(messageSender);

        ImageView imgSender = view.findViewById(R.id.report_img_sender_profile_chat);

        String firstName = messageSender.split(" ")[0];

        int imageResource = view.getContext().getResources()
                .getIdentifier(firstName.toLowerCase(), "drawable", view.getContext().getPackageName());

        if (imageResource != 0) {
            imgSender.setImageResource(imageResource);
        } else {
            imgSender.setImageResource(R.drawable.icon);
        }

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Report a message")
                .setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        switch (selectedId) {
                            case R.id.radioButtonOption1:
                                //Spam
                                chatViewModel.reportSpamMessage(eventId, messageId, "Spam");
                                break;
                            case R.id.radioButtonOption2:
                                // Threatening Language
                                chatViewModel.reportSpamMessage(eventId, messageId, "ThreateningLanguage");
                                break;
                            case R.id.radioButtonOption3:
                                // Inappropriate Content
                                chatViewModel.reportSpamMessage(eventId, messageId, "InappropriateContent");
                                break;
                            case R.id.radioButtonOption4:
                                // Personal Attacks
                                chatViewModel.reportSpamMessage(eventId, messageId, "PersonalAttacks");
                                break;
                            case R.id.radioButtonOption5:
                                // Fake Account
                                chatViewModel.reportSpamMessage(eventId, messageId, "FakeAccount");
                                break;
                            case R.id.radioButtonOption6:
                                // Other
                                chatViewModel.reportSpamMessage(eventId, messageId, "Other");
                                break;
                        }
                        Toast.makeText(requireContext(), "Message has been reported", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }
}

