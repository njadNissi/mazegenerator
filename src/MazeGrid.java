import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;

public class MazeGrid extends JPanel {
    public BufferedImage mazeImage;
    public Timer timer;
    public static int MAZE_FONT_STYLE = 0;//if 0 Black-on-White; if 1 White-on-Black
    private int rows;
    private int cols;
    private Cell[][] grid;
    private Cell current; //currently being visited cell
    private Stack<Cell> stack;

    public MazeGrid(){
        this.setFocusable(false);
        this.stack = new Stack<>();
        this.initGrid(MazeGeneratorView.HEIGHT / Cell.SIZE, MazeGeneratorView.WIDTH / Cell.SIZE);

        this.timer = new Timer(240, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (timer.isRunning()){
                    generateMaze();
                    repaint();
                }
            }
        });
    }

    public void initGrid(int rows, int c){
        if(rows == 0)
            rows = 10;
        else
            this.rows = rows;
        cols = c;

        if(cols == 0)
            this.cols = 10;
        else this.cols = cols;

        this.grid = new Cell[rows][cols];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                this.grid[i][j] = new Cell(i, j);
            }
        }
        //set starting point
        this.current = grid[0][0];
        this.current.visit();
        this.stack.push(current);
    }

    private void drawGrid(Graphics g){
        for (Cell[] row: grid)
            for (Cell cell: row)
                cell.draw(g);
        current.highlight(g);
    }

    private void generateMaze(){
        int randNeighbor = 0;
        Cell next = null;

        if(current.getUnvisitedNeighbors(grid) > 0){

            System.out.println(current.getUnvisitedNeighbors(grid) + " neighbors");
            while(next == null || next.visited()){
                randNeighbor = (int) (Math.random() * 4);
                if (randNeighbor == 0 && current.getRow() - 1 > -1) //top
                    next = grid[current.getCol()][current.getRow() - 1];
                else if (randNeighbor == 1 && current.getCol() + 1 < cols) // right
                    next = grid[current.getCol() + 1][current.getRow()];
                else if (randNeighbor == 2 && current.getRow() + 1 < rows) // bottom
                    next = grid[current.getCol()][current.getRow() + 1];
                else if (randNeighbor == 3 && current.getCol() - 1 > -1) // left
                    next = grid[current.getCol() - 1][current.getRow()];
            }
            //remove barriers between current and next
            Cell.removeBarriers(current, next, randNeighbor);
            // move to the next
            current = next;
            current.visit();
            this.stack.push(current);
        } else if (!stack.isEmpty()){
            current = stack.pop();
        } else{
            timer.stop();
        }
    }

    public void drawMazeOnImage(Graphics g){
        /**Draw maze on image*/
        super.paintComponent(g);

        setBG(g);

        drawGrid(g);

        g.dispose();
    }

    private void setBG(Graphics g){
        if (MAZE_FONT_STYLE == 0)
            g.setColor(Color.WHITE);
        else if (MAZE_FONT_STYLE == 1)
            g.setColor(Color.BLACK);
    }

    @Override
    public  void paintComponent(Graphics g){
        /**draw image on screen*/
        super.paintComponent(g);

        //image
        if (mazeImage == null) {
            mazeImage = new BufferedImage(MazeGeneratorView.WIDTH, MazeGeneratorView.HEIGHT, BufferedImage.TYPE_INT_ARGB);
            mazeImage.getGraphics().fillRect(0, 0, MazeGeneratorView.WIDTH, MazeGeneratorView.HEIGHT);
        }
        g.drawImage(mazeImage, 0, 0, null);
        drawMazeOnImage(mazeImage.getGraphics());
        mazeImage.getGraphics().dispose();
    }

    public void downloadImage(URL location, String name) throws IOException {
        File file = new File(location.getPath() + "/" + name + ".png");
        ImageIO.write(mazeImage, "png", file);
    }

}
