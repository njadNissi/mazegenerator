import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MazeGeneratorView extends JFrame {
    public static int WIDTH = 600;
    public static int HEIGHT = 600;
    private MazeGrid mazeGrid;

    public MazeGeneratorView(){
        this.setTitle("NJAD AnJie Maze Generator");
        this.setSize(WIDTH + 25, HEIGHT + 100);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.mazeGrid = new MazeGrid();

        JPanel panel = new JPanel(new BorderLayout());

        Panel sizePanel = new Panel();
        Label label = new Label("Maze size = " + HEIGHT / Cell.SIZE + " x " + WIDTH / Cell.SIZE);
        sizePanel.add(label);

        JPanel buttons = new JPanel(new GridLayout(2, 4));

        JButton changeStyle = new JButton("Black-on-White");
        changeStyle.addActionListener((a) ->{
            if(changeStyle.getText().equalsIgnoreCase("Black-on-White")){
                MazeGrid.MAZE_FONT_STYLE = 1;//change to White-on-Black
                //change to ?
                changeStyle.setText("White-on-Black");
            } else {
                MazeGrid.MAZE_FONT_STYLE = 0;//change to Black-on-White
                changeStyle.setText("Black-on-White");
            }
            mazeGrid.repaint();
        });
        buttons.add(changeStyle);

        JSpinner borderThickness = new JSpinner();
        borderThickness.setToolTipText("border thickness");
        borderThickness.setValue(Cell.BORDER_THICKNESS);
        borderThickness.addChangeListener((cl -> {
            Cell.BORDER_THICKNESS = (int) borderThickness.getValue();
            mazeGrid.repaint();
        }));
        buttons.add(borderThickness);

        JSpinner cellSize = new JSpinner();
        cellSize.setToolTipText("cell size");
        cellSize.setValue(Cell.SIZE);
        cellSize.addChangeListener((cl -> {
            Cell.SIZE = (int) cellSize.getValue();
            mazeGrid.initGrid(HEIGHT / Cell.SIZE, WIDTH / Cell.SIZE);
            mazeGrid.repaint();
            label.setText("Maze size = " + HEIGHT / Cell.SIZE + " x " + WIDTH / Cell.SIZE);
        }));
        buttons.add(cellSize);

        JButton generateMaze = new JButton("new maze");
        generateMaze.addActionListener((a) ->{
            mazeGrid.initGrid(HEIGHT / Cell.SIZE, WIDTH / Cell.SIZE);
            mazeGrid.repaint();
        });
        buttons.add(generateMaze);

        JButton start = new JButton("start");
        start.addActionListener((a) -> mazeGrid.timer.start());
        buttons.add(start);

        JButton pause = new JButton("pause");
        pause.addActionListener((a) -> mazeGrid.timer.stop());
        buttons.add(pause);

        JSpinner drawingSpeed = new JSpinner();
        drawingSpeed.setToolTipText("delay");
        drawingSpeed.setValue(mazeGrid.timer.getDelay());
        drawingSpeed.addChangeListener((cl -> {
            mazeGrid.timer.setDelay((int) drawingSpeed.getValue());
        }));
        buttons.add(drawingSpeed);

        JButton downloadMaze = new JButton("Download");
        downloadMaze.addActionListener(a -> {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(null);
            try {
                URL location = fc.getCurrentDirectory().toURI().toURL();
                String name = JOptionPane.showInputDialog("Enter name:");
                mazeGrid.downloadImage(location, name);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        buttons.add(downloadMaze);

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(mazeGrid, BorderLayout.CENTER);
        panel.add(sizePanel, BorderLayout.SOUTH);


        this.add(panel);
    }

    public void launch(){
        this.setVisible(true);
    }

}
