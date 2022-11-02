package todolist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

class Main {

    public static void main(String[] args) {

        TodoList toDoList = new TodoList();

        toDoList.start();
    }

}

public class TodoList {

    public static Map<String, Task> tasks = new LinkedHashMap<>();
    public static boolean applicationRunning = true;

    public void start() {
        showApplicationTitle();
        while (TodoList.applicationRunning) {
            showAvailableActions();
            int actionNumber = readAction();
            executeAction(actionNumber);

        }
    }

    public void executeAction(int actionNumber) {
        Actions action;
        switch (actionNumber) {
            case Actions.ADD_TASK -> {
                action = new AddTask();
                action.showActionsInformation();
                String command = action.readUserInput();
                if (!command.equals("0")) {
                    action.executeAction(command);
                }
            }

            case Actions.MARK_AS_DONE -> {
                if (tasks.size() > 0) {
                    action = new MarkAsDone();
                    action.showActionsInformation();
                    String id = action.readUserInput();
                    if (!id.equals("0")) {
                        action.executeAction(id);
                    }

                } else {
                    System.out.println("Your List is empty, add tasks first! ");

                }
            }

            case Actions.REMOVE_TASK -> {
                if (tasks.size() > 0) {
                    action = new RemoveTask();
                    action.showActionsInformation();
                    String id = action.readUserInput();
                    if (!id.equals("0")) {
                        action.executeAction(id);
                    }
                } else {
                    System.out.println("Your list is empty, add tasks first! ");
                }
            }

            case Actions.EDIT_TASK -> {
                if (tasks.size() > 0) {
                    action = new EditTask();
                    action.showActionsInformation();
                    String editCommand = action.readUserInput();
                    if (!editCommand.equals(0)) {
                        action.executeAction(editCommand);
                    }
                } else {
                    System.out.println("Your list is empty, add tasks first! ");
                }
            }

            case Actions.DISPLAY_ALL_TASKS -> {
                if (tasks.size() > 0) {
                    action = new TasksDisplay();
                    action.showActionsInformation();
                    action.executeAction(null);
                } else {
                    System.out.println("Your list is empty, add tasks first! ");
                }
            }

            case Actions.SORT_TASKS_BY_DATE -> {
                action = new DateSorting();
                action.executeAction(null);
            }

            case Actions.SAVE_TASKS_TO_FILE -> {
                if (tasks.size() > 0) {

                    action = new SaveTasksToFile();
                    action.showActionsInformation();
                    String path = action.readUserInput();
                    if (!path.equals("0")) {
                        action.executeAction(path);
                    }
                } else {
                    System.out.println("There are no tasks to be saved!");
                }
            }

            case Actions.READ_FROM_FILE -> {
                action = new ReadFromFile();
                action.showActionsInformation();
                String path = action.readUserInput();
                if (!path.equals("0")) {
                    action.executeAction(path);
                }
            }

            case Actions.EXIT ->
                applicationRunning = false;

        }
    }

    public void showApplicationTitle() {
        System.out.println("To DO List Application");
        System.out.println("-----------------------");
    }

    public void showAvailableActions() {
        System.out.println("");
        System.out.println("1. Add a task");
        System.out.println("2. Mark task as done");
        System.out.println("3. Remove task ");
        System.out.println("4. Edit task");
        System.out.println("5. Display all tasks");
        System.out.println("6. Sort tasks by date");
        System.out.println("7. save tasks to file");
        System.out.println("8. read from file");
        System.out.println("9. Exit");
        System.out.println("");
    }

    public int readAction() {
        List<Integer> availableActions = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        while (true) {
            try {
                System.out.print("Enter action: ");
                Scanner scan = new Scanner(System.in);
                int action = scan.nextInt();
                if (availableActions.contains(action)) {
                    return action;
                } else {
                    System.out.println("Please enter a valid action from the list: ");
                }
            } catch (Exception e) {
                System.out.println("Action must be a number...");

            }
        }
    }
}

class Task {

