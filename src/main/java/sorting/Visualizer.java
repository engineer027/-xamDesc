package sorting;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Visualizer extends JFrame implements ActionListener {
    private JFrame frame;
    private JLabel labelArrayLength;
    private JLabel labelSpeedSort;
    private JButton enter;
    private JButton sort;
    private JButton reset;
    private JTextField textArrayLength;
    private JTextField textSpeedSort;
    private int[] arrayNumbers;
    private List<JLabel> labels;
    private int speedSort;
    private Thread thread;

    Visualizer() {
        super("Write parameters");
        enter = new JButton("Enter");
        enter.setBounds(100,150,100, 30);
        enter.setBackground(Color.blue);
        enter.setForeground(Color.white);
        enter.addActionListener(this);
        add(enter);

        sort = new JButton("Sort");
        sort.setBounds(1200,100,100, 30);
        sort.setForeground(Color.white);
        sort.setBackground(Color.GREEN);
        sort.addActionListener(this);

        reset = new JButton("Reset");
        reset.setBounds(1200,150,100, 30);
        reset.setForeground(Color.white);
        reset.setBackground(Color.GREEN);
        reset.addActionListener(this);

        textArrayLength = new JTextField();
        textArrayLength.setBounds(100, 100, 100, 20);
        add(textArrayLength);

        textSpeedSort = new JTextField();
        textSpeedSort.setBounds(1200, 250, 100, 20);

        labelArrayLength = new JLabel("How many numbers to display?");
        labelArrayLength.setBounds(60, 50, 180, 20);
        add(labelArrayLength);

        labelSpeedSort = new JLabel("<html>Enter speed show sort [1;30]</html>");
        labelSpeedSort.setBounds(1200, 200, 100,40);

        setSize(300,300);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Visualizer();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == enter) {
            try {
                int arrayLength = Integer.parseInt(textArrayLength.getText());
                if (arrayLength > 50 || arrayLength <= 0) {
                    throw new RuntimeException();
                }
                setVisible(false);
                frame = new JFrame();
                frame.setSize(1500,800);
                frame.setLayout(null);
                frame.setVisible(true);
                frame.add(sort);
                frame.add(reset);
                frame.add(textSpeedSort);
                frame.add(labelSpeedSort);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                createRandomArray(arrayLength);
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(Visualizer.this,"Please enter the correct value!");
            }
        }
        if (actionEvent.getSource() == sort) {
            try {
                speedSort = Integer.parseInt(textSpeedSort.getText());
                if (speedSort > 30 || speedSort <= 0) {
                    throw new RuntimeException();
                }
                for (JLabel label: labels) {
                    label.setBackground(Color.GREEN);
                    label.setForeground(Color.white);
                }
                thread = new Thread() {
                    @Override
                    public void run() {
                        quickSort(arrayNumbers, 0, arrayNumbers.length - 1);
                    }
                };
                thread.start();
                reset.setEnabled(false);
                sort.setEnabled(false);
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(Visualizer.this,"Please enter the correct value!");
            }
        }
        if (actionEvent.getSource() == reset) {
            frame.setVisible(false);
            textArrayLength.setText("");
            setVisible(true);
            frame.removeAll();
        }
    }

    private int[] createRandomArray(int arrayLength) {
        arrayNumbers = new int[arrayLength];
        labels = new ArrayList<>();
        int x = 100;
        for (int i = 0; i < arrayLength; i++) {
            if (i % 10 == 0) {
                x = x + 120;
            }
            arrayNumbers[i] = (int) (Math.random() * 1000);
            JLabel label = new JLabel(arrayNumbers[i] + "", SwingConstants.CENTER);
            label.setBounds(x, 100 + (50 * (i % 10)), 100,30);
            label.setForeground(Color.RED);
            label.setBackground(Color.BLUE);
            label.setOpaque(true);
            frame.add(label);
            labels.add(label);
        }
        return arrayNumbers;
    }

    public void quickSort(int[] arr, int from, int to) {

        if (from < to) {
            int divideIndex = partition(arr, from, to);

            quickSort(arr, from, divideIndex - 1);

            quickSort(arr, divideIndex, to);
        } else {
            for (int i = from; i <= to; i++) {
                labels.get(i).setBackground(Color.BLUE);
                labels.get(i).setForeground(Color.white);
            }
        }
        if (labels.get(arrayNumbers.length - 1).getBackground() == Color.BLUE) {
            reset.setEnabled(true);
            sort.setEnabled(true);
        }
    }

    private int partition(int[] arr, int from, int to) {
        int rightIndex = to;
        int leftIndex = from;
        int pivot = arr[from + (to - from) / 2];
        labels.get(from + (to - from) / 2).setBackground(Color.pink);
        labels.get(from + (to - from) / 2).setBorder(new LineBorder(Color.pink));

        while (leftIndex <= rightIndex) {
            labels.get(rightIndex).setBorder(new LineBorder(Color.RED));

            while (arr[leftIndex] < pivot) {
                try {
                    labels.get(leftIndex).setBorder(new LineBorder(Color.RED));
                    thread.sleep(1000 / speedSort);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                labels.get(leftIndex).setBorder(new LineBorder(Color.white));
                leftIndex++;
            }
            labels.get(leftIndex).setBorder(new LineBorder(Color.RED));

            while (arr[rightIndex] > pivot) {
                try {
                    labels.get(rightIndex).setBorder(new LineBorder(Color.RED));
                    thread.sleep(1000 / speedSort);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                labels.get(rightIndex).setBorder(new LineBorder(Color.white));
                rightIndex--;
            }
            labels.get(rightIndex).setBorder(new LineBorder(Color.RED));

            if (leftIndex <= rightIndex) {
                swap(arr, rightIndex, leftIndex);
                labels.get(leftIndex).setBorder(new LineBorder(Color.white));
                labels.get(rightIndex).setBorder(new LineBorder(Color.white));
                leftIndex++;
                rightIndex--;
            }
        }
        labels.get(from + (to - from) / 2).setBackground(Color.GREEN);
        labels.get(from + (to - from) / 2).setBorder(new LineBorder(Color.GREEN));
        if (leftIndex >= 0) {
            labels.get(leftIndex).setBorder(new LineBorder(Color.white));
        }
        if (rightIndex >= 0) {
            labels.get(rightIndex).setBorder(new LineBorder(Color.white));
        }
        return leftIndex;
    }

    private void swap(int[] array, int index1, int index2) {
        int tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
        labels.get(index1).setText(array[index1] + "");
        labels.get(index2).setText(array[index2] + "");
        labels.get(index1).setForeground(Color.red);
        labels.get(index2).setForeground(Color.red);
        try {
            thread.sleep(1000 / speedSort);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        labels.get(index1).setForeground(Color.white);
        labels.get(index2).setForeground(Color.white);
    }
}
