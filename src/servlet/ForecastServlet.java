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
import entity.ConfForecast;
import entity.HttpResult;

/**
 * Servlet implementation class ForecastServlet
 */
@WebServlet("/conf_forecast")
public class ForecastServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForecastServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		SqlSession session=MybatisUtil.getSqlSession();
		List<ConfForecast> list=session.selectList("selectAllForecast");
		HttpResult<List<ConfForecast>> res=new HttpResult<List<ConfForecast>>();
		res.setCode(0);
		res.setMsg("query success");
		res.setResult(list);
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
		String title=request.getParameter("title");
		String password=request.getParameter("password");
		String channelId=request.getParameter("channel_id");
		int capacity=Integer.valueOf(request.getParameter("capacity"));
		String creator=request.getParameter("creator");
		boolean hasfile=Boolean.valueOf(request.getParameter("hasfile"));
		long startTime=Long.valueOf(request.getParameter("start_time"));
		
		ConfForecast forecast=new ConfForecast();
		forecast.setTitle(title);
		forecast.setPassword(password);
		forecast.setChannelId(channelId);
		forecast.setCapacity(capacity);
		forecast.setCreator(creator);
		forecast.setHasfile(hasfile);
		forecast.setStartTime(startTime);
		
		SqlSession session=MybatisUtil.getSqlSession();
		HttpResult res=new HttpResult();
		
		session.insert("insertForecast", forecast);
		session.commit();
		session.close();
		res.setCode(0);
		res.setMsg("create forecast success");
		
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(res));
		out.flush();
		out.close();
		
		
	}

}
