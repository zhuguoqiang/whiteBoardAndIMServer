package app.whiteboard;

import java.util.LinkedList;
import java.util.List;

public final class GraphMeta {

	public Coord beganCoord;
	public Coord endedCoord;
	public List<Coord> coordArray;

	public GraphMeta(Coord beganCoord, Coord endedCoord) {
		this.beganCoord = beganCoord;
		this.endedCoord = endedCoord;
		this.coordArray = new LinkedList<Coord>();
	}

	/**
	 * 
	 * @author Ambrose Xu
	 */
	public class Coord {
		public float x;
		public float y;

		public Coord(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}
