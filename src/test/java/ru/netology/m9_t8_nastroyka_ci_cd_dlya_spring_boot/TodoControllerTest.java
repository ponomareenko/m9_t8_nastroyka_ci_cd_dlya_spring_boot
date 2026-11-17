package ru.netology.m9_t8_nastroyka_ci_cd_dlya_spring_boot;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.netology.controller.TodoController;
import ru.netology.model.Task;
import ru.netology.repository.TodoRepository;
import ru.netology.service.TodoService;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class TodoControllerTest {
    @Test
    void shouldReturnCreatedTaskWhenRequestIsValid() throws Exception {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        TodoController controller = new TodoController(service);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act + Assert
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "Купить молоко"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Купить молоко"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.createdAt").value(not(emptyOrNullString())));
    }

    @Test
    void shouldRejectTaskWithBlankTitle() throws Exception {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        TodoController controller = new TodoController(service);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act + Assert
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"title": ""}
                        """))
                .andExpect(status().isBadRequest());

        assertTrue(repository.getTaskList().isEmpty());
    }

    @Test
    void shouldReturnStoredTasksAsJson() throws Exception {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        TodoController controller = new TodoController(service);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        Task milkTask = new Task(12, "Купить молоко", true, "2026-11-16T10:00:00");
        repository.setTaskList(milkTask);

        // Act + Assert
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tasks").value(hasSize(1)))
                .andExpect(jsonPath("$.tasks[0].id").value(12))
                .andExpect(jsonPath("$.tasks[0].title").value("Купить молоко"))
                .andExpect(jsonPath("$.tasks[0].status").value(true))
                .andExpect(jsonPath("$.tasks[0].createdAt").value("2026-11-16T10:00:00"));
    }

    @Test
    void shouldUpdateExistingTaskThroughApi() throws Exception {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        TodoController controller = new TodoController(service);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        Task milkTask = new Task(12, "Купить молоко", false, "2026-11-16T10:00:00");
        repository.setTaskList(milkTask);

        // Act + Assert
        mockMvc.perform(put("/tasks/{id}", 12)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "Купить молоко и хлеб",
                          "status": true
                        }
                        """))
                .andExpect(status().isOk());

        // Assert
        assertEquals(1, repository.getTaskList().size());
        Task updatedTask = repository.getTaskList().getFirst();
        assertEquals(12, updatedTask.getId());
        assertEquals("Купить молоко и хлеб", updatedTask.getTitle());
        assertTrue(updatedTask.getStatus());
        assertEquals("2026-11-16T10:00:00", updatedTask.getCreatedAt());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingUnknownTask() throws Exception {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        TodoController controller = new TodoController(service);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        Task milkTask = new Task(12, "Купить молоко", false, "2026-11-16T10:00:00");
        repository.setTaskList(milkTask);

        // Act + Assert
        mockMvc.perform(put("/tasks/{id}", 34)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "title": "Купить молоко и хлеб",
                          "status": true
                        }
                        """))
                .andExpect(status().isNotFound());

        // Assert
        assertEquals(1, repository.getTaskList().size());
        Task updatedTask = repository.getTaskList().getFirst();
        assertEquals(12, updatedTask.getId());
        assertEquals("Купить молоко", updatedTask.getTitle());
        assertFalse(updatedTask.getStatus());
        assertEquals("2026-11-16T10:00:00", updatedTask.getCreatedAt());
    }

    @Test
    void shouldDeleteExistingTaskThroughApi() throws Exception {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        TodoController controller = new TodoController(service);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        Task milkTask = new Task(12, "Купить молоко", true, "2026-11-16T10:00:00");
        Task carTask = new Task(36, "Забрать машину с ремонта", false, "2026-11-16T15:30:00");
        repository.setTaskList(milkTask);
        repository.setTaskList(carTask);

        // Act + Assert
        mockMvc.perform(delete("/tasks/{id}", 36))
                .andExpect(status().isOk());

        // Assert
        assertEquals(1, repository.getTaskList().size());
        Task remainingTask = repository.getTaskList().getFirst();
        assertEquals(12, remainingTask.getId());
        assertEquals("Купить молоко", remainingTask.getTitle());
        assertTrue(remainingTask.getStatus());
        assertEquals("2026-11-16T10:00:00", remainingTask.getCreatedAt());
    }
}
