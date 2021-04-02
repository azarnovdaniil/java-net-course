package ru.daniilazarnov;

import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class YesNoConsole extends Console {

    private final String qDescription;


    YesNoConsole(Consumer<String> messageSort, String qDescription) {
        super(messageSort);
        this.qDescription=qDescription;

    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(qDescription);
        String str=scanner.nextLine();
        if(!isAnswerValid(str)){
            do {
                System.out.println("Please, input 'yes' or 'no' only!");
                str=scanner.nextLine();
            }while (!isAnswerValid(str));
        }
        messageSort.accept(str);
    }

    private boolean isAnswerValid (String answer){
        return Stream.of("yes","y","YES","Y","n","N","no","NO").anyMatch(op->op.equals(answer));
    }
}
