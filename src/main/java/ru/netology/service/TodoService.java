package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.model.Task;
import ru.netology.repository.TodoRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private AtomicInteger genId = new AtomicInteger(0);

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public synchronized boolean setTaskList(String title) {
        return todoRepository.setTaskList(increaseId(), title, false, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    public synchronized List<Task> getTaskList() {
        return todoRepository.getTaskList().stream()
                .map(task -> new Task(
                        task.getId(),
                        task.getTitle(),
                        task.getStatus(),
                        task.getCreatedAt()
                ))
                .toList();
    }

    public Task getTask(int id) {
        for(Task taskCount : getTaskList()) {
            if (id == taskCount.getId()) {
                return taskCount;
            }
        }

        return null;
    }

    private int increaseId() {
        return genId.incrementAndGet();
    }

    public synchronized boolean updateTask(int id, String title, boolean status) {
        for(Task taskCount : todoRepository.getTaskList()) {
            if (id == taskCount.getId()) {
                taskCount.setStatus(status);
                taskCount.setTitle(title);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteTask(int id) {
        for (int i = 0; i < todoRepository.getTaskList().size(); i++) {
            if (id == todoRepository.getTaskList().get(i).getId()) {
                todoRepository.deleteTask(i);
                return true;
            }
        }
        return false;
    }
}
