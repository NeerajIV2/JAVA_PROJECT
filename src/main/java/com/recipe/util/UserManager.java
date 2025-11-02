package com.recipe.util;

import com.recipe.model.User;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String USERS_FILE = "users.xml";
    private static final String FILE_PATH;

    static {
        String userDir = System.getProperty("user.dir");
        FILE_PATH = userDir + File.separator + "data" + File.separator + USERS_FILE;
        initializeFile();
        createDefaultAdmin();
    }

    private static void initializeFile() {
        try {
            File file = new File(FILE_PATH);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.exists()) {
                createInitialXMLFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createInitialXMLFile(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("users");
        doc.appendChild(root);

        saveDocument(doc, file);
    }

    private static void createDefaultAdmin() {
        try {
            User admin = getUserByUsername("admin");
            if (admin == null) {
                admin = new User("admin", "admin@recipe.com", "admin123", "Administrator");
                admin.setId("ADMIN_1");
                admin.setRole("ADMIN");
                saveUser(admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getUserByUsername(String username) {
        List<User> users = getAllUsers();
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public static User getUserById(String id) {
        List<User> users = getAllUsers();
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return users;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            NodeList userNodes = doc.getElementsByTagName("user");
            for (int i = 0; i < userNodes.getLength(); i++) {
                Element userElement = (Element) userNodes.item(i);
                User user = parseUser(userElement);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void saveUser(User user) throws Exception {
        File file = new File(FILE_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc;

        if (file.exists()) {
            doc = builder.parse(file);
        } else {
            doc = builder.newDocument();
            Element root = doc.createElement("users");
            doc.appendChild(root);
        }

        Element root = doc.getDocumentElement();
        Element userElement = findOrCreateUserElement(doc, root, user.getId());

        updateUserElement(doc, userElement, user);

        saveDocument(doc, file);
    }

    private static Element findOrCreateUserElement(Document doc, Element root, String id) {
        if (id == null || id.isEmpty()) {
            id = generateUserId();
        }

        NodeList users = root.getElementsByTagName("user");
        for (int i = 0; i < users.getLength(); i++) {
            Element user = (Element) users.item(i);
            if (user.getAttribute("id").equals(id)) {
                return user;
            }
        }

        Element newUser = doc.createElement("user");
        newUser.setAttribute("id", id);
        root.appendChild(newUser);
        return newUser;
    }

    private static void updateUserElement(Document doc, Element userElement, User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(userElement.getAttribute("id"));
        } else {
            userElement.setAttribute("id", user.getId());
        }

        clearElement(userElement);

        appendTextElement(doc, userElement, "username", user.getUsername());
        appendTextElement(doc, userElement, "email", user.getEmail());
        appendTextElement(doc, userElement, "password", user.getPassword());
        appendTextElement(doc, userElement, "fullName", user.getFullName());
        appendTextElement(doc, userElement, "role", user.getRole());

        // Favorites
        Element favoritesElement = doc.createElement("favorites");
        for (String recipeId : user.getFavoriteRecipeIds()) {
            Element favElement = doc.createElement("recipeId");
            favElement.setTextContent(recipeId);
            favoritesElement.appendChild(favElement);
        }
        userElement.appendChild(favoritesElement);

        // Bookmarks
        Element bookmarksElement = doc.createElement("bookmarks");
        for (String recipeId : user.getBookmarkedRecipeIds()) {
            Element bookElement = doc.createElement("recipeId");
            bookElement.setTextContent(recipeId);
            bookmarksElement.appendChild(bookElement);
        }
        userElement.appendChild(bookmarksElement);
    }

    private static User parseUser(Element userElement) {
        User user = new User();
        user.setId(userElement.getAttribute("id"));
        user.setUsername(getTextContent(userElement, "username"));
        user.setEmail(getTextContent(userElement, "email"));
        user.setPassword(getTextContent(userElement, "password"));
        user.setFullName(getTextContent(userElement, "fullName"));
        user.setRole(getTextContent(userElement, "role"));

        // Parse favorites
        NodeList favoriteNodes = userElement.getElementsByTagName("favorites");
        if (favoriteNodes.getLength() > 0) {
            Element favoritesElement = (Element) favoriteNodes.item(0);
            NodeList recipeIds = favoritesElement.getElementsByTagName("recipeId");
            for (int i = 0; i < recipeIds.getLength(); i++) {
                user.getFavoriteRecipeIds().add(recipeIds.item(i).getTextContent());
            }
        }

        // Parse bookmarks
        NodeList bookmarkNodes = userElement.getElementsByTagName("bookmarks");
        if (bookmarkNodes.getLength() > 0) {
            Element bookmarksElement = (Element) bookmarkNodes.item(0);
            NodeList recipeIds = bookmarksElement.getElementsByTagName("recipeId");
            for (int i = 0; i < recipeIds.getLength(); i++) {
                user.getBookmarkedRecipeIds().add(recipeIds.item(i).getTextContent());
            }
        }

        return user;
    }

    private static void appendTextElement(Document doc, Element parent, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.setTextContent(textContent != null ? textContent : "");
        parent.appendChild(element);
    }

    private static String getTextContent(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }

    private static void clearElement(Element element) {
        NodeList children = element.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            element.removeChild(children.item(i));
        }
    }

    private static void saveDocument(Document doc, File file) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("indent", "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream(file));
        transformer.transform(source, result);
    }

    private static String generateUserId() {
        return "USER_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
}

