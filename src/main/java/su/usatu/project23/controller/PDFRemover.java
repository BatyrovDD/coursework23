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

@WebServlet("delete_pdf_report")

public class PDFRemover extends HttpServlet {
	
	private Project23DAO dao;
	
	public PDFRemover() {
		dao = new Project23DAOImplementation();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;
		
		String token = request.getParameter("token"); 
		String documentName = request.getParameter("documentName"); 
	
		boolean deletePdf = dao.deletePdfReport(token, documentName);
		if (!deletePdf) {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Document not found");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Status code 404
		} else {
			jsonOutput = JsonResponseUtil.formJsonResponse("success", "Document deleted sucessfully", "");
		}
		out.println(jsonOutput);

	}
	
}