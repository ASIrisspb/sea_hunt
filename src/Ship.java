import java.util.ArrayList;
public abstract class Ship {
    //общий класс кораблей
    private int x; //координата корабля
    private int y; //координата корабля
    private boolean alive; //флаг для контроля жизни корабля
    private int visibleArea = 2; //начальная зона видимости (меняется лутбоксами)
    private int amountMissile = 10; //ограничение количества торпед в начале игры
    private int currentMissile = 0; //изначальное количество торпед
    private int speedMissiles = 1; //изначальная скорость торпед
    private ArrayList<Missile> missiles_list = new ArrayList<>();  //комплект снарядов корабля
    public Ship(int x, int y) {
        alive = true;
        this.x = x; //начальная координата корабля
        this.y = y; //начальная координата корабля
        Game.fieldGame.getField()[x][y].setObject(this); //ставим корабль
        // То есть назначаем текущей клетке наличие объекта.
        // Создаем комплект снарядов в количестве amountMissile
        for (int i = 0; i < amountMissile; i++) {
            missiles_list.add(null);
        }
    }
    abstract public void shipMove(String userWord); //Движение корабля. Определяется в наследниках
    public boolean isAlive() {
        return alive;
    } //проверка жизни корабля
    public void setAlive(boolean alive) {
        this.alive = alive;
    } //меняем жизнь корабля при его потоплении
    public int getX() {
        return x;
    } //получение координаты
    public void setX(int x) {
        this.x = x;
    } //изменение координаты
    public int getY() {
        return y;
    } //получение координаты
    public void setY(int y) {
        this.y = y;
    } //изменение координаты
    public int getVisibleArea() { //доступ к размеру зоны видимости
        return visibleArea;
    }
    public void setVisibleArea(int visibleArea) {
        //изменение зоны видимости при получении соответствующего лутбокса
        this.visibleArea = visibleArea;
    }
    public void pushMissile(String direction) {
        //Метод запуска торпеды. Торпеду можно запустить только если текущее количество пусков
        //меньше обоймы (но ее можно пополнять через лутбоксы)
        if (currentMissile < amountMissile) {
            //если можем запустить торпеду, то перебираем список торпед
//            int count = 1;
            for (int i = 0; i < missiles_list.size(); i++) {
                //если в списке есть нул, то значит есть место для торпеды, которую можно запустить
//                count++;
                if (missiles_list.get(i) == null) {
                    //нашли нул, вместо него создаем новую торпеду с координатами корабля
                    missiles_list.set(i, new Missile(x, y, direction, speedMissiles));
                    currentMissile++; //торпеду создали (=выпустили) значит увеличиваем счетчик
                    //и прекращаем перебор, так как стреляем только одной торпедой
                    break;
                }
            }
//            if () //сделать увеличение торпед
        } else System.out.println("Торпеды закончились");
    }
    public void missileTrack() {
        //метод трассировки (движения) торпед
        //перебираем список торпед
        for (int i = 0; i < missiles_list.size(); i++) {
            //если находим не нул, значит торпеда запущена
            if (missiles_list.get(i) != null) {
                //вызываем у торпеды метод движения
                missiles_list.get(i).missileMove();
                //после проверяем, не вышла ли торпеда за края поля
                if (!missiles_list.get(i).isAlive()) {
                    //если торпеда погибла, то затираем ее трек
                    missiles_list.get(i).eraseOldTrack();
                    //если вышла, то удаляем ее из списка, чтобы можно было добавить новую (через лутбокс)
                    missiles_list.set(i, null);
                }
            }
        }
    }
    public void setSpeedMissiles(int addition) {
        speedMissiles += addition;
    }
    public void setCurrentMissile(int currentMissile) {
        //прибавляем (точнее уменьшаем счетчик выпущенных торпед)
        this.currentMissile += currentMissile;
    }
    public int getCurrentMissile() {
        return currentMissile;
    }
    public int getAmountMissile() {
        return amountMissile;
    }
}
