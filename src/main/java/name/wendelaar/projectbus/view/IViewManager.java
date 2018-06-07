package name.wendelaar.projectbus.view;

import javafx.stage.Stage;
import name.wendelaar.projectbus.main.IHeadController;
import name.wendelaar.projectbus.view.ViewState;

public interface IViewManager {

    public void changeState(ViewState state);

    public Stage getStage();

    public IHeadController getHeadController();
}
