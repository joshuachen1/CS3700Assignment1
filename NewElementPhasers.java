import java.util.concurrent.Phaser;
import java.util.function.BinaryOperator;

public class NewElementPhasers implements Runnable {
    private Phaser phaser;
    private int[] oldArray;
    private int index;
    private int sleep;

    public NewElementPhasers(Phaser ph, int[] oldArray, int index, int sleep) {
        this.phaser = ph;
        this.oldArray = oldArray;
        this.index = index;
        this.sleep = sleep;
    }

    @Override
    public void run() {
        phaser.register();

        // Using BinaryOperator Functional Interface to return an Integer
        // Lambda expression finds the average between two numbers: a and b
        BinaryOperator<Integer> avg = (Integer a, Integer b) -> (a + b) / 2;
        int replacement;

        try {
            System.out.println("++" + Thread.currentThread().getName() + " has arrived.");

            replacement = avg.apply(oldArray[index - 1], oldArray[index + 1]);

            // All threads must wait for all numThreads threads to catch up
            phaser.arriveAndAwaitAdvance();

            Thread.sleep(sleep);

            System.out.println("--" + Thread.currentThread().getName() + " has left.");
            oldArray[index] = replacement;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        phaser.arriveAndDeregister();
    }
}