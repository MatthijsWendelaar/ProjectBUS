package name.wendelaar.projectbus.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import name.wendelaar.projectbus.main.IHeadController;
import name.wendelaar.projectbus.main.LlsApi;
import name.wendelaar.projectbus.main.MainManager;
import name.wendelaar.projectbus.view.controller.Controller;

import java.io.IOException;

public class ViewManager extends Application implements IViewManager {

    private static ViewManager instance;

    private MainManager mainManager;
    private ViewState state;

    private Stage stage;

    private Scene currentScene;
    private Controller currentController;

    public static ViewManager getInstance() {
        if (instance == null) {
            launch();
        }
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        this.mainManager = (MainManager) LlsApi.getController();
        mainManager.setViewManager(this);
        this.stage = primaryStage;
        changeState(mainManager.getCurrentState());
        stage.setResizable(true);
        stage.show();
    }

    @Override
    public void changeState(ViewState state) {
        if (state == null || state == this.state) {
            return;
        }
        this.state = state;
        loadScene(this.state);
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public IHeadController getHeadController() {
        return mainManager;
    }

    public Controller getViewController() {
        return currentController;
    }

    public Scene getScene() {
        return currentScene;
    }

    private void loadScene(ViewState state) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + state.getViewFile()));
            loader.load();
            currentScene = new Scene(loader.getRoot());
            currentController = loader.getController();
            currentController.receiveViewManager(this);
            currentController.setupAfterInitialization();

            stage.setTitle("LLS - " + currentController.getTitle());
            stage.setScene(currentScene);
        } catch (IOException ex) {
            ex.printStackTrace(); //TODO: error handling!
        }
    }
}
