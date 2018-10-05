import java.util.concurrent.Phaser;
import java.util.function.BinaryOperator;

public class AverageArrayPhasers {
    public static void main(String[] args) throws InterruptedException {
        Phaser phaser = new Phaser();
        int[] oldArr = {11, 31, 9, 32, 67, 29, 61, 7, 24, 41};
        final int numThreads = oldArr.length - 2;
        int sleepTime = 1000;

        int phaseCount = phaser.getPhase();
        System.out.println("Current Phase: " + phaseCount);

        printArray(oldArr);

        new AverageArrayPhasers().newElement(phaser, oldArr, 1, 2000);
        new AverageArrayPhasers().newElement(phaser, oldArr, 2, 4000);
        new AverageArrayPhasers().newElement(phaser, oldArr, 3, 6000);

        Thread.sleep();

        phaseCount = phaser.getPhase();
        System.out.println("Current Phase: " + phaseCount);

        printArray(oldArr);
    }

    public static void printArray(int[] oldArray) {
        for (int i = 0; i < oldArray.length; i++) {
            System.out.print(oldArray[i] + " ");
        }
        System.out.println();
    }

    private void newElement(Phaser ph, int[] oldArray, int index, int sleep) {
        // Thread registers itself to the phaser
        ph.register();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Using BinaryOperator Functional Interface to return an Integer
                // Lambda expression finds the average between two numbers: a and b
                BinaryOperator<Integer> avg = (Integer a, Integer b) -> (a + b) / 2;
                int replacement;

                try {
                    System.out.println("++" + Thread.currentThread().getName() + " has arrived.");

                    replacement = avg.apply(oldArray[index - 1], oldArray[index + 1]);

                    // All threads must wait for all numThreads threads to catch up
                    ph.arriveAndAwaitAdvance();

                    Thread.sleep(sleep);

                    System.out.println("--" + Thread.currentThread().getName() + " has left.");
                    oldArray[index] = replacement;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}