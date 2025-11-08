# Start from the official Keycloak image
FROM quay.io/keycloak/keycloak:25.0.2

# 1. Declare build-time arguments.
#    We will pass values to these from docker-compose.
ARG KC_DB_ARG=mysql
ARG KC_PROXY_HEADERS_ARG=none

# 2. Use the build arguments to set the ENV variables.
#    This makes them available to the RUN command.
ENV KC_DB=${KC_DB_ARG}
ENV KC_PROXY_HEADERS=${KC_PROXY_HEADERS_ARG}

# 3. Run the build step. This will now use BOTH $KC_DB and $KC_PROXY_HEADERS.
#    This "bakes in" the proxy configuration.
RUN /opt/keycloak/bin/kc.sh build

# The default start command will be inherited from the base image
# and will now have the correct configuration available.