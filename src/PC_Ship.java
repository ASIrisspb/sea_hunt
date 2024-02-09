public class PC_Ship extends Ship{
    public PC_Ship(int x, int y) {
        super(x, y);
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

    }
}
