package su.usatu.project23.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import su.usatu.project23.dao.Project23DAO;
import su.usatu.project23.dao.Project23DAOImplementation;
import su.usatu.project23.util.JsonResponseUtil;

@WebServlet("get_user_reports")

public class PDFGetter extends HttpServlet {
	
	private Project23DAO dao;
	
	public PDFGetter() {
		dao = new Project23DAOImplementation();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;
		
		int userId = Integer.parseInt(request.getParameter("ownerId")); 
	
		String[] outputArray = dao.getUserPdfFiles(userId);
		if (outputArray.length == 0) {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Docs not found");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Status code 404
		} else {
			jsonOutput = JsonResponseUtil.formJsonResponse("success", "Found", outputArray);
		}
		out.println(jsonOutput);

	}
	
}