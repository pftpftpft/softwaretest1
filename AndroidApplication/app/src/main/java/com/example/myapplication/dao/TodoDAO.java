package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.entity.TodoItem;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TodoDAO {
    @Query("SELECT * FROM TodoItem ORDER BY finished")
    List<TodoItem> getAll();

    @Query("SELECT * FROM TODOITEM WHERE id = :id")
    TodoItem getItemsByID(Integer id);

    @Update
    void updateItem(TodoItem item);

    @Insert
    void insertAll(TodoItem ...todoItems);

    @Delete
    void deleteItem(TodoItem todoItem);
}