    private String id;
    private String title;
    private LocalDate dueDate;
    private String status;
    private String projectName;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public static Task buildTask(String id, String title, LocalDate dueDate, String status, String projectName) {
        Task task = new Task();

        task.setId(id);
        task.setTitle(title);
        task.setDueDate(dueDate);
        task.setStatus(status);
        task.setProjectName(projectName);

        return task;
    }

    @Override
    public String toString() {
        return id + "," + title + "," + DateSorting.convertDateToString(dueDate, "dd-MM-yyyy") + "," + status + "," + projectName;
    }

}

abstract class Actions {

    public static final int ADD_TASK = 1;
    public static final int MARK_AS_DONE = 2;
    public static final int REMOVE_TASK = 3;
    public static final int EDIT_TASK = 4;
    public static final int DISPLAY_ALL_TASKS = 5;
    public static final int SORT_TASKS_BY_DATE = 6;
    public static final int SAVE_TASKS_TO_FILE = 7;
    public static final int READ_FROM_FILE = 8;
    public static final int EXIT = 9;

    public abstract void showActionsInformation();

    public abstract String readUserInput();

    public abstract void executeAction(String command);

}

class AddTask extends Actions {

    @Override
    public void showActionsInformation() {
        System.out.println("");
        System.out.println("To add a new task, please follow the instructions and press ENTER:");
        System.out.println("Task ID, Task Title, Due Date (format: dd-mm-yyyy), Status, Project Name");
        System.out.println("eg: 1, Dharssini Vidhya,01-12-2021,in-progress,Java Project");
        System.out.println("Enter 0 to RETURN");
    }

    @Override
    public String readUserInput() {
        while (true) {
            System.out.println("");
            System.out.print("Enter Information: ");
            Scanner in = new Scanner(System.in);
            String userInput = in.nextLine();

            if (!userInput.equals("0")) {
                String[] parts = userInput.split(",");
                if (parts.length == 5) {
                    if (DateSorting.isDateValid("dd-MM-yyyy", parts[2])) {
                        if (TodoList.tasks.get(parts[0]) == null) {
                            return userInput;
                        } else {
                            System.out.println("A task with this ID already exists, try again: ");
                        }
                    } else {
                        System.out.println("The date entered is invalid, try again: ");
                    }
                } else {
                    System.out.println("Please follow instructions, try again: ");
                }
            } else {
                return userInput;
            }
        }
    }

    @Override
    public void executeAction(String command) {
        String[] parts = command.split(",");
        Task task = Task.buildTask(parts[0], parts[1], DateSorting.parseDate("dd-MM-yyyy", parts[2]),
                parts[3], parts[4]);

        TodoList.tasks.put(task.getId(), task);
        System.out.println("Task successfully added!");

    }
}

class EditTask extends Actions {

    @Override
    public void showActionsInformation() {
        System.out.println("");
        System.out.println("to update a task, follow the instructions and press ENTER: ");
        System.out.println("IP.TodoListApplication.App.Task ID, IP.TodoListApplication.App.Task Title, Due Date (format: dd-mm-yyyy), IP.TodoListApplication.App.Task Status, Project Name");
        System.out.println("ID here represent the ID of the task where an update will occur");
        System.out.println("insert a (-) when an update is not needed to that specific parameter");
        System.out.println("");
        System.out.println("Enter 0 to RETURN");
    }

    @Override
    public String readUserInput() {
        while (true) {
            System.out.println("");
            System.out.println("Enter information");
            Scanner in = new Scanner(System.in);
            String userInput = in.nextLine();

            if (!userInput.equals(0)) {
                String[] parts = userInput.split(",");
                if (parts.length == 5) {
                    boolean dateValidationRequired = true;
                    if (parts[2].equals("-")) {
                        dateValidationRequired = false;
                    }

                    boolean isDateValid = true;
                    if (dateValidationRequired) {
                        isDateValid = DateSorting.isDateValid("dd-mm-yyyy", parts[2]);
                    }

                    if (isDateValid) {
                        if (TodoList.tasks.get(parts[0]) != null) {
                            return userInput;
                        } else {
                            System.out.println("ID doesn't exist, try again: ");
                        }
                    } else {
                        System.out.println("Please follow instructions or enter 0 to RETURN");
                    }
                } else {
                    return userInput;
                }
            }
        }

    }

