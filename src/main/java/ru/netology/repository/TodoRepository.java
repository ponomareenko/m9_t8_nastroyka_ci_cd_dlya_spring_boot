package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Task;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TodoRepository {
    private final List<Task> taskList = new ArrayList<>();

    public boolean setTaskList(int id, String title, boolean status, String createdAt) {
        return taskList.add(new Task(id, title, status, createdAt));
    }

    public List<Task> getTaskList() {
        return taskList.stream()
                .map(task -> new Task(
                        task.getId(),
                        task.getTitle(),
                        task.getStatus(),
                        task.getCreatedAt()
                ))
                .toList();
    }

    public void deleteTask(int index) {
        taskList.remove(index);
    }

    public void updateTitle(int index, String title) {
        taskList.get(index).setTitle(title);
    }

    public void updateStatus(int index, boolean status) {
        taskList.get(index).setStatus(status);
    }
}
