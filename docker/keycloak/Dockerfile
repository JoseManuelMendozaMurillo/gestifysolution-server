# Start from the official Keycloak image
FROM quay.io/keycloak/keycloak:25.0.2

# 1. Declare a build-time argument. 
#    We will pass a value to this from docker-compose.
#    'mysql' is a fallback default if nothing is passed.
ARG KC_DB_ARG=mysql

# 2. Use the build argument to set the ENV variable.
#    This makes $KC_DB available to the RUN command 
#    and also sets it for the final container.
ENV KC_DB=${KC_DB_ARG}

# 3. Run the build step. This will now use the $KC_DB value.
RUN /opt/keycloak/bin/kc.sh build

# The default start command will be inherited from the base image
# and will now have the MySQL driver available.