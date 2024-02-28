import java.util.ArrayList;

public class PC_Ship extends Ship{
    private ArrayList<Missile> missilesInAreaView = new ArrayList<>(); //список торпед в зоне видимости
    private ArrayList<Lootbox> lootboxesInAreaView = new ArrayList<>(); //список лутбоксов в зоне видимости
    private UserShip isViewUserShip; //корабль игрока в зоне видимости
    private boolean pc_choice; //маркер, чтобы остановиться, когда выбор сделан
    public PC_Ship(int x, int y) {
        super(x, y);
//this.setVisibleArea(15); //ДЛЯ ТЕСТОВ!
    }

    @Override
    public String toString() {
        //переопределение toString для вывода корабля
        if (this.isAlive()) {
            return " \u001b[31m■\u001b[0m "; //враг красным цветом
        } else {
            return " ⩲ ";
        }
    }

    @Override
    public void shipMove(String userWord) {
        //Метод определения хода компьютера

/* АЛГОРИТМ:
 1. Сканируем в зоне видимости на предмет объектов
    1.1 Если видим корабль врага на линии выстрела, то стреляем
        - как-то учесть дальность стрельбы?
        - нужно ли преследовать его, если он не на линии выстрела?
    1.2 Иначе если видим торпеду, идущую прямо на нас, то уходим в сторону
        - если торпеда не на линии, то ход должен быть такой, чтобы не оказаться на ее пути
    1.3 Иначе если видим лутбоксы, то идем к ближайшему
 2. Если сканирование не обнаружило объектов, то нужно исследовать поле (как?)
    = ПОКА СДЕЛАН РАНДОМНЫЙ ПОПРЫГУНЧИК В СЛУЧАЙНУЮ СТОРОНУ
    - идти в зоны, где еще не был и не видел? Как запоминать это?
    - если уже побывал и увидел все клетки поля, тогда что?
    - можно пока случайно выбирать направление и идти к дальней стороне
    - нужно ли сохранять направление движения на несколько ходов?
*/

System.out.println("shipMove. VisibleArea = " + this.getVisibleArea());

        //фиксируем текущие координаты, чтобы потом убрать с них корабль
        int begin_x = this.getX();
        int begin_y = this.getY();
        //проверяем зону видимости на наличие объектов
        checkObjectsAround();
        //ставим маркер принятия решения на тру
        pc_choice = true;
        //если объекты в зоне видимости есть, то раздел 1 АЛГОРИТМА
        //ВСЕ МЕТОДЫ ВНУТРИ ДОЛЖНЫ ПРОСТО ИЗМЕНИТЬ КООРДИНАТЫ
        if (isViewUserShip != null) {
            //определяем нужно ли стрелять по кораблю игрока
            //внутри метода зашито изменение маркера pc_choice - если сделан выстрел, то маркер меняем на false,
            // чтобы дальше не идти по ифам
System.out.println("testing PC: user ship " + isViewUserShip);
            decideToShoot();
        }
        if (pc_choice && !missilesInAreaView.isEmpty()) {
            //если выстрел не сделали, и есть торпеды на линии, то уходим от торпед

System.out.println("testing PC: missiles " + missilesInAreaView);


        }
        if (pc_choice && !lootboxesInAreaView.isEmpty()) {
            //если же не стреляли в игрока, не уходили от торпед, то ищем лутбокс, к которому хотим идти

System.out.println("testing PC: lootboxes " + lootboxesInAreaView);
            //метод определения движения к ближайшему лутбоксу через метод определения ближайшего лутбокса
            moveToNearestObject(findNearestLootbox());
        }
        if (pc_choice){
            //иначе (объектов нет) раздел 2 АЛГОРИТМА
System.out.println("testing PC: rand step");
            randStepPC();
        }

        //ЗАВЕРШЕНИЕ ХОДА ПО ИЗМЕНЕННЫМ КООРДИНАТАМ ВЫШЕ
        //проверяем, что стоит на новом месте
        //для этого берем клетку с новыми координатами корабля:
        Object object = Game.fieldGame.getField()[this.getX()][this.getY()].getObject();
        //и если это лутбокс, то забираем его (если там нул, то все instanceof вернут фолс)
        if (object instanceof Lootbox) {
            ((Lootbox) object).getGift(this);
        } else if (object instanceof UserShip) {
            //или если это корабль пользователя (таран)
            //оба гибнут
            this.setAlive(false);
            ((UserShip) object).setAlive(false);
        } else if (object instanceof Missile) {
            //или если это торпеда
            //корабль погибает, торпеда тоже
            ((Missile) object).setCrush(true);
            this.setAlive(false);
        }
        //старый корабль убираем с поля
        Game.fieldGame.getField()[begin_x][begin_y].setObject(null);
        //ставим ПК на новое место.
        //если координаты не изменяться, то корабль сотрется и поставиться на то же самое место
        Game.fieldGame.getField()[this.getX()][this.getY()].setObject(this);
    }

