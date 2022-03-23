package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import db.DB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.lang.Process;
import java.lang.Runtime;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 *
 * @author v.druzhinin
 */

public class SignUpServlet extends HttpServlet {
    private final AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    //sign up
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");

        /*if (login == null || pass == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }*/

        accountService.addNewUser(new UserProfile(login, pass, login));
        DB db = new DB();
        db.addUser(login, pass, login);

        response.setContentType("text/html;charset=utf-8");
        //if(accountService.getUserByLogin(login) != null)
        response.getWriter().println("User " + login + " added!");
        response.getWriter().println("<a href=\"/\">Back to start page</a>");
//        Process process = Runtime.getRuntime().exec("ip -4 a");
        Process process = Runtime.getRuntime().exec("hostname -i | awk '{print $3}'");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            response.getWriter().println(line);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

}

