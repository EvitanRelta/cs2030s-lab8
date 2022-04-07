# To give execution permission, run:
# chmod +x path/to/file.sh
clear
DIVIDER="\n$(seq -s= $(tput cols - 1) | tr -d '[:digit:]')\n"
header() {
  local header=$1
  printf "$DIVIDER$header\n"
}

# Compiling all java classes
#header "[Compiling 'cs2030s.fp']"
header "[Compiling 'all java files']"
javac -Xlint:rawtypes,unchecked *.java


# Running tests
header "[Running 'Main < test1.in']"
java Main < test1.in #| grep -v ok | grep -P "(^\s+[^\.]|failed)"

header "[Running 'Main < test2.in']"
java Main < test2.in #| grep -v ok | grep -P "(^\s+[^\.]|failed)"


# Check style
header "[Checking styles - all java files]"
java -jar ~cs2030s/bin/checkstyle.jar -c ~cs2030s/bin/cs2030_checks.xml *.java


# Test generating 'javadoc'
#header "[Generating javadoc - 'cs2030s.fp.InfiniteList']"
#javadoc -quiet -private -d docs cs2030s/fp/InfiniteList.java
