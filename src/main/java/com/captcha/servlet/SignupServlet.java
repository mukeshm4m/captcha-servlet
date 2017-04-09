package com.captcha.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.catcha.util.CaptchaValidator;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

	private static final long serialVersionUID = 4649039933574893350L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			System.out.println("First Name: " + request.getParameter("firstName"));
			System.out.println("Last Name: " + request.getParameter("lastName"));
			System.out.println("Email: " + request.getParameter("email"));

			String captchaResponse = request.getParameter("g-recaptcha-response");

			String responseString = "";
			response.setContentType(getServletContext().getMimeType(request.getPathInfo()));

			if (CaptchaValidator.validate(captchaResponse)) {
				responseString = "<h1 style=\"color:green\">Registered Successfully</h1>";
			} else {
				responseString = "<h1 style=\"color:red\">You can't leave Captcha empty</h1>";
			}

			byte[] buffer = responseString.getBytes();

			response.getOutputStream().write(buffer);
			
		} catch (Exception exception) {
			System.err.println(exception);
		}
	}
}
