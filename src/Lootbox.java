public class Lootbox {
    //«лутбокс» образован сложением английских слов loot (внутриигровые ценности)
    // и box (коробка, ящик, контейнер).
    private final int rate; //размер увеличения зоны видимости

    private int x, y; //координаты
    private boolean alive; //флаг существования лутбокса
    private static int density = 2; //плотность расположения лутбоксов на поле
    // или минимальное расстояние между ними
    //количество "занимаемых" клеток определяется: (density * 2 + 1)^2
    public Lootbox() {
        alive = true;
        rate = 1; //может потом сделаю генерацию других значений
        //генерируем случайные и проверяем их допустимость
        do {
            x = (int) (Math.random() * Game.fieldGame.getRange());
            y = (int) (Math.random() * Game.fieldGame.getRange());
        } while (!checkXY());
        //после цикла мы должны получить допустимые координаты
        //поэтому ставим на такую клетку наш лутбокс
        Game.fieldGame.getField()[x][y].setObject(this);
    }
    private boolean checkXY() {
        //метод проверки допустимости установки на данной клетке лутбокса
        //если данная клетка занята, то не ставим сюда лутбокс и выходим из метода
        if (Game.fieldGame.getField()[x][y].getObject() instanceof Ship) return false;
        //иначе (клетка свободна) проверяем наличие другого лутбокса рядом
        else {
            //смотрим вокруг возможного положения
            for (int i = x - density; i <= x + density; i++) {
                for (int j = y - density; j <= y + density; j++) {
                    //перебираем все клетки в заданном радиусе,
                    // чтобы среди них не было другого лутбокса
                    if (i >= 0 && i < Game.fieldGame.getRange() && j >= 0
                            && j < Game.fieldGame.getRange()) {
                        //не выходим за пределы поля!!
                        if (Game.fieldGame.getField()[i][j].getObject() instanceof Lootbox)
                            //если есть хотя бы 1 лутбокс, то не ставим рядом
                            return false;
                    }
                }
            }
        }
        //если цикл не выявил соседства другого лутбокса, то говорим, что можно ставить
        return true;
    }
    public static int getDensity() {
        return density;
    }
    @Override
    public String toString() {
        return " \u001b[33m◊\u001b[0m "; //
    }
    public void getGift(Ship ship) {
        //метод получения кораблем содержимого лутбокса (подарка)
        alive = false; //пометка, что не живой (использован)
        switch ((int) (Math.random() * 2)) {
            //добавляем кораблю обзор
            case 0 -> {
                ship.setVisibleArea(ship.getVisibleArea() + rate);
                if (ship instanceof UserShip) {
                    System.out.println("Лутбокс принес вам увеличение обзора на " + rate);
                }
            }
            //увеличиваем скорость торпеды
            case 1 -> {
                ship.setSpeedMissiles(rate);
                if (ship instanceof UserShip) {
                    System.out.println("Лутбокс принес вам увеличение скорости торпед на " + rate);
                }
            }
            case 2 -> {
                ship.setCurrentMissile(-rate);
                if (ship instanceof UserShip) {
                    System.out.println("Лутбокс принес вам увеличение количества торпед на " + rate);
                }
            }
        }
    }
    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        //Сеттер маркера жизни лутбокса. Если торпеда попала в лутбокс, она его уничтожает,
        //точнее делает пометку, что не живой
        this.alive = alive;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
