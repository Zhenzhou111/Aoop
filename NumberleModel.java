package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class NumberleModel extends Observable {
    /**
     * One flag should, if set, cause an error message to be displayed if the equation is not
     * valid; this will not then count as one of the tries.
     * • Another flag should, if set, display the target equation for testing purposes.
     * • A third flag should, if set, cause the equation to be randomly selected. If unset, the
     * equation will be fixed
     */
    public static boolean FLAG_COUNT_ERROR = false;
    public static boolean FLAG_TEST = false;
    public static boolean FLAG_RANDOM = true;

    private String curExp = "2+3=5*1";
    private List<String> allExps = new ArrayList<>();

    private Block[][] board;
    private int time = 0;

    public NumberleModel(int maxGuessTime) {
        this("equations.txt", maxGuessTime);
    }

    public NumberleModel(String fileName, int maxGuessTime) {
        // guess must larger than 0
        assert maxGuessTime > 0;
        read(fileName);
        if (FLAG_TEST) {
            curExp = "2+3=5*1";
            System.out.println("the test equation is: " + curExp);
        }
        board = new Block[maxGuessTime][7];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Block();
            }
        }
    }

    public void read(String file) {
        // Read expressions from the file and shuffle them as needed.Read expressions from the file and shuffle them as needed.
        try {
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                allExps.add(line);
            }
            if (FLAG_RANDOM) {
                Collections.shuffle(allExps);
            }
            curExp = allExps.get(0);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String getExp() {
        // Gets the expression of the current attempt.
        Block[] blocks = board[time];
        return Arrays.stream(blocks).map(bl -> bl.getInput()).collect(Collectors.joining());
    }

    public boolean isValidExp() {
        // Check whether the currently attempted expression is valid.
        if (FLAG_COUNT_ERROR) {
            return true;
        }
        boolean isValid;
        String exp = getExp();
        String[] str = exp.split("=");
        if (str.length != 2) {
            setChanged();
            notifyObservers();
            return false;
        }
        isValid = cal(str[0]) == cal(str[1]);
        if (!isValid) {
            setChanged();
            notifyObservers();
        }
        return isValid;
    }

    public boolean isRight(Block[] blocks) {
        // Check whether all blocks currently attempted are correct.
        return Arrays.stream(blocks).filter(block -> block.getCheck() == CheckEnum.GREEN).count() == blocks.length;
    }

    public boolean isOver() {
        return time >= board.length;
    }

    public void input(String c) {
//        assert !"0123456789+-*/=".contains(c);
        for (int i = 0; i < board[time].length; i++) {
            if (board[time][i].getInput().equals(" ")) {
                board[time][i].setInput(c);
                setChanged();
                notifyObservers();
                break;
            }
        }

    }

    public void delete() {
        for (int i = board[time].length - 1; i >= 0; i--) {
            if (!board[time][i].getInput().equals(" ")) {
                board[time][i].setInput(" ");
                setChanged();
                notifyObservers();
                break;
            }
        }

    }

    public Block[] guess() {
        // Update the game board and change the number of attempts.
        String exp = getExp();
        Block[] blocks = board[time];
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            blocks[i].setInput(Character.toString(c));
            // Set the check state of the block according to the corresponding relationship between the current character and the target expression.
            if (c == curExp.charAt(i)) {
                blocks[i].setCheck(CheckEnum.GREEN);
            } else if (curExp.contains(Character.toString(c))) {
                blocks[i].setCheck(CheckEnum.ORANGE);
            } else {
                blocks[i].setCheck(CheckEnum.D_GREY);
            }
        }
        time++;
        setChanged();
        notifyObservers();
        return blocks;
    }

    public Block[][] getBoard() {
        // Get the current game board
        return board;
    }

    public int getTime() {
        // Gets the number of current attempts.
        return time;
    }

    public void reset() {
        // Reset game state
        time = 0; // Number of recharge attempts
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Block();
            }
        }
        if (FLAG_RANDOM) {
            Collections.shuffle(allExps);
        }
        curExp = allExps.get(0);
        if (FLAG_TEST) {
            curExp = "2+3=5*1";
            System.out.println("the test equation is: " + curExp);
        }
        setChanged();
        notifyObservers("reset"); // Notify observers that the game has been reset.
    }

    public String infixToPostFix(String str) {
        // Convert infix expression to suffix expression
        String[] split = str.split("");
        Stack<String> ops = new Stack<>();
        StringBuilder postFix = new StringBuilder();
        String pix = "+-*/";
        int[] tag = new int[]{0, 0, 1, 1};
        for (int i = 0; i < split.length; i++) {
            String c = split[i];
            if (pix.contains(c)) {
                while (!ops.isEmpty() && tag[pix.indexOf(ops.peek())] >= tag[pix.indexOf(c)]) {
                    postFix.append(ops.pop()).append(" ");
                }
                ops.add(c);
            } else {
                int index = i;
                while (index < split.length - 1 && !pix.contains(split[++index])) {
                    c += split[index];
                }
                i = i + c.length() - 1;
                postFix.append(c).append(" ");
            }
        }
        while (!ops.isEmpty()) {
            postFix.append(ops.pop()).append(" ");
        }
        return postFix.toString();
    }

    public int cal(String exp) {
        // Calculate the value of an expression
        exp = infixToPostFix(exp);
        String[] split = exp.split(" +");
        Stack<Integer> fix = new Stack<>();
        for (String c : split) {
            if ("+".equals(c)) {
                Integer a = fix.pop();
                Integer b = fix.pop();
                fix.add(a + b);
            } else if ("-".equals(c)) {
                Integer a = fix.pop();
                Integer b = fix.pop();
                fix.add(b - a);
            } else if ("*".equals(c)) {
                Integer a = fix.pop();
                Integer b = fix.pop();
                fix.add(a * b);
            } else if ("/".equals(c)) {
                Integer a = fix.pop();
                Integer b = fix.pop();
                fix.add(b / a);
            } else {
                fix.add(Integer.parseInt(c));
            }
        }

        return fix.pop(); // Pop up from the stack and return the calculated final result
    }
}
