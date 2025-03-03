import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class DiaryService implements TimeManagementInterface {
    public HashMap<Integer, Task> diary;

    public DiaryService() {
        diary = new HashMap<>();
    }

    public HashMap<Integer, Task> getDiary() {
        return diary;
    }

    public final void setDiary(HashMap<Integer, Task> diary) {
        this.diary = diary;
    }

    public void addKeyValue(int id, Task task) {
        this.diary.put(id, task);
    }

    public void removeKeyValue(int id) {
        this.diary.remove(id);
    }

    @Override
    public String toString() {
        return "DiaryService{" +
                "diary=" + diary +
                '}';
    }

    @Override
    public boolean shouldDateBePrinted(String userInputDate, Task task) {
        //Calculating differences

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate userInputConverted = LocalDate.parse(userInputDate, formatter);
        long differenceInMonths = ChronoUnit.MONTHS.between(YearMonth.from(task.getTime()), YearMonth.from(userInputConverted));
        long differenceInYears = ChronoUnit.YEARS.between(task.getTime(), userInputConverted);

        boolean checker = false;
        switch (task.getRepeatability()) {
            case "One-time":
                if (task.getTime().equals(userInputConverted)) {
                    checker = true;
                }
                break;
            case "Daily":
                checker = true;
                break;
            case "Weekly":
                if (task.getTime().getDayOfWeek().equals(userInputConverted.getDayOfWeek())) {
                    checker = true;
                }
                break;
            case "Monthly":
                if (task.getTime().plusMonths(differenceInMonths).format(formatter).equals(userInputConverted.format(formatter))) {
                    checker = true;
                }
                break;
            case "Yearly":
                if (task.getTime().plusYears(differenceInYears).format(formatter).equals(userInputConverted.format(formatter))) {
                    checker = true;
                }
                break;
            default:

        }

        return checker;
    }
}
