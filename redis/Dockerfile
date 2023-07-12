FROM redis:6.0-alpine


COPY ./entrypoint.sh /entrypoint.sh
RUN chmod 755 /entrypoint.sh

#ENTRYPOINT ["/amr2-entrypoint.sh"]

ENTRYPOINT ["sh", "/entrypoint.sh"]