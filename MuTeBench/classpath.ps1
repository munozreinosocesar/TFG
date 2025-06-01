$jarFiles = Get-ChildItem lib\*.jar,lib\hibernate\*.jar
$classpath = $jarFiles -join ";"
$classpath
