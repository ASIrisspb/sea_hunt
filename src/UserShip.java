public class UserShip extends Ship{
    public UserShip(int x, int y, FieldGame fieldGame) {
        super(x, y, fieldGame);
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
    public void shipMove(String userWord) {
        //метод передвижения корабля
        //сначала "снимаем" корабль с клетки
        this.actionByCell(this.getX(),this.getY(),null,false);
        //и убираем зону видимости
        this.view(false);
        //теперь меняем координату, если это возможно
        switch (userWord) {
            case "1" -> {
                if (this.getY() + 1 >= this.getFieldGame().getRange())
                    System.out.println("Недопустимый ход! Нельзя выйти за края поля");
                else this.setY(this.getY() + 1);
            }
            case "2" -> {
                if (this.getY() - 1 < 0)
                    System.out.println("Недопустимый ход! Нельзя выйти за края поля");
                else this.setY(this.getY() - 1);
            }
            case "3" -> {
                if (this.getX() - 1 < 0)
                    System.out.println("Недопустимый ход! Нельзя выйти за края поля");
                else this.setX(this.getX() - 1);
            }
            case "4" -> {
                if (this.getX() + 1 >= this.getFieldGame().getRange())
                    System.out.println("Недопустимый ход! Нельзя выйти за края поля");
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
        Object object = this.getFieldGame().getField()[x][y].getObject();
        //если не нул, то там есть объект
        if (object != null) {
            //если это лутбокс, то "забираем" его
            if (object instanceof Lootbox) {
                ((Lootbox) object).getGift(this);
            }
            //если там корабль врага, то оба погибают
            if (object instanceof PC_Ship) {
                //открываем все поле
                this.setVisibleArea(this.getFieldGame().getRange());
                //помечаем наш корабль потопленным
                this.setAlive(false);
                //помечаем корабль врага также потопленным
                ((PC_Ship) object).setAlive(false);
            }
        }
        //метод постановки корабля на клетку или его снятия с неё
        this.getFieldGame().getField()[x][y].setObject(obj); //объект или null
        this.getFieldGame().getField()[x][y].setVisible(visible); //видимость
    }
    public void view (boolean isView) {
        //метод обзора вокруг корабля (только для пользователя)
        for (int i = this.getX() - this.getVisibleArea(); i <= this.getX() + getVisibleArea(); i++) {
            for (int j = this.getY() - this.getVisibleArea(); j <= this.getY() + this.getVisibleArea(); j++) {
                //перебираем все клетки в заданном радиусе и устанавливаем им видимость
                if (i >= 0 && i < this.getFieldGame().getRange() && j >= 0
                        && j < this.getFieldGame().getRange()) {
                    //не выходим за пределы поля!!
                    this.getFieldGame().getField()[i][j].setVisible(isView);
                }
            }
        }
    }
}