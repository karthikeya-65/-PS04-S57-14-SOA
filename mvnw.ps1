param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]]$MavenArgs
)

$toolsDir = "$env:USERPROFILE\tools"
if (Test-Path $toolsDir) {
    $jdk = Get-ChildItem "$toolsDir\jdk-17*" -Directory -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($jdk -and (Test-Path "$toolsDir\apache-maven-3.9.9\bin\mvn.cmd")) {
        $env:JAVA_HOME = $jdk.FullName
        $env:PATH = "$($jdk.FullName)\bin;$toolsDir\apache-maven-3.9.9\bin;$env:PATH"
        & "$toolsDir\apache-maven-3.9.9\bin\mvn.cmd" @MavenArgs
        exit $LASTEXITCODE
    }
}

if (-not $env:JAVA_HOME -and -not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Error "JAVA_HOME or java executable not found in PATH"
    exit 1
}

$mvnCmd = Get-Command mvn -ErrorAction SilentlyContinue
if ($mvnCmd) {
    & $mvnCmd.Source @MavenArgs
    exit $LASTEXITCODE
}

Write-Error "Maven command not found in PATH"
exit 1
