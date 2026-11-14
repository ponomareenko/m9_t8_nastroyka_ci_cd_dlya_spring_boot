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
    private final AtomicInteger genId = new AtomicInteger(0);

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public synchronized boolean setTaskList(String title) {
        return todoRepository.setTaskList(increaseId(), title, false, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    public synchronized List<Task> getTaskList() {
        return todoRepository.getTaskList();
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
        int index = findTaskIndexById(id);
        if (index != -1) {
            todoRepository.updateTitle(index, title);
            todoRepository.updateStatus(index, status);
            return true;
        }
        return false;
    }

    public synchronized boolean deleteTask(int id) {
        int index = findTaskIndexById(id);
        if (index != -1) {
            todoRepository.deleteTask(index);
            return true;
        }
        return false;
    }

    private int findTaskIndexById(int id) {
        List<Task> taskList = todoRepository.getTaskList();
        for (int i = 0; i < taskList.size(); i++) {
            if (id == taskList.get(i).getId()) {
                return i;
            }
        }
        return -1;
    }
}
