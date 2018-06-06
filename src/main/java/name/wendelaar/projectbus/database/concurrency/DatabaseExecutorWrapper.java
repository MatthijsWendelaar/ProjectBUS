package name.wendelaar.projectbus.database.concurrency;

import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;

public class DatabaseExecutorWrapper {

    private ExecutorService service;

    public DatabaseExecutorWrapper(ExecutorService service) {
        this.service = service;
    }

    public ExecutorService getService() {
        return service;
    }

    public void submitRunnable(Runnable runnable) {
        service.submit(runnable);
    }

    public void submit(Task task) {
        service.submit(task);
    }
}
