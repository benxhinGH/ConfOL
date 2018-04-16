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

import com.google.gson.Gson;

import db.MybatisUtil;
import entity.HttpResult;
import entity.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String phonenumber=request.getParameter("phonenumber");
		String password=request.getParameter("password");
		SqlSession session=MybatisUtil.getSqlSession();
		User user=session.selectOne("selectUserByPhone",phonenumber);
		session.commit();
		session.close();
		HttpResult<User> result=new HttpResult<User>();
		if(user==null){
			//用户不存在
			result.setCode(-1);
			result.setMsg("user not found");
		}else if(user.getPassword().equals(password)){
			//密码正确
			result.setCode(0);
			result.setMsg("login succeess");
			result.setResult(user);
			System.out.println(phonenumber+"登录");
		}else{
			//密码错误
			result.setCode(1);
			result.setMsg("password error");
		}
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(result));
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
