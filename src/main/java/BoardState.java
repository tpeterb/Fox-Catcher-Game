import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents the state of the Fox Catcher game.
 */
public class BoardState implements Cloneable {

    /**
     * The size of the board the game is played on.
     */
    public static final int BOARD_SIZE = 8;

    /**
     * The array containing the pieces.
     */
    private Piece[] pieces;

    /**
     * The type of the piece that can be moved next time.
     */
    private PieceType pieceTypeToMove;

    /**
     * Creates a {@code BoardState} object with the five pieces given.
     * This constructor makes it possible to define the pieces with
     * positions that are different from the ordinary ones. The first piece
     * must be the fox, the order of the dogs is arbitrary.
     * @param pieces The pieces with which the Fox Catcher game is played.
     */
    public BoardState(PieceType pieceTypeToMove, Piece... pieces) {
        if (arePiecePositionsValid(pieces)) {
            if (pieces[0].getPieceType() != PieceType.FOX) {
                throw new IllegalArgumentException();
            }
            this.pieceTypeToMove = pieceTypeToMove;
            this.pieces = deepClone(pieces);
        }
    }

    /**
     * Creates a {@code BoardState} object that represents the initial state
     * of the game. All pieces are placed in those positions that are specified in
     * the rules of the game. The first piece must be the fox, the order of the dogs
     * is arbitrary.
     */
    public BoardState(PieceType pieceTypeToMove) {
        this(pieceTypeToMove,
                new Piece(PieceType.FOX, new Position(0, 2)),
                new Piece(PieceType.DOG, new Position(7, 1)),
                new Piece(PieceType.DOG, new Position(7, 3)),
                new Piece(PieceType.DOG, new Position(7, 5)),
                new Piece(PieceType.DOG, new Position(7, 7)));
    }

    private boolean isPositionValid(Position position) {
        return position.row() >= 0 && position.row() < BOARD_SIZE
                && position.col() >= 0 && position.col() < BOARD_SIZE;
    }

