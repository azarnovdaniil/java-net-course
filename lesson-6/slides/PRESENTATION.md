# Сборщики проектов

## Структура урока

### Историческое интро

- javac
```javac
javac <java file>
```
- jar 
```jar
Main-Class: com.mypackage.MyClass

jar cmvf META-INF/MANIFEST.MF <new-jar-filename>.jar  <files to include>
```
- make
- ant
- maven
- gradle
- sbt/bazel/buck/...

### Maven

- Структура проекта
```
.
├── pom.xml
└── src
    ├── main
    │    ├── java
    │    │   └── ru
    │    │      └── daniilazarnov
    │    │           └── Main.java
    │    └── resources
    └── test
        ├── java
        │    └── ru
        │        └── daniilazarnov
        │            └── MainTest.java
        └── resources
```
- про M2 и архитектуру maven
- обзор pom.xml
- Жизненный цикл
- обзор плагинов

## Домашнее задание

- Продолжаем писать проект

## Ссылки

- [About javac](https://www.baeldung.com/javac)