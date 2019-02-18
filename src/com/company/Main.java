package com.company;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    static final String path = System.getProperty("user.dir"); // her får man c:/User/Thom/...
    public static void main(String[] args) {

        System.out.println(path);
        try {
            ServerSocket serverSocket = new ServerSocket(1337);
            while (true) {
                System.out.println("Afventer forbindelse...");
                Socket socket = serverSocket.accept(); // den blokerer
                System.out.println("forbindelse oprettet");
                serviceTheClient(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void serviceTheClient(Socket socket) {
        try {
            Scanner fromClient = new Scanner(socket.getInputStream());
            System.out.println("fra Client");
            String fromClientString = fromClient.nextLine();
            System.out.println(fromClientString);
            StringTokenizer st = new StringTokenizer(fromClientString);
            System.out.println(fromClientString);

            System.out.println("token");
            System.out.println(st.nextToken());
            System.out.println("stop token");

            String tokenString = st.nextToken();
            String fin = tokenString.substring(1,tokenString.length());
            System.out.println(fin);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());


            if (fin.equals("index.html")) {
                String pathIndex = path + "/index.html";
                File file = new File(pathIndex);
                SearchFile(dataOutputStream, file);
            }
            else if (fin.equals("about.html")){
                String pathIndex = path + "/about.html";
                File file = new File(pathIndex);
                SearchFile(dataOutputStream, file);
            }
            else if (fin.equals("products.html")) {
                String pathIndex = path + "/products.html";
                File file = new File(pathIndex);
                SearchFile(dataOutputStream, file);

            }else{
                String pathIndex = path + "/error.html";
                File file = new File(pathIndex);
                FileFound(dataOutputStream, file);
            }


        } catch (Exception e) {
            System.out.println(e);


        }

    }

    private static void SearchFile(DataOutputStream dataOutputStream, File file) throws IOException {
        if (!file.isFile()) {
            System.out.println("filen ikke fundet");
        } else {
            // send svar til client(browser):
            System.out.println("filen fundet");

            FileFound(dataOutputStream, file);
        }
    }

    private static void FileFound(DataOutputStream dataOutputStream, File file) throws IOException {
        int length = (int) file.length();
        byte[] byteArr = new byte[length];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(byteArr);
        fileInputStream.close();

        dataOutputStream.writeBytes("/HTTP//1.0 200 Her kommer Data\r\n");
        dataOutputStream.writeBytes("Content-Length: " + length + "\r\n");
        dataOutputStream.writeBytes("\r\n"); // vigtig: denne adskiller header fra indhold

        dataOutputStream.write(byteArr, 0, length);
        dataOutputStream.writeBytes("\n"); // check om det er nødvendigt
    }
}