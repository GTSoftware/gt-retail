#!/usr/bin/env sh
set -eu

#if [[ ! -f "/etc/nginx/conf.d/default.conf" ]]; then
  envsubst '${API_HOST} ${API_PORT} ${WEB_PORT}' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf

  #rm /etc/nginx/conf.d/default.conf.template
#fi

exec "$@"