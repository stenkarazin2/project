package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import db.DB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

public class SignInServlet extends HttpServlet {
    private final AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    //sign in
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");

        response.setContentType("text/html;charset=utf-8");
        DB db = new DB();
        if(db.userFound(login, pass, login)) {
            response.getWriter().println("Authenticated: " + login);
            response.getWriter().println("\n<a href=\"/\">Back to start page</a>");
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            response.getWriter().println("Unauthenticated");
            response.getWriter().println("\n<a href=\"/\">Back to start page</a>");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}

