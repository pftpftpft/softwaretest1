package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.databinding.FragmentAddBinding;
import com.example.myapplication.databinding.FragmentFirstBinding;
import com.example.myapplication.entity.TodoItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddItemFragment extends Fragment {
    private @NonNull FragmentAddBinding binding;
    private AppDatabase db;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = AppDatabase.getInstance(getActivity());

        // 防止退回到编辑页面
        FragmentManager fm = getParentFragmentManager();


        // 文本行清空
        binding.editText.setText("");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("提醒")
                .setMessage("确认不填写内容就要退出吗?此举不会有新的项目添加。")
                .setPositiveButton("我确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavHostFragment.findNavController(AddItemFragment.this)
                                .popBackStack();
                    }
                })
                .setNegativeButton("我还想再编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();

        binding.confirnAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isContentEmpty = true;
                Date curDate = new Date(System.currentTimeMillis());
                String newContent = binding.editText.getText().toString();
                Boolean newBoolean = false;
                String newTime = format.format(curDate);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TodoItem newItem = new TodoItem(newBoolean,newContent,newTime);
                        db.todoDAO().insertAll(newItem);
                    }
                });
                if (!newContent.isEmpty()) {
                    isContentEmpty = false;
                    t.start();
                }
                if (!isContentEmpty) {
                    NavHostFragment.findNavController(AddItemFragment.this)
                            .popBackStack();
//                    NavHostFragment.findNavController().popBackStack();

                } else {
                    alertDialog.show();
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
