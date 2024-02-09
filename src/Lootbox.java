public class Lootbox {
    //«лутбокс» образован сложением английских слов loot (внутриигровые ценности)
    // и box (коробка, ящик, контейнер).
    private final int rate; //размер увеличения зоны видимости
    private boolean alive; //флаг существования лутбокса
    private static int density = 2; //плотность расположения лутбоксов на поле
    // или минимальное расстояние между ними
    //количество "занимаемых" клеток определяется: (density * 2 + 1)^2
    public Lootbox() {
        alive = true;
        rate = 1; //может потом сделаю генерацию других значений
        int x, y; //координаты
        //генерируем случайные и проверяем их допустимость
        do {
            x = (int) (Math.random() * Game.fieldGame.getRange());
            y = (int) (Math.random() * Game.fieldGame.getRange());
        } while (!checkXY(x, y));
        //после цикла мы должны получить допустимые координаты
        //поэтому ставим на такую клетку наш лутбокс
        Game.fieldGame.getField()[x][y].setObject(this);
    }
    private boolean checkXY(int x, int y) {
        //метод проверки допустимости установки на данной клетке лутбокса
        boolean acceptable = true; //допустимость
        //если данная клетка занята, то не ставим сюда лутбокс
        if (Game.fieldGame.getField()[x][y].getObject() instanceof Ship) return false;

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
                             acceptable = false;
                }
            }
        }
        return acceptable;
    }
    public int getRate() {
        return rate;
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
        //пока только для увеличения обзора!!!
        ship.setVisibleArea(ship.getVisibleArea() + rate);
        alive = false;
    }
    public boolean isAlive() {
        return alive;
    }
}
