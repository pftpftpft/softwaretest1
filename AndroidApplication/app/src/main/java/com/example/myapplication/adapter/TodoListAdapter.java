package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.dao.TodoDAO;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.TodoItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TodoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<TodoItem> data;

    private AppDatabase db;
    private CheckBox cardCheckBox;
    private TextView cardContentTextView;

    public TodoListAdapter(Context inContext, ArrayList<TodoItem> inData) {
        context = inContext;
        data = inData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.todo_card_view, parent, false);
        TodoListCardHolder holder = new TodoListCardHolder(itemView);

        holder.myCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView textView = holder.myCheckTextView;
                if (isChecked) {
                    textView.setTextColor(0x3f3f3f3f);
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    textView.setTextColor(Color.BLACK);
                }
                holder.myCheckBox.setChecked(isChecked);
                Observable.create(new ObservableOnSubscribe<TodoItem>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<TodoItem> emitter) throws Exception {
                        TodoItem checkItem = data.get(holder.getAdapterPosition());
                        emitter.onNext(checkItem);
                    }
                }).subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<TodoItem>() {
                            @Override
                            public void accept(TodoItem todoItem) throws Exception {
                                todoItem.setFinished(isChecked);
                                db.todoDAO().updateItem(todoItem);
                            }
                        });
            }
        });

        holder.myImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = holder.getAdapterPosition();
                TodoItem deleteItem = data.get(index);
                data.remove(index);
                notifyItemRemoved(index);
                notifyItemRangeChanged(index, getItemCount());

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.todoDAO().deleteItem(deleteItem);
                    }
                });
                t.start();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        db = AppDatabase.getInstance(context);
        TodoListCardHolder todoListCardHolder = (TodoListCardHolder) holder;
        cardCheckBox = todoListCardHolder.myCheckBox;
        cardContentTextView = todoListCardHolder.myCheckTextView;

        todoListCardHolder.myTextView.setText(data.get(position).getTime());
        cardContentTextView.setText(data.get(position).getTodoContent());
        // TODO
//        todoListCardHolder.myImageButton.setOnClickListener(new View.OnClickListener() {
//            private static final String TAG = "my android debug";
//
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: i have clicked the view");
//            }
//        });

        // Checkbox
        cardCheckBox.setChecked(data.get(position).getFinished());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TodoListCardHolder extends RecyclerView.ViewHolder {
        public CardView myCardView;
        public CheckBox myCheckBox;
        public TextView myCheckTextView;
        public TextView myTextView;
        public ImageButton myImageButton;

        public TodoListCardHolder(@NonNull View itemView) {
            super(itemView);

            myCardView = (CardView) itemView.findViewById(R.id.cardView);
            myCheckBox = (CheckBox) itemView.findViewById(R.id.todoCheckBox);
            myTextView = (TextView) itemView.findViewById(R.id.Time_TextView);
            myCheckTextView = (TextView) itemView.findViewById(R.id.check_TextView);
            myImageButton = (ImageButton) itemView.findViewById(R.id.deleteButton);
        }
    }
}
