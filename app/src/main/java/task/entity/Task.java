package task.entity;

public class Task {

    // private attributes
    int id;
    String taskName = null;
    String taskDesc = null;
    int day, month, year;

    // constructors

    public Task() {
    }

    public Task(int id, String taskName, String taskDesc, int day, int month, int year) {
        this.id = id;

        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}