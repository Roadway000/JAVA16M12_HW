import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Task2 {
    public static void main(String[] args) throws InterruptedException {
        Ticker t1 = new Ticker((n) -> {
            if (n % 3 == 0 && n % 5 != 0 && n > 0) { System.out.println(n+" FIZZ"); }
        });
        Ticker t2 = new Ticker((n) -> {
            if (n % 5 == 0 && n % 3 != 0 && n > 0) { System.out.println(n+" BAZZ"); }
        });
        Ticker t3 = new Ticker((n) -> {
            if (n % 3 == 0 && n % 5 == 0 && n > 0) { System.out.println(n+" FIZZ BAZZ"); }
        });
        Ticker t4 = new Ticker((n) -> {
            if (n % 3 != 0 && n % 5 != 0 && n > 0) { System.out.println(n); }
        });
        List<Ticker> listTicker = new ArrayList<>();
        listTicker.add(t1);
        listTicker.add(t2);
        listTicker.add(t3);
        listTicker.add(t4);

        for (Ticker t: listTicker) t.start();

        for (int i=0; i<31; i++) {
            for (Ticker t: listTicker) t.process(i);

            while (true) {
                int TickerCount = 0;
                for (Ticker t: listTicker) {
                    if (t.isStepСompleted())
                        TickerCount++;
                }
                if (TickerCount == 4)
                    break;
            }
        }
        for (Ticker t: listTicker) t.setActive(false);
    }
}
class Ticker extends Thread {
    private int currentStep;
    private Boolean isActive = true;
    public Boolean getActive() { return isActive; }
    public void setActive(Boolean active) { isActive = active; }

    /*  @FunctionalInterface public interface Consumer<T> */
    private Consumer<Integer> actionProgram;
    private AtomicBoolean isStepСompleted = new AtomicBoolean(true);
    public Ticker(Consumer<Integer> actionProgram) {
        /* Представляє операцію, яка приймає один вхідний аргумент і не повертає результату.
        Очікується, що Consumer працюватиме через побічні ефекти.
        Це функціональний інтерфейс, функціональним методом якого є accept(Object). */
        this.actionProgram = actionProgram;
    }
    public boolean isStepСompleted() { return isStepСompleted.get(); }
    public void process (int currentStep) {
        this.currentStep = currentStep;
        this.isStepСompleted.set(false);
    }
    @Override
    public void run() {
        while (getActive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (isStepСompleted.get()) {
                continue;
            }
            /*  перевірка умови для кожної нитки (Thread)
              t1: (n) -> if (n % 3 == 0 && n % 5 != 0) { System.out.println(n+" FIZZ"); }
              t2: (n) -> if (n % 5 == 0 && n % 3 != 0) { System.out.println(n+" BAZZ"); }
              t3: (n) -> if (n % 3 == 0 && n % 5 == 0) { System.out.println(n+" FIZZ BAZZ"); }
              t4: (n) -> if (n % 3 != 0 && n % 5 != 0) { System.out.println(n); }
            */
            actionProgram.accept(currentStep);
            isStepСompleted.set(true);
        }
    }
}