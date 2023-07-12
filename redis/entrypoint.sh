#!/bin/bash

REDIS_IPS="173.18.0.10:6379 173.18.0.2:6379 173.18.0.3:6379 173.18.0.4:6379 173.18.0.5:6379 173.18.0.6:6379"

# Function to check if Redis node is running
function check_redis_node() {
    redis-cli -c -h "$1" ping > /dev/null 2>&1
    return $?
}

# Function to check if Redis cluster is already set up
function check_redis_cluster() {
    for ip in $REDIS_IPS; do
        check_redis_node "${ip%:*}"
        if [ $? -ne 0 ]; then
            return 1
        fi
    done
    return 0
}

# Check the status of Redis cluster
echo "Checking Redis cluster status..."
check_redis_cluster

# Check if Redis cluster is set up
if [ $? -eq 0 ]; then
    echo "Redis cluster is already set up."
else
    echo "Redis cluster is not set up. Setting up the cluster..."

    # Create Redis cluster
    echo "yes" | redis-cli --cluster create "$REDIS_IPS" --cluster-replicas 1

    echo "Redis cluster ready!"
fi
