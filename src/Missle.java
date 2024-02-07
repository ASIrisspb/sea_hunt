public class Missle {
private FieldGame fieldGame;
private int speed = 2;
private int x;
private int y;
private String direction;
    public Missle(FieldGame fieldGame, int x, int y, String direction) {
        this.fieldGame = fieldGame;
        this.direction = direction;
        this.x = x;
        this.y = y;
    }

    public void missleMove() {
        if (x >= 0 && x < fieldGame.getRange() && y >= 0 && y < fieldGame.getRange()) {
            if (!(fieldGame.getField()[x][y].getObject() instanceof UserShip)) {
                fieldGame.getField()[x][y].setObject(null);
            }
        }
        switch (direction) {
            case "5" -> y++;
            case "6" -> y--;
            case "7" -> x--;
            case "8" -> x++;
        }
        if (x >= 0 && x < fieldGame.getRange() && y >= 0 && y < fieldGame.getRange()) {
            fieldGame.getField()[x][y].setObject(this);
        }
    }
    @Override
    public String toString() {
        return switch (direction) {
            case "5" -> " ⇒ ";
            case "6" -> " ⇐ ";
            case "7" -> " ⇑ ";
            case "8" -> " ⇓ ";
            default -> "!!!";
        };
    }
}
