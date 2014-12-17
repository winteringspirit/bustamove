package MutilSocket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dùng để tạo ra các thread tương ứng với mỗi socket đáp ứng được nhiều kết nối
 * cùng một lúc Thực thi Runnable để có thể tạo 1 Thread
 * 
 * @author XuanVinh
 */

public class ServerReturn implements Runnable {
	private Socket socket;
	private Scanner input; // Tạo tham chiếu lên để nhận dữ liệu từ client
	private PrintWriter output; // Tạo tham chiếu đê gửi dữ liệu qua client
	private String message; // nội dùng cần gửi.

	/**
	 * Hàm khởi tao
	 * 
	 * @param sk
	 *            socket muốn tạo Thread
	 */
	public ServerReturn(Socket sk) {
		this.socket = sk;
	}

	/**
	 * Kiểm tra xem socket có còn đang kết nối với server ko, nếu ko thì xóa nó
	 * khỏi ds socket
	 * 
	 */
	public void CheckConnection() {
		if (!socket.isConnected()) {
			// xoa socket khỏi ds
			for (int i = 1; i <= SocketServer.arraySocket.size(); i++) {
				if (SocketServer.arraySocket.get(i) == this.socket) {
					SocketServer.arraySocket.remove(i);
				}
			}

			// thông báo các client khác biết
			for (int i = 1; i <= SocketServer.arraySocket.size(); i++) {
				Socket tempSocket = (Socket) SocketServer.arraySocket.get(i);
				try {
					PrintWriter tempOut = new PrintWriter(
							tempSocket.getOutputStream());
					tempOut.println(tempSocket.getLocalAddress().getHostName()
							+ " disconnect!");
					tempOut.flush();

					System.out.println(tempSocket.getLocalAddress()
							.getHostName() + " disconnect!");
				} catch (IOException ex) {
					Logger.getLogger(ServerReturn.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}

	}

	/**
	 * Thêm vào danh sách các user yêu cầu chat với nhau
	 */
	private void addUserRequire(String message) {
		if (message.contains("user!")) {
			ArrayList<String> arrStr = new ArrayList<String>();
			String temp1 = message.substring(5);
			temp1 = temp1.replace("[", "");
			temp1 = temp1.replace("]", "");

			String[] currentUser = temp1.split(", ");
			for (int i = 0; i < currentUser.length; i++)
				arrStr.add(currentUser[i]);

			if (arrStr.size() != 0)
				SocketServer.arryUsersRequite.add(arrStr);
		}
	}

	private String getIDPlayer(String message) {
		ArrayList<String> arrStr = new ArrayList<String>();
		String temp1 = message.substring(7);
		temp1 = temp1.replace("[", "");
		temp1 = temp1.replace("]", "");

		String[] currentData = temp1.split(", ");
		for (int i = 0; i < currentData.length; i++)
			arrStr.add(currentData[i]);
		return arrStr.get(0);
	}

	private String changeIDPlayer(String message, String idPlayer) {
		ArrayList<String> arrStr = new ArrayList<String>();
		String temp1 = message.substring(7);
		temp1 = temp1.replace("[", "");
		temp1 = temp1.replace("]", "");

		String[] currentData = temp1.split(", ");
		for (int i = 0; i < currentData.length; i++)
			arrStr.add(currentData[i]);

		arrStr.set(0, idPlayer);
		return arrStr.toString();
	}

	@Override
	public void run() {
		try {
			try {
				// tạo 2 tham chiểu tới client để truyền message
				input = new Scanner(socket.getInputStream());
				output = new PrintWriter(socket.getOutputStream());

				while (true) {
					CheckConnection();
					// Vòng lặp sẽ chờ ở đây để nhận thông điệp
					if (!input.hasNext()) {
						return;
					}
					message = input.nextLine();

					String idCurrent = this.getIDPlayer(message);

					System.out.println("Client Say: " + message);
					// nói cho tất cả client biết message này

					for (int i = 0; i < SocketServer.arrayUsers.size(); i++) {

						if (!SocketServer.arrayUsers.get(i).equals(idCurrent)) {
							Socket tempSocket = (Socket) SocketServer.arraySocket
									.get(i);
							PrintWriter tempOut = new PrintWriter(
									tempSocket.getOutputStream());
							String messageNew = this.changeIDPlayer(message,
									SocketServer.arrayUsers.get(i));

							if (message.contains("keyHold")) // "keyHold" ký
																// hiện để nhận
																// dạng message
																// này là sự
																// kiện nhấn giữ
																// phím
							{
								tempOut.println("keyHold" + messageNew);
							} else if (message.contains("Release"))// "keyRelease"
																	// ký hiện
																	// để nhận
																	// dạng
																	// messga
																	// này là sự
																	// kiện thả
																	// phím
							{
								tempOut.println("Release" + messageNew);
							}

							tempOut.flush();
							System.out.println("Send to : "
									+ tempSocket.getLocalAddress()
											.getHostName()
									+ SocketServer.arrayUsers.get(i));
						}
					}
				}

			} finally {
				socket.close(); // đóng kết nối.
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}
