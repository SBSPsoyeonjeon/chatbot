package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class JavaClient {

    public static void sendNode(int node){
        System.out.println("Run Client");

        Socket socket = null;
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;

        int notification;
        String msg = "";
        String rcv = "";
        //Scanner scanIn = new Scanner(System.in);
        int counter=0;

        try {
            //while (true){
                socket = new Socket("192.168.1.26", 8080);
                System.out.println("Connected...");

                ObjectOutputStream outputStream;
                ObjectInputStream inputStream;
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());

                //rcv =(String)inputStream.readObject();
                //System.out.println(rcv);
/*
                if(rcv.equals("stop") && (counter>0)){
                    //rcv="";
                    //break;
                }*/
                /*System.out.println("message to send: ");*/
                //node = scanIn.nextInt();
                //notification = scanIn.nextInt();

                switch(node){
                    case 1: msg="1"; break;
                    case 2: msg="2"; break;
                    case 3: msg="3"; break;
                    case 4: msg="4"; break;
                    case 5: msg="5"; break;
                    case 6: msg="6"; break;
                    case 7: msg="7"; break;
                    case 8: msg="8"; break;
                    case 9: msg="9"; break;
                    case 10: msg="10"; break;
                    default: msg="11"; break;
                }

                msg+="-2";
/*
                if(notification==1){
                    msg += "1";
                }else if(notification==2){
                    msg += "2";
                }else{
                    msg += "0";
                }
*/
                outputStream.writeObject(msg);
                outputStream.flush();
                //counter++;
            //}
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       // scanIn.close();
    }

        
/*
        if(bufferedReader != null){
            try {
                bufferedReader.close();
                System.out.println("bufferedReader closed");
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
            
        if(printWriter != null){
            printWriter.close();
            System.out.println("printWriter closed");
        }
            
        if(socket != null){
            try {
                socket.close();
                System.out.println("socket closed");
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
        */
}