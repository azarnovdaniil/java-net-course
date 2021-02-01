package ru.gb.putilin.cloudstorage;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testForI() {
        int[] ints = new int[10];
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);
        }
    }

    @Test
    void testFor() {
        int[] ints = new int[10];
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    @Test
    void testIterator() {
        List<String> strings = List.of("aa", "bb", "cc");

        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Test
    void name() {
        Function<String, String> functionPlusWhitespaceSymbol = s -> s + "DDDD";

        System.out.println(functionPlusWhitespaceSymbol.apply("aaa"));

        Supplier<String> supplier = () -> "JJJ";
        System.out.println(supplier.get());

        Consumer<String> consumer = s -> System.out.println(s);
        consumer.accept("aaa");
    }

    class MyClass {
        String field;

        public MyClass(String field) {
            this.field = field;
        }

        void method(MyLabmda myLabmda) {
            myLabmda.apply(field);
        }
    }

    interface MyLabmda {
        void apply(String s);
    }

    @Test
    void name1() {
        MyClass string = new MyClass("string");
        string.method(s -> System.out.println("feder"));
    }

    @Test
    void testSpliterator() {
        List<String> strings = List.of("aa", "bb", "cc");

        Spliterator<String> spliterator = strings.spliterator();
        spliterator.forEachRemaining(s -> System.out.println(s));
    }

    @Test
    void testStream() {
        List<String> strings = List.of("aa", "bb", "cc");

        strings.stream().map(s -> s + "dd").forEach(s -> System.out.println(s));

        System.out.println();

        SimpleStream simpleStream = new SimpleStream(strings);
        simpleStream.map(s -> s + "dd").print();
    }

    class SimpleStream {
        private final Iterable<String> iterable;
        private Function<String, String> mapper;

        public SimpleStream(Iterable<String> iterable) {
            this.iterable = iterable;
        }

        SimpleStream map(Function<String, String> mapper) {
            this.mapper = mapper;
            return this;
        }

        void print() {
            Iterator<String> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                System.out.println(mapper.apply(iterator.next()));
            }
        }
    }

    @Test
    void testMap() {
        List<String> strings = List.of("aa", "bb", "cc");
        Stream<String> stringStream = strings.stream().map(s -> s + "dd");

        stringStream.forEach(s -> System.out.println(s));
    }

    @Test
    void testMap1() {
        List<String> strings = List.of("aa", "bb", "cc");
        strings.stream()
                .map(s -> s + "dd")
                .map(s -> s + " ii")
                .map(s -> s.getBytes(StandardCharsets.UTF_8))
                .map(fefe -> fefe.length)
                .map(integer -> integer + 1)
                .forEach(integer -> System.out.println(integer));

        for (String string : strings) {
            String s = string + "dd";
        }
    }

    @Test
    void testReduce() {
        List<String> strings = List.of("aa", "bb", "cc");
        String reduce = strings.stream()
                // FIRST + aa -> FIRSTaa + bb -> aabb + cc -> aabbcc
                .reduce("FIRST", (s, s2) -> s + s2);

        System.out.println(strings);
        System.out.println(reduce);

        Optional<String> optionalReduce = strings.stream()
                .reduce((s, s2) -> s + s2);
        System.out.println(optionalReduce
                .or(() -> Optional.of("fsfes"))
                .map(s -> s + "")
                .orElse("DEFAULT"));
    }

    class MyOptional<R> {

        R value;

        public MyOptional(R value) {
            this.value = value;
        }

        R get() {
            return value;
        }

        R orElse(R defaultValue) {
            if (value == null) {
                return defaultValue;
            }
            return value;
        }
    }

    @Test
    void testSize() {
        List<String> strings = List.of("aa", "bb", "cc");

        int size = strings.size();
        System.out.println(size);

        Integer reduce = strings.stream()
                .map(s -> 1)
                .reduce(0, (integer, integer2) -> integer + integer2);

        System.out.println(reduce);
    }

    @Test
    void testFilter() {
        List<String> strings = List.of("aa", "bb", "cc");

        strings.stream()
                .filter(s -> s.equals("aa"))
                .forEach(s -> System.out.println(s));

        strings.stream().map(s -> {
            if(s.equals("aa")) {
                return s;
            } else {
                return "";
            }
        }).forEach(s -> System.out.println(s));

    }

    @Test
    void testMatch() {
        List<String> strings = List.of("aa", "bb", "cc");

        boolean allMatch = strings.stream()
                .allMatch(s -> s.equals("aa"));

        System.out.println(allMatch);

        boolean anyMatch = strings.stream()
                .anyMatch(s -> s.equals("aa"));
        System.out.println(anyMatch);

        boolean allMatchReduce = strings.stream()
                // aa -> true, bb -> false, cc -> false
                .map(s -> s.equals("aa"))
                // true && false -> false + false -> false
                .reduce((aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                .orElse(false);

        System.out.println(allMatchReduce);

        boolean anyMatchReduce = strings.stream()
                .map(s -> s.equals("aa"))
                .reduce(false, (aBoolean, aBoolean2) -> aBoolean || aBoolean2);

        System.out.println(anyMatchReduce);

        System.out.println(strings);

        Predicate<String> predicate = s -> s.equals("");
        Function<String, Boolean> function = s -> s.equals("");
    }

    @Test
    void testSorted() {
        List<Integer> integers = List.of(1, 3, 4, 2);

        integers.stream().sorted()
                .forEach(integer -> System.out.println(integer));
    }

    @Test
    void testCollect() {
        List<String> strings = List.of("aa", "bb", "cc");

        ArrayList<Object> result = new ArrayList<>();
        strings.stream()
                .map(s -> s + " F")
                .forEach(s -> result.add(s));

        result.add("fdsf");

        ArrayList<String> collect = strings.stream()
                .map(s -> s + " F")
                .collect(Collectors.toCollection(() -> new ArrayList<>()));

        collect.add("ffe");

        List<String> collect1 = strings.stream()
                .map(s -> s + " F")
                .collect(Collectors.toList());

        collect1.add("fsfs");
    }

    @Test
    void testParallel() {
        List<String> strings = List.of("aa", "bb", "cc");

        strings.stream()
                .parallel()
                .forEach(System.out::println);
    }

    @Test
    void testStreamOf() {
        Stream.of("aa", "aa").forEach(s -> System.out.println(s));

        Stream.concat(Stream.of(" ", "11"), Stream.of("aa", "gg"))
                .forEach(s -> System.out.println(s));

        Stream.generate(() -> "ff").limit(10).forEach(s -> System.out.println(s));
    }

    @Test
    void testFlatMap() {
        List<List<String>> aa = List.of(List.of("aa", "bb"), List.of("cc", "dd"));

        aa.stream()
                .flatMap(list -> list.stream())
                .forEach(s -> System.out.println(s));
    }
}
