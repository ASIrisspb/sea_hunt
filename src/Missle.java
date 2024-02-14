public class Missle {
    //класс для реализации торпед
private static int speed = 3; //Скорость торпеды. В нашем случае это количество клеток, занимаемых торпедой при движении
private int[] x; //координаты торпеды
private int[] y; //координаты торпеды
private boolean alive; //маркер жизни торпеды, чтобы было можно её удалять в мейне
private final String direction; //определитель направления движения
    public Missle(int x, int y, String direction) {
        this.direction = direction;
        this.x = new int[speed]; //создаем массив в зависимости от скорости торпеды на текущий момент
        this.x[0] = x; //фиксируем первую координату
        this.y = new int[speed]; //создаем массив в зависимости от скорости торпеды на текущий момент
        this.y[0] = y; //фиксируем первую координату
    }

    public void missleMove() {
        //метод движения торпеды
        int rangeField = Game.fieldGame.getRange(); //переменная для упрощения кода

        //скорость движения торпеды реализуем через цикл -
        for (int i = 0; i < this.x.length; i++) {

            switch (direction) {
                case "5" -> {
                    y[i] = y[0] + 1;
                    x[i] = x[0];
                }
                case "6" -> {
                    y[i] = y[0] - 1;
                    x[i] = x[0];
                }
                case "7" -> {
                    x[i] = x[0] - 1;
                    y[i] = y[0];
                }
                case "8" -> {
                    x[i] = x[0] + 1;
                    y[i] = y[0];
                }
            }
            if (x[i] >= 0 && x[i] < rangeField && y[i] >= 0 && y[i] < rangeField) {
                if (!(Game.fieldGame.getField()[x[i]][y[i]].getObject() instanceof UserShip)) {
//                    System.out.println("null!! x["+i+"] = " + x[i] + ", y["+i+"] = " + y[i]);
//                    Game.fieldGame.getField()[x[i]][y[i]].setObject(null);
                    Game.fieldGame.getField()[x[i]][y[i]].setObject(this);
//                } else {
//                    System.out.println("object!! x["+i+"] = " + x[i] + ", y["+i+"] = " + y[i]);
                }
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
