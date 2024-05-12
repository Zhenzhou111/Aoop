import model.Block;
import model.CheckEnum;
import model.NumberleModel;

import java.util.Arrays;
import java.util.Scanner;

public class ConsoleMain {
    public static void main(String[] args) {
        NumberleModel numberleModel = new NumberleModel(6);
        Scanner scanner = new Scanner(System.in);
        String exp;
        while (!numberleModel.isOver()) {
            print(numberleModel.getBoard());
            System.out.print("please input the express:");
            exp = scanner.nextLine();
            String[] split = exp.split("");
            for (String s : split) {
                numberleModel.input(s);
            }
            if (!numberleModel.isValidExp()) {
                Block[] blocks = numberleModel.getBoard()[numberleModel.getTime()];
                for (Block block : blocks) {
                    block.setInput(" ");
                }
                System.out.println("invalid exp");
                continue;
            }
            Block[] guess = numberleModel.guess();
            if (Arrays.stream(guess).filter(block -> block.getCheck() == CheckEnum.GREEN).count() == guess.length) {
                print(numberleModel.getBoard());
                System.out.println("you win");
                System.exit(0);
            }
            System.out.println("not right answer");
        }
        System.out.println("you lost");
    }

    static void print(Block[][] blocks) {
        for (int i = 0; i < blocks.length; i++) {
            System.out.println(Arrays.toString(blocks[i]));
        }
    }
}
