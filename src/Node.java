import java.io.*;
import java.net.*;
import java.util.*;

public class Node {
    public static final int OK = 254;
    public static final int NOT_OK = 253;
    private ArrayList<String> neighbors = new ArrayList<>();
    private ArrayList<String> tried = new ArrayList<>();
    private List<Socket> connected = Collections.synchronizedList(new ArrayList<>());
    private int owner;
    private ServerSocket serverSocket;
    private boolean candidat;
    private int id;
    private Random rand = new Random(0);
    private int send = 0;
    private int receive = 0;
    int puissance = 1;
    private int doNothing = 0;

    private String searchIp(){
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual() || iface.isPointToPoint())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    final String ip = addr.getHostAddress();
                    if(Inet4Address.class == addr.getClass()) return ip;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Node(int id, String filename, boolean candidat) throws InterruptedException, UnknownHostException {
        loadFile(filename);
        String ip = searchIp();
        if(ip != null){
            System.out.println(ip);
            System.out.println(Integer.valueOf(ip.substring(ip.lastIndexOf('.')+1, ip.length())));
            id = Integer.valueOf(ip.substring(ip.lastIndexOf('.')+1, ip.length()));
        }else{
            System.exit(10);
        }

        this.id = id;
        this.candidat = candidat;
        try {
            serverSocket = new ServerSocket(3000);
            init();
            while (true) {
                if (!todd()) {
                    break;
                }
                if (!teven()) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("GOTO SLEEP FOREVER " + id);
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        System.out.println("t0");
        if (candidat) {
            owner = id;
            System.out.println(id);
            //tant qu'on n'arrive pas à se connecter
            int neighbour = rand.nextInt(tried.size());
            new Sender(id, tried.get(neighbour), connected).run();
            tried.remove(neighbour);
            send++;
            puissance *= 2;
        }
    }

    private boolean todd() throws IOException {
        System.out.println("t impaire");
        Socket best = null;
        int bestValue = -1;
        while (true) {
            try {
                serverSocket.setSoTimeout(1000);
                Socket socket = serverSocket.accept();
                doNothing = 0;
                int tempValue = socket.getInputStream().read();
                System.out.println("receive value " + tempValue);
                if (tempValue > bestValue) {
                    if (best != null) {
                        best.getOutputStream().write(NOT_OK);
                        best.getOutputStream().flush();
                        System.out.println("answer not ok");
                    }
                    best = socket;
                    bestValue = tempValue;
                } else {
                    socket.getOutputStream().write(NOT_OK);
                    socket.getOutputStream().flush();
                    System.out.println("answer not ok");
                }
            } catch (SocketTimeoutException ex) {
                doNothing++;
                if(doNothing == 10){
                    System.out.println("DO NOTHING" + id);
                    return false;
                }
                break;
            }
        }
        if (best != null) {
            if (bestValue > id) {
                candidat = false;
                owner = bestValue;
                System.out.println("loose turn");
                best.getOutputStream().write(OK);
                best.getOutputStream().flush();
                System.out.println("answer ok");
            } else {
                System.out.println("win turn");
                candidat = true;
                best.getOutputStream().write(NOT_OK);
                best.getOutputStream().flush();
                System.out.println("answer not ok");
            }
        }
        return true;
    }

    private boolean teven() throws IOException {
        System.out.println("t paire");
        for (int i = 0; i < connected.size(); i++) {
            Socket client = connected.get(i);
            int data = client.getInputStream().read();
            if (data == OK) {
                System.out.println("receive ok");
                receive++;
            } else {
                System.out.println("receive not ok");
            }
            client.close();
        }
        if (tried.isEmpty()) {
            return false;
        }
        connected.clear();
        if (candidat) {
            if (send > receive) {
                candidat = false;
                System.out.println("loose turn because " + send + " < " + receive);
                send = 0;
                receive = 0;
            } else {
                send = 0;
                receive = 0;
                System.out.println("win turn because " + send + " >= " + receive);
                System.out.println("puissance " + puissance);
                for (int i = 0; i < puissance && !tried.isEmpty(); i++) {
                    int neighbour = rand.nextInt(tried.size());
                    new Sender(id, tried.get(neighbour), connected).run();
                    tried.remove(neighbour);
                    send++;
                }
                puissance *= 2;
            }
        }
        return true;
    }

    private void loadFile(String filename) {
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String i = sc.nextLine();
                neighbors.add(i);
                tried.add(i);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
