package com.nt.gamemingle.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nt.gamemingle.adapters.ChatAdapter;
import com.nt.gamemingle.databinding.FragmentChatBinding;
import com.nt.gamemingle.model.ChatMessage;
import com.nt.gamemingle.model.Event;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ChatFragment extends BaseFragment {

    Event event;
    private ChatViewModel chatViewModel;
    private FragmentChatBinding binding;

    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatMessagesLocal = new ArrayList<>();
    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolBarVisibility(true);
        setBottomBarVisibility(false);
        event = getArguments().getParcelable("event");
        chatViewModel.getMessages(event.getEventId());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarVisibility(true);
        setBottomBarVisibility(false);
        event = getArguments().getParcelable("event");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatViewModel = new ChatViewModel(appViewModel);

        chatViewModel.getMessages(event.getEventId());

        chatAdapter = new ChatAdapter(chatMessagesLocal);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerViewChat.setLayoutManager(layoutManager);
        binding.recyclerViewChat.setAdapter(chatAdapter);

        ItemTouchHelper.SimpleCallback callback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                        return 0.1f;
                    }

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        if (direction == ItemTouchHelper.LEFT) {
                            String eventId = event.getEventId();
                            String messageId = chatMessagesLocal.get(position).getChatMessageId();
                            String message = chatMessagesLocal.get(position).getMessage();
                            String messageTime = chatMessagesLocal.get(position).getMessageTime();
                            String messageSender = chatMessagesLocal.get(position).getSender().getFullName();
                            String currentUserId = appViewModel.mAuth.getCurrentUser().getUid();
                            if(chatMessagesLocal.get(position).getSender().getUserId().equals(currentUserId)){
                                Toast.makeText(requireContext(), "You can't report your own message", Toast.LENGTH_SHORT).show();
                                chatAdapter.notifyDataSetChanged();
                                return;
                            }
                            ReportDialogFragment reportDialogFragment = new ReportDialogFragment(eventId, messageId, message, messageTime, messageSender, appViewModel);
                            reportDialogFragment.show(getParentFragmentManager(), "reportDialog");
                            chatAdapter.notifyDataSetChanged();
                        }
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewChat);
        binding.buttonSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.editTextChat.getText().toString();
                if (!message.isEmpty()) {

                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);

                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int year = calendar.get(Calendar.YEAR);
                    String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

                    chatViewModel.sendMessage(message, event.getEventId(), formattedTime, formattedDate);
                    binding.editTextChat.setText("");
                }
            }
        });

        chatViewModel.chatMessageMutableLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<ChatMessage>>() {
            @Override
            public void onChanged(ArrayList<ChatMessage> chatMessages) {
                chatAdapter.setChatMessages(chatMessages);
                chatMessagesLocal = chatMessages;
                chatAdapter.notifyDataSetChanged();
            }
        });
    }
}