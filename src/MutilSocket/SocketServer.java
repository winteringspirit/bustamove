package MutilSocket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

/**
 * Tạo một server để nhận các kết nối từng client
 * @author XuanVinh
 */

public class SocketServer {
	
	 //Lấy danh sách các socket đang kết nối với server
    public static ArrayList<Socket> arraySocket = new ArrayList<Socket>();
    //Lấy danh sách các user ứng với từng socket
    public static ArrayList<String> arrayUsers = new ArrayList<String>();
    
    //Lấy danh sách các user ứng với từng socket
    public static ArrayList<ArrayList<String>> arryUsersRequite = new ArrayList<ArrayList<String>>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            final int PORT = 999;
            // Tạo một server với số port = 999
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Waiting for clients..... connect.");
            
            // vòng lập chờ nhận các client đên kết nối.
            while(true)
            {
                // Lắng nghe và bắt các kết nối từ các client gửi tới.
                Socket socket = server.accept();
                //add vào danh sách socket
                arraySocket.add(socket);
                
                System.out.println("Clients connected from : " + socket.getLocalAddress().getHostName());
                //Thêm tên của client vào ds
                AddUserName(socket);               
                
                ServerReturn severreturn = new ServerReturn(socket);
                Thread X = new Thread(severreturn);
                X.start();
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
               
    }
    /**
     * Thêm một tên của một user hay client kết nối với server
     * @param sk Socket đang kết nối
     */
    public static void AddUserName(Socket sk)
    {
        try 
        {
            //Tạo tham chiếu nhận dữ liệu đến client 
            Scanner input = new Scanner(sk.getInputStream());
            //Chờ nhận dữ liệu(message bên client)
            String userName = input.nextLine();
            arrayUsers.add(userName);
            
            //Gửi danh sách tên userName này cho các client đang kết nối với server
            for(int i = 1; i <= SocketServer.arraySocket.size(); i++)
            {
                Socket tempSocket = (Socket)arraySocket.get(i - 1);
                //Tạo tham chiếu để gủi tới client
                PrintWriter out = new PrintWriter(tempSocket.getOutputStream());
                //phương thức để gửi
                out.println("00" + arrayUsers);
                //Gửi qua
                out.flush();
            }
        } 
        catch (IOException ex)
        {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
