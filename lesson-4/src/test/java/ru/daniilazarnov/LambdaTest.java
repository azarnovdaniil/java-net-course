package ru.daniilazarnov;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.*;

class LambdaTest {

    @Test
    void testRunnableBeforeJava8() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("I am Runnable from java 7!");
            }
        };

        runnable.run();
    }

    @Test
    void testRunnableAfterJava8() {
        Runnable runnable = () -> {
            System.out.println("I am Runnable from java 8!");
        };

        runnable.run();
    }

    @FunctionalInterface
    interface MyLambda {
        String apply(String s1, String s2);
    }

    @Test
    void testMyLambda() {

        class Person {

            private final String firstName;
            private final String lasName;

            public Person(String firstName, String lasName) {
                this.firstName = firstName;
                this.lasName = lasName;
            }

            public String getInfo(MyLambda myLambda) {
                return myLambda.apply(firstName, lasName);
            }

            public String getTsv() {
                return firstName + "\t" + lasName;
            }

            public String getCsv() {
                return firstName + "," + lasName;
            }
        }

        Person person = new Person("Vasya", "Pupkin");

        String csv = person.getInfo((f, l) -> f + "," + l);
        System.out.println(csv);

        String tsv = person.getInfo((f, l) -> f + "\t" + l);
        System.out.println(tsv);
    }


    @Test
    void testFinalOrEffectivelyFinal() {

        int i = 0;

        int[] ints = new int[1];
        ints[0]++;

        Function<String, String> function = str -> {
            String end = "Number: ";
            return str + " - " + end + i + ints[0];
        };

        String s = function.apply("Ivan");

        System.out.println(s);
    }

    @Test
    void testConsumer() {
        Consumer<String> consumer = s -> System.out.println("Print from consumer: " + s);

        consumer.accept("Vasya");
    }

    @Test
    void testSupplier() {
        Supplier<String> supplier = () -> "";

        System.out.println(supplier.get());
    }

    @Test
    void testFunction() {
        Function<String, String> function = str -> str + "1";

        System.out.println(function.apply("Maria"));
    }

    @Test
    void testPredicate() {
        Predicate<String> predicate = s -> {
            return s.isEmpty();
        };

        System.out.println(predicate.test(""));
        System.out.println(predicate.test("not empty"));
    }

    @Test
    void testMethodReference1() {
        List.of("1", "2", "3").forEach(System.out::println);

        int[] i = new int[]{0};
        i[0] = 1;

        methodReferenceWithTwo((var1, var2) -> System.out.println(var1.length() + 100 + var2 + i[0]));
    }

    @Test
    void testMethodReference2() {
        methodReferenceWithTwo(LambdaTest::printStringPlusInteger);

        SortedSet<String> set = new TreeSet<>(Comparator.comparingInt(String::length));
    }

    static void printStringPlusInteger(String firstName, String lastName) {
        System.out.println(firstName + "," + lastName);
    }

    static void methodReferenceWithTwo(BiConsumer<String, String> biConsumer) {
        biConsumer.accept("Ivan", "Fedorov");
    }

}
