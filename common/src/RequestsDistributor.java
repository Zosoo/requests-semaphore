import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Class used to distribute resources among incoming requests with the use of  parametrized ExtendedSemaphore
 */
class RequestsDistributor {
    private static SemaphoreExtended semaphore = new SemaphoreExtended(2, 3, 2);

    /**
     * Constructor used to parametrize Semaphore
     *
     * @param maxPermits         - maximum of total requests in the system
     * @param maxUsers-          max number of sum of the users in the system
     * @param maxRequestsPerUser - max number of requests for each user
     */
    RequestsDistributor(int maxPermits, int maxUsers, int maxRequestsPerUser) {
        this.semaphore = new SemaphoreExtended(maxPermits, maxUsers, maxRequestsPerUser);
    }

    /**
     * Method evaluating if the request has granted the required resources
     *
     * @param name    - name of the request owner
     * @param timeout - timeout of the request before it gets into system
     * @return - return true if the request was accepted otherwise returns false
     */
    Boolean work(String name, long timeout) {
        Boolean success = false;
        try {
            success = semaphore.tryAcquire(timeout, TimeUnit.MILLISECONDS, name);
            if (success) {
                Random generator = new Random();
                int i = generator.nextInt(2001);
                System.out.println("inside system " + name + " for  " + i * 0.001 + "s");
                Thread.sleep(i);
                semaphore.release(name);
            } else {
                System.out.println("failed " + name);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return success;
    }

}

