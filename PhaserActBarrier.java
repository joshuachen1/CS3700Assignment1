import java.util.concurrent.Phaser;
import java.util.function.BinaryOperator;

/**
 * Created by student on 10/5/18.
 */
public class PhaserActBarrier {
    public static void main(String[] args) throws InterruptedException {
        int[] oldArr = {11, 31, 9, 32, 67, 29, 61, 7, 24, 41};
        final int numThreads = oldArr.length - 2;

        Phaser phaserBarrier = new Phaser(0);
        phaserBarrier.bulkRegister(numThreads);

        printArray(oldArr);

        for (int i = 0; i < numThreads; i++) {
            new PhaserActBarrier().newElement(phaserBarrier, oldArr, i + 1);
        }

        Thread.sleep(5000);
        printArray(oldArr);
    }

    public static void printArray(int[] oldArray) {
        for (int i = 0; i < oldArray.length; i++) {
            System.out.print(oldArray[i] + " ");
        }
        System.out.println();
    }

    private void newElement(Phaser ph, int[] oldArray, int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Using BinaryOperator Functional Interface to return an Integer
                // Lambda expression finds the average between two numbers: a and b
                BinaryOperator<Integer> avg = (Integer a, Integer b) -> (a + b) / 2;
                int replacement;

                System.out.println("++" + Thread.currentThread().getName() + " has arrived.");

                replacement = avg.apply(oldArray[index - 1], oldArray[index + 1]);

                // All threads must wait for all numThreads threads to catch up
                // Phaser acting like a barrier
                ph.arriveAndAwaitAdvance();

                System.out.println("--" + Thread.currentThread().getName() + " has left.");
                oldArray[index] = replacement;

            }
        }).start();
    }
}
