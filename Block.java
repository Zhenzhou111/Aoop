package model;

public class Block {
    private int x;
    private int y;
    private String input=" ";
    CheckEnum check;

    public Block() {
        check = CheckEnum.EMPTY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public CheckEnum getCheck() {
        return check;
    }

    public void setCheck(CheckEnum check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "[" + input + "(" + check + ")]";
    }
}
