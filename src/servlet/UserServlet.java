package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import util.GsonUtil;
import db.MybatisUtil;
import entity.HttpResult;
import entity.User;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String phone=request.getParameter("username");
		SqlSession session=MybatisUtil.getSqlSession();
		HttpResult<User> res=new HttpResult<User>();
		
		User user=session.selectOne("selectUserByPhone",phone);
		if(user==null){
			res.setCode(-1);
			res.setMsg("user not found");
		}else{
			res.setCode(0);
			res.setMsg("query success");
			res.setResult(user);
		}
		session.close();
		
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(res));
		out.flush();
		out.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
