import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Task1 {
    public static void process1(Messenger messenger, int sleepSecond) {
        Thread oneSecundeThread = new Thread(() -> {
            Thread.currentThread().setName(sleepSecond+"Thread");
            System.out.println("Hello from "+Thread.currentThread().getName());
            while (messenger.inRunning()==true) {
                try {
                    Thread.currentThread().sleep(sleepSecond * 1000);
                    synchronized (Thread.currentThread()) {
                        if (messenger.getInProcess())
                            messenger.setInProcess(false);
                        else
                            messenger.printMessage("");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        oneSecundeThread.start();
    }

    public static void process2 (Messenger messenger, int sleepSecond) {
        Thread fifeSecundenThread = new Thread(() -> {
            Thread.currentThread().setName(sleepSecond+"Thread");
            System.out.println("Hello from "+Thread.currentThread().getName());
            while (messenger.inRunning()==true) {
                try {
                    Thread.currentThread().sleep(sleepSecond * 1000);
                    synchronized (Thread.currentThread()) {
                        messenger.setInProcess(true);
                        messenger.printMessage("Минуло %d секунд");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        fifeSecundenThread.start();
    }
    public static void main(String[] args) {
        Messenger messenger = new Messenger();
        process1(messenger,1);
        process2(messenger,5);
    }
}

class Messenger {
    volatile Boolean InProcess = false;
    volatile Boolean isRunning = true;
    private int count = 0;
    private DateFormat dF = new SimpleDateFormat("HH:mm:ss");
    public Boolean getInProcess() { return InProcess; }
    public void setInProcess(Boolean inProcess) { InProcess = inProcess; }
    public void setCount() { this.count++; }
    public Boolean inRunning() { return isRunning; }
    public void setRunning(Boolean stop) { isRunning = stop; }

    public void printMessage (String messege) throws InterruptedException {
        setCount();
        if (count < 31)
            System.out.println((messege.length()==0 ? count + ": " + dF.format(Calendar.getInstance().getTime()) : String.format(messege,count)));
        else
            setRunning(false);
    }
}