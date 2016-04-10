//package lab3.server;

public class Coordinates {
    public Coordinates(int row, int column) {
        RemoteClient.errorHandler(rowsInRange(row), "Invalid row value");
        RemoteClient.errorHandler(columnsInRange(column), "Invalid column value");

        this.row = row;
        this.column = column;
    }

//    public static Coordinates of(int x, int y) {
//    }

    private final int row;
    private final int column;

//    public static Coordinates of(int row, int column) {
//
//        return new Coordinates(row, column);
//    }

    private static boolean columnsInRange(int column) {
        return column >= 0 && column < 3;
    }

    private static boolean rowsInRange(int rows) {
        return rows >= 0 && rows < 3;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
