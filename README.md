# Recipe Management System

A comprehensive Recipe Management System built with Java, Servlets, JSP, and XML. This application allows users to create, manage, and share recipes with a structured XML-based storage system.

## Features

### Core Features

- **Recipe Creation & Editing**
  - Add new recipes with title, description, ingredients, preparation steps, and cooking time
  - Edit and update existing recipes
  - Support for recipe photos

- **Ingredient Management**
  - Store ingredients with quantity and measurement units
  - XML-based structured ingredient lists for easy parsing

- **Photo & Media Upload**
  - Attach recipe photos to enhance presentation
  - Support for image uploads

- **Categorization & Tags**
  - Categorize recipes (vegetarian, vegan, desserts, beverages, etc.)
  - Add tags for quick search and filtering

- **Recipe Catalog Display**
  - Display recipes in a catalog format with filters
  - Filter by cuisine type, difficulty level, preparation time, and category
  - Search bar for instant recipe lookup

- **Favorites & Bookmarking**
  - Users can bookmark favorite recipes for quick access
  - Personalized recipe collection per user

- **Export & Sharing**
  - Export recipes in XML format for backups or sharing
  - Export in plain text format
  - Print-friendly recipe view

- **Ratings & Reviews**
  - Users can rate and review recipes
  - Helps in ranking and promoting best recipes

- **Admin Panel**
  - Manage all submitted recipes
  - Approve or remove inappropriate content
  - User management
  - Dashboard with statistics

## Technology Stack

- **Backend**: Java 11
- **Web Framework**: Java Servlets
- **View Layer**: JSP (JavaServer Pages)
- **Storage**: XML files
- **Build Tool**: Maven
- **Frontend**: HTML, CSS, JavaScript

## Prerequisites

- Java JDK 11 or higher
- Maven 3.6 or higher
- Apache Tomcat 9.0 or higher (or any Servlet 4.0 compatible container)
- IDE (Eclipse, IntelliJ IDEA, or NetBeans) - Optional but recommended

## Project Structure

```
JAVA_PROJECT/
├── pom.xml                              # Maven configuration
├── README.md                            # This file
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── recipe/
│       │           ├── dao/             # Data Access Objects
│       │           ├── model/           # Entity classes (Recipe, User, etc.)
│       │           ├── servlet/         # Servlet classes
│       │           └── util/             # Utility classes (XML managers)
│       ├── webapp/
│       │   ├── WEB-INF/
│       │   │   └── web.xml             # Web application configuration
│       │   ├── css/
│       │   │   └── style.css           # Main stylesheet
│       │   ├── jsp/                    # JSP pages
│       │   ├── uploads/                # Uploaded images (created at runtime)
│       │   └── index.jsp               # Entry point
│       └── resources/                  # Resource files
└── data/                                # XML data files (created at runtime)
    ├── recipes.xml                     # Recipe storage
    └── users.xml                       # User storage
```

## Setup Instructions

### 1. Clone or Download the Project

Download the project to your local machine.

### 2. Build the Project

Open a terminal in the project root directory and run:

```bash
mvn clean package
```

This will:
- Compile all Java source files
- Package the application as a WAR file
- Output: `target/recipe-management-system.war`

### 3. Deploy to Tomcat

#### Option A: Using Tomcat Manager (Recommended)

1. Copy the WAR file to Tomcat's `webapps` directory:
   ```bash
   cp target/recipe-management-system.war /path/to/tomcat/webapps/
   ```

2. Start Tomcat server:
   ```bash
   /path/to/tomcat/bin/startup.sh  # Linux/Mac
   # or
   /path/to/tomcat/bin/startup.bat  # Windows
   ```

3. Access the application:
   ```
   http://localhost:8080/recipe-management-system/
   ```

#### Option B: Using IDE (Eclipse/IntelliJ)

1. **Eclipse**:
   - Right-click project → Run As → Run on Server
   - Select your Tomcat server
   - Click Finish

2. **IntelliJ IDEA**:
   - Run → Edit Configurations
   - Add → Tomcat Server → Local
   - Set deployment artifact
   - Apply and Run

### 4. Default Admin Credentials

The system creates a default admin user on first startup:

- **Username**: `admin`
- **Password**: `admin123`

### 5. Directory Permissions

Ensure the application has write permissions for:
- `data/` directory (for XML files)
- `src/main/webapp/uploads/` directory (for uploaded images)

## Usage Guide

### For Regular Users

1. **Register/Login**
   - Navigate to Login page
   - Register a new account or login with existing credentials

2. **Create Recipe**
   - Click "Add Recipe" in the navigation
   - Fill in recipe details, ingredients, and steps
   - Upload a photo (optional)
   - Click "Save Recipe"

3. **Browse Recipes**
   - View all recipes on the home page
   - Use search and filter options
   - Click on a recipe to view details

4. **Favorite/Bookmark**
   - While viewing a recipe, click "Favorite" or "Bookmark"
   - Access from your profile page

5. **Rate & Review**
   - View a recipe and scroll to the review section
   - Submit your rating and review

6. **Export Recipes**
   - From recipe detail page, click "Export"
   - Or export all your recipes from profile page

### For Administrators

1. **Login as Admin**
   - Use default admin credentials or your admin account

2. **Admin Dashboard**
   - View statistics and recent recipes
   - Access management options

3. **Manage Recipes**
   - Approve pending recipes
   - Edit or delete any recipe
   - View all recipes regardless of approval status

4. **Manage Users**
   - View all registered users
   - Monitor user activity

## API Endpoints

