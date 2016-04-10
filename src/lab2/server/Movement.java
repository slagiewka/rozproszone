//package lab3.server;

public class Movement {
    private final Coordinates coordinates;
    private final BoardCell marker;

    public Movement(Coordinates coordinates, BoardCell marker) {
        RemoteClient.errorHandler(marker != BoardCell.EMPTY, "Marker cannot be void");

        this.coordinates = coordinates;
        this.marker = marker;
    }

//    public static Movement of(Coordinates coordinates, BoardCell marker) {
//        ;
//
//        return new Movement(coordinates, marker);
//    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public BoardCell getMarker() {
        return marker;
    }
}
