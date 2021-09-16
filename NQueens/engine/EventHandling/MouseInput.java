package eventHandling;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

//wrapper class to help cut down code needed in classes that use MouseInput
// Table would use this classs in each Tile, which would help it capture the state of the mouse
// i.e what need to happened within each tile/JPanel at any given time
public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {
	private int srcPos = -1;
	private int destPos = -1;
	
	
	public void mouseLeftClicked(MouseEvent e, int tileId) {
		if(tileId == -1 || tileId == 65) {
			srcPos = -1;
			destPos = -1;
		}else {
			if(srcPos == -1) {
				srcPos = tileId;
			    System.out.println("src set");
			}else if(destPos == -1) {
				destPos = tileId;
				System.out.println("dest set");
			}
		}
	}
	
	public  int getSrc() {
		return this.srcPos;
	}
	
	public void reset() {
		srcPos = destPos = -1;
	}
	public boolean selectedPiece() {
		return srcPos != -1;
	}
	
	public boolean piecePlace() 
	{
		return selectedPiece() && destPos != -1;
	}
	
	public int getDest() {
		return this.destPos;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
