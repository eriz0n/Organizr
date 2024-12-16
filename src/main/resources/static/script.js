// Tabelle finden
taskTableBody = document.getElementById("tasks-table-body");

// Formular finden
taskForm = document.getElementById("task-form");

// Tabelle auffüllen
async function fetchTasks() {
    const response = await fetch("http://localhost:8080/tasks");
    const tasks = await response.json();

    // Tabelle löschen
    taskTableBody.innerHTML = "";

    // Für jedes Objekt eine Zeile generieren
    tasks.forEach(task => {
        taskTableBody.innerHTML += `
                <tr>
                    <td>${task.id}</td>
                    <td>${task.title}</td>
                    <td>${task.status}</td>
                    <td>${task.description}</td>
                    <td>${task.dueDate}</td>
                    <td>
                        <a href='tasks/${task.id}/ical'> <img src="images/icon-calender.svg"></a>
                        <button onclick="editTask(${task.id})"> <img src="images/icon-edit.svg"></button>
                        <button onclick="deleteTask(${task.id})"> <img src="images/icon-delete.svg"></button>
                    </td>
                </tr>`;
    });
}
fetchTasks();

// Fügt die Daten von einem Task in das Edit Formular ein
async function editTask(id) {
    taskForm.dataset.id = id;

    const response = await fetch(`http://localhost:8080/tasks/${id}`);
    const task = await response.json();

    document.getElementById("title").value = task.title;
    document.getElementById("status").value = task.status;
    document.getElementById("description").value = task.description;
    document.getElementById("dueDate").value = task.dueDate ? task.dueDate.substring(0, 16) : "";
}

// Löscht die Daten von einem Task
async function deleteTask(id) {
    await fetch(`http://localhost:8080/tasks/${id}`, {method: "DELETE"});
    // Aktualisieren der Anzeige
    await fetchTasks();
}

// Wird aufgerufen um den aktuellen Task am Server upzudaten
document.getElementById("updateTask").addEventListener("click", async function (event) {
    // JavaScript Objekt erzeugen
    const t = {
        title: document.getElementById("title").value,
        status: document.getElementById("status").value,
        description: document.getElementById("description").value,
        dueDate: document.getElementById("dueDate").value
    };

    // JavaScript Objekt an den Server senden
    await fetch(`http://localhost:8080/tasks/${taskForm.dataset.id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(t)
    });

    // Formular leeren
    document.getElementById("title").value = "";
    document.getElementById("status").value = "";
    document.getElementById("description").value = "";
    document.getElementById("dueDate").value = "";

    // Aktualisieren der Anzeige
    await fetchTasks();
})

// Wird aufgerufen, um einen neuen Task am Server zu erstellen
document.getElementById("insertTask").addEventListener("click", async function (event) {
    // JavaScript Objekt erzeugen
    const t = {
        title: document.getElementById("title").value,
        status: document.getElementById("status").value,
        description: document.getElementById("description").value,
        dueDate: document.getElementById("dueDate").value
    };

    // JavaScript Objekt an den Server senden
    await fetch("http://localhost:8080/tasks", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(t)
    });

    // Formular leeren
    document.getElementById("title").value = "";
    document.getElementById("status").value = "";
    document.getElementById("description").value = "";
    document.getElementById("dueDate").value = "";

    // Aktualisieren der Anzeige
    await fetchTasks();
})