/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import common.FileUtility;
import entity.Board;
import entity.Image;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import logic.BoardLogic;
import logic.ImageLogic;
import logic.LogicFactory;
import reddit.Post;
import reddit.Reddit;
import reddit.Sort;


/**
 *
 * @author Wei Li
 */
@WebServlet(name = "ImageView", urlPatterns = {"/ImageView"})
public class ImageView extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException{
        super.init(config);

        BoardLogic bLogic =  LogicFactory.getFor("Board");
        Board board = bLogic.getWithId(4);
        ImageLogic iLogic =  LogicFactory.getFor("Image");
        String path = System.getProperty("user.home")+"/My Documents/RedditImages/";
        try {
            FileUtility.createDirectory(path);
        } catch (IOException ex) {
        }
        //create a lambda that accepts post
        Consumer<Post> saveImage = (Post post) -> {
            //if post is an image and SFW    
            if(post.isImage() && (!post.isOver18()))
            {
                System.out.println(post.getUrl());
                String[] separated = post.getUrl().split("/"); 
                String fileName = separated[separated.length-1];
                String local_path = path+fileName;
                
                if ((iLogic.getImageWithLocalPath(local_path) == null)) {
                    System.out.println(local_path);
                    //create a map MAP<String,String[]> using hashmap
                    Map<String, String[]> map=new HashMap<>();
                    map.put("title", new String[]{post.getTitle()});
                    map.put("url",new String[]{post.getUrl()});
                    map.put("localPath",new String[]{local_path});  
                    Image image = iLogic.createEntity(map);
                    image.setBoard(board);   
                    iLogic.add(image);
                    FileUtility.downloadAndSaveFile(post.getUrl(), path);
                }
            }   
        };   
        //create a new scraper/
        Reddit scrap = new Reddit();
        //authenticate and set up a page for wallpaper subreddit with 10 posts soreted by HOT order
        scrap.authenticate().buildRedditPagesConfig("Wallpaper", 10, Sort.BEST);
        //get the next page 3 times and save the images.
        scrap.requestNextPage().proccessNextPage(saveImage);
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ImageView</title>");            
            out.println("</head>");
            out.println("<body>");

            ImageLogic logic = LogicFactory.getFor("Image");
            List<Image> entities = logic.getAll();
            for (Image e: entities) {
                //for other tables replace the code bellow with
                //extractDataAsList in a loop to fill the data.
                out.println("<div align=\"center\">");
                out.println("<div align=\"center\" class=\"imageContainer\">");
                out.printf("<img class=\"imageThumb\" src=\"image/%s\"/>",
                        FileUtility.getFileName(e.getUrl()));
                out.println("</div>");
                out.println("</div>");
            }
            out.println("</body>");
            out.println("</html>");
        }
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
