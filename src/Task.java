import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String title;
    private String description;
    private String repeatability;
    private String type;
    private final LocalDate time;
    private final int id;
    static int counter = 0;

    public Task(String description, String repeatability, String title, String type, String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        this.description = description;
        this.repeatability = repeatability;
        this.title = title;
        this.type = type;
        this.time = LocalDate.parse(time, formatter);
        this.id = counter;
        counter++;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepeatability() {
        return repeatability;
    }

    public void setRepeatability(String repeatability) {
        this.repeatability = repeatability;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(description, task.description) && Objects.equals(repeatability, task.repeatability) && Objects.equals(type, task.type) && Objects.equals(time, task.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, repeatability, type, time, id);
    }

    @Override
    public String toString() {
        return '\n' + "Title: " + title + '\n' +
                "Type: " + type + '\n' +
                "Repeatability: " + repeatability + '\n' +
                "Description: " + description + '\n';
    }

}
