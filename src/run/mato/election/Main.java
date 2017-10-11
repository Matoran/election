package run.mato.election;

public class Main {

    public static void main(String[] args) {
        if(args.length < 3){
            System.out.println("prog num_port voisin-x.txt INIT|WAIT");
            System.exit(1);
        }
        int port = Integer.valueOf(args[1]);
        String file = args[2];
        boolean candidat = args[3].equals("INIT");
        Node node = new Node(port, file, candidat);
    }
}
