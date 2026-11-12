package ru.netology.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.dto.TaskResponse;
import ru.netology.model.StatusTask;
import ru.netology.model.Task;
import ru.netology.service.TodoService;

@RestController
public class TodoController {
    private final TodoService todoService;
    private Task taskQuery;

//        todoService.setTaskList("test one");
//        todoService.setTaskList("test two");

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable("id") int id) {
        System.out.println(todoService.getTaskList().size());

        taskQuery = todoService.getTask(id);

        if (taskQuery != null) {
            return ResponseEntity.ok(new TaskResponse(taskQuery.getId(),
                    taskQuery.getTitle(),
                    taskQuery.getStatus(),
                    taskQuery.getCreatedAt()));
        }

        return ResponseEntity.notFound().build();
    }
}
