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
import entity.ConfFile;
import entity.HttpResult;

/**
 * Servlet implementation class ConfFileServlet
 */
@WebServlet("/conf_file")
public class ConfFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfFileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String channelId=request.getParameter("channel_id");
		SqlSession session=MybatisUtil.getSqlSession();
		HttpResult<ConfFile> res=new HttpResult<ConfFile>();
		
		ConfFile confFile=session.selectOne("selectConfFileByChannelId",channelId);
		session.close();
		res.setCode(0);
		res.setMsg("query success");
		res.setResult(confFile);
		System.out.println("查询文件信息为："+confFile.toString());
		
		response.setCharacterEncoding("utf-8");
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
