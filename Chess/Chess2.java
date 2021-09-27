import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Map.Entry;

public class Cell
{
	private Token token;
	private int x;
	private int y;

	public Cell(int x, int y, Token token)
	{
		this.setToken(token);
		this.setX(x);
		this.setY(y);
	}

	public Token getToken()
	{
		return this.token;
	}

	public void setToken(Token p)
	{
		this.token = p;
	}

	public int getX()
	{
		return this.x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return this.y;
	}

	public void setY(int y)
	{
		this.y = y;
	}
}

public abstract class Token
{

	private boolean killed = false;
	private boolean white = false;

	public Token(boolean white)
	{
		this.setWhite(white);
	}

	public boolean isWhite()
	{
		return this.white;
	}

	public void setWhite(boolean white)
	{
		this.white = white;
	}

	public boolean isKilled()
	{
		return this.killed;
	}

	public void setKilled(boolean killed)
	{
		this.killed = killed;
	}
	public abstract boolean canMove(Board board,Cell start, Cell end);
}


public class King extends Token
{
	private boolean castlingDone = false;

	public King(boolean white)
	{
		super(white);
	}

	public boolean isCastlingDone()
	{
		return this.castlingDone;
	}

	public void setCastlingDone(boolean castlingDone)
	{
		this.castlingDone = castlingDone;
	}
	public boolean canMove(Board board, Cell start, Cell end)
	{
		// we can't move the piece to a Cell that
		// has a piece of the same color
		if (end.getToken().isWhite() == this.isWhite()) return false;
		int x = Math.abs(start.getX() - end.getX());
		int y = Math.abs(start.getY() - end.getY());
		if (x + y == 1)
		{
			// check if this move will not result in the king
			// being attacked if so return true
			return true;
		}

		return this.isValidCastling(board, start, end);
	}

	private boolean isValidCastling(Board board,Cell start, Cell end)
	{

		if (this.isCastlingDone())
		{
			return false;
		}

		// Logic for returning true or false
	}

	public boolean isCastlingMove(Cell start, Cell end)
	{
		// check if the starting and
		// ending position are correct
	}
}
public class Knight extends Token
{
	public Knight(boolean white)
	{
		super(white);
	}
	public boolean canMove(Board board, Cell start,Cell end)
	{
		if (end.getToken().isWhite() == this.isWhite()) return false;
		int x = Math.abs(start.getX() - end.getX());
		int y = Math.abs(start.getY() - end.getY());
		return x * y == 2;
	}
}
public class Pawn extends Token
{
	public Knight(boolean white)
	{
		super(white);
	}
	public boolean canMove(Board board, Cell start,Cell end)
	{
		if (end.getToken().isWhite() == this.isWhite()) return false;
		
	}
}
public class Board {
	Cell[][] boxes;

	public Board()
	{
		this.resetBoard();
	}

	public Cell getBox(int x, int y)
	{

		if (x < 0 || x > 7 || y < 0 || y > 7)
		{
			throw new Exception("Index out of bound");
		}

		return boxes[x][y];
	}

	public void resetBoard()
	{
		// initialize white pieces
		boxes[0][0] = new Cell(0, 0, new Rook(true));
		boxes[0][1] = new Cell(0, 1, new Knight(true));
		boxes[0][2] = new Cell(0, 2, new Bishop(true));
		boxes[0][3] = new Cell(0, 0, new Queen(true));
		boxes[0][4] = new Cell(0, 0, new King(true));
		boxes[0][5] = new Cell(0, 2, new Bishop(true));
		boxes[0][6] = new Cell(0, 1, new Knight(true));
		boxes[0][7] = new Cell(0, 0, new Rook(true));

		boxes[1][0] = new Cell(1, 0, new Pawn(true));
		boxes[1][1] = new Cell(1, 1, new Pawn(true));
		boxes[1][2] = new Cell(1, 1, new Pawn(true));
		boxes[1][3] = new Cell(1, 1, new Pawn(true));
		boxes[1][4] = new Cell(1, 1, new Pawn(true));
		boxes[1][5] = new Cell(1, 1, new Pawn(true));
		boxes[1][6] = new Cell(1, 1, new Pawn(true));
		boxes[1][7] = new Cell(1, 1, new Pawn(true));

		// initialize black pieces
		boxes[7][0] = new Cell(0, 0, new Rook(false));
		boxes[7][1] = new Cell(0, 1, new Knight(false));
		boxes[7][2] = new Cell(0, 2, new Bishop(false));
		boxes[7][3] = new Cell(0, 0, new Queen(false));
		boxes[7][4] = new Cell(0, 0, new King(false));
		boxes[7][5] = new Cell(0, 2, new Bishop(false));
		boxes[7][6] = new Cell(0, 1, new Knight(false));
		boxes[7][7] = new Cell(0, 0, new Rook(false));
		
		boxes[6][0] = new Cell(1, 0, new Pawn(false));
		boxes[6][1] = new Cell(1, 1, new Pawn(false));
		boxes[6][2] = new Cell(1, 1, new Pawn(false));
		boxes[6][3] = new Cell(1, 1, new Pawn(false));
		boxes[6][4] = new Cell(1, 1, new Pawn(false));
		boxes[6][5] = new Cell(1, 1, new Pawn(false));
		boxes[6][6] = new Cell(1, 1, new Pawn(false));
		boxes[6][7] = new Cell(1, 1, new Pawn(false));

		// initialize remaining boxes without any piece
		for (int i = 2; i < 6; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				boxes[i][j] = new Cell(i, j, null);
			}
		}
	}
}
public abstract class Player
{
	public boolean whiteSide;
	public boolean humanPlayer;

