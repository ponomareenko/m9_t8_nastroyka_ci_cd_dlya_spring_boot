package ru.netology.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.netology.model.Task;
import ru.netology.repository.TodoRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private LocalDateTime dateTime;
    private AtomicInteger genId = new AtomicInteger(0);

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void setTaskList(String title) {
        todoRepository.setTaskList(increaseId(), title, false, dateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    public List<Task> getTaskList() {
        return todoRepository.getTaskList();
    }

    public Task getTask(int id) {
        for(Task taskCount : todoRepository.getTaskList()) {
            if (id == taskCount.getId()) {
                return taskCount;
            }
        }

        return null;
    }

    private int increaseId() {
        return genId.incrementAndGet();
    }
}
