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
import su.usatu.project23.model.User;
import su.usatu.project23.util.JsonResponseUtil;

@WebServlet("get_user_info")

public class UserGetter extends HttpServlet {

	private Project23DAO dao;

	public UserGetter() {
		dao = new Project23DAOImplementation();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;
		
		String apiKey = request.getParameter("token");

		User user = dao.getUserByToken(apiKey, "users");
		
		if (user.getId() == 1) {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "User Not Found");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Status code 404
		} else {
			jsonOutput = JsonResponseUtil.formJsonResponse("success", "User found", user);
		}


		out.println(jsonOutput);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