	public boolean isWhiteSide()
	{
		return this.whiteSide;
	}
	public boolean isHumanPlayer()
	{
		return this.humanPlayer;
	}
}

public class HumanPlayer extends Player
{

	public HumanPlayer(boolean whiteSide)
	{
		this.whiteSide = whiteSide;
		this.humanPlayer = true;
	}
}

public class ComputerPlayer extends Player
{

	public ComputerPlayer(boolean whiteSide)
	{
		this.whiteSide = whiteSide;
		this.humanPlayer = false;
	}
}

public class Move
{
	private Player player;
	private Cell start;
	private Cell end;
	private Piece pieceMoved;
	private Piece pieceKilled;
	private boolean castlingMove = false;

	public Move(Player player, Cell start, Cell end)
	{
		this.player = player;
		this.start = start;
		this.end = end;
		this.pieceMoved = start.getPiece();
	}

	public boolean isCastlingMove()
	{
		return this.castlingMove;
	}

	public void setCastlingMove(boolean castlingMove)
	{
		this.castlingMove = castlingMove;
	}
}
public enum GameStatus
{
	ACTIVE,
	BLACK_WIN,
	WHITE_WIN,
	FORFEIT,
	STALEMATE,
	RESIGNATION
}

public class Game
{
	private Player[] players;
	private Board board;
	private Player currentTurn;
	private GameStatus status;
	private List<Move> movesPlayed;

	private void initialize(Player p1, Player p2)
	{
		players[0] = p1;
		players[1] = p2;

		board.resetBoard();

		if (p1.isWhiteSide())
		{
			this.currentTurn = p1;
		}
		else
		{
			this.currentTurn = p2;
		}

		movesPlayed.clear();
	}

	public boolean isEnd()
	{
		return this.getStatus() != GameStatus.ACTIVE;
	}

	public boolean getStatus()
	{
		return this.status;
	}

	public void setStatus(GameStatus status)
	{
		this.status = status;
	}

	public boolean playerMove(Player player, int startX,int startY, int endX, int endY)
	{
		Cell startBox = board.getBox(startX, startY);
		Cell endBox = board.getBox(startY, endY);
		Move move = new Move(player, startBox, endBox);
		return this.makeMove(move, player);
	}

	private boolean makeMove(Move move, Player player)
	{
		Piece sourcePiece = move.getStart().getPiece();
		if (sourcePiece == null)
		{
			return false;
		}

		// valid player
		if (player != currentTurn)
		{
			return false;
		}

		if (sourcePiece.isWhite() != player.isWhiteSide())
		{
			return false;
		}

		// valid move?
		if (!sourcePiece.canMove(board, move.getStart(),move.getEnd()))
		{
			return false;
		}

		// kill?
		Piece destPiece = move.getStart().getPiece();
		if (destPiece != null)
		{
			destPiece.setKilled(true);
			move.setPieceKilled(destPiece);
		}

		// castling?
		if (sourcePiece != null && sourcePiece instanceof King && sourcePiece.isCastlingMove())
		{
			move.setCastlingMove(true);
		}

		// store the move
		movesPlayed.add(move);

		// move piece from the stat box to end box
		move.getEnd().setPiece(move.getStart().getPiece());
		move.getStart.setPiece(null);

		if (destPiece != null && destPiece instanceof King)
		{
			if (player.isWhiteSide())
			{
				this.setStatus(GameStatus.WHITE_WIN);
			}
			else
			{
				this.setStatus(GameStatus.BLACK_WIN);
			}
		}

		// set the current turn to the other player
		if (this.currentTurn == players[0])
		{
			this.currentTurn = players[1];
		}
		else
		{
			this.currentTurn = players[0];
		}

		return true;
	}
}
