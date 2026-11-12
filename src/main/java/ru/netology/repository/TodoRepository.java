package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Task;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TodoRepository {
    private List<Task> taskList = new ArrayList<>();

    public void setTaskList(int id, String title, boolean status, String createdAt) {
        taskList.add(new Task(id, title, status, createdAt));
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
