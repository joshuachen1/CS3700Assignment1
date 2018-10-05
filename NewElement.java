import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.BinaryOperator;

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
        // Using BinaryOperator Functional Interface to return an Integer
        // Lambda expression finds the average between two numbers: a and b
        BinaryOperator<Integer> avg = (Integer a, Integer b) -> (a + b) / 2;
        int replacement;
        
        try {
            replacement = avg.apply(oldArray[index - 1], oldArray[index + 1]);
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
}