### User Management
- `GET /users?action=login` - Show login page
- `GET /users?action=register` - Show registration page
- `POST /users?action=login` - Process login
- `POST /users?action=register` - Process registration
- `GET /users?action=logout` - Logout user
- `GET /users?action=profile` - View user profile
- `POST /users?action=favorite` - Toggle favorite recipe
- `POST /users?action=bookmark` - Toggle bookmark recipe

### Recipe Management
- `GET /recipes` - List all recipes
- `GET /recipes?action=view&id={id}` - View recipe details
- `GET /recipes?action=form` - Show recipe form
- `GET /recipes?action=edit&id={id}` - Edit recipe
- `POST /recipes?action=save` - Save recipe
- `POST /recipes?action=rate` - Rate recipe
- `POST /recipes?action=review` - Submit review
- `GET /recipes?action=delete&id={id}` - Delete recipe

### Admin
- `GET /admin` - Admin dashboard
- `GET /admin?action=recipes` - Manage recipes
- `GET /admin?action=users` - Manage users
- `POST /admin?action=approve&id={id}` - Approve recipe

### Export
- `GET /export?id={id}` - Export single recipe (XML)
- `GET /export?format=text&id={id}` - Export single recipe (Text)
- `GET /export` - Export all user's recipes
- `GET /export?format=text` - Export all recipes as text

### File Upload
- `POST /upload` - Upload recipe photo

## XML Data Format

### Recipe XML Structure

```xml
<recipes>
  <recipe id="RECIPE_123">
    <title>Recipe Title</title>
    <description>Recipe description</description>
    <cuisineType>Italian</cuisineType>
    <difficultyLevel>Medium</difficultyLevel>
    <preparationTime>30</preparationTime>
    <cookingTime>45</cookingTime>
    <servings>4</servings>
    <category>Main Course</category>
    <photoPath>uploads/image.jpg</photoPath>
    <userId>USER_123</userId>
    <authorName>John Doe</authorName>
    <averageRating>4.5</averageRating>
    <totalRatings>10</totalRatings>
    <approved>true</approved>
    <createdAt>2024-01-01T12:00:00</createdAt>
    <ingredients>
      <ingredient>
        <name>Flour</name>
        <quantity>2</quantity>
        <unit>cup</unit>
      </ingredient>
    </ingredients>
    <preparationSteps>
      <step>First step instructions</step>
    </preparationSteps>
    <tags>
      <tag>spicy</tag>
      <tag>quick</tag>
    </tags>
    <reviews>
      <review id="REVIEW_123">
        <userId>USER_456</userId>
        <username>Jane Doe</username>
        <rating>5</rating>
        <comment>Great recipe!</comment>
        <createdAt>2024-01-02T10:00:00</createdAt>
      </review>
    </reviews>
  </recipe>
</recipes>
```

## Configuration

### Application Properties

The application uses XML files for data storage. File locations are automatically configured:
- Recipes: `{project_root}/data/recipes.xml`
- Users: `{project_root}/data/users.xml`
- Uploads: `{project_root}/src/main/webapp/uploads/`

### File Upload Limits

- Maximum file size: 5MB
- Maximum request size: 10MB
- Allowed types: Images only

## Troubleshooting

### Common Issues

1. **XML files not created**
   - Ensure the application has write permissions in the project directory
   - Check that the `data/` directory can be created

2. **Images not uploading**
   - Verify `uploads/` directory exists and is writable
   - Check file size (must be < 5MB)
   - Ensure image file format is supported (jpg, png, gif)

3. **Servlets not working**
   - Verify `web.xml` is correctly configured
   - Ensure all servlet dependencies are included
   - Check Tomcat server logs for errors

4. **JSP pages not rendering**
   - Ensure JSTL dependency is included in `pom.xml`
   - Check that JSP pages are in `webapp/jsp/` directory

5. **Default admin not created**
   - Delete `data/users.xml` and restart the application
   - Default admin will be recreated automatically

## Extending the Application

### Adding New Features

1. **New Model Classes**: Add to `com.recipe.model`
2. **New Servlets**: Add to `com.recipe.servlet` and register in `web.xml`
3. **New JSP Pages**: Add to `webapp/jsp/` and create corresponding servlet mappings
4. **New Utilities**: Add to `com.recipe.util`

### Database Migration

To migrate from XML to a database:
1. Create database schema
2. Implement JDBC-based DAO classes
3. Update servlets to use new DAO
4. Keep XML export functionality for backward compatibility

### Social Media Integration

The application can be extended to:
- Share recipes on social media
- Import recipes from external sources
- Integration with food blogging platforms

## Security Considerations

⚠️ **Important**: This is a development/prototype application. For production use, consider:

1. **Password Security**: Implement password hashing (BCrypt, Argon2)
2. **Session Management**: Add CSRF protection
3. **Input Validation**: Add server-side validation for all inputs
4. **File Upload Security**: Validate file types and scan for malware
5. **SQL Injection**: If migrating to database, use prepared statements
6. **XSS Protection**: Escape all user-generated content
7. **Authentication**: Implement JWT or secure session management
8. **HTTPS**: Use HTTPS in production

## License

This project is provided as-is for educational and development purposes.

## Support

For issues or questions:
1. Check the troubleshooting section
2. Review server logs
3. Verify all prerequisites are met

## Future Enhancements

Potential improvements:
- Database integration (MySQL, PostgreSQL)
- Advanced search with full-text indexing
- Recipe sharing via email
- Meal planning features
- Shopping list generation
- Mobile-responsive design improvements
- Multi-language support
- Recipe video integration
- Nutrition information tracking

---

**Happy Cooking! 🍳👨‍🍳👩‍🍳**

