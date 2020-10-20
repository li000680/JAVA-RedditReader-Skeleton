package view;
import logic. BoardLogic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity. Board;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import logic.LogicFactory;


/**
 *
 * @author Wei Li
 */
@WebServlet(name = "BoardTable", urlPatterns = {"/BoardTable"})
public class BoardTableView extends HttpServlet{
protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>BoardViewNormal</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<table style=\"margin-left: auto; margin-right: auto;\" border=\"1\">");
            out.println("<caption>Board</caption>");
            
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>HostID</th>");
            out.println("<th>Url</th>");
            out.println("<th>Name</th>");
            out.println("</tr>");

            BoardLogic logic = LogicFactory.getFor("Board");
            List<Board> entities = logic.getAll();
            for (Board e : entities) {
                out.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                logic.extractDataAsList(e).toArray());
            }
            out.println("<tr>");
            
            out.println("<th>ID</th>");
            out.println("<th>HOST_ID</th>");
            out.println("<th>URL</th>");
            out.println("<th>NAME</th>");
            out.println("</tr>");
            out.println("</table>");
            out.printf("<div style=\"text-align: center;\"><pre>%s</pre></div>", toStringMap(request.getParameterMap()));
            out.println("</body>");
            out.println("</html>");
        }
    }
    private String toStringMap(Map<String, String[]> m) {
        StringBuilder builder = new StringBuilder();
        for (String k : m.keySet()) {
            builder.append("Key=").append(k)
                    .append(", ")
                    .append("Value/s=").append(Arrays.toString(m.get(k)))
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        processRequest(request, response);
    }
    @Override
    public String getServletInfo() {
        return "Sample of Board View Normal";
    }

    private static final boolean DEBUG = true;

    @Override
    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    @Override
    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }
}
