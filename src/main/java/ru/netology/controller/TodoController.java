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

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/test")
    public String test() {
        todoService.setTaskList("test one", "IN_PROGRESS");
        todoService.setTaskList("test two", "COMPLETED");
        return todoService.getTaskList().toString();
    }

    @GetMapping("/test1")
    public String test1() {
        todoService.setTaskList("test one", "IN_PROGRESS");
        todoService.setTaskList("test two", "COMPLETED");
        return todoService.getTask(0).getTitle();
    }

//    // SAVE
//    @GetMapping("/tasks/{id}")
//    public TaskResponse getTask(@PathVariable("id") int id) {
//        todoService.setTaskList("test one", "IN_PROGRESS");
//        todoService.setTaskList("test two", "COMPLETED");
//
//        taskQuery = todoService.getTask(id);
//
//        if (taskQuery != null) {
//            return new TaskResponse(taskQuery.getId(),
//                    taskQuery.getTitle(),
//                    taskQuery.getStatus(),
//                    taskQuery.getCreatedAt());
//        }
//
////        return ResponseEntity.notFound().build();
//        return new TaskResponse(-1, "", "", "");
//    }


    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable("id") int id) {
        todoService.setTaskList("test one", "IN_PROGRESS");
        todoService.setTaskList("test two", "COMPLETED");

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
