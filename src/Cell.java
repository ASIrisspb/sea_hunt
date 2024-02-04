public class Cell {
    private boolean visible; //видимость клетки
    private Object object; //наличие на клетке какого-либо объекта
//    public Cell() {
//        //Конструктор. Устанавливаем всем клеткам "невидимость".
//        //Пока не нужен, так как по умолчанию и так у всех фолс
//        this.visible = false;
//    }
    public boolean isVisible() {
        return visible; //геттер для получения значения видимости
    }
    public void setVisible(boolean visible) {
        this.visible = visible; //сеттер для установки видимости
    }
    public void setObject(Object object) {
        this.object = object; //сеттер для установки на клетку объекта
    }
    public Object getObject() {
        return object;
    }
    @Override
    public String toString() {
        //переопределяем toString, чтобы можно было сразу печатать
        if (visible) {
            //если видимость тру, то сначала смотри на наличие объекта
            if (object == null) {
                //если пусто, то выводим точку - пустое место на игровом поле
                return " . ";
            } else {
                //если объект есть, то выводим его через его toString
                return object.toString();
            }
        } else {
            //если клетка невидима, то выводим пробел - туман войны
            return "   ";
        }
    }
}
