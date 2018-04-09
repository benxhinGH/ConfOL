package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.HttpResult;
import tcp.ConnectionServer;
import util.GsonUtil;

/**
 * Servlet implementation class TcpServlet
 */
@WebServlet("/tcp")
public class TcpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static ConnectionServer tcpServer=new ConnectionServer();
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TcpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("请求建立tcp长连接");
		if(!tcpServer.isAlive()){
			tcpServer.start();
		}
		
		HttpResult res=new HttpResult();
		res.setCode(0);
		res.setMsg("connection allowed");
		
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(res));
		out.flush();
		out.close();
	}

}
