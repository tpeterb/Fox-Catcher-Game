import java.util.Objects;

/**
 * Represents a 2D position.
 */
public class Position implements Cloneable {

    private int row;
    private int col;

    /**
     * Creates a {@code Position} object.
     *
     * @param row the row coordinate of the position
     * @param col the column coordinate of the position
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * {@return the row coordinate of the position}
     */
    public int row() {
        return row;
    }

    /**
     * {@return the column coordinate of the position}
     */
    public int col() {
        return col;
    }

    /**
     * {@return the position whose vertical and horizontal distances from this
     * position are equal to the coordinate changes of the direction given}
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    public Position getPositionAt(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    public Position getUpLeft() {
        return getPositionAt(Direction.UP_LEFT);
    }

    public Position getUpRight() {
        return getPositionAt(Direction.UP_RIGHT);
    }

    public Position getDownLeft() {
        return getPositionAt(Direction.DOWN_LEFT);
    }

    public Position getDownRight() {
        return getPositionAt(Direction.DOWN_RIGHT);
    }

    /**
     * Changes the position by the coordinate changes of the direction given.
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    public void setTo(Direction direction) {
        row += direction.getRowChange();
        col += direction.getColChange();
    }

    public void setUpLeft() {
        setTo(Direction.UP_LEFT);
    }

    public void setUpRight() {
        setTo(Direction.UP_RIGHT);
    }

    public void setDownLeft() {
        setTo(Direction.DOWN_LEFT);
    }

    public void setDownRight() {
        setTo(Direction.DOWN_RIGHT);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof Position p) && p.row == row && p.col == col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public Position clone() {
        Position copy;
        try {
            copy = (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        return copy;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }
}