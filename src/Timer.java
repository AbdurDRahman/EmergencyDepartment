public class Timer extends Thread {
    private long startTime;
    private long currentTime;
    private long daysElapsed;
    private long hoursElapsed;
    private long minutesElapsed;
    private int secondToMinutes;
    private boolean stop;

    public Timer() {
        startTime = System.currentTimeMillis();
        currentTime = startTime;
        secondToMinutes = 10;
        stop = false;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                // Sleep for 1 second to reduce CPU usage
                Thread.sleep(1000);

                // Update time elapsed
                currentTime = System.currentTimeMillis();
                long timeElapsed = currentTime - startTime;

                // Convert time to simulated minutes, hours, and days
                minutesElapsed = (timeElapsed / 1000) * secondToMinutes;
                hoursElapsed = minutesElapsed / 60;
                daysElapsed = hoursElapsed / 24;
            } catch (InterruptedException e) {
                // Handle thread interruption
                System.err.println("Timer thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized Time getTimeElapsed() {
        Time time = new Time();
        time.setMinute((int) (minutesElapsed % 60));
        time.setHour((int) (hoursElapsed % 24));
        time.setDay((int) (daysElapsed));
        return time;
    }

    public synchronized long getDaysElapsed() {
        return daysElapsed;
    }

    public synchronized long getHoursElapsed() {
        return hoursElapsed;
    }

    public synchronized long getMinutesElapsed() {
        return minutesElapsed;
    }

    public synchronized long getStartTime() {
        return startTime;
    }

    public synchronized void incrementSecondToMinutes() {
        if (secondToMinutes < 10) {
            secondToMinutes++;
        }
    }

    public void stopTimer() {
        this.stop = true;
    }

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.start();
        timer.incrementSecondToMinutes();

        while (true) {
            try {
                // Sleep to reduce frequency of print statements
                Thread.sleep(1000);

                // Print elapsed time
                System.out.println("Minutes: " + timer.getMinutesElapsed() +
                        " Hours: " + timer.getHoursElapsed() +
                        " Days: " + timer.getDaysElapsed());
            } catch (InterruptedException e) {
                System.err.println("Main thread interrupted: " + e.getMessage());
                break;
            }
        }
    }
}
