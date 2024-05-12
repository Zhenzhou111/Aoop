package controller;

import model.Block;
import model.NumberleModel;
import view.NumberleView;

public class NumberleController {
    private NumberleModel model;

    public NumberleController(NumberleModel model) {
        this.model = model;
    }

    public Block[][] getBoard(){
        return model.getBoard();
    }

    public int getTime() {
        return model.getTime();
    }
    public void reset(){
        model.reset();
    }
    public void delete(){
        model.delete();
    }

    public void input(String c) {
        model.input(c);
    }

    public boolean checkValidExp() {
        return model.isValidExp();
    }

    public Block[] handleGuess() {
        return model.guess();
    }
}
