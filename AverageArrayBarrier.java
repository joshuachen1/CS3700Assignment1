//Thread local random
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
            newArray[index - 1] = avg(oldArray[index - 1], oldArray[index + 1]);
        }
    }
    public void averageNeighbors() {
        for (int i = 0, j = 1; i < thread.length; i++, j++) {
            thread[i] = new Thread(new NewElement(j));
        }

        for (int i = 0; i < thread.length; i++) {
            thread[i].start();
        }

        for (int i = 0; i < thread.length; i++) {
            try {
                thread[i].join();
            }
            catch (InterruptedException e) {
                System.out.println("Thread " + Thread.currentThread().getId() + ": INTERRUPTED");
            }
        }

        for (int i = 0; i < thread.length; i++) {
            oldArray[i + 1] = newArray[i];
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

    public static void main(String[] args) {
        int[] oldArr = {11, 31, 9, 32, 67, 29, 61, 7, 24, 41};
        AverageArrayBarrier test = new AverageArrayBarrier(oldArr);
        test.showOldArray();
        test.averageNeighbors();
        test.showNewArray();
        test.showOldArray();
    }
}