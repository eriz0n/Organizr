package at.htlle.organizer.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task
{
    private Long id;
    private String title;
    private String description;
    private String status; // "Offen", "In Bearbeitung", "Erledigt"
    private LocalDateTime dueDate;

    /**
     * @param id Id des Tasks
     * @param title Name des Tasks
     * @param description Beschreibung
     * @param status Aktueller Status
     * @param dueDate Zieldatum
     */
    public Task(Long id, String title, String description, String status, LocalDateTime dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
