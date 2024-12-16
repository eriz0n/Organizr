package at.htlle.organizer.service;

import org.springframework.stereotype.Service;

import at.htlle.organizer.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService
{
    private List<Task> tasks = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong();

    /**
     * Liefert alle Tasks zurück
     * @return
     */
    public List<Task> getAllTasks()
    {
        return tasks;
    }

    /**
     * Liefert einen speziellen task zurück
     * @param id Zurückliefernde ID
     * @return Task, oder sonst null (falls ID nicht existiert)
     */
    public Task getTaskById(Long id)
    {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Fügt einen Task in die Liste hinzu
     * @param task
     * @return
     */
    public Task insertTask(Task task)
    {
        // Neue ID ziehen
        Long newTaskId = idGenerator.incrementAndGet();
        task.setId(newTaskId);

        // In die Liste der Tasks hinzufügen
        tasks.add(task);

        // Zurückliefern
        return task;
    }

    /**
     * Updatet den Task mit der ID mit den Inhalten des updateTask, sofern dieser existiert
     * @param id ID des upzudatenden tasks
     * @param updateTask Infos die in den Task übernommen werden sollen
     * @return Den upgedateten Task oder null, falls die ID nicht existiert
     */
    public Task updateTask(Long  id, Task updateTask)
    {
        // Task suchen und ggf. returnen
        Task existingTask = getTaskById(id);
        if (existingTask == null)
        {
            return null;
        }

        // Wenn wir hier ankommen, dann haben wir einen Task gefunden
        // also übertragen wir die Daten des Tasks (shallow-copy))
        existingTask.setTitle(updateTask.getTitle());
        existingTask.setStatus(updateTask.getStatus());
        existingTask.setDescription(updateTask.getDescription());
        existingTask.setDueDate(updateTask.getDueDate());

        return existingTask;
    }

    /**
     * Löscht den Task mit der ID
     * @param id
     * @return True, wenn der Task gelöscht wurde
     */
    public boolean deleteTask(Long id)
    {
        return tasks.removeIf(t -> t.getId().equals(id));
    }
}
