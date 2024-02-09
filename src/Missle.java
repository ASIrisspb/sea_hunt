public class Missle {
    //класс для реализации торпед
private FieldGame fieldGame; //связка с полем
private static int speed = 2; //Скорость торпеды. В нашем случае это количество клеток, занимаемых торпедой при движении
private int[] x; //координаты торпеды
private int[] y; //координаты торпеды
private boolean alive; //маркер жизни торпеды, чтобы было можно её удалять в мейне
private String direction; //определитель направления движения
    public Missle(FieldGame fieldGame, int x, int y, String direction) {
        this.fieldGame = fieldGame;
        this.direction = direction;
        this.x = new int[speed]; //создаем массив в зависимости от скорости торпеды на текущий момент
        this.x[0] = x; //фиксируем первую координату
        this.y = new int[speed]; //создаем массив в зависимости от скорости торпеды на текущий момент
        this.y[0] = y; //фиксируем первую координату
    }

    public void missleMove() {
        //метод движения торпеды
        int rangeField = fieldGame.getRange(); //переменная для упрощения кода

        //скорость движения торпеды реализуем через цикл -
        for (int i = 0; i < this.x.length; i++) {
            if (x[i] >= 0 && x[i] < rangeField && y[i] >= 0 && y[i] < rangeField) {
                if (!(fieldGame.getField()[x[i]][y[i]].getObject() instanceof UserShip)) {
                    fieldGame.getField()[x[i]][y[i]].setObject(null);
                }
            }
            switch (direction) {
                case "5" -> y[i] = y[0] + i + 1;
                case "6" -> y[i] = y[0] - i - 1;
                case "7" -> x[i] = x[0] - i - 1;
                case "8" -> x[i] = x[0] + i + 1;
            }
            if (x[i] >= 0 && x[i] < rangeField && y[i] >= 0 && y[i] < rangeField) {
                fieldGame.getField()[x[i]][y[i]].setObject(this);
            }

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
