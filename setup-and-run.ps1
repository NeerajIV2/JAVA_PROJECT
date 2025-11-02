# Recipe Management System - Setup and Run Script
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Recipe Management System Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check Java
Write-Host "Checking Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "✓ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Java not found! Please install Java 11 or higher." -ForegroundColor Red
    exit 1
}

# Check Maven
Write-Host "Checking Maven..." -ForegroundColor Yellow
$mavenFound = $false

# Check if mvn is in PATH
try {
    $mvnVersion = mvn --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Maven found in PATH" -ForegroundColor Green
        Write-Host $mvnVersion -ForegroundColor Gray
        $mavenFound = $true
    }
} catch {
    # Maven not in PATH, continue checking
}

# Check common installation locations
if (-not $mavenFound) {
    $mavenPaths = @(
        "$env:ProgramFiles\Apache\maven",
        "$env:ProgramFiles(x86)\Apache\maven",
        "$env:USERPROFILE\maven",
        "$env:LOCALAPPDATA\Programs\maven",
        "C:\apache-maven"
    )
    
    foreach ($path in $mavenPaths) {
        if (Test-Path "$path\bin\mvn.cmd") {
            Write-Host "✓ Maven found at: $path" -ForegroundColor Green
            $env:PATH = "$path\bin;$env:PATH"
            $mavenFound = $true
            break
        }
    }
}

if (-not $mavenFound) {
    Write-Host ""
    Write-Host "✗ Maven not found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Would you like to install Maven?" -ForegroundColor Yellow
    Write-Host "1. Install via Chocolatey (requires admin)" -ForegroundColor Cyan
    Write-Host "2. Manual installation instructions" -ForegroundColor Cyan
    Write-Host "3. Skip (install Maven manually)" -ForegroundColor Cyan
    Write-Host ""
    $choice = Read-Host "Enter choice (1-3)"
    
    if ($choice -eq "1") {
        Write-Host "Attempting to install Maven via Chocolatey..." -ForegroundColor Yellow
        try {
            choco install maven -y
            if ($LASTEXITCODE -eq 0) {
                Write-Host "✓ Maven installed successfully!" -ForegroundColor Green
                # Refresh PATH
                $env:PATH = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")
                $mavenFound = $true
            }
        } catch {
            Write-Host "✗ Chocolatey installation failed. Please install Maven manually." -ForegroundColor Red
            Write-Host "Download from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
            exit 1
        }
    } elseif ($choice -eq "2") {
        Write-Host ""
        Write-Host "Manual Installation Steps:" -ForegroundColor Yellow
        Write-Host "1. Download Maven: https://maven.apache.org/download.cgi" -ForegroundColor Cyan
        Write-Host "2. Extract to: C:\Program Files\Apache\maven" -ForegroundColor Cyan
        Write-Host "3. Add to PATH: C:\Program Files\Apache\maven\bin" -ForegroundColor Cyan
        Write-Host "4. Restart PowerShell and run this script again" -ForegroundColor Cyan
        exit 1
    } else {
        Write-Host "Please install Maven and run this script again." -ForegroundColor Yellow
        exit 1
    }
}

# Build and Run
Write-Host ""
Write-Host "Building project..." -ForegroundColor Yellow
mvn clean package

if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Build failed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "✓ Build successful!" -ForegroundColor Green
Write-Host ""
Write-Host "Starting Tomcat server..." -ForegroundColor Yellow
Write-Host "Application will be available at: http://localhost:8080/" -ForegroundColor Cyan
Write-Host ""
Write-Host "Default login:" -ForegroundColor Cyan
Write-Host "  Username: admin" -ForegroundColor White
Write-Host "  Password: admin123" -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop the server" -ForegroundColor Yellow
Write-Host ""

mvn tomcat7:run

