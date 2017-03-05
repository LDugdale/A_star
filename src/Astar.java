import java.awt.*;
import java.io.FileReader;
import java.util.*;

/**
 * @author Laurie Dugdale
 */
public class Astar {

    PriorityQueue<Position> queue;
    Set<Coordinates> visited;
    char grid [][];
    int costSoFar;
    Position goal;
    Position start;

    Astar(){
        visited = new HashSet<>();

        queue = new PriorityQueue<Position>(10, new Comparator<Position>(){

            //override compare method
            public int compare(Position i, Position j) {
                if( i.getPriority() > j.getPriority()){
                    return 1;
                } else if (i.getPriority() < j.getPriority()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    public void aStar(){

        queue.add(start);
        costSoFar = 0;
        visited.add(start.getCoordinates());

        boolean found = false;

        while(!queue.isEmpty() && !found ){

            Position current = queue.poll();

            if(isGoal(current)){
                // found it!
                found = true;
            }

            // expand all possible moves from current
            for(Position next : getEdges(current)){
                if (next == null ){
                    continue;
                }

                //cost of move is entire cost so far + cost to move to new node
                int newCost = current.getCostSoFar() + costOfMove(next);

                // don't expand (or explore) nodes twice unless it's cheaper this time
                if (!visited.contains(next.getCoordinates()) && queue.contains(next) ||(newCost < costSoFar) ) {
                    System.out.println(next.getCoordinates().getX() + " " + next.getCoordinates().getY());
                    visited.add(next.getCoordinates());
                    next.setCostSoFar(newCost);

                    // the next node we want to look at is the one with the lowest projected cost
                    int priority = newCost + heuristic(next.getCoordinates());
                    next.setPriority(priority);
                    queue.add(next);
                }
            }
        }
    }

    public int heuristic(Coordinates pos){
//        System.out.println(Math.abs(x-food.getX())+ Math.abs(y-food.getY()));
        return Math.abs(pos.getX() - goal.getCoordinates().getX())+ Math.abs(pos.getX() - goal.getCoordinates().getY());
    }

    public Position [] getEdges(Position node){

        Position [] options = new Position [4];
        int x = node.getCoordinates().getX();
        int y = node.getCoordinates().getY();

        if (this.grid[x-1][y] != '%'){

            options[0] = new Position( new Coordinates(x-1, y));
        } else if (this.grid[x][y+1] != '%'){

            options[1] = new Position( new Coordinates(x, y+1));
        } else if (this.grid[x][y-1] != '%'){

            options[2] = new Position( new Coordinates(x, y-1));
        } else if (this.grid[x+1][y] != '%'){

            options[3] = new Position( new Coordinates(x+1, y));
        }

        return options;
    }

    public void start(){

//        Scanner in = new Scanner(System.in);

        try (Scanner in = new Scanner(new FileReader("test"))) {

            int pacmanX = in.nextInt();
            int pacmanY = in.nextInt();
            start = new Position( new Coordinates(pacmanX, pacmanY));
            start.setPriority(0);

            int foodX = in.nextInt();
            int foodY = in.nextInt();
            goal = new Position(new Coordinates(foodX, foodY));

            int mapX = in.nextInt();
            int mapY = in.nextInt();

            this.grid = new char[mapX][mapY];

            for (int i = 0; i < mapX; i++) {
                this.grid[i] = in.next().toCharArray();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        aStar();

        for(Position n : queue){

            System.out.println(n.getCoordinates().getX() + " " + n.getCoordinates().getY());
        }


    }

    public boolean isGoal(Position pos) {
        return grid[pos.getCoordinates().getX()][pos.getCoordinates().getY()] == '.';
    }

    public boolean isPassable(Position pos) {
        return grid[pos.getCoordinates().getX()][pos.getCoordinates().getY()] != '%';
    }

    public int costOfMove(Position pos) {
        return (isGoal(pos))? 0 : 1;
    }




    public static void main(String[] args) {

        Astar test = new Astar();
        test.start();
    }
}

class Coordinates {

    int x;
    int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

class Position {

    Coordinates coordinates;
    int priority;
    int costSoFar;

    public Position(Coordinates coordinates) {

        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCostSoFar() {
        return costSoFar;
    }

    public void setCostSoFar(int costSoFar) {
        this.costSoFar = costSoFar;
    }
}


