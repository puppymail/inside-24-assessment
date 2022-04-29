#!/bin/bash

read -rp "Name: " NAME
read -rp "Message: " MESSAGE
read -rp "Token: " TOKEN

curl --location --request POST "localhost:7000/api/message" \
--header "Authorization: Bearer_$TOKEN" \
--header "Content-Type: application/json" \
--data-raw "{
    \"name\": \"$NAME\",
    \"message\": \"$MESSAGE\"
}"
echo
read -p "Press any key to exit..." -srn 1