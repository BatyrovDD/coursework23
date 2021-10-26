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
import su.usatu.project23.model.ReportData;
import su.usatu.project23.util.JsonResponseUtil;

@WebServlet("generate_pdf")

public class PDFGenerator extends HttpServlet {

	private Project23DAO dao;

	public PDFGenerator() {
		dao = new Project23DAOImplementation();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;
		
		ReportData rd = new ReportData();
		String pdfReport;

		String token = request.getParameter("token");

		rd.width = request.getParameter("width");
		rd.height = request.getParameter("height");

		rd.sashesCount = request.getParameter("sashesCount");

		rd.glazing = request.getParameter("glazing");

		rd.totalAmount = request.getParameter("totalAmount");

		pdfReport = dao.createPdfReport(rd);

		if (pdfReport == "Failed") {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Failed to create PDF report");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Status code 500
		} else {
			jsonOutput = JsonResponseUtil.formJsonResponse("success", "OK", pdfReport);
			dao.assignPdfReportToUser(token, pdfReport);
		}

		out.println(jsonOutput);

	}

}
