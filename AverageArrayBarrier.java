import java.util.concurrent.CyclicBarrier;

public class AverageArrayBarrier {
    public static void main(String[] args) throws InterruptedException {
        int[] oldArr = {11, 31, 9, 32, 67, 29, 61, 7, 24, 41};
        final int numThreads = oldArr.length - 2;
        Thread[] thread = new Thread[numThreads];
        CyclicBarrier barrier = new CyclicBarrier(numThreads);

        printArray(oldArr);

        for (int i = 0; i < numThreads; i++) {
            thread[i] = new Thread(new NewElement(barrier, oldArr, i + 1));
            thread[i].start();
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
}
