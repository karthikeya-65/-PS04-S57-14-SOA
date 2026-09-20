param(
    [Parameter(Mandatory = $true)]
    [ValidateSet("eureka", "gateway", "user", "room", "booking", "all")]
    [string]$Service,

    [string]$Profile = "dev"
)

function Start-Microservice([string]$moduleName) {
    Write-Host "Starting $moduleName on profile [$Profile]..." -ForegroundColor Green
    Start-Process cmd.exe -ArgumentList "/k", "title Starlight Stays - $moduleName && cd /d `"$PSScriptRoot`" && mvnw.bat -pl $moduleName spring-boot:run `"-Dspring-boot.run.profiles=$Profile`""
}

switch ($Service) {
    "eureka"  { Start-Microservice "eureka-server" }
    "gateway" { Start-Microservice "api-gateway" }
    "user"    { Start-Microservice "user-service" }
    "room"    { Start-Microservice "room-service" }
    "booking" { Start-Microservice "booking-service" }
    "all"     {
        Write-Host "==========================================================" -ForegroundColor Cyan
        Write-Host "  Launching Starlight Stays & Resorts Spring Boot Platform " -ForegroundColor Yellow
        Write-Host "==========================================================" -ForegroundColor Cyan
        
        Write-Host "[1/5] Launching Eureka Discovery Server (:8761)..." -ForegroundColor Yellow
        Start-Microservice "eureka-server"
        Write-Host "Waiting 14 seconds for Eureka registry initialization..." -ForegroundColor DarkGray
        Start-Sleep -Seconds 14

        Write-Host "[2/5] Launching User Service (:8081)..." -ForegroundColor Yellow
        Start-Microservice "user-service"

        Write-Host "[3/5] Launching Room Service (:8082)..." -ForegroundColor Yellow
        Start-Microservice "room-service"
        Write-Host "Waiting 6 seconds for core catalog services to register..." -ForegroundColor DarkGray
        Start-Sleep -Seconds 6

        Write-Host "[4/5] Launching Booking Service (:8083)..." -ForegroundColor Yellow
        Start-Microservice "booking-service"
        Write-Host "Waiting 6 seconds for booking engine initialization..." -ForegroundColor DarkGray
        Start-Sleep -Seconds 6

        Write-Host "[5/5] Launching API Gateway & Web App (:8080)..." -ForegroundColor Yellow
        Start-Microservice "api-gateway"
        
        Write-Host "==========================================================" -ForegroundColor Cyan
        Write-Host "All 5 Spring Boot Microservices launched in dedicated windows!" -ForegroundColor Green
        Write-Host "Web Application Portal: http://localhost:8080" -ForegroundColor Yellow
        Write-Host "Eureka Registry Dashboard: http://localhost:8761" -ForegroundColor Yellow
        Write-Host "==========================================================" -ForegroundColor Cyan
    }
}
