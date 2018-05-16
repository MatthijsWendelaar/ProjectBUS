package name.wendelaar.projectbus;

import name.wendelaar.projectbus.view.MainManager;
import name.wendelaar.projectbus.view.ViewState;

public class Main {

    public static void main(String args[]) {
        new MainManager(ViewState.INDEX_DEFAULT);
    }
}
