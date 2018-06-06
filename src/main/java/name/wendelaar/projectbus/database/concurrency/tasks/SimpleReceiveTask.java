package name.wendelaar.projectbus.database.concurrency.tasks;

import javafx.concurrent.Task;

public abstract class SimpleReceiveTask<V> extends Task<V> {

    @Override
    protected V call() {
        return execute();
    }

    public abstract V execute();
}
