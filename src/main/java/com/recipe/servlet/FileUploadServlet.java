package com.recipe.servlet;

import com.recipe.model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileUploadServlet extends HttpServlet {
    private static final String UPLOAD_DIRECTORY = "uploads";
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int MAX_REQUEST_SIZE = 10 * 1024 * 1024; // 10MB

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // String recipeId = request.getParameter("recipeId");
        
        // Check if request contains multipart content
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request is not multipart");
            return;
        }

        // Configure upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(4096);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        try {
            String uploadPath = getUploadPath();
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            List<FileItem> formItems = upload.parseRequest(request);
            String fileName = null;

            if (formItems != null && formItems.size() > 0) {
                for (FileItem item : formItems) {
                    if (!item.isFormField() && item.getFieldName().equals("photo")) {
                        fileName = System.currentTimeMillis() + "_" + 
                                new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        item.write(storeFile);
                        
                        // Return the relative path
                        String relativePath = UPLOAD_DIRECTORY + "/" + fileName;
                        response.setContentType("application/json");
                        response.getWriter().write("{\"success\": true, \"path\": \"" + relativePath + "\"}");
                        return;
                    }
                }
            }

            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"No file uploaded\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"" + 
                    e.getMessage() + "\"}");
        }
    }

    private String getUploadPath() {
        String userDir = System.getProperty("user.dir");
        return userDir + File.separator + "src" + File.separator + "main" + 
                File.separator + "webapp" + File.separator + UPLOAD_DIRECTORY;
    }
}

