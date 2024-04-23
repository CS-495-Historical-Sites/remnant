jq '[.[] | select(.long_description != null and .long_description != "")]' input.json > filtered_output.json
