/*
История версий игры:
2.1 добавлены лутбоксы (бонусы на поле), которые добавляют видимость поля
2.2 сделан расчет количества лутбоксов в зависимости от размера поля
2.3 создан механизм для пополнения лутбоксов, если какой-то был использован,
чтобы их количество было постоянным
3.1 добавлен класс "торпеды", реализовано ее движение
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
public class Game {
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static String userWord; //для ввода пользователем
    static String menuMove = """
            Сделайте свой ход, выбрав пункт меню:
            1 - вправо, 2 - влево, 3 - вверх, 4 - вниз (любое другое значение оставит корабль на месте),
            либо выпусти торпеду, отправив ее: 5 - вправо, 6 - влево, 7 - вверх, 8 - вниз""";
    static boolean isLoop = true; //для возможных циклов
    static int minRangeField = 10; //минимальный размер поля
    static int maxRangeField = 20; //максимальный размер поля
    static FieldGame fieldGame;
    static int range = 12; //размерность поля от пользователя
    static ArrayList<Lootbox> lootbox_list = new ArrayList<>(); //набор (список) лутбоксов
    public static void main(String[] args) throws IOException {
        //вступление и описание
        System.out.println("""
                Приветствую в игре "Морской охотник" - тут тебе нужно найти и уничтожить врага (\u001b[31m■\u001b[0m).
                Ты сам - (\u001b[32m■\u001b[0m). В игре есть туман войны - твоя зона видимости отмечена точками.
                (\u001b[33m◊\u001b[0m) - это лутбоксы, они дают какой-то бонус.
                За ход ты можешь либо переместиться, либо сделать выстрел. Меню действий будет выводиться.
                Чтобы получить бонус - "наступи" на лутбок, но если ты "наступишь" на врага - вы оба погибните.
                Ты также погибнешь, если "натупишь" на торпеду (изображаются стрелками)""");

        //выбор размера поля
//        while (isLoop) {
//            System.out.printf("Напиши размер поля (от %d до %d) \n",
//                    minRangeField, maxRangeField);
//            userWord = reader.readLine();
//            for (int i = minRangeField; i <= maxRangeField ; i++) {
//                if (userWord.equals(String.valueOf(i))) {
//                    isLoop = false;
//                    range = i;
//                    break;
//                }
//            }
//            if (isLoop) System.out.printf("Нужно написать число от %d до %d\n",
//                    minRangeField, maxRangeField);
//        }

        //создаем поле заданного размера
        fieldGame = new FieldGame(range);

        //определяем случайные координаты нашего корабля в 1-й четверти поля
        int ourX_initial = (int) (Math.random()* (range / 2));
        int ourY_initial = (int) (Math.random()* (range / 2));
        //создаем наш корабль
        UserShip ourShip = new UserShip(ourX_initial,ourY_initial);
        //включаем нашему кораблю обзор заданного размера
        ourShip.view(true);

        //определяем случайные координаты врага в 3-й четверти поля
        int enemyX_initial = (int) (Math.random()* (range / 2) + (range / 2));
        int enemyY_initial = (int) (Math.random()* (range / 2) + (range / 2));
        //создаем врага
        PC_Ship enemy = new PC_Ship(enemyX_initial,enemyY_initial);

        //определяем количество лутбоксов
        int count_lootbox = (int) (Math.pow(range, 2) / (Math.pow(Lootbox.getDensity() * 2 + 1, 2)));

        //игровой цикл
        while (true) {
            //проверяем количество лутбоксов на поле
            if (lootbox_list.size() < count_lootbox) {
                //заполняем список лутбоксов, если их меньше заданного числа
                for (int i = lootbox_list.size(); i < count_lootbox; i++) {
/*ВАЖНО! в конструкторе лутбокса есть цикл, если игра зависла, то, возможно, причина в том,
что не может найти место для лутбокса */
                    lootbox_list.add(new Lootbox());
                }
            }
            //рисуем поле (перед ходом игрока)
            fieldGame.printField();

            //определяем оставшееся количество торпед
            int restMissile = ourShip.getAmountMissile() - ourShip.getCurrentMissile();
            //сообщаем пользователю
            System.out.println("У тебя в запасе " + restMissile + " торпед");
            //выводим меню действий
            System.out.println(menuMove);
            //пользователь делает свой ход
            userWord = reader.readLine();
            //определяем действие игрока
            switch (userWord) {
                case "1","2","3","4" -> ourShip.shipMove(userWord); //метод передвижения корабля
                case "5","6","7","8" -> ourShip.pushMissile(userWord); //метод запуска торпед
                //если игрок решил ничего не делать
                default -> System.out.println("Корабль остался на месте");
            }
            //метод отрисовки запущенных торпед
            ourShip.missileTrack();
            //после действия игрока проверяем лутбоксы на предмет использования
            checkLootbox();

/* ДЕЙСТВИЯ КОМПЬЮТЕРА */

            //после пользователя делает ход компьютер
            enemy.shipMove("");
            //метод отрисовки запущенных торпед
            enemy.missileTrack();
            //после хода компьютера проверяем лутбоксы на предмет использования
            checkLootbox();

/* Проверка окончания игры!!! */
            //оба игрока сделали свои ходы, теперь проверяем их результат
            if (!ourShip.isAlive() && !enemy.isAlive()) {
                System.out.println("Ничья - оба погибли");
                break;
            }
            if (!ourShip.isAlive()) {
                System.out.println("Вы проиграли - ваш корабль потоплен!");
                break;
            }
            if (!enemy.isAlive()) {
                System.out.println("Поздравляем! Вы победили - враг потоплен!");
                break;
            }
        }
        //так как игра закончилась, то открываем делаем видимым все поле
        ourShip.setVisibleArea(Game.fieldGame.getRange());
        //и открываем его перед финальной печатью
        ourShip.view(true);
        //рисуем поле, чтобы нарисовать его в самом конце игры
        fieldGame.printField();
    }


    private static void checkLootbox() {
        //метод проверки количества лутбоксов на поле
        for (int i = 0; i < lootbox_list.size(); i++) {
            //проходим по списку и смотрим у каждого маркер
            if (!lootbox_list.get(i).isAlive()) {
                //если лутбокс был использован, то маркер станет false
                lootbox_list.remove(lootbox_list.get(i));
                //тогда удаляем из списка и прекращаем перебор,
                // так как за один ход игрок (или PC) мог использовать только один лутбокс
                System.out.println("Был использован (или потоплен) лутбокс, " +
                        "в новом месте на поле появился новый");
                break;
            }
        }
    }
}