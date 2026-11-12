package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Task;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TodoRepository {
//    private List<Task> taskList = new ArrayList<>(); // TODO: Включить а то что ниже удалить в финале
    private List<Task> taskList = new ArrayList<>(List.of(
            new Task(1, "test one", false, "24-11-12")));

    public void setTaskList(int id, String title, boolean status, String createdAt) {
        taskList.add(new Task(id, title, status, createdAt));
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
