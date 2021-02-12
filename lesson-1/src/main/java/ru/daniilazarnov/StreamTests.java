package ru.daniilazarnov;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTests {
    public static void main(String[] args) {
        long count = IntStream.of(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5).filter(w -> w > 0).count();
//        System.out.println(count);

        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, "Париж", "Лондон", "Мадрид");
        for (String i : list) {
//            System.out.println(i);
        }
//        list.stream().forEach(l -> System.out.println(l));

        Stream<String> citiesStream = Arrays.stream(new String[]{"Париж", "Лондон", "Мадрид"}) ;
        citiesStream
                .filter(s -> s.length() == 6)
                .forEach(s -> System.out.println(s)); // выводим все элементы массива

        Stream<String> citiesStream2 =Stream.of("Париж", "Лондон", "Мадрид");
        citiesStream2.forEach(s->System.out.println(s));
    }
}
