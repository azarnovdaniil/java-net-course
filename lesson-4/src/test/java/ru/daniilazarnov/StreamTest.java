package ru.daniilazarnov;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StreamTest {

    @Test
    void testForI() {
        int[] ints = new int[10];
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);
        }

        while (true) {
            break;
        }

        do {
            break;
        } while (true);
    }

    @Test
    void testFor() {
        int[] ints = new int[10];

        for (int anInt : ints) {
            System.out.println(anInt);
        }

        class MyIterable implements Iterable<String> {

            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public String next() {
                        return "";
                    }
                };
            }
        }

        MyIterable myIterable = new MyIterable();

        for (String s : myIterable) {
            System.out.println(s);
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
    void testStream0() {

        class Person {

            private final String firstName;
            private final String lasName;

            public Person(String firstName, String lasName) {
                this.firstName = firstName;
                this.lasName = lasName;
            }

            @Override
            public String toString() {
                return "Person{" +
                        "firstName='" + firstName + '\'' +
                        ", lasName='" + lasName + '\'' +
                        '}';
            }
        }

        List<Person> persons = List.of(
                new Person("Ivan", "Fedorov"),
                new Person("Daniil", "Azarnov"),
                new Person("Mike", "Petrov"),
                new Person("Alex", "Bashirov")
        );


        List<Person> firstLetterAInLastName = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (person.lasName.charAt(0) == 'A') {
                int length = person.lasName.length();
                counter += length;
            }
        }

        System.out.println(counter);

        System.out.println(firstLetterAInLastName);

        final int count = persons.stream()
                .filter(person -> person.lasName.charAt(0) == 'A')
                .map(person -> person.lasName.length())
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println(count);
    }

    @Test
    void testStream() {
        List<String> strings = List.of("aa", "bb", "cc");

        strings.stream()
                .map(str -> str + "-map1")
                .map(s -> s + "-map2")
                .forEach(System.out::println);

        System.out.println(strings);

//        Stream<String> stringStream = strings.stream()
//                .map(s -> s + "dd")
//                .map(s -> s + " map2")
//                .peek(System.out::println);
//
//        System.out.println();
//
//        stringStream.forEach(System.out::println);
//
//        System.out.println(strings);
//
//        System.out.println();
    }

    @Test
    void testTerminalOperation() {
        List<String> strings = List.of("aa", "bb", "cc");

        Stream<String> stringStream = strings.stream()
                .map(str -> str + "-map1")
                .peek(s -> System.out.println("Peek: " + s))
                .map(s -> s + "-map2");

        stringStream.forEach(System.out::println);

        assertThrows(IllegalStateException.class, () -> stringStream.forEach(System.out::println));
    }

    @Test
    void testReuseStream() {
        Stream<String> stream = List.of("1", "2", "3").stream();

        stream.map(Integer::valueOf).map(x -> x + 1).forEach(System.out::println);

        assertThrows(IllegalStateException.class, () -> stream.forEach(System.out::println));
    }

    @Test
    void testStreamSimpleStream() {
        List<String> strings = List.of("aa", "bb", "cc");

        class SimpleStream {
            private final Iterable<String> iterable;
            private Function<String, String> mapper;

            public SimpleStream(Iterable<String> iterable) {
                this.iterable = iterable;
            }

            /**
             * Lazy operation
             */
            SimpleStream map(Function<String, String> mapper) {
                this.mapper = mapper;
                return this;
            }

            /**
             * Terminal operation
             */
            void print() {
                Iterator<String> iterator = iterable.iterator();
                while (iterator.hasNext()) {
                    System.out.println(mapper.apply(iterator.next()));
                }
            }
        }

        SimpleStream simpleStream = new SimpleStream(strings);
        simpleStream.map(s -> s + "dd").print();
    }

    @Test
    void testMap() {
        List<String> strings = List.of("aa", "bb", "cc");

        strings.stream()
                .map(s -> s + "dd")
                .forEach(s -> System.out.println(s));
    }

    @Test
    void testMap1() {
        List<String> strings = List.of("aa", "bb", "cc");
        strings.stream()
                .map(s -> s + "dd")
                .map(s -> s + " ii")
                .map(s -> s.getBytes(StandardCharsets.UTF_8))
                .map(bytes -> bytes.length)
                .map(integer -> integer + 1)
                .mapToInt(Integer::intValue)
                .forEach(System.out::println);
    }

    @Test
    void testReduce0() {
        List<String> strings = List.of("aa", "bb", "cc");

        System.out.println(strings.stream()
                .map(s -> s.length())
                .reduce((s, s2) -> s + s2).get());
    }

    @Test
    void testReduce() {
        List<String> strings = List.of("aa", "bb", "cc");

        String reduce = strings.stream()
                // FIRST + aa -> FIRSTaa + bb -> aabb + cc -> aabbcc
                .reduce("FIRST", (s, s2) -> {
                    System.out.println(s);
                    System.out.println(s2);
                    String result = s + s2;
                    System.out.println(result);
                    System.out.println();
                    return result;
                });

        System.out.println(strings);
        System.out.println(reduce);

        List<String> empty = List.of();
        Optional<String> optionalReduce = empty.stream()
                .reduce((s, s2) -> s + s2);

        List<Boolean> booleans = List.of(true, false, true, false);
        Optional<Boolean> reduce1 = booleans.stream().reduce((aBoolean, aBoolean2) -> aBoolean || aBoolean2);
        System.out.println(reduce1.get());

        System.out.println(optionalReduce.orElse("DEFAULT"));
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


        int size = strings.stream()
                .map(s -> 1)
                .reduce((integer, integer2) -> integer + integer2)
                .get();

        System.out.println(size);

        int size1 = strings.size();
        System.out.println(size1);

        Integer reduce = strings.stream()
                .map(s -> 1)
                .reduce(0, Integer::sum);

        int sum = List.of(1, 2, 3, 4)
                .stream()
                .reduce(0, Integer::sum);

        System.out.println(List.of("aa", "aaa", "aa")
                .stream()
                .map(String::length)
                .map(x -> x >= 2)
                .reduce(true, (b1, b2) -> b1 && b2)
                .booleanValue());

        System.out.println("aresxdghftjgymhujhkvjghmfcngmv".chars()
                .map(x -> x == 'a' ? 1 : 0)
                .sum());

        //System.out.println(reduce);
    }

    @Test
    void testFilter() {
        List<String> strings = List.of("aa", "bb", "cc");

        strings.stream()
                .peek(s -> System.out.println("Before filter: " + s))
                .filter(s -> s.equals("aa"))
                .forEach(s -> System.out.println("After filter: " + s));

        strings.stream().map(s -> {
            if (s.equals("aa")) {
                return s;
            } else {
                return "";
            }
        }).forEach(System.out::println);

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
    }

    @Test
    void testSorted() {
        List<Integer> integers = List.of(1, 3, 4, 2);

        integers.stream()
                .sorted()
                .forEach(System.out::println);

        System.out.println(List.of("Cотников", "Цоцонова", "Фоменков")
                .stream()
                .sorted(Comparator.comparingInt(s -> s.charAt(0)))
                .findFirst()
                .orElse("Азарнов"));
    }

    @Test
    void testStreamStudent() {
        List.of(new Student("Ivan", "Petrov", 1),
                new Student("Fedor", "Smirnov", 2),
                new Student("Fedot", "Matveev", 3)
        ).stream()
                .filter(student -> student.getCourse() >= 2)
                .forEach(System.out::println);
    }

    class Student {
        private final String firstName;
        private final String lastName;
        private final int course;

        public Student(String firstName, String lastName, int course) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.course = course;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public int getCourse() {
            return course;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", course=" + course +
                    '}';
        }
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
    void testWalk() throws IOException {
        Files.walk(Path.of("../lesson-1/dir"))
                .filter(Files::isRegularFile)
                .filter(s -> s.toString().endsWith(".txt"))
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                //.flatMap(s -> s.chars().boxed())
                .forEach(System.out::println);
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

        Stream.generate(() -> "ff")
                .limit(10)
                .forEach(s -> System.out.println(s));
    }

    @Test
    void testFlatMap() {
        List<List<String>> aa = List.of(List.of("aa", "bb"), List.of("cc", "dd"));

        aa.stream()
                .flatMap(list -> list.stream())
                .forEach(s -> System.out.println(s));
    }
}
