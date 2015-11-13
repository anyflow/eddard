#!/bin/sh
EXEC_FILE="$0"
BASE_NAME=`basename "$EXEC_FILE"`
if [ "$EXEC_FILE" = "./$BASE_NAME" ] || [ "$EXEC_FILE" = "$BASE_NAME" ]; then
    FULL_PATH=`pwd`
else
    FULL_PATH=`echo "$EXEC_FILE" | sed 's/'"${BASE_NAME}"'$//'`
    cd "$FULL_PATH" > /dev/null 2>&1
    FULL_PATH=`pwd`
fi
cd $FULL_PATH
echo 'Current Path: '`pwd`

nohup java -Dname=${project.build.finalName} ${project.build.startOption} -jar "${project.build.finalName}.jar" > startup.out 2>&1 &

echo "logging for 5 seconds..."

tail -f -n1000 startup.out &
PID=$!

sleep 5
echo "Bootstrapping finished."

rm -rf startup.out

kill $PID

exit 0