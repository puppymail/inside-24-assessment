#!/bin/bash

TOKEN=$(curl --verbose --location --request POST "localhost:7000/api/login" \
        --header "Content-Type: application/json" \
        --data-raw "{
            \"name\": \"root\",
            \"password\": \"root\"
        }" | grep -Po "(?>(?<=^{\"token\":\").+(?=\"}$))")

echo "The token is: $TOKEN"
echo "------------------------------"
curl --verbose --location --request POST "localhost:7000/api/message" \
--header "Authorization: Bearer_$TOKEN" \
--header "Content-Type: application/json" \
--data-raw "{
    \"name\": \"root\",
    \"message\": \"message 1\"
}"
echo "------------------------------"
curl --location --request POST "localhost:7000/api/message" \
--header "Authorization: Bearer_$TOKEN" \
--header "Content-Type: application/json" \
--data-raw "{
    \"name\": \"root\",
    \"message\": \"message 2\"
}"
echo "------------------------------"
curl --verbose --location --request POST "localhost:7000/api/message" \
--header "Authorization: Bearer_$TOKEN" \
--header "Content-Type: application/json" \
--data-raw "{
    \"name\": \"root\",
    \"message\": \"history 10\"
}"
