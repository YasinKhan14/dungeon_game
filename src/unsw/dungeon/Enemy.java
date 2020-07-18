package unsw.dungeon;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.util.ElementScanner6;

public class Enemy extends Entity implements Interactable, Moveable {

	private boolean onMap;
	private Dungeon dungeon;


    public Enemy(int x, int y) {
		super(x, y);
		onMap = true;
	}
	
	public void greedyApproach(Player player) {
		double leftCost = 0;
		double rightCost = 0;
		double upCost = 0;
		double downCost = 0;

		if (canMove(getX(), getY() + 1))
			upCost = euclideanDistance(getX(), getY() + 1, player.getX(), player.getY());

		if(canMove(getX(), getY() - 1))
			downCost = euclideanDistance(getX(), getY() - 1, player.getX(), player.getY());

		if(canMove(getX() -1, getY()))
			leftCost = euclideanDistance(getX() - 1, getY(), player.getX(), player.getY());

		if (canMove(getX() + 1, getY()))
			rightCost = euclideanDistance(getX() + 1, getY(), player.getX(), player.getY());

		if (isMin(rightCost, leftCost, downCost, upCost))
			moveRight();
		else if (isMin(leftCost, rightCost, downCost, upCost))
			moveLeft();
		else if (isMin(upCost, leftCost, downCost, rightCost))
			moveUp();
		else 
			moveDown();

		

	}

	public boolean isMin(double comp, double x1, double x2, double x3) {
		double costArray[] = {comp, x1, x2, x3};
		for (double d : costArray) {
			if (comp > d)
				return false;
		}
		return true;
	}

	public double euclideanDistance (int x1, int y1, int x2, int y2) {
		double euclideanDistance = Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1) * (y2 - y1));
		return euclideanDistance;
		
	}
    
	public int moveableIntersect(Moveable moveable) {
		if (((Player) moveable).hasSword() > 0 ) 
			return - 1;
		else
			return -2;
	}

	public boolean defeatedObject() {
		return !onMap;
	}

	@Override
	public void moveUp() {
		updateMap(getX(), getY() + 1);

	}

	@Override
	public void moveDown() {
		updateMap(getX(), getY() - 1);
	}

	@Override
	public void moveLeft() {
		updateMap(getX() - 1, getY());

	}

	@Override
	public void moveRight() {
		updateMap(getX() + 1, getY());

	}

	@Override
	public boolean canMove(int x, int y) {
		List<Entity> objectList = dungeon.getMap()[x][y];
        for (Entity obj : objectList){
            if (obj == null) {
                continue;
            }
            // assuming wall is also interactable
            if (!(obj instanceof Interactable)){
                continue;
            }
            // type casting into interactables
            Interactable i = (Interactable) obj;

            switch(i.moveableIntersect(this)){
                // wall or portal or door (clsoed) or boulder (for enemy)
                case 1:
                    return false;
            }
        }
		return true;
	}

	@Override
	public void updateMap(int x, int y) {
		dungeon.updateMap(this, x, y);
	}
    
}