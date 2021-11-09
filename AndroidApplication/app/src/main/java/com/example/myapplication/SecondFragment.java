package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplication.adapter.TodoListAdapter;
import com.example.myapplication.dao.TodoDAO;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.TodoItem;
import com.example.myapplication.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private RecyclerView mRv;

    private ArrayList<TodoItem> arrayListMo;
    private AppDatabase db;
    private TodoDAO todoDAO;

    private Disposable thread;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void generate() {
        // thread
        // test data part
        thread = Observable.create((ObservableOnSubscribe<List<TodoItem>>) emitter -> {
            // test data part
//            for(int i = 0; i < 30; i++) {
//                TodoItem item = new TodoItem(i, i%2 == 0,"test"+ String.valueOf(i), "timeTest"+String.valueOf(i));
//                todoDAO.insertAll(item);
//            }
//            TodoItem item = new TodoItem(102, true, "nonononoono", "tttttt");
//            todoDAO.insertAll(item);

            emitter.onNext(todoDAO.getAll());
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(todoItems -> {
                    if (!todoItems.isEmpty()) {
                        TodoListAdapter adapter = new TodoListAdapter(getActivity(), (ArrayList<TodoItem>) todoItems);
                        mRv.setAdapter(adapter);
                        //线性布局
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mRv.setLayoutManager(linearLayoutManager);
                    } else {
                        AlertDialog emptyAlertDialog = new AlertDialog.Builder(getActivity())
                                .setTitle("您的代办事务列表为空")
                                .setMessage("您的代办事务为空，点击右下角“+”号开始添加代办事务吧！")
                                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                });
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrayListMo = new ArrayList<>();

        // TODO
        // database
        // single instance
        db = AppDatabase.getInstance(getActivity());
        todoDAO = db.todoDAO();

        mRv = (RecyclerView) getActivity().findViewById(R.id.list_recyclerview);
        generate();

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_AddFragment);
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .popBackStack(R.id.SecondFragment,false);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}