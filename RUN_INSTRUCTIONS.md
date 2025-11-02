# How to Run the Recipe Management System

## Prerequisites Check ✅

- ✅ Java is installed (OpenJDK 25 detected)
- ⚠️ Maven needs to be installed/configured

## Quick Setup Steps

### Step 1: Install Maven

**Option A: Using Chocolatey (Recommended for Windows)**
```powershell
# Install Chocolatey first (if not installed)
# Then run:
choco install maven
```

**Option B: Manual Installation**
1. Download Maven: https://maven.apache.org/download.cgi
2. Extract to: `C:\Program Files\Apache\maven` (or your preferred location)
3. Add to PATH:
   - Open Environment Variables
   - Add: `C:\Program Files\Apache\maven\bin` to System PATH
4. Restart terminal/PowerShell

**Option C: Using Scoop**
```powershell
scoop install maven
```

### Step 2: Verify Installation

```powershell
mvn --version
```

You should see Maven version information.

### Step 3: Run the Project

**Method 1: Using the batch script**
```powershell
.\run.bat
```

**Method 2: Using Maven directly**
```powershell
mvn clean package tomcat7:run
```

**Method 3: Step by step**
```powershell
# Build the project
mvn clean package

# Run on Tomcat
mvn tomcat7:run
```

### Step 4: Access the Application

Once started, open your browser and go to:
```
http://localhost:8080/
```

## Default Login

- **Username**: `admin`
- **Password**: `admin123`

## Troubleshooting

### Port 8080 Already in Use

Edit `pom.xml` and change the port:
```xml
<port>8081</port>
```
Then access: `http://localhost:8081/`

### Maven Command Not Found

1. Ensure Maven is installed
2. Check PATH: `echo %PATH%`
3. Restart terminal after adding Maven to PATH
4. Try full path: `C:\Program Files\Apache\maven\bin\mvn.cmd --version`

### Build Errors

- Ensure Java 11+ is installed: `java -version`
- Clean Maven cache: `mvn clean`
- Delete `target` folder and rebuild

### Can't Find Dependencies

- Maven will download dependencies automatically on first run
- Check internet connection
- If behind proxy, configure Maven settings

## Alternative: Using IDE

### Eclipse
1. Import as Maven project
2. Right-click → Run As → Maven build
3. Goals: `clean package tomcat7:run`

### IntelliJ IDEA
1. Open project
2. Maven tool window → Plugins → tomcat7 → tomcat7:run

### VS Code
1. Install "Maven for Java" extension
2. Open command palette → Maven: Run Goal
3. Enter: `tomcat7:run`

## Project Structure After First Run

After running, these directories will be created:
```
data/
├── recipes.xml      # Recipe data
└── users.xml        # User data

src/main/webapp/
└── uploads/         # Uploaded images
```

