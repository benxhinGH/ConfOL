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
import entity.HttpCallback;
import entity.HttpResult;
import entity.User;
import util.AuthcodeHelper;
import util.GsonUtil;
import util.SmsUtil;

/**
 * Servlet implementation class AuthcodeServlet
 */
@WebServlet("/authcode")
public class AuthcodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	boolean DEBUG=false;
       
	public static AuthcodeHelper authcodeHelper=new AuthcodeHelper();
	
	static{
		authcodeHelper.start();
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthcodeServlet() {
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
		
		if(DEBUG){
			HttpResult res=new HttpResult();
			res.setCode(0);
			res.setMsg("send success");
			PrintWriter out=response.getWriter();
			out.write(GsonUtil.getGson().toJson(res));
			out.flush();
			out.close();
			return;
		}
		
		
		
		final String phonenumber=request.getParameter("phonenumber");
		//检查手机号是否已被注册
		SqlSession session=MybatisUtil.getSqlSession();
		User user=session.selectOne("selectUserByPhone", phonenumber);
		if(user!=null){
			HttpResult res=new HttpResult();
			res.setCode(-2);
			res.setMsg("phone has been registered");
			PrintWriter out=response.getWriter();
			out.write(GsonUtil.getGson().toJson(res));
			out.flush();
			out.close();
			return;
		}
		
		
		
		final String authcode=authcodeHelper.generateAuthcode();
		final HttpResult result=new HttpResult();
		SmsUtil.sendMsg(phonenumber, "您的验证码为："+authcode+"，有效时常为5分钟，请尽快注册。【会议同步系统】", new HttpCallback(){

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				authcodeHelper.put(phonenumber, authcode);
				result.setCode(0);
				result.setMsg("send authcode successfully");
			}

			@Override
			public void onError(int errorcode, String errormsg) {
				// TODO Auto-generated method stub
				result.setCode(-1);
				result.setMsg("errorcode:"+errorcode+"msg:"+errormsg);
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				
			}
			
		});
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(result));
		out.flush();
		out.close();
	}
	

}
