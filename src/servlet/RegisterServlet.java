package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import util.AuthcodeHelper;
import util.GsonUtil;
import db.MybatisUtil;
import entity.HttpResult;
import entity.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
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
		String phonenumber=request.getParameter("phonenumber");
		String authcode=request.getParameter("authcode");
		String password=request.getParameter("password");
		SqlSession session=MybatisUtil.getSqlSession();
		User user=session.selectOne("selectUserByPhone", phonenumber);
		HttpResult result=new HttpResult();
		if(user==null){
			boolean auth=AuthcodeServlet.authcodeHelper.verifyAuthcode(phonenumber, authcode);
			if(!auth){
				result.setCode(-2);
				result.setMsg("authcode error");
			}else{
				session.insert("insertUser", new User(phonenumber,password));
				session.commit();
				session.close();
				result.setCode(0);
				result.setMsg("register success");
			}
			
			
		}else{
			result.setCode(-1);
			result.setMsg("phonenumber exist");
		}
		
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(result));
		out.flush();
		out.close();     
		
	}

}
