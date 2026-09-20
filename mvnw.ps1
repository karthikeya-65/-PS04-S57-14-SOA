param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]]$MavenArgs
)

$toolsDir = "$env:USERPROFILE\tools"
$jdk = Get-ChildItem "$toolsDir\jdk-17*" -Directory | Select-Object -First 1

if (-not $jdk) {
    Write-Error "JDK 17 directory not found in $toolsDir"
    exit 1
}

$env:JAVA_HOME = $jdk.FullName
$env:PATH = "$($jdk.FullName)\bin;$toolsDir\apache-maven-3.9.9\bin;$env:PATH"

$mvnCmd = "$toolsDir\apache-maven-3.9.9\bin\mvn.cmd"
& $mvnCmd @MavenArgs