    private void decideToShoot() {
System.out.println("method decideToShoot");
        if (isViewUserShip.getX() == this.getX()) {
            if (this.getY() > isViewUserShip.getY()) {
                this.pushMissile("6"); //стреляем влево
                //если сделали выстрел, то меняем маркер выбора на false, чтобы показать, что ход сделан
                pc_choice = false;
            } else {
                this.pushMissile("5"); //стреляем вправо
                //если сделали выстрел, то меняем маркер выбора на false, чтобы показать, что ход сделан
                pc_choice = false;
            }
        } else if (isViewUserShip.getY() == this.getY()) {
            if (this.getX() > isViewUserShip.getX()) {
                this.pushMissile("7"); //стреляем вверх
                //если сделали выстрел, то меняем маркер выбора на false, чтобы показать, что ход сделан
                pc_choice = false;
            } else {
                this.pushMissile("8"); //стреляем вниз
                //если сделали выстрел, то меняем маркер выбора на false, чтобы показать, что ход сделан
                pc_choice = false;
            }
        }
System.out.println("pc_choice = " + pc_choice);
    }

    private void moveToNearestObject(Lootbox nearestLootbox) {
        int dist_x = this.getX() - nearestLootbox.getX();
        int dist_y = this.getY() - nearestLootbox.getY();
        if (dist_x == 0) {
            if (dist_y < 0) this.setY(this.getY() + 1);
            else this.setY(this.getY() - 1);
        } else if (dist_y == 0){
            if (dist_x < 0) this.setX(this.getX() + 1);
            else this.setX(this.getX() - 1);
        } else if (Math.abs(dist_y) <= Math.abs(dist_x)) {
            if (dist_y < 0) this.setY(this.getY() + 1);
            else this.setY(this.getY() - 1);
        } else {
            if (dist_x < 0) this.setX(this.getX() + 1);
            else this.setX(this.getX() - 1);
        }
        pc_choice = false;
    }

    private Lootbox findNearestLootbox() {
        int minDistance = Integer.MAX_VALUE;
        Lootbox nearestLootboxe = null;
        for (int i = 0; i < lootboxesInAreaView.size(); i++) {
            int distance = Math.abs(lootboxesInAreaView.get(i).getX() - this.getX())
                    + Math.abs(lootboxesInAreaView.get(i).getY() - this.getY());
            if (distance < minDistance) {
                minDistance = distance;
                nearestLootboxe = lootboxesInAreaView.get(i);
            }
        }
//        System.out.println("minDistance - " + minDistance);
//        System.out.println("nearestLootboxe " + " x = " + nearestLootboxe.getX()
//                + " y = " + nearestLootboxe.getY());
        return nearestLootboxe;
    }

    private void checkObjectsAround() {
        //перед каждым осмотром очищаем списки
        lootboxesInAreaView.clear();
        missilesInAreaView.clear();
        isViewUserShip = null;
        //проверяем все клетки в зоне видимости и получаем список объектов
        for (int i = this.getX() - this.getVisibleArea(); i <= this.getX() + getVisibleArea(); i++) {
            for (int j = this.getY() - this.getVisibleArea(); j <= this.getY() + this.getVisibleArea(); j++) {
                //перебираем все клетки в заданном радиусе
                if (i >= 0 && i < Game.fieldGame.getRange() &&
                        j >= 0 && j < Game.fieldGame.getRange()) {
                    //не выходим за пределы поля!!
                    Object object = Game.fieldGame.getField()[i][j].getObject(); //для краткости кода
                    //добавляем в списки
                    if (object instanceof Lootbox) lootboxesInAreaView.add((Lootbox) (object));
                    if (object instanceof Missile) missilesInAreaView.add((Missile) (object));
                    if (object instanceof UserShip) isViewUserShip = (UserShip) object;
                }
            }
        }
//        System.out.println("lootboxesInAreaView_x - " + lootboxesInAreaView_x);
//        System.out.println("lootboxesInAreaView_y - " + lootboxesInAreaView_y);
    }

    private void randStepPC() {
        //пока просто дергунчик )))) НУЖНО БУДЕТ ИЗМЕНИТЬ!!!!!!
        boolean isLoop = true;
        Game.fieldGame.getField()[this.getX()][this.getY()].setObject(null);
        while (isLoop) {
            switch ((int) (Math.random()*4+1)) {
                case 1 -> {
                    if (this.getY() + 1 < Game.fieldGame.getRange()) {
                        this.setY(this.getY() + 1);
                        isLoop = false;
                    }
                }
                case 2 -> {
                    if (this.getY() - 1 > 0) {
                        this.setY(this.getY() - 1);
                        isLoop = false;
                    }
                }
                case 3 -> {
                    if (this.getX() - 1 > 0) {
                        this.setX(this.getX() - 1);
                        isLoop = false;
                    }
                }
                case 4 -> {
                    if (this.getX() + 1 < Game.fieldGame.getRange()) {
                        this.setX(this.getX() + 1);
                        isLoop = false;
                    }
                }
            }
        }

    }
}
