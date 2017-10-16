public class Main {

    public static void main(String[] args) {
        if(args.length < 3){
            System.out.println("prog num_port voisin-x.txt INIT|WAIT");
            System.exit(1);
        }
        int port = Integer.valueOf(args[0]);
        String file = args[1];
        boolean candidat = args[2].equals("INIT");
        Node node = new Node(port, file, candidat);
    }
}
