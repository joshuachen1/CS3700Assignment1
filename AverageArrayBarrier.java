//Thread local random
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class AverageArrayBarrier {
    private int[] oldArray;
    private int[] newArray;
    private final int numThreads;
    private CyclicBarrier barrier;
    private Thread[] thread;

    public AverageArrayBarrier(int[] oldArray) {
        this.oldArray = oldArray;
        newArray = new int[oldArray.length - 2];
        numThreads = oldArray.length - 2;
        barrier = new CyclicBarrier(numThreads);
        thread = new Thread[numThreads];
    }

    public class NewElement implements Runnable{
        private int index;

        public NewElement(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            try {
                newArray[index - 1] = avg(oldArray[index - 1], oldArray[index + 1]);
                try {
                    // All threads must wait for all numThreads to catch up
                    barrier.await();
                    Thread.sleep(1000);
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                oldArray[index] = newArray[index - 1];
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void averageNeighbors() {
        for (int i = 0, j = 1; i < thread.length; i++, j++) {
            thread[i] = new Thread(new NewElement(j));
            thread[i].start();
        }
    }

    public void showNewArray() {
        for (int i = 0; i < newArray.length; i++) {
            System.out.print(newArray[i] + " ");
        }
        System.out.println();
    }

    public void showOldArray() {
        for (int i = 0; i < oldArray.length; i++) {
            System.out.print(oldArray[i] + " ");
        }
        System.out.println();
    }

    public static int avg (int a, int b) {
        return (a + b) / 2;
    }

    public static void main(String[] args) throws InterruptedException{
        int[] oldArr = {11, 31, 9, 32, 67, 29, 61, 7, 24, 41};

        AverageArrayBarrier test = new AverageArrayBarrier(oldArr);

        test.showOldArray();

        test.averageNeighbors();

        Thread.sleep(10000);

        test.showNewArray();
        test.showOldArray();

    }
}
