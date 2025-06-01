#Función de inicio y carga de base de datos
function Init_Database {
    param ($ScenarioPath)
    Write-Host "Creando base de datos Wikipedia..." -ForegroundColor Green
    Write-Host "Cargando datos para prueba de rendimiento..." -ForegroundColor Green
    java -Xmx8G "-Dlog4j.configuration=log4j.properties" -cp "$(.\classpath.ps1);build" com.oltpbenchmark.multitenancy.MuTeBench --scenario $ScenarioPath
}

#Función de ejecución de las prueba
function Exec_Sample {
    param ($SampleNumber, $ScenarioPath, $OutputDir, $AnalysisBuckets)
    Write-Host "Ejecutando iteracion $SampleNumber..." -ForegroundColor Green
    java -Xmx8G "-Dlog4j.configuration=log4j.properties" -cp "$(.\classpath.ps1);build" com.oltpbenchmark.multitenancy.MuTeBench --scenario $ScenarioPath -o $OutputDir -histograms --analysis-buckets $AnalysisBuckets
}

#Definición de variables globales
$Prueba = "Constant Load Execution"
$ExecPath = Split-Path -Parent $MyInvocation.MyCommand.Definition
$NumIterations = 5
$AnalysisBuckets = 60
$WaitTime = 30
$ScenarioPath = "$ExecPath\init_db\scenario.xml"
$ScenarioPath2 = "$ExecPath\exec\scenario.xml"
Set-Location ..\..

#Ejecución del benchmark
Write-Host "Inicio de prueba con MuTeBench - $Prueba" -ForegroundColor Blue

#Init_Database -ScenarioPath $ScenarioPath
Write-Host "Base de datos cargada correctamente." -ForegroundColor Cyan

for ($i = 1; $i -le $NumIterations; $i++) {
    $OutputDir = "$ExecPath\exec\results$i\results$i"

    Exec_Sample -SampleNumber $i -ScenarioPath $ScenarioPath2 -OutputDir $OutputDir -AnalysisBuckets $AnalysisBuckets
    
    if ($i -lt $NumIterations) {
        Write-Host "Esperando $WaitTime segundos entre ejecuciones..." -ForegroundColor Yellow
        Start-Sleep -Seconds $WaitTime
    }
}

Write-Host "Benchmark completado - $Prueba" -ForegroundColor Blue
