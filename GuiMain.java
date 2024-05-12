import controller.NumberleController;
import model.NumberleModel;
import view.NumberleView;

public class GuiMain {
    public static void main(String[] args) {
        NumberleModel model = new NumberleModel(6);
        NumberleView view  = new NumberleView(new NumberleController(model));
        model.addObserver(view);
        view.setVisible(true);
    }
}
