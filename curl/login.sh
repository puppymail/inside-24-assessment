#!/bin/bash

read -rp "Name: " NAME
read -rsp "Password: " PASSWORD

TOKEN=$(curl --location --request POST "localhost:7000/api/login" \
        --header "Content-Type: application/json" \
        --data-raw "{
            \"name\": \"$NAME\",
            \"password\": \"$PASSWORD\"
        }" | grep -Po "(?>(?<=^{\"token\":\").+(?=\"}$))")
echo
echo "The token is: $TOKEN"
read -p "Press any key to exit..." -srn 1