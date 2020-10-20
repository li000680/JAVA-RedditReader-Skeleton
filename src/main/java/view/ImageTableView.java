package view;
import logic.ImageLogic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.Image;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import logic.LogicFactory;
/**
 *
 * @author Wei Li
 */
@WebServlet(name = "ImageTable", urlPatterns = {"/ImageTable"})
public class ImageTableView extends HttpServlet{
 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>ImageViewNormal</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<table style=\"margin-left: auto; margin-right: auto;\" border=\"1\">");
            out.println("<caption>Image</caption>");
            //this is an example, for your other tables use getColumnNames from
            //logic to create headers in a loop.
            out.println("<tr>");
           ImageLogic logic = LogicFactory.getFor("Image");
            logic.getColumnNames().forEach(col-> out.printf("<th>%s</th>",col));
            out.println("</tr>");

            List<Image> entities = logic.getAll();
            for (Image e: entities) {
                //for other tables replace the code bellow with
                //extractDataAsList in a loop to fill the data.
                out.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                        logic.extractDataAsList(e).toArray());
            }

           out.println("<tr>");
            //this is an example, for your other tables use getColumnNames from
            //logic to create headers in a loop.
            out.println("<th>ID</th>");
            out.println("<th>BOARD_ID</th>");
            out.println("<th>TITLE</th>");
            out.println("<th>URL</th>");
            out.println("<th>LOCAL_PATH</th>");
            out.println("<th>DATE</th>");
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

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Sample of Image View Normal";
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
