FROM redis:latest


COPY ./entrypoint.sh /amr2-entrypoint.sh
RUN chmod 755 /amr2-entrypoint.sh

CMD ["bash"]

ENTRYPOINT ["/amr2-entrypoint.sh"]