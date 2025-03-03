import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DiaryService diaryService = new DiaryService();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        try (Scanner scanner = new Scanner(System.in)) {
            label:
            while (true) {
                printMenu();
                System.out.print("Chose an option: ");
                if (scanner.hasNextInt()) {
                    int menu = scanner.nextInt();
                    switch (menu) {
                        case 1:
                            inputTask(scanner, diaryService, formatter);
                            break;
                        case 2:
                            deleteTask(diaryService, scanner, formatter);
                            break;
                        case 3:
                            printTask(diaryService, scanner, formatter);
                            break;
                        case 0:
                            break label;
                    }
                } else {
                    scanner.next();
                    System.out.println("Chose an option from the menu above.");
                }
            }
        }
    }

    private static void inputTask(Scanner scanner, DiaryService diaryService, DateTimeFormatter formatter) {
        boolean validInput = false;
        String taskTitle = "";
        String taskType = "";
        String taskDescription = "";
        String repeatability = "";
        int repeatabilityInt;
        String time = "";

        while (!validInput) {
            try {
                System.out.print("Enter task title: ");
                taskTitle = scanner.next();

                System.out.print(
                        """

                                \
                                Choose task type.
                                1. Professional.
                                2. Personal.
                                Input:\s"""
                );

                int taskTypeInt;
                while (true) {
                    try {
                        taskTypeInt = scanner.nextInt();
                        scanner.nextLine();
                        if (taskTypeInt == 1) {
                            taskType = "Professional";
                            break;
                        } else if (taskTypeInt == 2) {
                            taskType = "Personal";
                            break;
                        } else {
                            System.out.print("Chose from 1 to 2." + '\n' + "Input: ");
                        }
                    } catch (InputMismatchException _) {
                        System.out.println("Chose from 1 to 2.");
                        scanner.nextLine();
                    }
                }

                System.out.print('\n' + "Enter task description: ");
                taskDescription = scanner.nextLine();

                System.out.print(
                        """

                                \
                                Choose task repeatability.
                                1. One-time.
                                2. Daily.
                                3. Weekly.
                                4. Monthly.
                                5. Yearly.
                                Input:\s"""
                );

                while (true) {
                    try {
                        repeatabilityInt = scanner.nextInt();
                        scanner.nextLine();
                        if (repeatabilityInt > 0 && repeatabilityInt <= 5) {
                            repeatability = switch (repeatabilityInt) {
                                case 1 -> "One-time";
                                case 2 -> "Daily";
                                case 3 -> "Weekly";
                                case 4 -> "Monthly";
                                case 5 -> "Yearly";
                                default -> "";
                            };
                            break;
                        } else {
                            System.out.print("Chose from 1 to 5." + '\n' + "Input: ");
                        }
                    } catch (InputMismatchException _) {
                        System.out.println("Chose from 1 to 5.");
                        scanner.nextLine();
                    }
                }

                time = userDateChecker(scanner, "From what date should this task be scheduled?" + '\n'
                        + "Enter date (e.g. " + LocalDate.now().format(formatter) +"): ");

                // If all inputs are valid, break out of the loop
                validInput = true;

            } catch (Exception e) {
                System.out.println("An error occurred. Please try again.");
                scanner.nextLine(); // Clear buffer if any error occurred
            }
        }

// Create and add the task
        Task task = new Task(taskDescription, repeatability, taskTitle, taskType, time);
        diaryService.addKeyValue(task.getId(), task);

    }

    private static void deleteTask(DiaryService diaryService, Scanner scanner, DateTimeFormatter formatter) {
        if (diaryService.getDiary().isEmpty()) {
            System.out.println("Add task first.");
            return;
        }
        String taskDate = userDateChecker(scanner, "Enter date of the task(s) that should be deleted." + '\n'
                + "Enter date (e.g. " + LocalDate.now().format(formatter) +"): ");
        List<Task> listOfTaskForDeletion = diaryService.getDiary().values().stream()
                .filter(task -> diaryService.shouldDateBePrinted(taskDate, task))
                .toList();

        if (listOfTaskForDeletion.isEmpty()) {
            System.out.println("There are no tasks scheduled for this date.");
            return;
        }

        int userInput;
        int numerator = 1;

        System.out.println("What task would you like to delete?");
        for (Task task : listOfTaskForDeletion) {
            System.out.println(numerator++ + ". " + task.getTitle());
        }
        System.out.print("Input: ");

        while (true) {
            try {
                userInput = scanner.nextInt();
                scanner.nextLine();
                if (userInput > 0 && userInput <= listOfTaskForDeletion.size()) {
                    diaryService.getDiary().remove(listOfTaskForDeletion.get(userInput - 1).getId());
                    System.out.println("Task deleted." + '\n');
                    break;
                } else {
                    System.out.println("Choose from 1 to " + listOfTaskForDeletion.size() + '\n' + "Input: ");
                }
            } catch (InputMismatchException _) {
                System.out.println("Choose from 1 to " + listOfTaskForDeletion.size());
                scanner.nextLine();
            }
        }
    }

    //Not finished, maybe will continue...
    /*private static void editTask(DiaryService diaryService, Scanner scanner, DateTimeFormatter formatter) {
        if (diaryService.getDiary().isEmpty()) {
            System.out.println("Add task first.");
            return;
        }

        int userInput;
        int userInput2;


        int numerator = 1;
        List<Integer> idOfTasksToEdit = new ArrayList<>();
        System.out.println("What task would you like to edit?");
        for (Task task : diaryService.getDiary().values()) {
            System.out.println(numerator++ + ". " + task.getTitle());
            idOfTasksToEdit.add(task.getId());
        }
        System.out.print("Input: ");

        while (true) {
            try {
                userInput = scanner.nextInt();
                if (userInput > 0 && userInput <= diaryService.getDiary().size()) {
                    System.out.println("""
                            What parameter would you like to edit?
                            1. Title.
                            2. Type.
                            3. Description.
                            4. Repeatability.
                            """);
                    userInput2 = scanner.nextInt();
                    if (userInput2 > 0 && userInput2 <= 4) {
                        switch (userInput2) {
                            case 1:
                                diaryService.getDiary(idOfTasksToEdit.get(userInput - 1)).
                        }
                    }
                } else {

                    System.out.println("Choose from 1 to " + diaryService.getDiary().size() + '\n' + "Input: ");

                }

            }
        }


    }*/

    private static void printTask(DiaryService diaryService, Scanner scanner, DateTimeFormatter formatter) {
        String lookForThisDate = userDateChecker(scanner, "For what date would you like to see the schedule?" + '\n'
                + "Enter date (e.g. " + LocalDate.now().format(formatter) +"): ");

        for (Task task : diaryService.getDiary().values()) {
            if (diaryService.shouldDateBePrinted(lookForThisDate, task)) {
                System.out.println(task);
            }
        }
    }

    private static void printMenu() {
        System.out.println(
                """
                        1. Add task.
                        2. Remove task.
                        3. Get task for the day.
                        0. Exit.
                        """
        );
    }

    private static String userDateChecker(Scanner scanner, String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        while (true) {
            try {
                System.out.print('\n' + message);
                String date = scanner.next();
                LocalDate checkDate = LocalDate.parse(date, formatter);
                return checkDate.format(formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. ");
                scanner.nextLine();
            }
        }
    }
}