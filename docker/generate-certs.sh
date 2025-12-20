#!/bin/bash

# Create certs directory if it doesn't exist
mkdir -p certs

# Generate private key
openssl genrsa -out certs/tls.key 2048

# Generate CSR (Certificate Signing Request)
openssl req -new -key certs/tls.key -out certs/tls.csr -subj "/CN=gestifysolution-keycloak/O=Gestify/C=ES"

# Generate self-signed certificate
openssl x509 -req -days 365 -in certs/tls.csr -signkey certs/tls.key -out certs/tls.crt

# Set appropriate permissions
chmod 644 certs/tls.crt
chmod 600 certs/tls.key

echo "Certificates generated successfully in the certs directory"