    @Override
    public void executeAction(String command) {

        String[] parts = command.split(",");

        boolean isTaskEdited = false;
        if (!parts[1].equals("-")) {
            TodoList.tasks.get(parts[0]).setTitle(parts[1]);
            isTaskEdited = true;
        }

        if (!parts[2].equals("-")) {
            TodoList.tasks.get(parts[0]).setDueDate(DateSorting.parseDate("dd-mm-yyyy", parts[2]));
            isTaskEdited = true;
        }

        if (!parts[3].equals("-")) {
            TodoList.tasks.get(parts[0]).setStatus(parts[3]);
            isTaskEdited = true;
        }
        if (!parts[4].equals("-")) {
            TodoList.tasks.get(parts[0]).setProjectName(parts[4]);
            isTaskEdited = true;
        }
        if (isTaskEdited) {
            System.out.println("Tasks successfully updated!!");
        } else {
            System.out.println("No change was applied...");
        }
    }
}

class MarkAsDone extends Actions {

    @Override
    public void showActionsInformation() {
        System.out.println("");
        System.out.println("To mark a task as done, enter ID and press ENTER: ");
        System.out.println("");
        System.out.println("Enter 0 to RETURN");
    }

    @Override
    public String readUserInput() {
        while (true) {
            System.out.println("");
            System.out.print("Enter task id:");
            Scanner in = new Scanner(System.in);
            try {
                String userInput = in.nextLine();
                int userInputAsNum = Integer.parseInt(userInput);
                if (userInputAsNum != 0) {
                    Task task = TodoList.tasks.get(userInput);
                    if (task != null) {
                        return userInput;
                    } else {
                        System.out.println("There is no task with this ID, try again: ");
                    }
                } else {
                    return userInput;
                }
            } catch (NumberFormatException err) {
                System.out.println("Enter a valid ID or 0 to RETURN");
            }
        }
    }

    @Override
    public void executeAction(String command) {
        TodoList.tasks.get(command).setStatus("Done");
        System.out.println("Status is set as Done for the task with ID: " + command);

    }
}

class RemoveTask extends Actions {

    @Override
    public void showActionsInformation() {
        System.out.println("");
        System.out.println("To remove a task, enter ID and press ENTER");
        System.out.println("");
        System.out.println("Enter 0 to RETURN");
    }

    @Override
    public String readUserInput() {

        while (true) {
            System.out.println("");
            System.out.print("Enter ID: ");
            Scanner in = new Scanner(System.in);
            try {
                String userInput = in.nextLine();
                int userInputAsNum = Integer.parseInt(userInput);
                if (userInputAsNum != 0) {
                    Task task = TodoList.tasks.get(userInput);
                    if (task != null) {
                        return userInput;
                    } else {
                        System.out.println("ID doesn't exist, try another ID: ");
                    }
                } else {
                    return userInput;
                }

            } catch (NumberFormatException err) {
                System.out.println("Please enter a valid ID or 0 to RETURN");
            }

        }

    }

    @Override
    public void executeAction(String command) {
        TodoList.tasks.remove(command);

        System.out.println("IP.TodoListApplication.App.Task with ID: " + command + ", was successfully removed...");

    }
}

class TasksDisplay extends Actions {

    @Override
    public void showActionsInformation() {
        System.out.println("");
        System.out.println("Here are all the tasks: ");
    }

    @Override
    public String readUserInput() {
        throw new UnsupportedOperationException("The requested operation is not supported.");
    }

    @Override
    public void executeAction(String command) {
        TodoList.tasks.forEach((key, task) -> {
            System.out.println("ID: " + key + ", Title: " + task.getTitle() + ", Due Date: "
                    + DateSorting.convertDateToString(task.getDueDate(), "dd-MM-yyyy") + ", Status: "
                    + task.getStatus() + ", Project: " + task.getProjectName());
        });

    }
}

