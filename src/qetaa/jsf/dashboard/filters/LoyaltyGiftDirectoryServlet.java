package qetaa.jsf.dashboard.filters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import qetaa.jsf.dashboard.helpers.AppConstants;

@WebServlet("/images/gifts/*")
public class LoyaltyGiftDirectoryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    String filename = request.getPathInfo().substring(1);
	        File file = new File(AppConstants.getGiftsDirectory(), filename);
	        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
	        response.setHeader("Content-Length", String.valueOf(file.length()));
	        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
	        Files.copy(file.toPath(), response.getOutputStream());
	    }
}
