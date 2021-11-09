package com.example.myapplication.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TodoItem {

    public TodoItem(Boolean isFinished, String todoContent, String time) {
        this.isFinished = isFinished;
        this.todoContent = todoContent;
        this.time = time;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public String getTodoContent() {
        return todoContent;
    }

    public void setTodoContent(String todoContent) {
        this.todoContent = todoContent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public TodoItem(Integer id,Boolean isFinished, String todoContent, String time) {
//        this.id = id;
//        this.isFinished = isFinished;
//        this.todoContent = todoContent;
//        this.time = time;
//    }

    @PrimaryKey
    private Integer id;
    @ColumnInfo(name = "finished")
    private Boolean isFinished;
    @ColumnInfo(name = "content")
    private String todoContent;
    @ColumnInfo(name = "time")
    private String time;
}
