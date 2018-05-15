package name.wendelaar.projectbus.manager;

import javafx.stage.Stage;
import name.wendelaar.projectbus.view.ViewState;

public interface IViewManager {

    public void changeState(ViewState state);

    public Stage getStage();

    public IHeadController getHeadController();
}
