public class Time {
    private int Day;
    private int hour;
    private int minute;
    public Time(){
        Day = 0;
        hour = 0;
        minute = 0;
    }

    public Time(int day, int hour, int minute) {
        this.Day = day;
        this.hour = hour;
        this.minute = minute;
    }
    public int getDay() {
        return Day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setDay(int date) {
        Day = date;
    }

    public void setHour(int hour) {
        while (hour > 23) {
            setDay(this.Day + 1);
            hour -=24;
        }
        this.hour = hour;
    }

    public void setMinute(int minute) {
        while(minute > 59) {
            setHour(this.hour + 1);
            minute -= 60;
        }
        this.minute = minute;
    }

    public Time calcDuration(Time t1) {
        Time t2 = new Time();

        int minuteDiff = this.minute - t1.getMinute();
        int hourDiff = this.hour - t1.getHour();
        int dayDiff = this.Day - t1.getDay();

        // Adjust for borrowing if needed
        if (minuteDiff < 0) {
            minuteDiff += 60;
            hourDiff--; // Borrow 1 hour
        }

        if (hourDiff < 0) {
            hourDiff += 24;
            dayDiff--; // Borrow 1 day
        }

        t2.setMinute(Math.abs(minuteDiff));
        t2.setHour(Math.abs(hourDiff));
        t2.setDay(Math.abs(dayDiff));

        return t2;
    }

    public Time AddTime(Time t1) {
        Time t3 = new Time();
        t3.setDay(t1.getDay() + this.getDay());
        t3.setHour(t1.getHour() + this.getHour());
        t3.setMinute(t1.getMinute() + this.getMinute());
        return t3;
    }
    public Boolean IsGreaterOrEqual(Time t1){
    if(this.Day > t1.getDay()){
        return true;
    } else if (this.Day == t1.getDay()) {
        if((this.hour >= t1.getHour()) && (this.minute >= t1.getMinute())){
            return true;
        }
    }
    return false;

    }
    public boolean islessthan(Time t1){
        return ! this.IsGreaterOrEqual(t1);
    }
    public void displayTime(){
        System.out.print("Day: " + this.Day);
        System.out.print(",Hour: " + this.hour);
        System.out.println(",Minute: " + this.minute);
    }
    @Override
    public String toString() {
        return  "  "+ Day + " : " + hour + " : " + minute;
    }


    public static void main(String[] args) {
        Time t1 = new Time();
        Time t2 = new Time();
        t1.setDay(0);
        t1.setHour(5);
        t1.setMinute(50);
        t2.setDay(1);
        t2.setHour(5);
        t2.setMinute(50);
        t1.AddTime(t2).displayTime();
    }
}
