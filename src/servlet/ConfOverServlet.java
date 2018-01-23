package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import util.GsonUtil;
import db.MybatisUtil;
import entity.ConfIng;
import entity.ConfOver;
import entity.HttpResult;

/**
 * Servlet implementation class ConfOverServlet
 */
@WebServlet("/conf_over")
public class ConfOverServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfOverServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String selectType=request.getParameter("selectType");
		SqlSession session=MybatisUtil.getSqlSession();
		
		HttpResult<List<ConfOver>> httpRes=new HttpResult<List<ConfOver>>();
		List<ConfOver> dbRes = null;
		switch(selectType){
		case "all":
			dbRes=session.selectList("selectAllConfOver");
			break;
		default:
			break;
		}
		session.commit();
		session.close();
		
		if(dbRes!=null){
			httpRes.setCode(0);
			httpRes.setMsg("query success");
			httpRes.setResult(dbRes);
		}else{
			httpRes.setCode(-1);
			httpRes.setMsg("query error");
		}
		
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(httpRes));
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
