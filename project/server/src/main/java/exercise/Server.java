package exercise;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Server!");
        new DiscardServer().runManyPipeline();
    }
}
