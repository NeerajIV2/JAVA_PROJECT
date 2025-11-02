# Quick Start Guide - Running the Recipe Management System

## Option 1: Using Maven (Recommended)

### Install Maven (if not installed):
1. Download Maven from: https://maven.apache.org/download.cgi
2. Extract and add to PATH
3. Or use Chocolatey: `choco install maven`

### Run the project:
```bash
# Build and run
mvn clean package
mvn tomcat7:run

# Or in one command:
mvn clean package tomcat7:run
```

The application will be available at: **http://localhost:8080/**

---

## Option 2: Using Eclipse IDE

1. Open Eclipse
2. File → Import → Maven → Existing Maven Projects
3. Select the project directory
4. Right-click project → Run As → Maven build
5. Goals: `clean package tomcat7:run`
6. Click Run

---

## Option 3: Using IntelliJ IDEA

1. Open IntelliJ IDEA
2. File → Open → Select project directory
3. Wait for Maven to sync
4. Open Maven tool window (View → Tool Windows → Maven)
5. Expand: recipe-management-system → Plugins → tomcat7 → tomcat7:run
6. Double-click to run

---

## Option 4: Manual Deployment to Tomcat

1. Build WAR file:
   ```bash
   mvn clean package
   ```

2. Copy WAR to Tomcat:
   ```bash
   copy target\recipe-management-system.war C:\path\to\tomcat\webapps\
   ```

3. Start Tomcat:
   ```bash
   C:\path\to\tomcat\bin\startup.bat
   ```

4. Access: http://localhost:8080/recipe-management-system/

---

## Default Login Credentials

- **Username**: `admin`
- **Password**: `admin123`

---

## Troubleshooting

### Port 8080 already in use?
Change port in `pom.xml`:
```xml
<port>8081</port>
```

### Maven not found?
- Install Maven or use Maven Wrapper
- Check PATH environment variable

### Build errors?
- Ensure Java 11+ is installed
- Check `java -version` and `javac -version`

