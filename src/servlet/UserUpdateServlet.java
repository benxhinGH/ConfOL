package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import db.MybatisUtil;
import entity.HttpResult;
import entity.User;
import entity.UserUpdateInfo;
import util.GsonUtil;

/**
 * Servlet implementation class UserUpdateServlet
 */
@WebServlet("/user_update")
public class UserUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserUpdateServlet() {
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
		String userUpdateJson=request.getParameter("update_info");
		UserUpdateInfo info=GsonUtil.getGson().fromJson(userUpdateJson, UserUpdateInfo.class);
		SqlSession session=MybatisUtil.getSqlSession();
		HttpResult res=new HttpResult();
		if(info.getType()==UserUpdateInfo.TYPE_NICKNAME){
			String nickname=info.getValue();
			String phone=info.getUsername();
			User user=session.selectOne("selectUserByPhone",phone);
			if(user==null){
				res.setCode(-1);
				res.setMsg("user not found");
			}
			user.setNickname(nickname);
			session.update("updateUserInfo",user);
			session.commit();
			session.close();
			res.setCode(0);
			res.setMsg("update success");
			
		}else if(info.getType()==UserUpdateInfo.TYPE_PASSWORD){
			
		}
		
		
		
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(res));
		out.flush();
		out.close();
		
		
	}

}
