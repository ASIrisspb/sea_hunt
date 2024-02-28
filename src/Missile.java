import java.util.Arrays;

public class Missile {
    //класс для реализации торпед
    private int speed; //Скорость торпеды. В нашем случае это количество клеток, занимаемых торпедой при движении
    private int x; //координаты торпеды
    private int[] x_old; //старые координаты, чтобы на следующей прорисовке их затирать
    private int y; //координаты торпеды
    private int[] y_old; //старые координаты, чтобы на следующей прорисовке их затирать
    private boolean alive; //маркер жизни торпеды, чтобы было можно её удалять в мейне
    private boolean crush; //маркер того, что торпеда попала во что-то
    private final String direction; //определитель направления движения
    public Missile(int x, int y, String direction, int speed) {
        this.speed = speed;
        this.direction = direction;
        this.x = x; //фиксируем первую координату
        this.y = y; //фиксируем первую координату
        alive = true;
        crush = false;
        x_old = new int[speed];
        Arrays.fill(x_old, -1);
        y_old = new int[speed];
        Arrays.fill(y_old, -1);
    }
    public void missileMove() {
        //метод движения торпеды
        int rangeF = Game.fieldGame.getRange(); //переменная для упрощения кода
        //чистим старый трек
        eraseOldTrack();
        //скорость движения торпеды реализуем через цикл - какова скорость торпеды, столько повторения цикла
        //это дает прорисовку торпеды на несколько клеток, для демонстрации движения торпеды
        for (int i = 0; i < speed; i++) {
            switch (direction) {
                case "5" -> y = y + 1; //пуск вправо от корабля
                case "6" -> y = y - 1; //пуск влево от корабля
                case "7" -> x = x - 1; //пуск вверх от корабля
                case "8" -> x = x + 1; //пуск вниз от корабля
            }
            //делаем проверки, куда попала торпеда с новыми координатами
            if (x >= 0 && x < rangeF && y >= 0 && y < rangeF) {
                //для укорачивания кода
                Object obj = Game.fieldGame.getField()[x][y].getObject();
                //заносим текущую координату в старые для затирки в следующем ходу
                x_old[i] = x;
                y_old[i] = y;
                if (obj instanceof PC_Ship) {
                    //если попала во врага, то убиваем его
                    ((PC_Ship) obj).setAlive(false);
                    this.crush = true;
                    System.out.println("Торпеда потопила корабль!");
                    break;
                } else if (obj instanceof Lootbox) {
                    //если попали в лутбокс, то взрываем лутбокс, торпеда погибает
                    ((Lootbox) obj).setAlive(false);
                    Game.fieldGame.getField()[x][y].setObject(this);
                    this.crush = true;
                    System.out.println("Торпеда попала в лутбокс и потопила его, погибнув сама");
                    break;
                } else if (obj instanceof Missile) {
                    //если попали в другую торпеду (врага)
                    System.out.println("Торпеды столкнулись, обе погибли");
                    ((Missile) obj).setCrush(true);
                    Game.fieldGame.getField()[x][y].setObject(this);
                    this.crush = true;
                    break;
                } else {
                    //это действие, если торпеда ничего не встретила и просто идет по пустому полю
                    Game.fieldGame.getField()[x][y].setObject(this);
                }
            } else {
                //торпеда вышла за пределы поля - погибла
                crush = true;
            }
        }
    }
    public void eraseOldTrack() {
        //метод очистки пройденного пути
        //проходим по старым координатам и затираем их
        for (int j = 0; j < x_old.length; j++) {
            if (x_old[j] != -1) {
                //если не первое движение торпеды (тогда координаты -1 из констурктора), то затираем
                if (!(Game.fieldGame.getField()[x_old[j]][y_old[j]].getObject() instanceof Ship)) {
                    //не затираем если был корабль
                    Game.fieldGame.getField()[x_old[j]][y_old[j]].setObject(null);
                }
            }
        }
        //проверяем не врезалась ли на прошлом ходу торпеда во что-то
        if (crush) {
            //если врезалась, то помечаем как неживую и выходим из метода
            alive = false;
        }
    }
    public boolean isAlive() {
        return alive;
    }

    public void setCrush(boolean crush) {
        //установка метки, что торпеда во что-то попала
        this.crush = crush;
    }

    @Override
    public String toString() {
        return switch (direction) {
            case "5" -> " ⇒ ";
            case "6" -> " ⇐ ";
            case "7" -> " ⇑ ";
            case "8" -> " ⇓ ";
            default -> "!!!"; //не должен сработать
        };
    }
}
