package ru.daniilazarnov;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

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
    void testSpliterator() {
        List<String> strings = List.of("aa", "bb", "cc");

        Spliterator<String> spliterator = strings.spliterator();
        spliterator.forEachRemaining(s -> System.out.println(s));
    }

    @Test
    void testStream() {
        List<String> strings = List.of("aa", "bb", "cc");

        strings.stream().map(s -> s + "dd").forEach(s -> System.out.println(s));

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
        strings.stream()
                .map(s -> s + "dd")
                .forEach(s -> System.out.println(s));
    }

    @Test
    void testReduce() {
        List<String> strings = List.of("aa", "bb", "cc");
        String reduce = strings.stream()
                .reduce("1", (s, s2) -> s + s2);

        System.out.println(reduce);

        Optional<String> optionalReduce = strings.stream()
                .reduce((s, s2) -> s + s2);
        System.out.println(optionalReduce.get());
    }

    @Test
    void testSize() {
        List<String> strings = List.of("aa", "bb", "cc");

        int size = strings.size();
        System.out.println(size);

        int[] ints = new int[1];
        Integer reduce = strings.stream()
                .map(s -> ints[0]++)
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

}
