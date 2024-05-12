package view;

import controller.NumberleController;
import model.Block;
import model.CheckEnum;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;

public class NumberleView extends JFrame implements ActionListener, Observer {
    private static final Color GREEN = Color.decode("#2fc1a5");
    private static final Color ORANGE = Color.decode("#f79a6f");
    private static final Color GREY = Color.decode("#a4aec4");
    private static final Color EMPTY = Color.decode("#fbfcff");
    public static final Color BG = Color.decode("#dce1ed");
    private NumberleController controller;
    private JPanel contentPane;
    private List<JButton> btns = new ArrayList<>();
    JButton reset;

    /**
     * Create the frame.
     */
    public NumberleView(NumberleController controller) {
        assert controller != null;
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Numberle");
        setBounds(new Rectangle(100, 100));
        setSize(600, 520);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.add(new JLabel(" "), BorderLayout.NORTH);

        contentPane.add(panel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JPanel numberPane = new JPanel();
        bottomPanel.add(numberPane);
        for (int i = 0; i <= 9; i++) {
            JButton btn = new JButton(i + "");
            btn.setFont(new Font("", Font.BOLD, 16));
            btn.setBackground(BG);
            btn.setPreferredSize(new Dimension(50, 50));
            numberPane.add(btn);
            btn.addActionListener(this);
            btns.add(btn);
        }

        JPanel opPanel = new JPanel();
        bottomPanel.add(opPanel);
        String[] op = new String[]{"⬅", "+", "-", "*", "/", "=", "ok"};
        for (String s : op) {
            JButton btn = new JButton(s);
            btn.setBackground(BG);
            btn.setPreferredSize(new Dimension(50, 50));
            opPanel.add(btn);
            btn.addActionListener(this);
            btns.add(btn);
        }
        reset = new JButton("reset");
        reset.setEnabled(false);
        reset.setBackground(BG);
        reset.setPreferredSize(new Dimension(80, 50));
        reset.addActionListener(this);
        opPanel.add(reset);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("ok")) {
            if (!controller.checkValidExp()) {
                JOptionPane.showMessageDialog(this, "invalid exp");
                return;
            }
            Block[] guess = controller.handleGuess();
            for (Block block : guess) {
                for (JButton btn : btns) {
                    if (btn.getActionCommand().equals(block.getInput())) {
                        if (block.getCheck() == CheckEnum.GREEN) {
                            btn.setBackground(GREEN);
                        } else if (block.getCheck() == CheckEnum.ORANGE) {
                            btn.setBackground(ORANGE);
                        } else if (block.getCheck() == CheckEnum.D_GREY) {
                            btn.setBackground(GREY);
                            btn.setEnabled(false);
                        }
                    }
                }
            }
            // panel.repaint();
            if (Arrays.stream(guess).filter(block -> block.getCheck() == CheckEnum.GREEN).count() == guess.length) {
                JOptionPane.showMessageDialog(this, "you win");
                btns.forEach(b -> b.setEnabled(false));
            }
            reset.setEnabled(true);
            if (controller.getTime() == controller.getBoard().length) {
                JOptionPane.showMessageDialog(this, "you lost");
            }

        } else if (actionCommand.equals("⬅")) {
            controller.delete();
        } else if (actionCommand.equals("reset")) {
            controller.reset();
        } else {
            controller.input(actionCommand);
        }
    }

    JPanel panel = new JPanel() {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(new Font("", Font.BOLD, 24));
            Block[][] board = controller.getBoard();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    Block block = board[i][j];
                    g2d.setColor(Color.LIGHT_GRAY);
                    g.drawRect(55 * j + 100, i * 55, 50, 50);
                    if (block.getCheck() == CheckEnum.GREEN) {
                        g2d.setColor(GREEN);
                    } else if (block.getCheck() == CheckEnum.D_GREY) {
                        g2d.setColor(GREY);
                    } else if (block.getCheck() == CheckEnum.ORANGE) {
                        g2d.setColor(ORANGE);
                    } else if (block.getCheck() == CheckEnum.EMPTY) {
                        g2d.setColor(EMPTY);
                    }
                    g.fillRect(55 * j + 100, i * 55, 50, 50);
                    g2d.setColor(Color.BLACK);
                    g.drawString(block.getInput(), 55 * j + 100 + 18, i * 55 + 32);
                }
            }
            g2d.setFont(new Font("", Font.BOLD, 14));
            g.drawString("allow try: " + (board.length - controller.getTime()), 0, 10);
        }
    };


    @Override
    public void update(Observable o, Object arg) {
        if ("reset".equals(arg)) {
            btns.forEach(b -> {
                b.setEnabled(true);
                b.setBackground(BG);
            });
        }
        panel.repaint();
    }
}
