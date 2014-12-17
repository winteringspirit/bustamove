package MutilSocket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.newdawn.slick.SlickException;

import maingame.GamePlayLayer;
import maingame.MainGame;


/**
 * Tạo một client socket để kết nói với server
 * Kế thừa Runnable để tạo ra Thread
 * @author XuanVinh
 */
public class SocketClient implements Runnable
 {
	
		private Socket socket;
	    private Scanner input; //Tạo tham chiếu để nhận message từ client
	    private PrintWriter out; // Tạo tham chiếu để gửi message qua bên client
	    private List<Integer> _HeldKeys = new ArrayList<Integer>(); // danh sách các phim được nhấn
	    /**
	     * Socket cần tạo
	     * @param sk 
	     */
	    public SocketClient(Socket sk)
	    {
	        this.socket = sk;
	    }

	    @Override
	    public void run() 
	    {
	      try
	      {
	          try
	          {
	              // tạo các tham chiếu để nhận và gửi tin
	              input = new Scanner(this.socket.getInputStream());
	              out = new PrintWriter(this.socket.getOutputStream());
	              out.flush();
	              checkStream();
	          }
	          finally
	          {
	              this.socket.close();
	          }
	          
	      }
	      catch(Exception ex)
	      {
	          System.out.println(ex.getMessage());
	      }
	              
	    }
	    /**
	     * Ngắt kết nối với 1 socket
	     */
	    public void disconnect()
	    {
	      //  out.println(FrameChat.nameUser + "has disconnected");
	        out.flush();
	        try 
	        {
	            socket.close();
	            JOptionPane.showMessageDialog(null, "You disconnected !");
	            System.exit(0);
	        }
	        catch (IOException ex) 
	        {
	            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
	   /**
	    * Kiểm tra message nhận được từ bên server gửi qua
	    */
	    public void checkStream()
	    {
	        while(true)
	        {
	            this.receive();
	        }
	    }
	    
	    private String getIDPlayer(String message)
	    {
	    	 ArrayList<String> arrStr = new ArrayList<String>();   
	         String temp1 = message.substring(7);
	             temp1 = temp1.replace("[", "");
	             temp1 = temp1.replace("]", "");
	             
	           String[] currentData = temp1.split(", ");             
	           for(int i = 0; i < currentData.length; i++)
	               arrStr.add(currentData[i]);
	    	return arrStr.get(0);
	    }
	    
	    public void receive()
	    {
	        if(input.hasNext())
	        {
	            String message = input.nextLine();
	            
	            if(message.contains("keyHold")) // "keyHold" ký hiện để nhận dạng message này là sự kiện nhấn giữ phím
	            {
	                String temp1 = message.substring(7);
	                temp1 = temp1.replace("[", "");
	                temp1 = temp1.replace("]", "");
	                
	                String[] currentData = temp1.split(", ");
	                
	                String idPlayer = currentData[0];
	                for(int j = 1; j < currentData.length; j++)
	                	this._HeldKeys.add(Integer.parseInt(currentData[j]));
	                
	                MainGame._Scene.keyHoldPlayer(idPlayer, this._HeldKeys.get(0));				  
				    
					this._HeldKeys.clear(); // remove data send	             
	            }
	            if(message.contains("Release")) // "keyRelease" ký hiện để nhận dạng messga  này là sự kiện thả phím
	            {	              
				    MainGame._Scene.keyRelease(this.getIDPlayer(message));
	            }
	            else
	            {
	         
	            }
	        }
	    }
	    /**
	     * Gửi message lên server
	     * @param x // nội dung message
	     */
	    public void send(String x)
	    {
	        out.println(x);
	        out.flush();     
	    }
	    
	    /**
	     * 
	     * @param x // nội dung message
	     */
	    public void sendUser(ArrayList<String> x)
	    {
	        out.println("keyHold" + x);
	        out.flush();     
	    }

}
