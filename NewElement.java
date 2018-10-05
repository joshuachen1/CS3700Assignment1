import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class NewElement implements Runnable{
    private CyclicBarrier barrier;
    private int[] oldArray;
    private int index;

    public NewElement(CyclicBarrier barrier, int[] oldArray, int index) {
        this.barrier = barrier;
        this.oldArray = oldArray;
        this.index = index;
    }

    @Override
    public void run() {
        int replacement;
        try {
            replacement = avg(oldArray[index - 1], oldArray[index + 1]);
            try {
                // All threads must wait for all numThreads threads to catch up
                barrier.await();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            oldArray[index] = replacement;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int avg (int a, int b) {
        return (a + b) / 2;
    }
}