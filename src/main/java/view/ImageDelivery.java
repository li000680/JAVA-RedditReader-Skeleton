package view;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author Wei Li
 */
@WebServlet(name = "Image", urlPatterns = {"/image/*"})
public class ImageDelivery extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        File file = new File(System.getProperty("user.home") + "/My Documents/RedditImages", req.getPathInfo());
        resp.setHeader("Content-Type", getServletContext().getMimeType(req.getPathInfo()));
        resp.setHeader("Content-Length", String.valueOf(file.length()));
        resp.setHeader("Content-Disposition", "inline; filename=\"" + req.getPathInfo() + "\"");
        try {
            Files.copy(file.toPath(), resp.getOutputStream());
        } catch (FileAlreadyExistsException e) {
            //destination file already exists
        } catch (IOException e) {
            //something else went wrong
            e.printStackTrace();
        }

    }
}
