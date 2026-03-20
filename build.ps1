# PowerShell wrapper para Maven embebido en el proyecto
# Uso: .\build.ps1 test
# .\build.ps1 exec:java
# .\build.ps1 clean compile

param(
    [string[]]$MavenArgs = @("--version")
)

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$mavenHome = Join-Path $projectRoot "tools\apache-maven-3.9.6"
$mavenBin = Join-Path $mavenHome "bin\mvn.cmd"

if (-not (Test-Path $mavenBin)) {
    Write-Error "Maven no encontrado en: $mavenBin"
    exit 1
}

# Ejecutar Maven con los argumentos
& $mavenBin @MavenArgs

