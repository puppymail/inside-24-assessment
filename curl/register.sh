#!/bin/bash

read -rp "Name: " NAME
read -rsp "Password: " PASSWORD

curl --location --request POST "localhost:7000/api/register" \
--header "Content-Type: application/json" \
--data-raw "{
    \"name\": \"$NAME\",
    \"password\": \"$PASSWORD\"
}"
echo
echo "Sender \"$NAME\" registered"
read -p "Press any key to exit..." -srn 1