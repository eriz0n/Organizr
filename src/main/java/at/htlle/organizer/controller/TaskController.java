package at.htlle.organizer.controller;

import at.htlle.organizer.model.Task;
import at.htlle.organizer.service.TaskService;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Summary;
import biweekly.util.Duration;
import biweekly.util.Frequency;
import biweekly.util.Recurrence;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController
{
    private TaskService taskService;

    public TaskController()
    {
        // Task Service erzeugen
        this.taskService = new TaskService();

        // Testdaten hinzufügen
        taskService.insertTask(new Task(1L, "POS lernen", "viel lernen", "Nicht begonnen", null));
        taskService.insertTask(new Task(2L, "Maturaball", "viel feiern", "Beendet", LocalDateTime.of(2024, 11, 16, 20, 0)));
        taskService.insertTask(new Task(3L, "DA abgeben", "viel schreiben", "Nicht begonnen", LocalDateTime.of(2025, 2, 25, 0, 0)));
    }

    /**
     * Liefert alle Tasks zurück
     * @return
     */
    @GetMapping
    public List<Task> getAllTasks()
    {
        return taskService.getAllTasks();
    }

    /**
     * Liefert einen Task zurück
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id)
    {
        Task task = taskService.getTaskById(id);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    /**
     * Liefert einen spezifischen Task als Endpoint zurück
     */
    @GetMapping("/{id}/ical")
    public ResponseEntity<byte[]> exportTaskAsIcal(@PathVariable Long id)
    {
        // Task holen
        Task task = taskService.getTaskById(id);

        // Erzeugen des iCAL Events
        ICalendar ical = new ICalendar();
        VEvent event = new VEvent();
        Summary summary = event.setSummary(task.getTitle());
        summary.setLanguage("de-at");

        event.setDateStart(task.getDueDate() == null ? Date.valueOf(LocalDate.now()) : Date.valueOf(task.getDueDate().toLocalDate()));

        Duration duration = new Duration.Builder().hours(1).build();
        event.setDuration(duration);

        Recurrence recur = new Recurrence.Builder(Frequency.WEEKLY).interval(2).build();
        event.setRecurrenceRule(recur);

        ical.addEvent(event);
        String icalString = Biweekly.write(ical).go();

        // Zurückliefern der Daten
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=task.ics")
                .body(icalString.getBytes());
    }

    /**
     * Löscht einen Task aus der internen Liste
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id)
    {
        boolean deleted = taskService.deleteTask(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Updated einen Task aus der internen Liste
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask)
    {
        Task task = taskService.updateTask(id, updatedTask);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    /**
     * Erstelle einen Task in der internen Liste
     * @return
     */
    @PostMapping
    public ResponseEntity<Task> insertTask(@RequestBody Task insertTask)
    {
        Task task = taskService.insertTask(insertTask);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }
}