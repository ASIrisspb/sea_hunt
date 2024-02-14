public abstract class Ship {
    //общий класс кораблей
    private int x; //координата корабля
    private int y; //координата корабля
    private boolean alive; //флаг для контроля жизни корабля
    private int visibleArea = 10; //начальная зона видимости (меняется лутбоксами)
    public Ship(int x, int y) {
        alive = true;
        this.x = x; //начальная координата корабля
        this.y = y; //начальная координата корабля
        Game.fieldGame.getField()[x][y].setObject(this); //ставим корабль
        // то есть назначаем текущей клетке наличие объекта.
    }
    abstract public void shipMove(String userWord);
    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getVisibleArea() {
        return visibleArea;
    }
    public void setVisibleArea(int visibleArea) {
        this.visibleArea = visibleArea;
    }
}
