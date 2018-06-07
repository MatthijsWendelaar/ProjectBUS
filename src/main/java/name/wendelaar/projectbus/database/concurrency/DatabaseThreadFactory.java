package name.wendelaar.projectbus.database.concurrency;

import java.util.concurrent.ThreadFactory;

public class DatabaseThreadFactory implements ThreadFactory {

    private int id = 0;
    private String name;

    public DatabaseThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, name + id++);
        thread.setDaemon(true);
        return thread;
    }
}
