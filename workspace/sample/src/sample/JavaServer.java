package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class JavaServer {
	//int minIndex;
	//public static Socket socket = null;
	public static void main(String srgs[])
    {   
        ServerSocket serverSocket = null;
        Socket socket = null;
        BufferedReader bufferedReader = null;
        PrintStream printStream = null;
        
        String[] array = null;
        int[] intArray = new int[6];
        
        int node[][] = {
				{-95,-90,-83,-93,-90,-92},
				{-95,-93,-79,-91,-91,-93},
				{-95,-90,-86,-86,-89,-94},
				{-95,-92,-92,-82,-89,-93},
				{-95,-93,-93,-85,-88,-93},
				{-96,-81,-86,-90,-90,-94},
				{-95,-79,-86,-92,-89,-93},
				{-93,-84,-88,-88,-89,-90},
				{-92,-92,-91,-85,-80,-85},
				{-95,-92,-93,-90,-79,-88},
				{-83,-87,-92,-95,-91,-91},
				{-86,-88,-93,-96,-88,-90},
				{-87,-89,-91,-94,-85,-84},
				{-90,-94,-95,-95,-89,-84},
				{-89,-94,-94,-94,-86,-81},
				{-91,-93,-94,-95,-90,-88}
		};
		
		//int diff[][] = new int[16][6];
		//int tdiff[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		//int minIndex;
        try{
            //serverSocket = new ServerSocket(0);
        	serverSocket = new ServerSocket(55547);
            System.out.println("I'm waiting here: " 
                + serverSocket.getLocalPort());            
                                
            socket = serverSocket.accept();
            System.out.println("from " + 
                socket.getInetAddress() + ":" + socket.getPort());
            
            InputStreamReader inputStreamReader = 
                new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            
            String line;
            
            while((line=bufferedReader.readLine()) != null){
                array = line.replaceAll("null", "").split(",");
                dumpArray(array);
                int diff[][] = new int[16][6];		//16 node, 6 beacon signal value
        		int tdiff[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //total diffrent, each node value
        		int minIndex;	//smallesst tdiff value, closest node
                for(int i = 0;i < array.length;i++)
                {
                   intArray[i] = Integer.parseInt(array[i]);
                }
                
                for(int i=0;i<16;i++){
                	tdiff[i]=0;
    				for(int j=0;j<6;j++){
    					int temp = node[i][j] - intArray[j];
    					diff[i][j] = (int) Math.pow(temp, 2);
    					tdiff[i] += diff[i][j];
    				}
    			}
                minIndex = 0;
    		    for (int i = 0; i < 16; i++) {
    		        int newnumber = tdiff[i];
    		        if ((newnumber < tdiff[minIndex])) {
    		            minIndex = i;
    		        }
    		    }
    		    System.out.println(minIndex+1 +" is the closest node");
    		    //client.sendNode();
    		    //database.storeData();
    		    //database.getData();
    		    if(minIndex<2){
        		    JavaClient.sendNode(1);
    		    }else if(minIndex<3){
        		    JavaClient.sendNode(7);
    		    }else if(minIndex<5){
        		    JavaClient.sendNode(4);
    		    }else if(minIndex<7){
        		    JavaClient.sendNode(2);
    		    }else if(minIndex<8){
        		    JavaClient.sendNode(8);
    		    }else if(minIndex<10){
        		    JavaClient.sendNode(5);
    		    }else if(minIndex<12){
        		    JavaClient.sendNode(3);
    		    }else if(minIndex<13){
        		    JavaClient.sendNode(9);
    		    }else if(minIndex<15){
        		    JavaClient.sendNode(6);
    		    }else {
    		    	JavaClient.sendNode(10);
    		    }
            }
        }catch(IOException e){
            System.out.println(e.toString());
        }finally{
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    System.out.print(ex.toString());
                }
            }
            
            if(printStream!=null){
                printStream.close();
            }
            
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.out.print(ex.toString());
                }
            }
        }
        
    }
	public static void dumpArray(String[] array) {
	    for (int i = 0; i < array.length; i++)
	      System.out.format("array[%d] = %s%n", i, array[i]);
	  }
}
