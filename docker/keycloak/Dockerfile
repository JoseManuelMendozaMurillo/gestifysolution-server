# Start from the official Keycloak image
FROM quay.io/keycloak/keycloak:25.0.2

# 1. Declare build-time arguments.
ARG KC_DB_ARG=mysql
ARG KC_PROXY_HEADERS_ARG=none
# Add new build-time argument for features
ARG KC_FEATURES_ARG=none

# 2. Use the build arguments to set the ENV variables.
ENV KC_DB=${KC_DB_ARG}
ENV KC_PROXY_HEADERS=${KC_PROXY_HEADERS_ARG}
# Add new env var for features
ENV KC_FEATURES=${KC_FEATURES_ARG}

# 3. Run the build step. This will now bake in all three settings.
RUN /opt/keycloak/bin/kc.sh build

# The default start command will be inherited from the base image
# and will now have the correct configuration available.