class ReadFromFile extends Actions {

    @Override
    public void showActionsInformation() {
        System.out.println("");
        System.out.println("Please enter path to read:");
        System.out.println("");
        System.out.println("Enter 0 to RETURN");
    }

    @Override
    public String readUserInput() {
        while (true) {
            System.out.println("");
            System.out.print("path:");

            Scanner scan = new Scanner(System.in);
            String userInput = scan.nextLine();

            return userInput;
        }
    }

//       In this overridden method reside the implementation of
//      of how a given path is taken from the user
//      logged into to fetch information and display it to user
//      <p>
//      IMPORTANT after executing read, press 5 to display the files that was read
//     from the file
    @Override
    public void executeAction(String path) {
        try {
            try (Scanner input = new Scanner(new File(path))) {
                while (input.hasNextLine()) {
                    String file = input.nextLine();
                    String[] parts = file.split(",");
                    Task task = Task.buildTask(parts[0], parts[1], DateSorting.parseDate("dd-MM-yyyy", parts[2]),
                            parts[3], parts[4]);
                    if (TodoList.tasks.get(parts[0]) != null) {
                        TodoList.tasks.replace(parts[0], task);
                    } else {
                        TodoList.tasks.put(parts[0], task);
                    }

                }
            }

            System.out.println("Tasks are being read!");
        } catch (FileNotFoundException e) {
            System.out.println("Path or file do not exist...");

        }

    }
}

class SaveTasksToFile extends Actions {

    @Override
    public void showActionsInformation() {
        System.out.println("");
        System.out.println("Please enter path to file:");

        System.out.println("");
        System.out.println("Enter 0 to RETURN");
    }

    @Override
    public String readUserInput() {
        while (true) {
            System.out.println("");
            System.out.print("path:");

            Scanner scan = new Scanner(System.in);
            String userInput = scan.nextLine();

            return userInput;

        }
    }

    @Override
    public void executeAction(String path) {
        try {
            try (PrintWriter pw = new PrintWriter(new FileOutputStream(path))) {
                List<String> lines = TodoList.tasks.entrySet().stream().map(entry -> entry.getValue().toString()).collect(Collectors.toList());

                lines.forEach((line) -> {
                    pw.println(line);
                });
            }
            System.out.println("task succesfully saved into file: " + path);
        } catch (FileNotFoundException e) {
            System.out.println("Path or file do not exist...");
        }

    }
}

class DateSorting extends Actions {

    @Override
    public void showActionsInformation() {
        throw new UnsupportedOperationException("The requested operation is not supported.");
    }

    @Override
    public String readUserInput() {
        throw new UnsupportedOperationException("The requested operation is not supported.");
    }

    @Override
    public void executeAction(String command) {
        List<Map.Entry<String, Task>> entries = new ArrayList<>(TodoList.tasks.entrySet());
        Collections.sort(entries, (Map.Entry<String, Task> task1, Map.Entry<String, Task> task2) -> {
            LocalDate dueDateFirstTask = task1.getValue().getDueDate();
            LocalDate dueDateSecondTask = task2.getValue().getDueDate();
            int result = dueDateFirstTask.compareTo(dueDateSecondTask);
            return result;
        });

        TodoList.tasks.clear();
        entries.forEach((entry) -> {
            TodoList.tasks.put(entry.getKey(), entry.getValue());
        });

        System.out.println("Tasks successfully Sorted!");

    }

    public static boolean isDateValid(String format, String value) {
        DateTimeFormatter formattings = DateTimeFormatter.ofPattern(format);
        try {
            LocalDate localDate = LocalDate.parse(value, formattings);

            String result = localDate.format(formattings);

            return result.equals(value);
        } catch (DateTimeParseException err) {

        }
        return false;
    }

    public static String convertDateToString(LocalDate date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String result = null;
        try {
            result = date.format(formatter);

        } catch (DateTimeParseException e) {

        }
        return result;
    }

    public static LocalDate parseDate(String format, String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(value, formatter);
        return localDate;
    }
}
