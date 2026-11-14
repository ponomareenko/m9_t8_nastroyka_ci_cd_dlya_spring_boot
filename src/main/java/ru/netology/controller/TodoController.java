package ru.netology.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.dto.request.UpdateTaskRequest;
import ru.netology.dto.response.AllTasksResponse;
import ru.netology.dto.request.CreateTaskRequest;
import ru.netology.dto.response.TaskResponse;
import ru.netology.model.Task;
import ru.netology.service.TodoService;

@RestController
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // показать задачу
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable("id") int id) {
        Task taskQuery = todoService.getTask(id);

        if (taskQuery != null) {
            return ResponseEntity.ok(new TaskResponse(taskQuery.getId(),
                    taskQuery.getTitle(),
                    taskQuery.getStatus(),
                    taskQuery.getCreatedAt()));
        }

        return ResponseEntity.notFound().build();
    }

    // добавить задачу
    @PostMapping("/tasks")
    public ResponseEntity<String> createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        if (todoService.setTaskList(createTaskRequest.title())) {
            return ResponseEntity.ok("Успешно! Задача добавлена.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка! Задача не добавлена.");
    }

    // получить все задачи
    @GetMapping("/tasks")
    public AllTasksResponse allTasksResponse() {
        return new AllTasksResponse(todoService.getTaskList().toArray(Task[]::new));
    }

    // обновить задачу
    @PutMapping("/tasks/{id}")
    public ResponseEntity<String> updateTask(@PathVariable("id") int id, @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
        if (todoService.updateTask(id,updateTaskRequest.title(), updateTaskRequest.status())) {
            return ResponseEntity.ok("Успешно! Задача обновлена.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка! Задача не обновлена.");
    }

    // удалить задачу
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") int id) {
        if (todoService.deleteTask(id)) {
            return ResponseEntity.ok("Успешно! Задача удалена.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка! Задачи не существует.");
    }
}
