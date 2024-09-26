#!/bin/bash

# Generate SQL script using environment variables
cat <<EOF > /docker-entrypoint-initdb.d/init-db.sql
-- create databases
CREATE DATABASE IF NOT EXISTS \`${MYSQL_DATABASE}\`;
CREATE DATABASE IF NOT EXISTS \`${KEYCLOAK_DATABASE_NAME}\`;
EOF

# Execute the original entrypoint script
exec /usr/local/bin/docker-entrypoint.sh "$@"

