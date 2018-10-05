import java.util.concurrent.Phaser;

public class AverageArrayPhasers {
    public static void main(String[] args) throws InterruptedException {
        Phaser phaser = new Phaser();
        int[] oldArr = {11, 31, 9, 32, 67, 29, 61, 7, 24, 41};
        final int numThreads = oldArr.length - 2;

        int phaseCount = phaser.getPhase();
        System.out.println("Current Phase: " + phaseCount);

        printArray(oldArr);

        new NewElementPhasers(phaser, oldArr, 1, 1000).run();

        phaser.arriveAndDeregister();

        Thread.sleep(5000);

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
}