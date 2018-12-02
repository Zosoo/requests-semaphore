import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class extending Java generic Semaphore with additional parametrized functionalities:
 * 1.Limiting the number of each user's requests
 * 2.Limiting the number of users inside the system
 */
class SemaphoreExtended extends java.util.concurrent.Semaphore {
    private int maxRequestsPerUser;
    private int maxUsers;
    private ConcurrentHashMap<String, AtomicLong> userConnectionsMap = new ConcurrentHashMap<>();
    private AtomicInteger usersCounter = new AtomicInteger(0);

    /**
     * SemaphoreExtended constructor
     * @param permits - number of total permitted requests
     * @param maxUsers - number of total permitted users
     * @param maxRequestsPerUser - number of permitted requests for each user
     */
    SemaphoreExtended(int permits, int maxUsers, int maxRequestsPerUser)
    {
        super(permits);
        this.maxUsers = maxUsers;
        this.maxRequestsPerUser = maxRequestsPerUser;
    }

    /**
     *
     * @param timeout amount of time the request can wait for available resources
     * @param unit unit of time in timeout
     * @param userId id of the request owner
     * @return returns true if the request was able to enter, otherwise returns false
     * @throws InterruptedException - in case of interruptions
     */
    boolean tryAcquire(long timeout, TimeUnit unit, String userId) throws InterruptedException {
        synchronized (userConnectionsMap) {
            if(userConnectionsMap.containsKey(userId)){
                userConnectionsMap.get(userId).get();
            }
            else if(usersCounter.get()== maxUsers) {
                return false;
            }

            else
            {
                userConnectionsMap.put(userId, new AtomicLong(1));
                usersCounter.incrementAndGet();
            }

            if (( userConnectionsMap.get(userId).get() <= maxRequestsPerUser && usersCounter.get()<= maxUsers)) {
                boolean locked = super.tryAcquire(timeout,unit);
                if (locked) {
                    System.out.println("succeeded " + userId);
                    userConnectionsMap.get(userId).incrementAndGet();
                    return true;
                }
            }}

        return false;
    }

    /**
     * Method used to release the resources
     * @param userId id of the user whose request will be released
     */
    public void release(String userId){
        AtomicLong usersConnections = userConnectionsMap.get(userId);
        userConnectionsMap.get(userId).decrementAndGet();
        if(usersConnections.get() == 1){
            usersCounter.decrementAndGet();
        }
        super.release(1);
    }
}