    private boolean arePiecePositionsValid(Piece[] pieces) {
        if (pieces.length != 5) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < pieces.length; i++) {
            if (!isPositionValid(pieces[i].getPosition())) {
                throw new IllegalArgumentException();
            }
            if (i < pieces.length - 1) {
                for (int j = i + 1; j < pieces.length; j++) {
                    if (pieces[i].getPosition().equals(pieces[j].getPosition())) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns the number of pieces on the board.
     * @return The number of pieces on the board.
     */
    public int getNumberOfPieces() {
        return pieces.length;
    }

    /**
     * Returns the piece at the given index. Index 0 refers
     * to the fox, and the remaining indexes refer to the dogs.
     * @param index The index of the piece to be returned.
     * @return The piece at the given index.
     */
    public Piece getPiece(int index) {
        return pieces[index].clone();
    }

    /**
     * Returns the type of the piece that can be moved next.
     * @return
     */
    public PieceType getPieceTypeToMove() {
        return this.pieceTypeToMove;
    }

    public boolean canMove(int index, Direction direction) {
        if (isIndexInvalid(index)) {
            throw new IllegalArgumentException();
        }
        if (index == 0) {
            return canMoveFox(direction);
        } else {
            return canMoveDog(index, direction);
        }
    }

    /**
     * Returns true if the fox can be moved in the direction given.
     * If the fox cannot be moved in the direction specified, the method
     * returns false.
     * @param direction The direction in which the fox should be moved.
     * @return True if the fox can be moved in the direction given.
     * If the fox cannot be moved in the direction specified, the method
     * returns false.
     */
    private boolean canMoveFox(Direction direction) {
        if (this.pieceTypeToMove != PieceType.FOX) {
            return false;
        }
        if (direction == Direction.UP_LEFT) {
            if (isInFirstRow(0) || isInFirstColumn(0)) {
                return false;
            }
            if (isSquareEmpty(this.pieces[0].getPosition().getPositionAt(Direction.UP_LEFT))) {
                return true;
            }
            return false;
        } else if (direction == Direction.UP_RIGHT) {
            if (isInFirstRow(0) || isInLastColumn(0)) {
                return false;
            }
            if (isSquareEmpty(this.pieces[0].getPosition().getPositionAt(Direction.UP_RIGHT))) {
                return true;
            }
            return false;
        } else if (direction == Direction.DOWN_LEFT) {
            if (isInLastRow(0) || isInFirstColumn(0)) {
                return false;
            }
            if (isSquareEmpty(this.pieces[0].getPosition().getPositionAt(Direction.DOWN_LEFT))) {
                return true;
            }
            return false;
        } else { // direction == Direction.DOWN_RIGHT
            if (isInLastRow(0) || isInLastColumn(0)) {
                return false;
            }
            if (isSquareEmpty(this.pieces[0].getPosition().getPositionAt(Direction.DOWN_RIGHT))) {
                return true;
            }
            return false;
        }
    }

    /**
     * Returns true if the dog at the given index can be moved in the specified direction.
     * If moving the dog in the given direction is not possible, the method returns false.
     * @param index The index of the dog.
     * @param direction The direction in which the dog should be moved.
     * @return True if the dog at the given index can be moved in the specified direction.
     * If moving the dog in the given direction is not possible, the method returns false.
     */
    private boolean canMoveDog(int index, Direction direction) {
        if (this.pieceTypeToMove != PieceType.DOG) {
            return false;
        }
        if (direction == Direction.UP_LEFT) {
            if (isInFirstRow(index) || isInFirstColumn(index)) {
                return false;
            }
            if (isSquareEmpty(this.pieces[index].getPosition().getPositionAt(Direction.UP_LEFT))) {
                return true;
            }
            return false;
        } else if (direction == Direction.UP_RIGHT) {
            if (isInFirstRow(index) || isInLastColumn(index)) {
                return false;
            }
            if (isSquareEmpty(this.pieces[index].getPosition().getPositionAt(Direction.UP_RIGHT))) {
                return true;
            }
            return false;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void move(int index, Direction direction) {
        if (isIndexInvalid(index)) {
            throw new IllegalArgumentException();
        }
        if (index == 0) {
            moveFox(direction);
        } else {
            moveDog(index, direction);
        }
    }

    private void moveFox(Direction direction) {
        if (canMoveFox(direction)) {
            pieces[0].getPosition().setTo(direction);
        }
    }

    private void moveDog(int index, Direction direction) {
        if (canMoveDog(index, direction)) {
            pieces[index].getPosition().setTo(direction);
        }
    }

    public boolean isGoal() {
        return foxWins() || dogWins();
    }

    public boolean foxWins() {
        int numberOfBypassedDogs = 0;
        for (int i = 1; i < 5; i++) {
            if (pieces[0].getPosition().row() > pieces[i].getPosition().row()) {
                numberOfBypassedDogs++;
            }
        }
        if (numberOfBypassedDogs == 4) {
            return true;
        }
        return false;
    }

    public boolean dogWins() {
        if (getPossibleMoves(0).isEmpty()) {
            return true;
        }
        return false;
    }

    public ArrayList<Direction> getPossibleMoves(int index) {
        ArrayList<Direction> directions = new ArrayList<Direction>();
        for (var direction : Direction.values()) {
            if (canMove(index, direction)) {
                directions.add(direction);
            }
        }
        return directions;
    }

    /**
     * Returns true if the square at the specified position is empty.
     * Otherwise, this method returns false;
     * @param position The position to be checked whether the square at the
     * specified position is empty or not.
     * @return True if the square at the given position is empty. Otherwise, this method
     * returns false;
     */

    public boolean isSquareEmpty(Position position) {
        for (var piece : pieces) {
            if (piece.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the piece at the specified index is in the first row
     * of the board. Otherwise, the method returns false. Index 0 represents
     * the fox, and the remaining indexes represent the dogs.
     * @param index The index of the piece.
     * @return True if the piece at the given index is in the first row
     * of the board. Otherwise, the method returns false;
     */
    private boolean isInFirstRow(int index) {
        if (isIndexInvalid(index)) {
            throw new IllegalArgumentException();
        }
        return pieces[index].getPosition().row() == 0;
    }

    /**
     * Returns true if the piece at the specified index is in the
     * last row of the board. Otherwise, the method returns false.
     * Index 0 represents the fox, and the other indexes from 1 to 4
     * represent the dogs.
     * @param index The index of the piece.
     * @return True if the piece at the specified index is in the
     * last row of the board. Otherwise, the method returns false.
     */
    private boolean isInLastRow(int index) {
        if (isIndexInvalid(index)) {
            throw new IllegalArgumentException();
        }
        return pieces[index].getPosition().row() == BOARD_SIZE - 1;
    }

    /**
     * Returns true if the piece at the given index is in the first column
     * of the board. Otherwise, the method returns false. Index 0 refers to
     * the fox, and the remaining indexes from 1 to 4 refer to the dogs.
     * @param index The index of the piece.
     * @return True if the piece at the specified index is in the first column
     * of the board. Otherwise, the method returns false.
     */
    private boolean isInFirstColumn(int index) {
        if (isIndexInvalid(index)) {
            throw new IllegalArgumentException();
        }
        return pieces[index].getPosition().col() == 0;
    }

    /**
     * Returns true if the piece at the given index is in the last column of the board.
     * Otherwise, this method returns false. Index 0 represents the fox, and the other
     * indexes represent the dogs.
     * @param index The index of the piece.
     * @return True if the piece at the given index is in the last column of the board.
     * Otherwise, this method returns false
     */
    private boolean isInLastColumn(int index) {
        if (isIndexInvalid(index)) {
            throw new IllegalArgumentException();
        }
        return pieces[index].getPosition().col() == BOARD_SIZE - 1;
    }

    private boolean isIndexInvalid(int index) {
        return index < 0 || index > getNumberOfPieces() - 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return (o instanceof BoardState boardState) &&
                this.pieceTypeToMove == boardState.pieceTypeToMove &&
                this.pieces.equals(boardState.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces, pieceTypeToMove);
    }

    @Override
    public String toString() { // "{DOG, [FOX, (0, 2)], [...], ...}"
        String result = "{";
        result += pieceTypeToMove + ", ";
        for (var piece : pieces) {
            if (!piece.equals(pieces[pieces.length - 1])) {
                result += piece.toString() + "], ";
            } else {
                result += piece.toString() + "]}";
            }
        }
        return result;
    }

    @Override
    public BoardState clone() {
        BoardState copy;
        try {
            copy = (BoardState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.pieces = deepClone(this.pieces);
        return copy;
    }

    private static Piece[] deepClone(Piece[] pieces) {
        Piece[] copy = pieces.clone();
        for (int i = 0; i < pieces.length; i++) {
            copy[i] = pieces[i].clone();
        }
        return copy;
    }

}
