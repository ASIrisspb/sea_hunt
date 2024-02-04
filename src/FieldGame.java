import java.util.Arrays;

public class FieldGame {
    private final int range; //размерность поля
    private final Cell[][] field; //клетки поля в массиве
    public FieldGame(int range) {
        //конструктор
        this.range = range;
        field = new Cell[range][];
        //заполняем массив клетками
        Arrays.setAll(field, i -> new Cell[range]);
        for (Cell[] cells : field) {
            Arrays.setAll(cells, i -> new Cell());
        }
    }
    public void printField() {
        //метод печати поля
        System.out.print(" ");
        for (int j = 0; j < range; j++) {
            System.out.print(" — ");
        }
        System.out.println();
        for (int i = 0; i < range; i++) {
            System.out.print("|");
            for (int j = 0; j < field[i].length; j++) {
                //просто печатаем клетку, а она уже сама определяет что вывести!!
                System.out.print(field[i][j]);
            }
            System.out.println("|");
        }
        System.out.print(" ");
        for (int j = 0; j < range; j++) {
            System.out.print(" — ");
        }
        System.out.println();
    }
    public int getRange() {
        //геттер для получения размерности поля во вне
        return range;
    }
    public Cell[][] getField() {
        //геттер для доступа именно к клеткам поля
        return field;
    }
}
