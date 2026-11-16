package ru.netology.m9_t8_nastroyka_ci_cd_dlya_spring_boot;

import org.junit.jupiter.api.Test;
import ru.netology.model.Task;
import ru.netology.repository.TodoRepository;
import ru.netology.service.TodoService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TodoServiceTest {
    @Test
    void shouldCreateAndStoreTask() {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        String title = "Купить молоко";

        // Act
        Task createdTask = service.setTaskList(title);

        // Assert
        // Проверь возвращённую задачу.
        assertNotNull(createdTask);
        assertEquals(1, createdTask.getId());
        assertEquals(title, createdTask.getTitle());
        assertFalse(createdTask.getStatus());
        assertNotNull(createdTask.getCreatedAt());

        // Получи содержимое репозитория.
        List<Task> storedTasks = repository.getTaskList();
        Task firstStorTask = storedTasks.getFirst();
        assertEquals(1, storedTasks.size());

        // Проверь количество задач и данные сохранённой задачи.
        assertEquals(createdTask.getId(), firstStorTask.getId());
        assertEquals(title, firstStorTask.getTitle());
        assertFalse(firstStorTask.getStatus());
        assertEquals(createdTask.getCreatedAt(), firstStorTask.getCreatedAt());
    }

    @Test
    void shouldReturnAllStoredTasks() {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        Task milkTask = new Task(12, "Купить молоко", true, "2026-11-16T10:00:00");
        Task carTask = new Task(36, "Забрать машину с ремонта", false, "2026-11-16T15:30:00");
        repository.setTaskList(milkTask);
        repository.setTaskList(carTask);

        // Act
        List<Task> actualTasks = service.getTaskList();

        // Assert
        assertEquals(2, actualTasks.size());

        Task actualMilkTask = actualTasks.stream().filter(task -> task.getId() == milkTask.getId()).findFirst().orElseThrow();

        assertEquals(milkTask.getTitle(), actualMilkTask.getTitle());
        assertEquals(milkTask.getStatus(), actualMilkTask.getStatus());
        assertEquals(milkTask.getCreatedAt(), actualMilkTask.getCreatedAt());

        Task actualCarTask = actualTasks.stream().filter(task -> task.getId() == carTask.getId()).findFirst().orElseThrow();

        assertEquals(carTask.getTitle(), actualCarTask.getTitle());
        assertEquals(carTask.getStatus(), actualCarTask.getStatus());
        assertEquals(carTask.getCreatedAt(), actualCarTask.getCreatedAt());
    }

    @Test
    void shouldReturnEmptyListWhenNoTasksExist() {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);

        // Act
        List<Task> actualTaskList = service.getTaskList();

        // Assert
        assertNotNull(actualTaskList);
        assertTrue(actualTaskList.isEmpty());
    }

    @Test
    void shouldUpdateExistingTask() {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        Task originalTask = new Task(12, "Купить молоко", false, "2026-11-16T10:00:00");
        repository.setTaskList(originalTask);

        // Act
        boolean updated = service.updateTask(12, "Купить молоко и хлеба", true);

        // Assert
        assertEquals(1, repository.getTaskList().size());
        Task updatedTask = repository.getTaskList().getFirst();

        assertTrue(updated);
        assertEquals("Купить молоко и хлеба", updatedTask.getTitle());
        assertTrue(updatedTask.getStatus());
        assertEquals(12, updatedTask.getId());
        assertEquals("2026-11-16T10:00:00", updatedTask.getCreatedAt());
    }

    @Test
    void shouldNotChangeStoredTasksWhenUpdatingUnknownId() {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        Task milkTask = new Task(12, "Купить молоко", true, "2026-11-16T10:00:00");
        repository.setTaskList(milkTask);

        // Act
        boolean updated = service.updateTask(33, "Купить машину", false);

        // Assert
        assertFalse(updated);
        assertEquals(1, repository.getTaskList().size());
        Task currentTask = repository.getTaskList().getFirst();

        assertEquals(12, currentTask.getId());
        assertEquals("Купить молоко", currentTask.getTitle());
        assertTrue(currentTask.getStatus());
        assertEquals("2026-11-16T10:00:00", currentTask.getCreatedAt());
    }

    @Test
    void shouldDeleteExistingTaskAndKeepOtherTaskUnchanged() {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        Task milkTask = new Task(12, "Купить молоко", true, "2026-11-16T10:00:00");
        Task carTask = new Task(36, "Забрать машину с ремонта", false, "2026-11-16T15:30:00");
        repository.setTaskList(milkTask);
        repository.setTaskList(carTask);

        // Act
        boolean deleted = service.deleteTask(12);

        // Assert
        assertTrue(deleted);

        List<Task> remainingTasks = repository.getTaskList();
        assertEquals(1, remainingTasks.size());
        Task remainingTask = remainingTasks.getFirst();

        assertEquals(36, remainingTask.getId());
        assertEquals("Забрать машину с ремонта", remainingTask.getTitle());
        assertFalse(remainingTask.getStatus());
        assertEquals("2026-11-16T15:30:00", remainingTask.getCreatedAt());
    }

    @Test
    void shouldNotChangeStoredTasksWhenDeletingUnknownId() {
        // Arrange
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        Task milkTask = new Task(12, "Купить молоко", true, "2026-11-16T10:00:00");
        Task carTask = new Task(36, "Забрать машину с ремонта", false, "2026-11-16T15:30:00");
        repository.setTaskList(milkTask);
        repository.setTaskList(carTask);

        // Act
        boolean deleted = service.deleteTask(45);

        assertFalse(deleted);

        List<Task> afterTasks = repository.getTaskList();
        assertEquals(2, afterTasks.size());

        Task afterMilkTask = afterTasks.stream().filter(task -> task.getId() == 12).findFirst().orElseThrow();
        assertEquals(12, afterMilkTask.getId());
        assertEquals("Купить молоко", afterMilkTask.getTitle());
        assertTrue(afterMilkTask.getStatus());
        assertEquals("2026-11-16T10:00:00", afterMilkTask.getCreatedAt());

        Task afterCarTask = afterTasks.stream().filter(task -> task.getId() == 36).findFirst().orElseThrow();
        assertEquals(36, afterCarTask.getId());
        assertEquals("Забрать машину с ремонта", afterCarTask.getTitle());
        assertFalse(afterCarTask.getStatus());
        assertEquals("2026-11-16T15:30:00", afterCarTask.getCreatedAt());
    }
}
