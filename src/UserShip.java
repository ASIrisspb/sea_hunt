public class UserShip extends Ship{
    public UserShip(int x, int y) {
        super(x, y);
//для тестов, чтобы видеть все поле, а ПК видит как обычно. УДАЛИТЬ ПОСЛЕ РЕЛИЗА!
        setVisibleArea(15);
    }
    @Override
    public String toString() {
        //переопределение toString для вывода корабля
        if (this.isAlive()) {
            return " \u001b[32m■\u001b[0m "; //наш корабль зеленый
        } else {
            //отмечаем место гибели
            return " ⩲ ";
        }
    }
    @Override
    public void shipMove(String userWord) {
        //метод передвижения корабля
        //сначала "снимаем" корабль с клетки
        this.actionByCell(this.getX(),this.getY(),null,false);
        //и убираем зону видимости
        this.view(false);
        //теперь меняем координату, если это возможно
        switch (userWord) {
            case "1" -> {
                if (this.getY() + 1 >= Game.fieldGame.getRange())
                    System.out.println("Недопустимый ход! Нельзя выйти за края поля. Ход потерян");
                else this.setY(this.getY() + 1);
            }
            case "2" -> {
                if (this.getY() - 1 < 0)
                    System.out.println("Недопустимый ход! Нельзя выйти за края поля. Ход потерян");
                else this.setY(this.getY() - 1);
            }
            case "3" -> {
                if (this.getX() - 1 < 0)
                    System.out.println("Недопустимый ход! Нельзя выйти за края поля. Ход потерян");
                else this.setX(this.getX() - 1);
            }
            case "4" -> {
                if (this.getX() + 1 >= Game.fieldGame.getRange())
                    System.out.println("Недопустимый ход! Нельзя выйти за края поля. Ход потерян");
                else this.setX(this.getX() + 1);
            }
        }
        //"ставим" корабль на новые координаты (если изменились)
        this.actionByCell(this.getX(),this.getY(),this,true);
        //и снова открываем зону видимости
        this.view(true);
    }
    public void actionByCell(int x, int y, Object obj, boolean visible) {
        //метод действия на клетке
        //получаем объект, связанный с клеткой
        Object object = Game.fieldGame.getField()[x][y].getObject();
        //если не нул, то там есть объект
        if (object != null) {
            //если это лутбокс, то "забираем" его
            if (object instanceof Lootbox) {
                ((Lootbox) object).getGift(this);
            }
            //если там корабль врага, то оба погибают
            if (object instanceof PC_Ship) {
                //помечаем наш корабль потопленным
                this.setAlive(false);
                //помечаем корабль врага также потопленным
                ((PC_Ship) object).setAlive(false);
            }
        }
        //метод постановки корабля на клетку или его снятия с неё
        Game.fieldGame.getField()[x][y].setObject(obj); //ставим объект на клетку
        Game.fieldGame.getField()[x][y].setVisible(visible); //видимость
    }
    public void view (boolean isView) {
        //метод обзора вокруг корабля (только для пользователя)
        for (int i = this.getX() - this.getVisibleArea(); i <= this.getX() + getVisibleArea(); i++) {
            for (int j = this.getY() - this.getVisibleArea(); j <= this.getY() + this.getVisibleArea(); j++) {
                //перебираем все клетки в заданном радиусе и устанавливаем им видимость
                if (i >= 0 && i < Game.fieldGame.getRange() && j >= 0
                        && j < Game.fieldGame.getRange()) {
                    //не выходим за пределы поля!!
                    Game.fieldGame.getField()[i][j].setVisible(isView);
                }
            }
        }
    }
}
