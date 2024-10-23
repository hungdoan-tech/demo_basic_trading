#!/bin/bash

echo "Starting Gradle build and tests..."
./gradlew clean build

if [ $? -ne 0 ]; then
  echo "Gradle build or tests failed. Exiting..."
  exit 1
fi

echo "Gradle build and tests completed successfully."

echo "Building Docker image..."
docker build -t demo_basic_trading .

if [ $? -ne 0 ]; then
  echo "Docker image build failed. Exiting..."
  exit 1
fi

echo "Docker image built successfully."

echo "Stopping any existing container with the name 'demo_basic_trading-container'..."
docker stop demo_basic_trading-container
docker rm demo_basic_trading-container


echo "Running Docker container..."
docker run -d --name demo_basic_trading-container -p 8080:8080 \
  -v /run/secrets/private.pem:/run/secrets/private.pem:ro \
  -v /run/secrets/public.pem:/run/secrets/public.pem:ro \
  demo_basic_trading

if [ $? -ne 0 ]; then
  echo "Failed to start Docker container. Exiting..."
  exit 1
fi

echo "Docker container started successfully and is running on port 8080."
