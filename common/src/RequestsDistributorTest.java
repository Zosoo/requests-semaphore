import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RequestsDistributorTest {
    private static RequestsDistributor requestsDistributor = new RequestsDistributor(10, 5, 4);

    /**
     * main method used to test RequestsDistributor with multiple threads
     *
     * @param args - for future refactoring
     */
    public static void main(String[] args) {
        List<String> myList = Arrays.asList("user1", "user2", "user3", "user4", "user5");
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            MyThread t1 = new MyThread(myList.get(random.nextInt(4)), random.nextInt(700));
            t1.start();
        }
    }

    /**
     * Class MyThread implemented to run RequestDistributor work method with the use of multithreading
     */
    static class MyThread extends Thread {
        String name;
        Integer timeout;
        Boolean success = false;

        /**
         * MyThread constructor with default timeout value - for future use
         *
         * @param name - name of the user sending the request
         */
        MyThread(String name) {
            this(name, 500);
        }

        /**
         * MyThread constructor
         *
         * @param name    - name of the user who is sending the request
         * @param timeout - defines how lonthe request will wait to enter the system
         */
        MyThread(String name, int timeout) {
            this.name = name;
            this.timeout = timeout;
        }

        public void run() {
            success = requestsDistributor.work(name, timeout);
//           System.out.println(success);
        }
    }
}
