package commands;

public class Schedule {
    private int year;
    private int month;
    private int day;
    public Schedule(String date){
        String[] container = date.split("-");
        this.year = Integer.parseInt(container[0]);
        this.month = Integer.parseInt(container[1]);
        this.day = Integer.parseInt(container[2]);
    }

}
