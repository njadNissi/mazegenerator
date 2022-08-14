import java.awt.*;

public class Cell {
    private int col;
    private int row;
    private boolean[] neighbors;//top, right, bottom, left
    private boolean visited;
    public static int BORDER_THICKNESS = 2;//a border being a rectangle needs with and height
    public static int SIZE = 20;

    /**GRAPHICS COORDINATES SETTING*/
    private int x1, x2, y1, y2;

    public Cell(int x, int y) {
        this.col = x;
        this.row = y;
        this.x1 = col * SIZE;
        this.x2 = SIZE * (col + 1);
        this.y1 = row * SIZE;
        this.y2 = SIZE * (row + 1);
        this.neighbors = new boolean[]{true, true, true, true};
        this.visited = false;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    /*public void draw(Graphics g){
        g.setColor(Color.BLACK);

        if (neighbors[0]) //top x1,y1 to x2,y1
            g.drawLine(x1, y1, x2, y1);
        if (neighbors[1]) //right x2,y1 to x2,y2
            g.drawLine(x2, y1, x2, y2);
        if (neighbors[2]) //bottom x1,y2 to x2,y2
            g.drawLine(x1, y2, x2, y2);
        if (neighbors[3]) //left x1,y1 to x1,y2
            g.drawLine(x1, y1, x1, y2);

        *//*if(this.visited()){
            g.setColor(Color.magenta);
            g.fillRect(x1 + 1, y1 + 1, MazeGrid.CELLSIZE - 2, MazeGrid.CELLSIZE - 2);
        }*//*
    }*/
    public void draw(Graphics g){
        if (MazeGrid.MAZE_FONT_STYLE == 0)
            g.setColor(Color.BLACK);
        else if (MazeGrid.MAZE_FONT_STYLE == 1)
            g.setColor(Color.WHITE);

        if (neighbors[0]) //top x1,y1 to x2,y1
            g.fillRect(x1, y1, SIZE, BORDER_THICKNESS);
        if (neighbors[1]) //right x2,y1 to x2,y2
            g.fillRect(x2, y1, BORDER_THICKNESS, SIZE);
        if (neighbors[2]) //bottom x1,y2 to x2,y2
            g.fillRect(x1, y2, SIZE, BORDER_THICKNESS);
        if (neighbors[3]) //left x1,y1 to x1,y2
            g.fillRect(x1, y1, BORDER_THICKNESS, SIZE);

        /*if(this.visited()){
            g.setColor(Color.getHSBColor(195, 110, 170));
            g.fillRect(x1 + 1, y1 + 1, MazeGrid.CELLSIZE - 2, MazeGrid.CELLSIZE - 2);
        }*/
    }
    public void highlight(Graphics g){
        g.setColor(Color.green);
        g.fillRect(x1, y1, SIZE, SIZE);
    }

    public void visit() {
        this.visited = true;
    }

    public boolean visited() {
        return this.visited;
    }

    public int getUnvisitedNeighbors(Cell[][] grid){
        int neighbors = 0;
        if (this.getRow() - 1 > -1 && !grid[getCol()][getRow() - 1].visited())
            neighbors++;
        if (this.getCol() + 1 < grid[0].length && !grid[getCol() + 1][getRow()].visited())
            neighbors++;
        if (this.getRow() + 1 < grid.length && !grid[getCol()][getRow() + 1].visited())
            neighbors++;
        if (this.getCol() - 1 > -1 && !grid[getCol() - 1][getRow()].visited())
            neighbors++;
        return neighbors;
    }

    public static void removeBarriers(Cell c1, Cell c2, int barrier){
        switch (barrier){
            case 0 -> {
                c1.neighbors[0] = false;
                c2.neighbors[2] = false;
            }
            case 1 -> {
                c1.neighbors[1] = false;
                c2.neighbors[3] = false;
            }
            case 2 -> {
                c1.neighbors[2] = false;
                c2.neighbors[0] = false;
            }
            case 3 -> {
                c1.neighbors[3] = false;
                c2.neighbors[1] = false;
            }
        }
    }


}
