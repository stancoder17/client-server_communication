package Server;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Statistics implements Runnable {
    private static Statistics instance;

    // All-time values
    protected static AtomicInteger clientsCount = new AtomicInteger(0);
    protected static AtomicInteger successfulOperationsCount = new AtomicInteger(0);
    protected static AtomicInteger invalidOperationsCount = new AtomicInteger(0);
    protected static AtomicInteger addCount = new AtomicInteger(0);
    protected static AtomicInteger subCount = new AtomicInteger(0);
    protected static AtomicInteger mulCount = new AtomicInteger(0);
    protected static AtomicInteger divCount = new AtomicInteger(0);
    protected static AtomicReference<Double> sum = new AtomicReference<>(0.0);

    // Values from last ten seconds
    protected static AtomicInteger tenSec_clientsCount = new AtomicInteger(0);
    protected static AtomicInteger tenSec_successfulOperationsCount = new AtomicInteger(0);
    protected static AtomicInteger tenSec_invalidOperationsCount = new AtomicInteger(0);
    protected static AtomicInteger tenSec_addCount = new AtomicInteger(0);
    protected static AtomicInteger tenSec_subCount = new AtomicInteger(0);
    protected static AtomicInteger tenSec_mulCount = new AtomicInteger(0);
    protected static AtomicInteger tenSec_divCount = new AtomicInteger(0);
    protected static AtomicReference<Double> tenSec_sum = new AtomicReference<>(0.0);

    private Statistics() {}

    protected static Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
        }
        return instance;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(
                    "======================{ Complete statistics }========================\n" +
                                    "Clients count: " + clientsCount.get() + "\n" +
                            "Successful operations count: " + successfulOperationsCount.get() + "\n" +
                            "Invalid operations count: " + invalidOperationsCount.get() + "\n" +
                            "ADD operations count: " + addCount.get() + "\n" +
                            "SUB operations count: " + subCount.get() + "\n" +
                            "MUL operations count: " + mulCount.get() + "\n" +
                            "DIV operations count: " + divCount.get() + "\n" +
                            "Sum: " + sum.get() + "\n" +
                            "================{ Statistics from the last 10 seconds }==============\n" +
                            "Clients count: " + tenSec_clientsCount + "\n" +
                            "Successful operations count: " + tenSec_successfulOperationsCount.get() + "\n" +
                            "Invalid operations count: " + tenSec_invalidOperationsCount.get() + "\n" +
                            "ADD operations count: " + tenSec_addCount.get() + "\n" +
                            "SUB operations count: " + tenSec_subCount.get() + "\n" +
                            "MUL operations count: " + tenSec_mulCount.get() + "\n" +
                            "DIV operations count: " + tenSec_divCount.get() + "\n" +
                            "Sum: " + tenSec_sum.get() + "\n" +
                            "====================================================================="
            );
            tenSec_clientsCount.set(0);
            tenSec_successfulOperationsCount.set(0);
            tenSec_invalidOperationsCount.set(0);
            tenSec_addCount.set(0);
            tenSec_subCount.set(0);
            tenSec_mulCount.set(0);
            tenSec_divCount.set(0);
            tenSec_sum.set(0.0);
        }
    }

    public static void addToSum(double value) {
        sum.updateAndGet(currentSum -> currentSum + value);
    }

    public static void addToTenSecSum(double value) {
        tenSec_sum.updateAndGet(currentSum -> currentSum + value);
    }
}
