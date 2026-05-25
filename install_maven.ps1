$MAVEN_VERSION = '3.9.4'
$zip = "apache-maven-$MAVEN_VERSION-bin.zip"
$dest = Join-Path (Get-Location) '.maven'
if (-Not (Test-Path $dest)) { New-Item -ItemType Directory -Path $dest | Out-Null }
$zipUrl = "https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/$zip"
Write-Output "Downloading $zipUrl"
Invoke-WebRequest -Uri $zipUrl -OutFile $zip
Write-Output "Extracting to $dest"
Expand-Archive -Path $zip -DestinationPath $dest -Force
Remove-Item $zip
$M2_HOME = Resolve-Path (Join-Path $dest "apache-maven-$MAVEN_VERSION")
$env:M2_HOME = $M2_HOME.Path
$env:PATH = $env:M2_HOME + '\bin;' + $env:PATH
Write-Output "Maven in session at $env:M2_HOME"
mvn -v
mvn test -DskipTests=false
