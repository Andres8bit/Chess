package eventHandling;

import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public interface BoardListener {
	public Move onOpponentMoveMade();
}
