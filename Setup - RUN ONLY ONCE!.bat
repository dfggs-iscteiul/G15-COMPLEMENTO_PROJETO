Rem Start a cmd window at the current directory
START cmd.exe /k "ECHO Start configuration... & docker image build -t "wordpress-with-java:5.4.1" . & docker-compose up & ECHO Configuration Finished! You can now close this window."
